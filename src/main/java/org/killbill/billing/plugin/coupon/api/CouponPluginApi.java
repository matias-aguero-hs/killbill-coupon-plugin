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
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.entitlement.api.Subscription;
import org.killbill.billing.entitlement.api.SubscriptionApiException;
import org.killbill.billing.plugin.coupon.dao.CouponDao;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.Coupon;
import org.killbill.billing.plugin.coupon.util.CouponHelper;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.killbill.billing.tenant.api.Tenant;
import org.killbill.billing.tenant.api.TenantApiException;
import org.killbill.billing.tenant.api.TenantUserApi;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.osgi.service.log.LogService;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class CouponPluginApi {

    private final OSGIKillbillAPI osgiKillbillAPI;
    private final CouponDao dao;
    private final LogService logService;

    public CouponPluginApi(final LogService logService, final CouponDao dao,
                           final OSGIKillbillAPI osgiKillbillAPI) {
        this.dao = dao;
        this.osgiKillbillAPI = osgiKillbillAPI;
        this.logService = logService;
    }

    public List<CouponsRecord> getAllCoupons() throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get all Coupons");
        return dao.getAllCoupons();
    }

    public List<CouponsAppliedRecord> getAllCouponsApplied() throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get all Coupons Applied");
        return dao.getAllCouponsApplied();
    }

    public CouponsRecord getCouponByCode(final String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get a Coupon by couponCode");
        return dao.getCouponByCode(couponCode);
    }

    public void deactivateCouponByCode(final String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to deactivate a Coupon by couponCode");
        dao.deactivateCouponByCode(couponCode);
    }

    public void deactivateApplicationsOfCoupon(final String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to deactivate all the Applications of a Coupon by couponCode");
        dao.deactivateApplicationsOfCoupon(couponCode);
    }

    public void increaseNumberOfInvoicesAndSetActiveStatus(final String couponCode, final Integer numberOfInvoices, final Boolean deactivate, final UUID subscriptionId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to increase the number of Invoices affected and deactivate or not a Coupon Application");
        Byte deactivation = (deactivate ? new Byte(Constants.BYTE_FALSE) : new Byte(Constants.BYTE_TRUE));
        dao.increaseNumberOfInvoicesAffected(couponCode, numberOfInvoices, deactivation, subscriptionId);
    }

    public void createCoupon(final Coupon coupon, TenantContext context) throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to create a Coupon");
        dao.createCoupon(coupon, context);
    }

    public UUID getTenantId(String apiKey) throws CouponApiException {
        logService.log(LogService.LOG_INFO, "Accesing osgiKillbillAPI to get the TenantUserApi");
        TenantUserApi tenantUserApi = osgiKillbillAPI.getTenantUserApi();
        if (null != tenantUserApi && !apiKey.isEmpty()) {
            Tenant tenant = null;
            try {
                logService.log(LogService.LOG_INFO, "Accesing the TenantUserApi to get the Tenant using the ApiKey: " + apiKey);
                tenant = tenantUserApi.getTenantByApiKey(apiKey);
                if (null != tenant) {
                    logService.log(LogService.LOG_INFO, "Returning Tenant Id as result");
                    return tenant.getId();
                }
                else logService.log(LogService.LOG_ERROR, "Tenant object is null");
            } catch (TenantApiException e) {
                logService.log(LogService.LOG_ERROR, "TenantApiException. Cause: " + e.getMessage());
                e.printStackTrace();
                throw new CouponApiException(e.getCause(), 0, "TenantApiException when trying to get Tenant by ApiKey");
            }
        }
        else logService.log(LogService.LOG_ERROR, "TenantUserApi is null or apiKey is Empty");
        throw new CouponApiException(new Throwable("Either TenantUserApi is null or apiKey is Empty"), 0, "Either TenantUserApi is null or apiKey is Empty");
    }

    public Object getObjectFromJsonRequest(HttpServletRequest request, LogService logService, Class clazz) {
        Object result = null;
        try {
            result = JsonHelper.getObjectFromRequest(request, clazz, logService);
        } catch (CouponApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    public CouponsAppliedRecord getCouponApplied(final String couponCode, final UUID subscriptionId, final UUID accountId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get an Applied Coupon");
        return dao.getCouponApplied(couponCode, subscriptionId, accountId);
    }

    /**
     * Get a coupon applied to a specific subscription
     *
     * @param subscriptionId
     * @return
     * @throws SQLException
     */
    public CouponsAppliedRecord getActiveCouponAppliedBySubscription(final UUID subscriptionId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get an Applied Coupon by Subscription Id");
        return dao.getActiveCouponAppliedBySubscription(subscriptionId);
    }

    /**
     *
     * @param couponCode
     * @param accountId
     * @throws SQLException
     */
    public boolean applyCoupon(String couponCode, UUID subscriptionId, UUID accountId, TenantContext context)
            throws SQLException, AccountApiException, SubscriptionApiException, CouponApiException {

        // Get Coupon by Code from DB
        logService.log(LogService.LOG_INFO, "Getting Coupon from the DB using couponCode: " + couponCode);
        CouponsRecord coupon = getCouponByCode(couponCode);

        Account account = null;
        Subscription subscription = null;

        logService.log(LogService.LOG_INFO, "Getting the Account User API from the OSGi Killbill API");
        if (null != accountId) {
            logService.log(LogService.LOG_INFO, "Getting the Account using the accountID: " + accountId);
            account = osgiKillbillAPI.getAccountUserApi().getAccountById(accountId, context);
        }

        logService.log(LogService.LOG_INFO, "Getting the Subscription User API from the OSGi Killbill API");
        if (null != subscriptionId) {
            logService.log(LogService.LOG_INFO, "Getting the Subscription using the subscriptionID: " + subscriptionId);
            subscription = osgiKillbillAPI.getSubscriptionApi().getSubscriptionForEntitlementId(subscriptionId, context);
        }

        if ((null != account) && (null != subscription)) {
            if (null != coupon) {
                logService.log(LogService.LOG_INFO, "Validating if Coupon can be applied");

                // validate that only one coupon is active for subscription
                CouponsAppliedRecord couponApplied = dao.getActiveCouponAppliedBySubscription(subscriptionId);
                if (couponApplied != null) {
                    String error = "Subscription " + subscriptionId + " already has an active applied coupon : "
                                   + couponApplied.getCouponCode();
                    logService.log(LogService.LOG_ERROR, error);
                    throw new CouponApiException(new Throwable(error), 0, error);
                }

                // validate unique constraint coupon - subscription
                couponApplied = dao.getCouponAppliedByCodeAndSubscription(couponCode, subscriptionId);
                if (couponApplied != null) {
                    String error = "Subscription " + subscriptionId + " already had applied coupon : " + couponCode;
                    logService.log(LogService.LOG_ERROR, error);
                    throw new CouponApiException(new Throwable(error), 0, error);
                }

                // validate if coupon can be applied
                // TODO verify
                String subProductName = subscription.getSubscriptionEvents().get(0).getNextProduct().getName();
                validateCoupon(coupon, account, subProductName);

                // save applied coupon
                logService.log(LogService.LOG_INFO, "Accessing the DAO to apply a Coupon");
                dao.applyCoupon(couponCode, subscriptionId, accountId, context);
            }
            else {
                String error = "Coupon does not exist. CouponCode = " + couponCode;
                logService.log(LogService.LOG_ERROR, error);
                throw new CouponApiException(new Throwable(error), 0, error);
            }
        }
        else {
            logService.log(LogService.LOG_ERROR, "There is no valid account ( " + accountId
                                                 + ") or subscription (" + subscriptionId + ").");
            return false;
        }
        return true;
    }

    /**
     * Validate coupon application
     *
     * @param couponCode
     * @param accountId
     * @param subscriptionProduct
     * @param context
     * @return
     * @throws CouponApiException
     */
    public boolean validateCoupon(String couponCode, UUID accountId, String subscriptionProduct, TenantContext context)
            throws CouponApiException {

        String error = "";

        try {
            // get coupon
            CouponsRecord coupon = getCouponByCode(couponCode);

            if (coupon == null) {
                error = "Coupon " + couponCode + " does not exist.";
                logService.log(LogService.LOG_ERROR,error);
                throw new CouponApiException(new Throwable(error), 0, error);
            }

            // get account
            Account account = osgiKillbillAPI.getAccountUserApi().getAccountById(accountId, context);

            if (account == null) {
                error = "Account " + accountId + " does not exist.";
                logService.log(LogService.LOG_ERROR,error);
                throw new CouponApiException(new Throwable(error), 0, error);
            }

            validateCoupon(coupon, account, subscriptionProduct);

        } catch (SQLException e) {
            error = "There is an error trying to get Coupon from database.";
            logService.log(LogService.LOG_ERROR, error);
            throw new CouponApiException(e, 0, error);
        } catch (AccountApiException e) {
            error = "There is an error trying to get Account.";
            logService.log(LogService.LOG_ERROR, error);
            throw new CouponApiException(e, 0, error);
        }

        return true;
    }


    /**
     * Validate if the coupon can be applied
     *
     * @param coupon
     * @param account
     * @param subscriptionProduct
     * @throws CouponApiException
     */
    private boolean validateCoupon(final CouponsRecord coupon, final Account account, final String subscriptionProduct)
            throws CouponApiException {

        String error = "";

        // validate expiration date and is_active
        if (!CouponHelper.isActive(coupon) || CouponHelper.hasExpired(coupon)) {
            error = "Coupon " + coupon.getCouponCode() + " is not active or has expired.";
            logService.log(LogService.LOG_ERROR,error);
            throw new CouponApiException(new Throwable(error), 0, error);
        }

        try {
            // check products
            List<CouponsProductsRecord> products = getProductsOfCoupon(coupon.getCouponCode());

            if ((products != null) && !products.isEmpty()) {

                // validate products
                CouponsProductsRecord productFound = Iterables.tryFind(products, new Predicate<CouponsProductsRecord>() {
                    @Override
                    public boolean apply(@Nullable CouponsProductsRecord product) {
                        return product.getProductName().equals(subscriptionProduct);
                    }
                }).orNull();

                if (productFound == null) {
                    error = "Coupon " + coupon.getCouponCode()
                            + " cannot be applied because it has not product " + subscriptionProduct;
                    logService.log(LogService.LOG_ERROR,error);
                    throw new CouponApiException(new Throwable(error), 0, error);
                }
            }
        } catch (SQLException e) {
            error = "There is an error getting products from coupon " + coupon.getCouponCode();
            logService.log(LogService.LOG_ERROR, error);
            throw new CouponApiException(e, 0, error);
        }

        logService.log(LogService.LOG_INFO, "Coupon " + coupon.getCouponCode() + " is valid to apply.");
        return true;
    }

    /**
     * Get coupons applied to customer using the accountId
     * @param accountId
     * @return
     */
    public List<CouponsAppliedRecord> getCouponsAppliedByAccountId(UUID accountId) {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get a list of Applied Coupons from the DB using the accountId");
        try {
            return dao.getCouponsAppliedByAccountId(accountId);
        } catch (SQLException e) {
            logService.log(LogService.LOG_ERROR, "Error getting list of Applied Coupons for accountId: " + accountId + ". Cause: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Get products associated to a Coupon
     * @param couponCode
     * @return
     */
    public List<CouponsProductsRecord> getProductsOfCoupon(String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get a list of Products associated with a Coupon with code: " + couponCode);
        try {
            return dao.getProductsOfCoupon(couponCode);
        } catch (SQLException e) {
            logService.log(LogService.LOG_ERROR, "Error getting list of Products associated with a Coupon with code: " + couponCode + ". Cause: " + e.getMessage());
            e.printStackTrace();
            // TODO check this block
        }
        return new ArrayList<>();
    }
}
