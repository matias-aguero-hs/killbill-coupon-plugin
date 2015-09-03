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

import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.account.api.AccountUserApi;
import org.killbill.billing.plugin.coupon.dao.CouponDao;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.Coupon;
import org.killbill.billing.tenant.api.Tenant;
import org.killbill.billing.tenant.api.TenantApiException;
import org.killbill.billing.tenant.api.TenantUserApi;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.osgi.service.log.LogService;

public class CouponPluginApi {

    private final OSGIKillbillAPI osgiKillbillAPI;
    private final CouponDao dao;
    private final LogService logService;

    public CouponPluginApi(final LogService logService, final CouponDao dao, final OSGIKillbillAPI osgiKillbillAPI) {
        this.dao = dao;
        this.osgiKillbillAPI = osgiKillbillAPI;
        this.logService = logService;
    }

    public CouponsRecord getCouponByCode(final String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get a Coupon by couponCode");
        return dao.getCouponByCode(couponCode);
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

    public CouponsAppliedRecord getCouponApplied(final String couponCode, final UUID accountId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get an Applied Coupon");
        return dao.getCouponApplied(couponCode, accountId);
    }

    /**
     * Get a coupon applied to a specific subscription
     *
     * @param subscriptionId
     * @return
     * @throws SQLException
     */
    public CouponsAppliedRecord getCouponAppliedBySubscription(final UUID subscriptionId) throws SQLException {
        return dao.getCouponAppliedBySubscription(subscriptionId);
    }

    /**
     *
     * @param couponCode
     * @param accountId
     * @throws SQLException
     */
    public void applyCoupon(String couponCode, UUID accountId, TenantContext context) throws SQLException, AccountApiException {

        Account account = null;
        logService.log(LogService.LOG_INFO, "Getting the Account User API from the OSGi Killbill API");
        AccountUserApi accountUserApi = osgiKillbillAPI.getAccountUserApi();
        if (null != accountUserApi && null != accountId) {
            logService.log(LogService.LOG_INFO, "Getting the Account using the accountID: " + accountId);
            account = accountUserApi.getAccountById(accountId, context);
        }


        if (null != account) {
            // Get Coupon by Code from DB
            logService.log(LogService.LOG_INFO, "Getting Coupon from the DB using couponCode: " + couponCode);
            CouponsRecord coupon = getCouponByCode(couponCode);
            if (null != coupon) {
                logService.log(LogService.LOG_INFO, "Validating if Coupon can be applied");
                // TODO validate if coupon can be applied


                // save applied coupon
                logService.log(LogService.LOG_INFO, "Accessing the DAO to apply a Coupon");
                dao.applyCoupon(couponCode, accountId, context);
            }
            else {
                logService.log(LogService.LOG_ERROR, "Error getting Coupon from the DB for couponCode: " + couponCode);
            }
        }
        else {
            logService.log(LogService.LOG_ERROR, "Error getting Account for accountId: " + accountId);
        }
    }

    /**
     * Get coupons applied to customer
     * @param accountId
     * @return
     */
    public List<CouponsAppliedRecord> getCouponsApplied(UUID accountId) {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get a list of Applied Coupons from the DB");
        try {
            return dao.getCouponsApplied(accountId);
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
    public List<CouponsProductsRecord> getProductsOfCoupon(String couponCode) {
        logService.log(LogService.LOG_INFO, "Accessing the DAO to get a list of Products associated with a Coupon with code: " + couponCode);
        try {
            return dao.getProductsOfCoupon(couponCode);
        } catch (SQLException e) {
            logService.log(LogService.LOG_ERROR, "Error getting list of Products associated with a Coupon with code: " + couponCode + ". Cause: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
