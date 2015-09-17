/*
 * Copyright 2014-2015 Groupon, Inc
 * Copyright 2014-2015 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.plugin.coupon.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Nullable;

import org.killbill.billing.entitlement.api.Entitlement;
import org.killbill.billing.entitlement.api.EntitlementApiException;
import org.killbill.billing.entitlement.api.Subscription;
import org.killbill.billing.entitlement.api.SubscriptionApi;
import org.killbill.billing.entitlement.api.SubscriptionApiException;
import org.killbill.billing.entitlement.api.SubscriptionEvent;
import org.killbill.billing.entitlement.api.SubscriptionEventType;
import org.killbill.billing.entitlement.plugin.api.EntitlementContext;
import org.killbill.billing.entitlement.plugin.api.EntitlementPluginApi;
import org.killbill.billing.entitlement.plugin.api.EntitlementPluginApiException;
import org.killbill.billing.entitlement.plugin.api.OnFailureEntitlementResult;
import org.killbill.billing.entitlement.plugin.api.OnSuccessEntitlementResult;
import org.killbill.billing.entitlement.plugin.api.OperationType;
import org.killbill.billing.entitlement.plugin.api.PriorEntitlementResult;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.DefaultPriorEntitlementResult;
import org.killbill.billing.plugin.coupon.model.DiscountTypeEnum;
import org.killbill.billing.plugin.coupon.model.DurationTypeEnum;
import org.killbill.billing.plugin.coupon.model.ErrorPriorEntitlementResult;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillLogService;
import org.osgi.service.log.LogService;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class CouponEntitlementPluginApi implements EntitlementPluginApi {

    public static final String COUPON_PROPERTY = Constants.PLUGIN_NAME + ":coupon";

    final OSGIKillbillLogService logService;
    final OSGIKillbillAPI killbillAPI;
    private final CouponPluginApi couponPluginApi;

    public CouponEntitlementPluginApi(final Properties properties, final OSGIKillbillAPI killbillAPI,
                                      final OSGIKillbillLogService logService, final CouponPluginApi couponPluginApi) {
        this.logService = logService;
        this.killbillAPI = killbillAPI;
        this.couponPluginApi = couponPluginApi;
    }

    @Override
    public PriorEntitlementResult priorCall(final EntitlementContext entitlementContext, final Iterable<PluginProperty> pluginProperties)
            throws EntitlementPluginApiException {

        if (entitlementContext.getOperationType() != OperationType.CREATE_SUBSCRIPTION) {
            return new DefaultPriorEntitlementResult();
        }

        String couponCode = findCouponInRequest(pluginProperties);
        if (couponCode == null) {
            return new DefaultPriorEntitlementResult();
        }

        String productName = entitlementContext.getPlanPhaseSpecifier().getProductName();
        try {
            couponPluginApi.validateCoupon(couponCode, entitlementContext.getAccountId(), productName, entitlementContext);
        } catch (CouponApiException e) {
            // if validations don't pass, stop creating subscription
            logService.log(LogService.LOG_ERROR, e.getMessage());
            return new ErrorPriorEntitlementResult();
        }

        return new DefaultPriorEntitlementResult();
    }

    @Override
    public OnSuccessEntitlementResult onSuccessCall(final EntitlementContext entitlementContext, final Iterable<PluginProperty> pluginProperties)
            throws EntitlementPluginApiException {

        if (entitlementContext.getOperationType().equals(OperationType.CREATE_SUBSCRIPTION)) {
            applyCouponToNewSubscription(entitlementContext, pluginProperties);
            return null;
        }

        if (entitlementContext.getOperationType().equals(OperationType.CANCEL_SUBSCRIPTION)) {
            deactivateIfHasCoupon(entitlementContext);
        }

        if (entitlementContext.getOperationType().equals(OperationType.CHANGE_PLAN)) {
            if (hasChangedBillingPeriodOrProduct(entitlementContext)) {
                deactivateIfHasCoupon(entitlementContext);
            }
        }
        return null;
    }

    private boolean hasChangedBillingPeriodOrProduct(EntitlementContext entitlementContext) {
        // check what kind of "change" was made to the plan, if it was billing period or type of product, deactivate the coupon applied!
        SubscriptionEvent lastChangeEvent = null;
        try {
            SubscriptionApi subscriptionApi = killbillAPI.getSubscriptionApi();
            if (null != subscriptionApi) {
                Subscription subscription = subscriptionApi.getSubscriptionForEntitlementId(getEntitlementId(entitlementContext), entitlementContext);
                if (null != subscription) {
                    List<SubscriptionEvent> subscriptionEvents = subscription.getSubscriptionEvents();

                    for (SubscriptionEvent subscriptionEvent : subscriptionEvents) {
                        if (null != subscriptionEvent.getSubscriptionEventType() && subscriptionEvent.getSubscriptionEventType().equals(SubscriptionEventType.CHANGE)) {
                            lastChangeEvent = subscriptionEvent;
                        }
                    }
                    // billing period or product have changed, so deactivate the coupon applied
                    return (null != lastChangeEvent
                            && null != lastChangeEvent.getPrevBillingPeriod()
                            && null != lastChangeEvent.getNextBillingPeriod()
                            && null != lastChangeEvent.getPrevProduct()
                            && null != lastChangeEvent.getNextProduct()
                            && (!lastChangeEvent.getPrevBillingPeriod().equals(lastChangeEvent.getNextBillingPeriod())
                            || !lastChangeEvent.getPrevProduct().equals(lastChangeEvent.getNextProduct())));
                }
            }
        } catch (SubscriptionApiException e) {
            e.printStackTrace();
        } catch (EntitlementApiException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void deactivateIfHasCoupon(EntitlementContext entitlementContext) throws EntitlementPluginApiException {
        try {
            UUID subscriptionId = getEntitlementId(entitlementContext);
            CouponsAppliedRecord couponsAppliedRecord = couponPluginApi.getActiveCouponAppliedBySubscription(subscriptionId);
            if (null != couponsAppliedRecord) {
                couponPluginApi.deactivateApplicationOfCouponByCodeAndSubscription(couponsAppliedRecord.getCouponCode(), subscriptionId);
            }
        } catch (EntitlementApiException e) {
            String error = "Error getting EntitlementId";
            logService.log(LogService.LOG_ERROR, error);
            throw new EntitlementPluginApiException(error);
        } catch (SQLException e) {
            String error = "Error deactivating Coupon";
            logService.log(LogService.LOG_ERROR, error);
            throw new EntitlementPluginApiException(error);
        }
    }

    /**
     * Find the best coupon to apply automatically
     *
     * @param entitlementContext
     * @param pluginProperties
     * @return
     */
    private boolean applyCouponToNewSubscription(final EntitlementContext entitlementContext, final Iterable<PluginProperty> pluginProperties) throws EntitlementPluginApiException {

        String couponCode = findCouponInRequest(pluginProperties);
        CouponsRecord requestCoupon = null;
        List<CouponsAppliedRecord> couponsApplied = new ArrayList<CouponsAppliedRecord>();

        try {
            // get coupon from request
            if (couponCode != null) {
                requestCoupon = couponPluginApi.getCouponByCode(couponCode);
            }

            // get active coupons applied
            String productName = entitlementContext.getPlanPhaseSpecifier().getProductName();
            couponsApplied = couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(entitlementContext.getAccountId(), productName);

            // compare discounts and get the coupon to apply
            CouponsRecord couponToApply = findBestCouponToApply(requestCoupon, couponsApplied);

            if (couponToApply == null) {
                logService.log(LogService.LOG_INFO, "Nothing to apply.");
                return true;
            }

            // if needed, calculate number of invoices
            Integer maxInvoices = calculateMaxInvoicesToApply(couponToApply, couponsApplied);

            // apply coupon with higher discount
            logService.log(LogService.LOG_INFO, "Going to get subscription id from externalkey = " + entitlementContext.getExternalKey());
            UUID entitlementId = getEntitlementId(entitlementContext);
            if (entitlementId == null) {
                String error = "There are no Entitlements for externalKey = " + entitlementContext.getExternalKey();
                logService.log(LogService.LOG_ERROR, error);
                throw new EntitlementPluginApiException(error);
            }

            logService.log(LogService.LOG_INFO, "Going to apply coupon " + couponCode + " to subscription " + entitlementId);
            couponPluginApi.applyCoupon(couponToApply.getCouponCode(), maxInvoices, entitlementId, entitlementContext.getAccountId(), entitlementContext);

            // TODO inform user that coupon was applied ??

            return true;

        } catch (Exception e) {
            // TODO inform user that the coupon couldn't be applied
            // TODO this exception won't stop Subscription creation.
            throw new EntitlementPluginApiException(e);
        }

    }

    /**
     * Calculate how many invoices need to apply discounts
     *
     * @param couponToApply
     * @param couponsApplied
     * @return
     */
    private Integer calculateMaxInvoicesToApply(final CouponsRecord couponToApply, final List<CouponsAppliedRecord> couponsApplied) {

        if (couponToApply.getDuration().equals(DurationTypeEnum.forever.toString())) {
            return 0;
        }

        CouponsAppliedRecord appliedCoupon = Iterables.tryFind(couponsApplied, new Predicate<CouponsAppliedRecord>() {
            @Override
            public boolean apply(@Nullable CouponsAppliedRecord input) {
                return input.getCouponCode().equals(couponToApply.getCouponCode());
            }
        }).orNull();

        return (appliedCoupon != null) ? (appliedCoupon.getMaxInvoices() - appliedCoupon.getNumberOfInvoices())
                                       : couponToApply.getNumberOfInvoices();
    }

    /**
     * Find the best coupon to apply based on higher discount and duration
     *
     * @param requestCoupon
     * @param couponsApplied
     * @return
     */
    private CouponsRecord findBestCouponToApply(final CouponsRecord requestCoupon, final List<CouponsAppliedRecord> couponsApplied) {

        if ((requestCoupon == null) && ((couponsApplied == null) || couponsApplied.isEmpty())) {
            logService.log(LogService.LOG_INFO, "There are no coupons to apply automatically.");
            return null;
        }

        if ((requestCoupon != null) && ((couponsApplied == null) || couponsApplied.isEmpty())) {
            logService.log(LogService.LOG_INFO,
                           "There are no active apply coupons for this account. Apply automatically coupon from request "
                           + requestCoupon.getCouponCode());
            return requestCoupon;
        }

        List<CouponsRecord> candidateCoupons = new ArrayList<CouponsRecord>();

        if ((couponsApplied != null) && !couponsApplied.isEmpty()) {
            logService.log(LogService.LOG_INFO,
                           "There are active apply coupons for this account.");

            for (CouponsAppliedRecord couponApplied : couponsApplied) {
                try {
                    candidateCoupons.add(couponPluginApi.getCouponByCode(couponApplied.getCouponCode()));
                    logService.log(LogService.LOG_INFO,
                                   "Coupon " + couponApplied.getCouponCode() + " + added as a candidate.");
                } catch (SQLException e) {
                    logService.log(LogService.LOG_ERROR, "Error getting coupon " + couponApplied.getCouponCode());
                }
            }

            if (requestCoupon != null) {
                candidateCoupons.add(requestCoupon);
                logService.log(LogService.LOG_INFO,
                               "Coupon " + requestCoupon.getCouponCode() + " + added as a candidate.");
            }
        }

        return compareAndGetBestCouponToApply(candidateCoupons);
    }

    /**
     *
     *
     * @param candidateCoupons
     * @return
     */
    private CouponsRecord compareAndGetBestCouponToApply(final List<CouponsRecord> candidateCoupons) {

        Double biggerDiscount = 0D;
        CouponsRecord bestCoupon = null;

        for (CouponsRecord coupon : candidateCoupons) {
            // TODO change next line when discountAmount type is implemented
            Double couponDiscount = (coupon.getDiscountType().equals(DiscountTypeEnum.percentage.toString())) ? coupon.getPercentageDiscount() : 0 ;
            if (couponDiscount.compareTo(biggerDiscount) > 0) {
                biggerDiscount = couponDiscount;
                bestCoupon = coupon;
            } else if (couponDiscount.compareTo(biggerDiscount) == 0) {
                // equal discount
                if (coupon.getDuration().equals(DurationTypeEnum.forever)) {
                    bestCoupon = coupon;
                } else if ((coupon.getDuration().equals(DurationTypeEnum.multiple))
                           && (bestCoupon.getDuration().equals(DurationTypeEnum.once))) {
                    bestCoupon = coupon;
                }
            }
        }

        return bestCoupon;
    }

    /**
     * Return the UUID for the first entitlement
     *
     * @param entitlementContext
     * @return
     * @throws EntitlementApiException
     */
    private UUID getEntitlementId(final EntitlementContext entitlementContext) throws EntitlementApiException {

        List<Entitlement> entitlements = killbillAPI.getEntitlementApi().getAllEntitlementsForAccountIdAndExternalKey(
                entitlementContext.getAccountId(), entitlementContext.getExternalKey(), entitlementContext);

        return ((entitlements != null) && !entitlements.isEmpty()) ? entitlements.get(0).getId() : null;
    }

    @Override
    public OnFailureEntitlementResult onFailureCall(final EntitlementContext entitlementContext, final Iterable<PluginProperty> pluginProperties) throws EntitlementPluginApiException {
        return null;
    }

    /**
     * Find if there is a coupon in the request
     *
     * @param pluginProperties
     * @return
     */
    private String findCouponInRequest(@Nullable final Iterable<PluginProperty> pluginProperties) {

        if ((pluginProperties == null) || !pluginProperties.iterator().hasNext()) {
            return null;
        }

        PluginProperty coupon = Iterables.tryFind(pluginProperties, new Predicate<PluginProperty>() {
            @Override
            public boolean apply(@Nullable PluginProperty input) {
                return input.getKey().equals(COUPON_PROPERTY);
            }
        }).orNull();

        return (coupon != null) ? (String) coupon.getValue() : null;
    }
}
