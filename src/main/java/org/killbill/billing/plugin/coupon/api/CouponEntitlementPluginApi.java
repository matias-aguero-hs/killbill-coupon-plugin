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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Nullable;

import org.killbill.billing.entitlement.api.Entitlement;
import org.killbill.billing.entitlement.api.EntitlementApiException;
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

        if (entitlementContext.getOperationType() != OperationType.CREATE_SUBSCRIPTION) {
            applyCouponToNewSubscription(entitlementContext, pluginProperties);
            return null;
        }

        if (entitlementContext.getOperationType() != OperationType.CANCEL_SUBSCRIPTION) {
            // TODO deactivate couponApplied
        }

        if (entitlementContext.getOperationType() != OperationType.CHANGE_PLAN) {
            // verify billing period changes
            // TODO deactivate couponApplied
        }

        return null;
    }

    /**
     *
     * @param entitlementContext
     * @param pluginProperties
     * @return
     */
    private boolean applyCouponToNewSubscription(final EntitlementContext entitlementContext, final Iterable<PluginProperty> pluginProperties) {

        String couponCode = findCouponInRequest(pluginProperties);
        CouponsRecord requestCoupon = null;
        List<CouponsAppliedRecord> couponsApplied = new ArrayList<CouponsAppliedRecord>();
        List<CouponsRecord> appliedCoupons = new ArrayList<CouponsRecord>();

        try {
            // TODO get coupon from request
            if (couponCode == null) {
                requestCoupon = couponPluginApi.getCouponByCode(couponCode);
            }

            // TODO get active coupons applied
            couponsApplied = couponPluginApi.getCouponsAppliedByAccountId(entitlementContext.getAccountId());
            appliedCoupons = getCouponsForApplications(couponsApplied);

            // TODO compare discounts

            // TODO if needed, calculate number of invoices

            // TODO apply coupon with higger discount
            logService.log(LogService.LOG_INFO, "Going to get subscription id from externalkey = " + entitlementContext.getExternalKey());
            UUID entitlementId = getEntitlementId(entitlementContext);
            if (entitlementId == null) {
                String error = "There are no Entitlements for externalKey = " + entitlementContext.getExternalKey();
                logService.log(LogService.LOG_ERROR, error);
                throw new EntitlementPluginApiException(error);
            }

            logService.log(LogService.LOG_INFO, "Going to apply coupon " + couponCode + " to subscription " + entitlementId);
            couponPluginApi.applyCoupon(couponCode, entitlementId, entitlementContext.getAccountId(), entitlementContext);

            // TODO inform user that coupon was applied ??

            return true;

        } catch (Exception e) {
            // TODO inform user that the coupon couldn't be applied
            // TODO this exception won't stop Subscription creation.
            // throw new EntitlementPluginApiException(e);
        }
        return false;
    }

    /**
     * TODO document me
     *
     * @param couponsApplied
     * @return
     */
    private List<CouponsRecord> getCouponsForApplications(final List<CouponsAppliedRecord> couponsApplied) {
        // TODO implement
        return null;
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
