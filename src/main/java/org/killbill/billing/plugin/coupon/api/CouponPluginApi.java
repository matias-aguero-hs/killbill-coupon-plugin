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
import java.util.UUID;

import org.killbill.billing.plugin.coupon.CouponJson;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.plugin.coupon.dao.CouponDao;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.tenant.api.TenantApiException;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;

public class CouponPluginApi {

    private final OSGIKillbillAPI osgiKillbillAPI;
    private final CouponDao dao;

    public CouponPluginApi(final CouponDao dao, final OSGIKillbillAPI osgiKillbillAPI) {
        this.dao = dao;
        this.osgiKillbillAPI = osgiKillbillAPI;
    }

    public CouponsRecord getCouponByCode(final String couponCode) throws SQLException {
        return dao.getCouponByCode(couponCode);
    }

    public void createCoupon(final CouponJson couponJson) throws SQLException {
        dao.createCoupon(couponJson);
    }

    public UUID getTenantId(String apiKey) throws TenantApiException {
        // TODO refactor
        return osgiKillbillAPI.getTenantUserApi().getTenantByApiKey(apiKey).getId();
    }

    /**
     *
     * @param couponCode
     * @param accountId
     * @throws SQLException
     */
    public void applyCoupon(String couponCode, UUID accountId, TenantContext context) throws SQLException, AccountApiException {

        // TODO tenat context
        Account account = osgiKillbillAPI.getAccountUserApi().getAccountById(accountId, context);

        if (account == null) {
            // TODO inform error
        }

        CouponsRecord coupon = getCouponByCode(couponCode);

        if (coupon == null) {
            // TODO inform error
        }

        // TODO validate if coupon can be applied

        // save applied coupon
        dao.applyCoupon(couponCode, accountId, context);

    }

}
