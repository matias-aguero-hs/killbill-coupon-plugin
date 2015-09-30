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
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.jooq.Result;
import org.jooq.impl.DSL;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.Coupon;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.DurationTypeEnum;
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
                                       COUPONS.AMOUNT_DISCOUNT,
                                       COUPONS.AMOUNT_CURRENCY,
                                       COUPONS.DURATION,
                                       COUPONS.NUMBER_OF_INVOICES,
                                       COUPONS.MAX_REDEMPTIONS,
                                       COUPONS.START_DATE,
                                       COUPONS.EXPIRATION_DATE,
                                       COUPONS.KB_TENANT_ID)
                           .values(coupon.getCouponCode(),
                                   coupon.getCouponName(),
                                   coupon.getDiscountType().toString(),
                                   coupon.getPercentageDiscount(),
                                   coupon.getAmountDiscount(),
                                   coupon.getAmountCurrency(),
                                   coupon.getDuration().toString(),
                                   (coupon.getDuration().toString().equals(DurationTypeEnum.multiple.toString())) ? coupon.getNumberOfInvoices() : 0,
                                   coupon.getMaxRedemptions(),
                                   coupon.getStartDate(),
                                   coupon.getExpirationDate(),
                                   context.getTenantId().toString())
                           .execute();
                        return null;
                    }
                });

        List<String> products = coupon.getProducts();
        if (null != products) {
            // Add List of Products and Coupon associated to the table
            insertProductsToCoupon(coupon.getCouponCode(), products, context.getTenantId().toString());
        }
    }

    /**
     * Method to insert Products to a Coupon
     * @param couponCode
     * @param products
     * @param tenantId
     * @throws SQLException
     */
    public void insertProductsToCoupon(final String couponCode, final List<String> products, final String tenantId) throws SQLException {
        // Add List of Products and Coupon associated to the table
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
                                   .values(couponCode,
                                           product,
                                           tenantId)
                                   .execute();
                                return null;
                            }
                        });
            }
        }
    }

    /**
     * Method to remove Products to a Coupon
     * @param couponCode
     * @param products
     * @throws SQLException
     */
    public void removeProductsToCoupon(final String couponCode, final List<String> products) throws SQLException {
        if (null != products) {
            logService.log(LogService.LOG_INFO, "Executing query to Remove associated Products of a Coupon in the DB");
            for (final String product : products) {
                execute(dataSource.getConnection(),
                        new WithConnectionCallback<Void>() {
                            @Override
                            public Void withConnection(final Connection conn) throws SQLException {
                                DSL.using(conn, dialect, settings)
                                   .delete(COUPONS_PRODUCTS)
                                   .where(COUPONS_PRODUCTS.COUPON_CODE.equal(couponCode))
                                   .and(COUPONS_PRODUCTS.PRODUCT_NAME.equal(product))
                                   .execute();
                                return null;
                            }
                        });
            }
        }
    }

    /**
     * Method to update a Coupon object in the DB

     */
    public void updateCoupon(final Coupon coupon, final List<String> productsToAdd,
                             final List<String> productsToRemove, final String tenantId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to Update a Coupon in the DB");
        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                           .update(COUPONS)
                           .set(COUPONS.COUPON_NAME, coupon.getCouponName())
                           .set(COUPONS.MAX_REDEMPTIONS, coupon.getMaxRedemptions())
                           .set(COUPONS.START_DATE, coupon.getStartDate())
                           .set(COUPONS.EXPIRATION_DATE, coupon.getExpirationDate())
                           .where(COUPONS.COUPON_CODE.equal(coupon.getCouponCode()))
                           .execute();
                        return null;
                    }
                });

        if (!productsToAdd.isEmpty()) {
            insertProductsToCoupon(coupon.getCouponCode(), productsToAdd, tenantId);
        }
        if (!productsToRemove.isEmpty()) {
            removeProductsToCoupon(coupon.getCouponCode(), productsToRemove);
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

    /**
     * Method to deactivate a Coupon object by couponCode from the DB
     * @param couponCode
     * @return
     * @throws SQLException
     */
    public void deactivateCouponByCode(final String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to Deactivate a Coupon by couponCode in the DB");
        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                           .update(COUPONS)
                           .set(COUPONS.IS_ACTIVE, Byte.valueOf(Constants.BYTE_FALSE))
                           .where(COUPONS.COUPON_CODE.equal(couponCode))
                           .execute();
                        return null;
                    }
                });
    }

    /**
     * Method to delete a Coupon object by couponCode from the DB
     * @param couponCode
     * @return
     * @throws SQLException
     */
    public void deleteCouponByCode(final String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to Delete a Coupon by couponCode in the DB");
        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                           .delete(COUPONS)
                           .where(COUPONS.COUPON_CODE.equal(couponCode))
                           .execute();
                        return null;
                    }
                });
    }

    /**
     * Method to deactivate all the Applications of a Coupon object by couponCode from the DB
     * @param couponCode
     * @return
     * @throws SQLException
     */
    public void deactivateApplicationsOfCoupon(final String couponCode) throws SQLException {
        String notes = "Coupon deactivated on " + new Date(Calendar.getInstance().getTimeInMillis());;
        logService.log(LogService.LOG_INFO, "Executing query to Deactivate all the Applications of a Coupon by couponCode in the DB");
        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                           .update(COUPONS_APPLIED)
                           .set(COUPONS_APPLIED.IS_ACTIVE, Byte.valueOf(Constants.BYTE_FALSE))
                           .set(COUPONS_APPLIED.NOTES, notes)
                           .where(COUPONS_APPLIED.COUPON_CODE.equal(couponCode))
                           .and(COUPONS_APPLIED.IS_ACTIVE.equal(Byte.valueOf(Constants.BYTE_TRUE)))
                           .execute();
                        return null;
                    }
                });
    }

    /**
     * Method to deactivate the Application of a Coupon object by couponCode and subscriptionId from the DB
     * @param couponCode
     * @return
     * @throws SQLException
     */
    public void deactivateApplicationOfCouponByCodeAndSubscription(final String couponCode, final UUID subscriptionId) throws SQLException {
        String notes = "Coupon deactivated on " + new Date(Calendar.getInstance().getTimeInMillis());;
        logService.log(LogService.LOG_INFO, "Executing query to Deactivate the Application of a Coupon by couponCode and subscription Id in the DB");
        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                           .update(COUPONS_APPLIED)
                           .set(COUPONS_APPLIED.IS_ACTIVE, Byte.valueOf(Constants.BYTE_FALSE))
                           .set(COUPONS_APPLIED.NOTES, notes)
                           .where(COUPONS_APPLIED.COUPON_CODE.equal(couponCode))
                           .and(COUPONS_APPLIED.KB_SUBSCRIPTION_ID.equal(subscriptionId.toString()))
                           .and(COUPONS_APPLIED.IS_ACTIVE.equal(Byte.valueOf(Constants.BYTE_TRUE)))
                           .execute();
                        return null;
                    }
                });
    }

    /**
     * Method to increase the number of invoices affected during the invoice generation
     * @param couponCode
     * @param numberOfInvoices
     * @throws SQLException
     */
    public void increaseNumberOfInvoicesAffected(final String couponCode, final Integer numberOfInvoices, final Byte deactivation, final UUID subscriptionId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to Increase the number of invoices affected with the discount application and re-set its Active status");
        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                           .update(COUPONS_APPLIED)
                           .set(COUPONS_APPLIED.NUMBER_OF_INVOICES, numberOfInvoices)
                           .set(COUPONS_APPLIED.IS_ACTIVE, deactivation)
                           .set(COUPONS_APPLIED.NOTES,
                                (deactivation.equals(Byte.valueOf(Constants.BYTE_FALSE))) ?
                                "Coupon Application has been deactivated after being applied in " + numberOfInvoices +
                                " different invoices (max. value of the Coupon " + couponCode + ") for this Subscription"
                                : null)
                           .where(COUPONS_APPLIED.COUPON_CODE.equal(couponCode))
                           .and(COUPONS_APPLIED.KB_SUBSCRIPTION_ID.equal(subscriptionId.toString()))
                           .execute();
                        return null;
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
    public void applyCoupon(final String couponCode, final int maxInvoices, final UUID subscriptionId, final UUID accountId, final TenantContext context) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to store an applied Coupon with its subscriptionId and accountId in the DB");
        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                           .insertInto(COUPONS_APPLIED,
                                       COUPONS_APPLIED.COUPON_CODE,
                                       COUPONS_APPLIED.CREATED_DATE,
                                       COUPONS_APPLIED.MAX_INVOICES,
                                       COUPONS_APPLIED.KB_SUBSCRIPTION_ID,
                                       COUPONS_APPLIED.KB_ACCOUNT_ID,
                                       COUPONS_APPLIED.KB_TENANT_ID)
                           .values(couponCode,
                                   new Date(Calendar.getInstance().getTimeInMillis()),
                                   maxInvoices,
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
     * Method to get a list of Coupons Applied from the DB using its couponCode
     * @param couponCode
     * @return
     * @throws SQLException
     */
    public List<CouponsAppliedRecord> getCouponsAppliedByCouponCode(final String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to get a List of Coupons Applied from the DB");
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<Result<CouponsAppliedRecord>>() {
                           @Override
                           public Result<CouponsAppliedRecord> withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_APPLIED)
                                         .where(COUPONS_APPLIED.COUPON_CODE.equal(couponCode))
                                         .orderBy(COUPONS_APPLIED.KB_ACCOUNT_ID, COUPONS_APPLIED.CREATED_DATE)
                                         .fetch();
                           }
                       });
    }

    /**
     * Method to get a list of Active Coupons Applied from the DB using its couponCode
     * @param couponCode
     * @return
     * @throws SQLException
     */
    public List<CouponsAppliedRecord> getActiveCouponsAppliedByCouponCode(final String couponCode) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to get a List of Active Coupons Applied from the DB");
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<Result<CouponsAppliedRecord>>() {
                           @Override
                           public Result<CouponsAppliedRecord> withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_APPLIED)
                                         .where(COUPONS_APPLIED.COUPON_CODE.equal(couponCode))
                                         .and(COUPONS_APPLIED.IS_ACTIVE.equal(Byte.valueOf(Constants.BYTE_TRUE)))
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
     * Method to get a Coupon Applied object by couponCode and subscriptionId
     * @param couponCode
     * @param subscriptionId
     * @return
     * @throws SQLException
     */
    public CouponsAppliedRecord getCouponApplied(final String couponCode, final UUID subscriptionId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to get a Coupon Applied object by couponCode and subscriptionId from the DB");
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<CouponsAppliedRecord>() {
                           @Override
                           public CouponsAppliedRecord withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_APPLIED)
                                         .where(COUPONS_APPLIED.COUPON_CODE.equal(couponCode))
                                         .and(COUPONS_APPLIED.KB_SUBSCRIPTION_ID.equal(subscriptionId.toString()))
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
    public CouponsAppliedRecord getActiveCouponAppliedBySubscription(final UUID subscriptionId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to get a List of Coupons Applied from the DB using subscriptionId");
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<CouponsAppliedRecord>() {
                           @Override
                           public CouponsAppliedRecord withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_APPLIED)
                                         .where(COUPONS_APPLIED.KB_SUBSCRIPTION_ID.equal(subscriptionId.toString()))
                                         .and(COUPONS_APPLIED.IS_ACTIVE.equal(Byte.valueOf(Constants.BYTE_TRUE)))
                                         .fetchOne();
                           }
                       });
    }

    /**
     * Method to get a Coupon Applied object by code and subscriptionId
     * @param couponCode
     * @param subscriptionId
     * @return
     * @throws SQLException
     */
    public CouponsAppliedRecord getCouponAppliedByCodeAndSubscription(final String couponCode, final UUID subscriptionId) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to get a List of Coupons Applied from the DB using subscriptionId");
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<CouponsAppliedRecord>() {
                           @Override
                           public CouponsAppliedRecord withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS_APPLIED)
                                         .where(COUPONS_APPLIED.KB_SUBSCRIPTION_ID.equal(subscriptionId.toString()))
                                         .and(COUPONS_APPLIED.COUPON_CODE.equal(couponCode))
                                         .fetchOne();
                           }
                       });
    }

    /**
     * Method to get a list of Coupons Applied from the DB using its accountId
     * @param accountId
     * @return
     * @throws SQLException
     */
    public List<CouponsAppliedRecord> getActiveCouponsAppliedByAccountIdAndProduct(final UUID accountId, final String productName) throws SQLException {
        logService.log(LogService.LOG_INFO, "Executing query to get a List of Coupons Applied from the DB");
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<Result<CouponsAppliedRecord>>() {
                           @Override
                           public Result<CouponsAppliedRecord> withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                       .select(COUPONS_APPLIED.fields())
                                       .from(COUPONS_APPLIED)
                                       .join(COUPONS_PRODUCTS)
                                       .on(COUPONS_APPLIED.COUPON_CODE.equal(COUPONS_PRODUCTS.COUPON_CODE))
                                       .where(COUPONS_APPLIED.KB_ACCOUNT_ID.equal(accountId.toString()))
                                       .and(COUPONS_PRODUCTS.PRODUCT_NAME.equal(productName))
                                       .fetch().into(COUPONS_APPLIED);
                           }
                       });
    }

}
