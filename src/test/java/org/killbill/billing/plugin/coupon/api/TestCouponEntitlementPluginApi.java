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

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.entitlement.api.Entitlement;
import org.killbill.billing.entitlement.api.EntitlementApi;
import org.killbill.billing.entitlement.api.EntitlementApiException;
import org.killbill.billing.entitlement.api.SubscriptionApi;
import org.killbill.billing.entitlement.api.SubscriptionApiException;
import org.killbill.billing.entitlement.api.SubscriptionEvent;
import org.killbill.billing.entitlement.api.SubscriptionEventType;
import org.killbill.billing.entitlement.plugin.api.EntitlementPluginApiException;
import org.killbill.billing.entitlement.plugin.api.OnFailureEntitlementResult;
import org.killbill.billing.entitlement.plugin.api.OnSuccessEntitlementResult;
import org.killbill.billing.entitlement.plugin.api.OperationType;
import org.killbill.billing.entitlement.plugin.api.PriorEntitlementResult;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.mock.MockEntitlement;
import org.killbill.billing.plugin.coupon.mock.MockEntitlementContext;
import org.killbill.billing.plugin.coupon.mock.MockProduct;
import org.killbill.billing.plugin.coupon.mock.MockSubscription;
import org.killbill.billing.plugin.coupon.mock.MockSubscriptionEvent;
import org.killbill.billing.plugin.coupon.mock.TestCouponHelper;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.DefaultPriorEntitlementResult;
import org.killbill.billing.plugin.coupon.model.DurationTypeEnum;
import org.killbill.billing.plugin.coupon.model.ErrorPriorEntitlementResult;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillLogService;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by maguero on 11/09/15.
 */
public class TestCouponEntitlementPluginApi extends Mockito {

    private CouponEntitlementPluginApi entitlementPluginApi;
    private CouponPluginApi couponPluginApi;
    private OSGIKillbillAPI osgiKillbillAPI;
    private EntitlementApi entitlementApi;
    private SubscriptionApi subscriptionApi;
    private OSGIKillbillLogService logService;
    private MockEntitlementContext context;
    private MockEntitlement entitlement;
    private List<PluginProperty> properties;
    private PluginProperty pluginProperty;
    private MockSubscription subscription;

    @Before
    public void setUp() {
        osgiKillbillAPI = mock(OSGIKillbillAPI.class);
        logService = mock(OSGIKillbillLogService.class);
        couponPluginApi = mock(CouponPluginApi.class);
        entitlementPluginApi = new CouponEntitlementPluginApi(null, osgiKillbillAPI, logService, couponPluginApi);
        entitlementApi = mock(EntitlementApi.class);
        subscriptionApi = mock(SubscriptionApi.class);

        context = new MockEntitlementContext();
        entitlement = new MockEntitlement();
        pluginProperty = new PluginProperty(CouponEntitlementPluginApi.COUPON_PROPERTY, Constants.COUPON_CODE, false);
        properties = new ArrayList<PluginProperty>();
        properties.add(pluginProperty);
        subscription = new MockSubscription();

        when(osgiKillbillAPI.getEntitlementApi()).thenReturn(entitlementApi);
        when(osgiKillbillAPI.getSubscriptionApi()).thenReturn(subscriptionApi);
    }

    // ----------------------
    //     FAILURE CALL
    // ----------------------

    @Test
    public void testOnFailureCall() throws EntitlementPluginApiException {

        OnFailureEntitlementResult result = entitlementPluginApi.onFailureCall(null, null);
        assertTrue(result == null);

    }

    // ----------------------
    //     PRIOR CALL
    // ----------------------

