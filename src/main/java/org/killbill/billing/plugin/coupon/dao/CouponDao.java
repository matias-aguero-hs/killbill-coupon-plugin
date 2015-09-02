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

package org.killbill.billing.plugin.coupon.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import org.jooq.impl.DSL;
import org.killbill.billing.plugin.coupon.CouponJson;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.DiscountTypeEnum;
import org.killbill.billing.plugin.dao.PluginDao;
import org.killbill.billing.util.callcontext.TenantContext;

import static org.killbill.billing.plugin.coupon.dao.gen.tables.Coupons.COUPONS;
import static org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsApplied.COUPONS_APPLIED;

public class CouponDao extends PluginDao {

    public CouponDao(final DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    public CouponsRecord getCouponByCode(final String couponCode) throws SQLException {
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<CouponsRecord>() {
                           @Override
                           public CouponsRecord withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS)
                                         .where(COUPONS.COUPON_CODE.equal(couponCode))
                                         .fetchOne();
                           }
                       });
    }

    public void createCoupon(final CouponJson couponJson, TenantContext context) throws SQLException {
        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                           .insertInto(COUPONS,
                                       COUPONS.COUPON_CODE,
                                       COUPONS.COUPON_NAME,
                                       COUPONS.DISCOUNT_TYPE,
                                       COUPONS.PERCENTAGE_DISCOUNT,
                                       COUPONS.KB_TENANT_ID)
                           .values(couponJson.getCouponCode(),
                                   couponJson.getCouponName(),
                                   couponJson.getDiscountType().toString(),
                                   couponJson.getPercentageDiscount(),
                                   context.getTenantId().toString())
                           .execute();
                        return null;
                    }
                });
    }

    /**
     * TODO document me
     * @param couponCode
     * @param accountId
     * @throws SQLException
     */
    public void applyCoupon(final String couponCode, final UUID accountId, final TenantContext context) throws SQLException {
        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                                .insertInto(COUPONS_APPLIED,
                                            COUPONS_APPLIED.COUPON_CODE,
                                            COUPONS_APPLIED.KB_ACCOUNT_ID,
                                            COUPONS_APPLIED.KB_TENANT_ID)
                                .values(couponCode,
                                        accountId.toString(),
                                        context.getTenantId().toString())
                                .execute();
                        return null;
                    }
                });
    }

}
