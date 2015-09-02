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
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.jooq.Result;
import org.jooq.impl.DSL;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.model.Coupon;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.dao.PluginDao;
import org.killbill.billing.util.callcontext.TenantContext;

import static org.killbill.billing.plugin.coupon.dao.gen.tables.Coupons.COUPONS;
import static org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsApplied.COUPONS_APPLIED;
import static org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsProducts.COUPONS_PRODUCTS;

public class CouponDao extends PluginDao {

    public CouponDao(final DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    /**
     * Method to get a Coupon object by couponCode
     * @param couponCode
     * @return
     * @throws SQLException
     */
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

    public void createCoupon(final Coupon coupon, final TenantContext context) throws SQLException {
        // Add Coupon to Coupons table
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
                           .values(coupon.getCouponCode(),
                                   coupon.getCouponName(),
                                   coupon.getDiscountType().toString(),
                                   coupon.getPercentageDiscount(),
                                   context.getTenantId().toString())
                           .execute();
                        return null;
                    }
                });

        List<String> products = coupon.getProducts();
        // Add List of Products and Coupon associated to the table
        for (final String product : products) {
            execute(dataSource.getConnection(),
                    new WithConnectionCallback<Void>() {
                        @Override
                        public Void withConnection(final Connection conn) throws SQLException {
                            DSL.using(conn, dialect, settings)
                               .insertInto(COUPONS_PRODUCTS,
                                           COUPONS_PRODUCTS.COUPON_CODE,
                                           COUPONS_PRODUCTS.PRODUCT_NAME,
                                           COUPONS.KB_TENANT_ID)
                               .values(coupon.getCouponCode(),
                                       product,
                                       context.getTenantId().toString())
                               .execute();
                            return null;
                        }
                    });
        }
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

    public Result<CouponsAppliedRecord> getCouponsApplied(final UUID accountId) throws SQLException {
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<Result<CouponsAppliedRecord>>() {
                           @Override
                           public Result<CouponsAppliedRecord> withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_APPLIED)
                                         .where(COUPONS_APPLIED.KB_ACCOUNT_ID.equal(accountId.toString()))
                                         .fetch();
                           }
                       });
    }

    public Result<CouponsProductsRecord> getProductsOfCoupon(final String couponCode) throws SQLException {
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<Result<CouponsProductsRecord>>() {
                           @Override
                           public Result<CouponsProductsRecord> withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_PRODUCTS)
                                         .where(COUPONS_PRODUCTS.COUPON_CODE.equal(couponCode))
                                         .fetch();
                           }
                       });
    }

    /**
     * Method to get a Coupon Applied object by couponCode and accountId
     * @param couponCode
     * @param accountId
     * @return
     * @throws SQLException
     */
    public CouponsAppliedRecord getCouponApplied(final String couponCode, final UUID accountId) throws SQLException {
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<CouponsAppliedRecord>() {
                           @Override
                           public CouponsAppliedRecord withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_APPLIED)
                                         .where(COUPONS_APPLIED.COUPON_CODE.equal(couponCode))
                                          .and(COUPONS_APPLIED.KB_ACCOUNT_ID.equal(accountId.toString()))
                                         .fetchOne();
                           }
                       });
    }
}