    @Test
    public void testPriorCallOtherOperation() throws EntitlementPluginApiException {

        MockEntitlementContext otherContext = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CANCEL_SUBSCRIPTION;
            }
        };
        PriorEntitlementResult result = entitlementPluginApi.priorCall(otherContext, null);
        assertTrue(result instanceof DefaultPriorEntitlementResult);

    }

    @Test
    public void testPriorCallNoCoupon() throws EntitlementPluginApiException {

        PriorEntitlementResult result = entitlementPluginApi.priorCall(context, null);
        assertTrue(result instanceof DefaultPriorEntitlementResult);

    }

    @Test
    public void testPriorCallInvalidCoupon() throws EntitlementPluginApiException, CouponApiException {

        when(couponPluginApi.validateCoupon(any(), any(), any(), any())).thenThrow(CouponApiException.class);

        PriorEntitlementResult result = entitlementPluginApi.priorCall(context, properties);
        assertTrue(result instanceof ErrorPriorEntitlementResult);
    }

    @Test
    public void testPriorCallValidCoupon() throws EntitlementPluginApiException, CouponApiException {

        when(couponPluginApi.validateCoupon(any(), any(), any(), any())).thenReturn(true);

        PriorEntitlementResult result = entitlementPluginApi.priorCall(context, properties);
        assertTrue(result instanceof DefaultPriorEntitlementResult);
    }

    // ----------------------
    //     SUCCESS CALL
    // ----------------------

    @Test
    public void testOnSuccessCallOtherOperation() throws EntitlementPluginApiException {

        MockEntitlementContext context = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CANCEL_SUBSCRIPTION;
            }
        };
        OnSuccessEntitlementResult result = entitlementPluginApi.onSuccessCall(context, null);
        assertTrue(result == null);

    }

    @Test
    public void testOnSuccessCallNoCoupon() throws EntitlementPluginApiException {

        boolean result = entitlementPluginApi.applyCouponToNewSubscription(context, null);
        assertTrue(result);

    }

    @Test
    public void testOnSuccessCallCouponNoEntitlement() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException {

        when(couponPluginApi.getCouponByCode(any())).thenReturn(TestCouponHelper.createBaseCoupon());
        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(null);
        when(couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(any(), any())).thenReturn(null);
        when(couponPluginApi.applyCoupon(any(), any(), any(), any(), any())).thenReturn(true);

        try {
            entitlementPluginApi.onSuccessCall(context, properties);
        } catch (EntitlementPluginApiException e) {
            assertTrue(e.getMessage().contains("There are no Entitlements"));
            return;
        }
        fail();
    }

    @Test(expected = EntitlementPluginApiException.class)
    public void testOnSuccessCallInvalidCoupon() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);

        when(couponPluginApi.getCouponByCode(any())).thenReturn(TestCouponHelper.createBaseCoupon());
        when(couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(any(), any())).thenReturn(null);
        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(couponPluginApi.applyCoupon(any(), any(), any(), any(), any())).thenThrow(CouponApiException.class);

        entitlementPluginApi.onSuccessCall(context, properties);
        fail();
    }

    @Test
    public void testOnSuccessCallValidCoupon() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);

        when(couponPluginApi.getCouponByCode(any())).thenReturn(TestCouponHelper.createBaseCoupon());
        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(any(), any())).thenReturn(null);
        when(couponPluginApi.applyCoupon(any(), any(), any(), any(), any())).thenReturn(true);

        boolean result = entitlementPluginApi.applyCouponToNewSubscription(context, properties);
        assertTrue(result);

    }

    @Test
    public void testOnSuccessCallNoCoupons() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);

        when(couponPluginApi.getCouponByCode(any())).thenReturn(null);
        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(any(), any())).thenReturn(null);
        when(couponPluginApi.applyCoupon(any(), any(), any(), any(), any())).thenReturn(true);

        boolean result = entitlementPluginApi.applyCouponToNewSubscription(context, null);
        assertTrue(result);
    }

    @Test
    public void testOnSuccessCallAppliedCoupons() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);

        CouponsRecord c1 = TestCouponHelper.createBaseCoupon();
        c1.setCouponCode("c1");
        c1.setDuration(DurationTypeEnum.once.toString());
        c1.setNumberOfInvoices(1);
        c1.setPercentageDiscount(15D);

        CouponsRecord c2 = TestCouponHelper.createBaseCoupon();
        c2.setCouponCode("c2");
        c2.setDuration(DurationTypeEnum.multiple.toString());
        c2.setNumberOfInvoices(6);
        c2.setPercentageDiscount(20D);

        CouponsAppliedRecord coupon1 = TestCouponHelper.createBaseCouponApplied(UUID.randomUUID(), UUID.randomUUID());
        coupon1.setCouponCode("c1");
        coupon1.setNumberOfInvoices(2);

        CouponsAppliedRecord coupon2 = TestCouponHelper.createBaseCouponApplied(UUID.randomUUID(), UUID.randomUUID());
        coupon2.setCouponCode("c2");
        coupon2.setNumberOfInvoices(4);

        List<CouponsAppliedRecord> appliedCoupons = new ArrayList<CouponsAppliedRecord>();
        appliedCoupons.add(coupon1);
        appliedCoupons.add(coupon2);

        when(couponPluginApi.getCouponByCode(Constants.COUPON_CODE)).thenReturn(null);
        when(couponPluginApi.getCouponByCode("c1")).thenReturn(c1);
        when(couponPluginApi.getCouponByCode("c2")).thenReturn(c2);
        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(any(), any())).thenReturn(appliedCoupons);
        when(couponPluginApi.applyCoupon(any(), any(), any(), any(), any())).thenReturn(true);

        boolean result = entitlementPluginApi.applyCouponToNewSubscription(context, null);
        assertTrue(result);
        verify(couponPluginApi, times(1)).applyCoupon(matches("c2"), eq(2), any(), any(), any());

    }

    @Test
    public void testOnSuccessCallAppliedCouponsSameDiscount() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);

        CouponsRecord c1 = TestCouponHelper.createBaseCoupon();
        c1.setCouponCode("c1");
        c1.setDuration(DurationTypeEnum.once.toString());
        c1.setNumberOfInvoices(1);
        c1.setPercentageDiscount(20D);

        CouponsRecord c2 = TestCouponHelper.createBaseCoupon();
        c2.setCouponCode("c2");
        c2.setDuration(DurationTypeEnum.multiple.toString());
        c2.setNumberOfInvoices(6);
        c2.setPercentageDiscount(20D);

        CouponsAppliedRecord coupon1 = TestCouponHelper.createBaseCouponApplied(UUID.randomUUID(), UUID.randomUUID());
        coupon1.setCouponCode("c1");
        coupon1.setNumberOfInvoices(2);

        CouponsAppliedRecord coupon2 = TestCouponHelper.createBaseCouponApplied(UUID.randomUUID(), UUID.randomUUID());
        coupon2.setCouponCode("c2");
        coupon2.setNumberOfInvoices(4);

        List<CouponsAppliedRecord> appliedCoupons = new ArrayList<CouponsAppliedRecord>();
        appliedCoupons.add(coupon1);
        appliedCoupons.add(coupon2);

        when(couponPluginApi.getCouponByCode(Constants.COUPON_CODE)).thenReturn(null);
        when(couponPluginApi.getCouponByCode("c1")).thenReturn(c1);
        when(couponPluginApi.getCouponByCode("c2")).thenReturn(c2);
        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(any(), any())).thenReturn(appliedCoupons);
        when(couponPluginApi.applyCoupon(any(), any(), any(), any(), any())).thenReturn(true);

        boolean result = entitlementPluginApi.applyCouponToNewSubscription(context, null);
        assertTrue(result);
        verify(couponPluginApi, times(1)).applyCoupon(matches("c1"), eq(4), any(), any(), any());
    }

    @Test
    public void testOnSuccessCallWithCouponAndAppliedCoupons() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);

        CouponsRecord c1 = TestCouponHelper.createBaseCoupon();
        c1.setDuration(DurationTypeEnum.multiple.toString());
        c1.setNumberOfInvoices(4);
        c1.setPercentageDiscount(25D);

        CouponsRecord c2 = TestCouponHelper.createBaseCoupon();
        c2.setCouponCode("c2");
        c2.setDuration(DurationTypeEnum.multiple.toString());
        c2.setNumberOfInvoices(6);
        c2.setPercentageDiscount(20D);

        CouponsAppliedRecord coupon1 = TestCouponHelper.createBaseCouponApplied(UUID.randomUUID(), UUID.randomUUID());
        coupon1.setCouponCode("c2");
        coupon1.setNumberOfInvoices(2);

        List<CouponsAppliedRecord> appliedCoupons = new ArrayList<CouponsAppliedRecord>();
        appliedCoupons.add(coupon1);

        when(couponPluginApi.getCouponByCode(Constants.COUPON_CODE)).thenReturn(c1);
        when(couponPluginApi.getCouponByCode("c2")).thenReturn(c2);
        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(any(), any())).thenReturn(appliedCoupons);
        when(couponPluginApi.applyCoupon(any(), any(), any(), any(), any())).thenReturn(true);

        boolean result = entitlementPluginApi.applyCouponToNewSubscription(context, properties);
        assertTrue(result);
        verify(couponPluginApi, times(1)).applyCoupon(matches(Constants.COUPON_CODE), eq(4), any(), any(), any());
    }

    // ******************************************************************
    //                      CHANGE PLAN
    // ******************************************************************

    @Test
    public void testOnSuccessCallChangePlanEntitlementApiException() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {
        MockEntitlementContext otherContext = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CHANGE_PLAN;
            }
        };
        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);
        CouponsAppliedRecord couponsAppliedRecord = new CouponsAppliedRecord();
        couponsAppliedRecord.setCouponCode(Constants.COUPON_CODE);

        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenThrow(EntitlementApiException.class);
        entitlementPluginApi.onSuccessCall(otherContext, properties);
        verify(couponPluginApi, never()).deactivateApplicationOfCouponByCodeAndSubscription(any(), any());
    }

    @Test
    public void testOnSuccessCallChangePlanSubscriptionApiException() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {
        MockEntitlementContext otherContext = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CHANGE_PLAN;
            }
        };
        when(subscriptionApi.getSubscriptionForEntitlementId(any(), any())).thenThrow(SubscriptionApiException.class);

        entitlementPluginApi.onSuccessCall(otherContext, properties);
        verify(couponPluginApi, never()).deactivateApplicationOfCouponByCodeAndSubscription(any(), any());
    }

    @Test(expected = EntitlementPluginApiException.class)
    public void testOnSuccessCallChangePlanEntitlementPluginApiException() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {
        MockEntitlementContext otherContext = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CHANGE_PLAN;
            }
        };
        MockSubscription otherSubscription = new MockSubscription() {
            @Override
            public List<SubscriptionEvent> getSubscriptionEvents() {
                List<SubscriptionEvent> result = new ArrayList<SubscriptionEvent>();
                MockSubscriptionEvent subscriptionEvent = new MockSubscriptionEvent() {
                    @Override
                    public SubscriptionEventType getSubscriptionEventType() {
                        return SubscriptionEventType.CHANGE;
                    }

                    @Override
                    public BillingPeriod getPrevBillingPeriod() {
                        return BillingPeriod.MONTHLY;
                    }

                    @Override
                    public BillingPeriod getNextBillingPeriod() {
                        return BillingPeriod.ANNUAL;
                    }

                };
                result.add(subscriptionEvent);
                return result;
            }
        };

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);
        CouponsAppliedRecord couponsAppliedRecord = new CouponsAppliedRecord();
        couponsAppliedRecord.setCouponCode(Constants.COUPON_CODE);

        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(), any())).thenReturn(otherSubscription);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any())).thenThrow(EntitlementApiException.class);

        entitlementPluginApi.onSuccessCall(otherContext, properties);
        verify(couponPluginApi, never()).deactivateApplicationOfCouponByCodeAndSubscription(any(), any());
    }

    @Test(expected = EntitlementPluginApiException.class)
    public void testOnSuccessCallChangePlanSQLException() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {
        MockEntitlementContext otherContext = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CHANGE_PLAN;
            }
        };
        MockSubscription otherSubscription = new MockSubscription() {
            @Override
            public List<SubscriptionEvent> getSubscriptionEvents() {
                List<SubscriptionEvent> result = new ArrayList<SubscriptionEvent>();
                MockSubscriptionEvent subscriptionEvent = new MockSubscriptionEvent() {
                    @Override
                    public SubscriptionEventType getSubscriptionEventType() {
                        return SubscriptionEventType.CHANGE;
                    }

                    @Override
                    public BillingPeriod getPrevBillingPeriod() {
                        return BillingPeriod.MONTHLY;
                    }

                    @Override
                    public BillingPeriod getNextBillingPeriod() {
                        return BillingPeriod.ANNUAL;
                    }

                };
                result.add(subscriptionEvent);
                return result;
            }
        };

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);
        CouponsAppliedRecord couponsAppliedRecord = new CouponsAppliedRecord();
        couponsAppliedRecord.setCouponCode(Constants.COUPON_CODE);

        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(), any())).thenReturn(otherSubscription);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any())).thenThrow(SQLException.class);

        entitlementPluginApi.onSuccessCall(otherContext, properties);
        verify(couponPluginApi, never()).deactivateApplicationOfCouponByCodeAndSubscription(any(), any());
    }

    @Test
    public void testOnSuccessCallChangePlanBillingPeriod() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {
        MockEntitlementContext otherContext = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CHANGE_PLAN;
            }
        };
        MockSubscription otherSubscription = new MockSubscription() {
            @Override
            public List<SubscriptionEvent> getSubscriptionEvents() {
                List<SubscriptionEvent> result = new ArrayList<SubscriptionEvent>();
                MockSubscriptionEvent subscriptionEvent = new MockSubscriptionEvent() {
                    @Override
                    public SubscriptionEventType getSubscriptionEventType() {
                        return SubscriptionEventType.CHANGE;
                    }

                    @Override
                    public BillingPeriod getPrevBillingPeriod() {
                        return BillingPeriod.MONTHLY;
                    }

                    @Override
                    public BillingPeriod getNextBillingPeriod() {
                        return BillingPeriod.ANNUAL;
                    }

                };
                result.add(subscriptionEvent);
                return result;
            }
        };

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);
        CouponsAppliedRecord couponsAppliedRecord = new CouponsAppliedRecord();
        couponsAppliedRecord.setCouponCode(Constants.COUPON_CODE);

        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(), any())).thenReturn(otherSubscription);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any())).thenReturn(couponsAppliedRecord);

        entitlementPluginApi.onSuccessCall(otherContext, properties);
        verify(couponPluginApi, times(1)).deactivateApplicationOfCouponByCodeAndSubscription(matches(Constants.COUPON_CODE), any());
    }

    @Test
    public void testOnSuccessCallChangePlanActiveCouponProduct() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {
        MockEntitlementContext otherContext = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CHANGE_PLAN;
            }
        };
        MockSubscription otherSubscription = new MockSubscription() {
            @Override
            public List<SubscriptionEvent> getSubscriptionEvents() {
                List<SubscriptionEvent> result = new ArrayList<SubscriptionEvent>();
                MockSubscriptionEvent subscriptionEvent = new MockSubscriptionEvent() {
                    @Override
                    public SubscriptionEventType getSubscriptionEventType() {
                        return SubscriptionEventType.CHANGE;
                    }

                    @Override
                    public Product getNextProduct() {
                        MockProduct product = new MockProduct() {
                            @Override
                            public String getName() {
                                return "Sports";
                            }
                        };
                        return product;
                    }
                };
                result.add(subscriptionEvent);
                return result;
            }
        };

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);
        CouponsAppliedRecord couponsAppliedRecord = new CouponsAppliedRecord();
        couponsAppliedRecord.setCouponCode(Constants.COUPON_CODE);
        CouponsProductsRecord couponProduct = new CouponsProductsRecord();
        couponProduct.setCouponCode(Constants.COUPON_CODE);
        couponProduct.setProductName("Sports");
        final List<CouponsProductsRecord> productsOfCoupon = new ArrayList<CouponsProductsRecord>();
        productsOfCoupon.add(couponProduct);

        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(), any())).thenReturn(otherSubscription);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any())).thenReturn(couponsAppliedRecord);
        when(couponPluginApi.getProductsOfCoupon(any())).thenReturn(productsOfCoupon);

        boolean result = entitlementPluginApi.hasChangedBillingPeriodOrProduct(otherContext);
        assertFalse(result);
        entitlementPluginApi.onSuccessCall(otherContext, properties);
        verify(couponPluginApi, never()).deactivateApplicationOfCouponByCodeAndSubscription(any(), any());
    }

    @Test
    public void testOnSuccessCallChangePlanNonCouponProduct() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException, EntitlementPluginApiException {
        MockEntitlementContext otherContext = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CHANGE_PLAN;
            }
        };
        MockSubscription otherSubscription = new MockSubscription() {
            @Override
            public List<SubscriptionEvent> getSubscriptionEvents() {
                List<SubscriptionEvent> result = new ArrayList<SubscriptionEvent>();
                MockSubscriptionEvent subscriptionEvent = new MockSubscriptionEvent() {
                    @Override
                    public SubscriptionEventType getSubscriptionEventType() {
                        return SubscriptionEventType.CHANGE;
                    }

                    @Override
                    public Product getNextProduct() {
                        MockProduct product = new MockProduct() {
                            @Override
                            public String getName() {
                                return "Sports";
                            }
                        };
                        return product;
                    }
                };
                result.add(subscriptionEvent);
                return result;
            }
        };

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);
        CouponsAppliedRecord couponsAppliedRecord = new CouponsAppliedRecord();
        couponsAppliedRecord.setCouponCode(Constants.COUPON_CODE);
        CouponsProductsRecord couponProduct = new CouponsProductsRecord();
        couponProduct.setCouponCode(Constants.COUPON_CODE);
        couponProduct.setProductName("Standard");
        final List<CouponsProductsRecord> productsOfCoupon = new ArrayList<CouponsProductsRecord>();
        productsOfCoupon.add(couponProduct);

        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(), any())).thenReturn(otherSubscription);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any())).thenReturn(couponsAppliedRecord);
        when(couponPluginApi.getProductsOfCoupon(any())).thenReturn(productsOfCoupon);

        entitlementPluginApi.onSuccessCall(otherContext, properties);
        verify(couponPluginApi, times(1)).deactivateApplicationOfCouponByCodeAndSubscription(matches(Constants.COUPON_CODE), any());
    }

}
