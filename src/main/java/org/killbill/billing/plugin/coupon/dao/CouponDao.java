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
import org.osgi.service.log.LogService;

import static org.killbill.billing.plugin.coupon.dao.gen.tables.Coupons.COUPONS;
import static org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsApplied.COUPONS_APPLIED;
import static org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsProducts.COUPONS_PRODUCTS;

public class CouponDao extends PluginDao {

    private final LogService logService;

    public CouponDao(final LogService logService, final DataSource dataSource) throws SQLException {
        super(dataSource);
        this.logService = logService;
    }


    // -----------------------------------------------------------------------------
    //                              COUPON METHODS
    // -----------------------------------------------------------------------------

    /**
     * Method to get a Coupon object by couponCode from the DB
     * @param couponCode
     * @return
     * @throws SQLException
     */
    public CouponsRecord getCouponByCode(final String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to Get a Coupon by couponCode in the DB");
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

    /**
     * Method to create a Coupon in the DB
     * @param coupon
     * @param context
     * @throws SQLException
     */
    public void createCoupon(final Coupon coupon, final TenantContext context) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to Create a Coupon in the DB");
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

        // Add List of Products and Coupon associated to the table
        List<String> products = coupon.getProducts();
        if (null != products) {
            logService.log(LogService.LOG_INFO, "Executing query to Add associated Products of a Coupon in the DB");
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
    }

    /**
     * Method to get a list of Products associated with a Coupon from the DB
     * @param couponCode
     * @return
     * @throws SQLException
     */
    public List<CouponsProductsRecord> getProductsOfCoupon(final String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to get a List of Products associated with a Coupon from the DB");
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
     * Method to get all Coupons object from the DB
     * @return
     * @throws SQLException
     */
    public List<CouponsRecord> getAllCoupons() throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to Get all Coupons in the DB");
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<Result<CouponsRecord>>() {
                           @Override
                           public Result<CouponsRecord> withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS)
                                         .fetch();
                           }
                       });
    }

    /**
     * Method to get all Coupons Applied object from the DB
     * @return
     * @throws SQLException
     */
    public List<CouponsAppliedRecord> getAllCouponsApplied() throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to Get all Coupons in the DB");
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<Result<CouponsAppliedRecord>>() {
                           @Override
                           public Result<CouponsAppliedRecord> withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_APPLIED)
                                         .fetch();
                           }
                       });
    }


    // -----------------------------------------------------------------------------
    //                              APPLY COUPON METHODS
    // -----------------------------------------------------------------------------

    /**
     * Method to store an applied Coupon with its respective subscriptionId and accountId in the DB
     * @param couponCode
     * @param accountId
     * @throws SQLException
     */
    public void applyCoupon(final String couponCode, final UUID subscriptionId, final UUID accountId, final TenantContext context) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to store an applied Coupon with its subscriptionId and accountId in the DB");
        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                           .insertInto(COUPONS_APPLIED,
                                       COUPONS_APPLIED.COUPON_CODE,
                                       COUPONS_APPLIED.KB_SUBSCRIPTION_ID,
                                       COUPONS_APPLIED.KB_ACCOUNT_ID,
                                       COUPONS_APPLIED.KB_TENANT_ID)
                           .values(couponCode,
                                   subscriptionId.toString(),
                                   accountId.toString(),
                                   context.getTenantId().toString())
                           .execute();
                        return null;
                    }
                });
    }

    /**
     * Method to get a list of Coupons Applied from the DB using its accountId
     * @param accountId
     * @return
     * @throws SQLException
     */
    public List<CouponsAppliedRecord> getCouponsAppliedByAccountId(final UUID accountId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to get a List of Coupons Applied from the DB");
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



    /**
     * Method to get a Coupon Applied object by couponCode and accountId
     * @param couponCode
     * @param subscriptionId
     * @param accountId
     * @return
     * @throws SQLException
     */
    public CouponsAppliedRecord getCouponApplied(final String couponCode, final UUID subscriptionId, final UUID accountId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to get a Coupon Applied object by couponCode and accountId from the DB");
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<CouponsAppliedRecord>() {
                           @Override
                           public CouponsAppliedRecord withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_APPLIED)
                                         .where(COUPONS_APPLIED.COUPON_CODE.equal(couponCode))
                                         .and(COUPONS_APPLIED.KB_SUBSCRIPTION_ID.equal(subscriptionId.toString()))
                                         .and(COUPONS_APPLIED.KB_ACCOUNT_ID.equal(accountId.toString()))
                                         .fetchOne();
                           }
                       });
    }

    /**
     * Method to get a Coupon Applied object by subscriptionId
     * @param subscriptionId
     * @return
     * @throws SQLException
     */
    public CouponsAppliedRecord getCouponAppliedBySubscription(final UUID subscriptionId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to get a List of Coupons Applied from the DB using subscriptionId");
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<CouponsAppliedRecord>() {
                           @Override
                           public CouponsAppliedRecord withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_APPLIED)
                                         .where(COUPONS_APPLIED.KB_SUBSCRIPTION_ID.equal(subscriptionId.toString()))
                                         .fetchOne();
                           }
                       });
    }
}
