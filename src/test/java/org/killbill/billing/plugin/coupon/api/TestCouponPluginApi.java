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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountUserApi;
import org.killbill.billing.account.api.MutableAccountData;
import org.killbill.billing.catalog.api.BillingActionPolicy;
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.catalog.api.Limit;
import org.killbill.billing.catalog.api.Plan;
import org.killbill.billing.catalog.api.PlanPhase;
import org.killbill.billing.catalog.api.PlanPhasePriceOverride;
import org.killbill.billing.catalog.api.PriceList;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.catalog.api.ProductCategory;
import org.killbill.billing.entitlement.api.Entitlement;
import org.killbill.billing.entitlement.api.EntitlementApiException;
import org.killbill.billing.entitlement.api.Subscription;
import org.killbill.billing.entitlement.api.SubscriptionApi;
import org.killbill.billing.entitlement.api.SubscriptionEvent;
import org.killbill.billing.entitlement.api.SubscriptionEventType;
import org.killbill.billing.plugin.coupon.dao.CouponDao;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.Coupon;
import org.killbill.billing.plugin.coupon.model.CouponTenantContext;
import org.killbill.billing.tenant.api.Tenant;
import org.killbill.billing.tenant.api.TenantApiException;
import org.killbill.billing.tenant.api.TenantUserApi;
import org.killbill.billing.util.callcontext.CallContext;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by jgomez on 07/09/15.
 */
public class TestCouponPluginApi extends Mockito {

    private CouponPluginApi couponPluginApi;
    private OSGIKillbillAPI osgiKillbillAPI;
    private TenantUserApi tenantUserApi;
    private AccountUserApi accountUserApi;
    private SubscriptionApi subscriptionApi;
    private CouponDao dao;
    private LogService logService;
    private UUID tenantId;

    @Before
    public void setUp() {
        osgiKillbillAPI = mock(OSGIKillbillAPI.class);
        dao = mock(CouponDao.class);
        logService = mock(LogService.class);
        tenantUserApi = mock(TenantUserApi.class);
        accountUserApi = mock(AccountUserApi.class);
        subscriptionApi = mock(SubscriptionApi.class);
        tenantId = UUID.randomUUID();
        couponPluginApi = new CouponPluginApi(logService, dao, osgiKillbillAPI);
    }

    @Test
    public void testGetAllCouponsOK() throws Exception {
        when(dao.getAllCoupons()).thenReturn(new ArrayList<>());

        List<CouponsRecord> result = couponPluginApi.getAllCoupons();

        assertTrue(result.isEmpty());
    }

    @Test(expected = SQLException.class)
    public void testGetAllCouponsSQLException() throws Exception {
        when(dao.getAllCoupons()).thenThrow(SQLException.class);

        couponPluginApi.getAllCoupons();
    }

    @Test
    public void testGetAllCouponsAppliedOK() throws Exception {
        when(dao.getAllCouponsApplied()).thenReturn(new ArrayList<>());

        List<CouponsAppliedRecord> result = couponPluginApi.getAllCouponsApplied();

        assertTrue(result.isEmpty());
    }

    @Test(expected = SQLException.class)
    public void testGetAllCouponsAppliedSQLException() throws Exception {
        when(dao.getAllCouponsApplied()).thenThrow(SQLException.class);

        couponPluginApi.getAllCouponsApplied();
    }

    @Test
    public void testGetCouponByCodeOK() throws Exception {
        CouponsRecord coupon = new CouponsRecord();
        coupon.setCouponCode(Constants.COUPON_CODE);
        when(dao.getCouponByCode(anyString())).thenReturn(coupon);

        CouponsRecord result = couponPluginApi.getCouponByCode(Constants.COUPON_CODE);

        assertEquals(result, coupon);
    }

    @Test(expected = SQLException.class)
    public void testGetCouponByCodeSQLException() throws Exception {
        when(dao.getCouponByCode(anyString())).thenThrow(SQLException.class);
        couponPluginApi.getCouponByCode(Constants.COUPON_CODE);
    }

    @Test
    public void testGetTenantIdOK() throws Exception {
        Tenant tenant = buildMockTenant();

        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);

        UUID result = couponPluginApi.getTenantId("apiKey");

        assertEquals(result, tenantId);
    }

    @Test(expected = CouponApiException.class)
    public void testGetTenantIdCouponApiException() throws Exception {
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(null);

        couponPluginApi.getTenantId("apiKey");
    }

    @Test(expected = CouponApiException.class)
    public void testGetTenantIdTenantApiException() throws Exception {
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenThrow(TenantApiException.class);

        couponPluginApi.getTenantId("apiKey");
    }

    @Test(expected = CouponApiException.class)
    public void testGetTenantIdWithNullTenant() throws Exception {
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(null);

        couponPluginApi.getTenantId("apiKey");
    }

    @Test
    public void testGetCouponAppliedOK() throws Exception {
        CouponsAppliedRecord couponApplied = new CouponsAppliedRecord();
        couponApplied.setCouponCode(Constants.COUPON_CODE);
        when(dao.getCouponApplied(anyString(), any(UUID.class), any(UUID.class))).thenReturn(couponApplied);

        CouponsAppliedRecord result = couponPluginApi.getCouponApplied(Constants.COUPON_CODE, UUID.randomUUID(), UUID.randomUUID());

        assertEquals(result, couponApplied);
    }

    @Test(expected = SQLException.class)
    public void testGetCouponAppliedSQLException() throws Exception {
        when(dao.getCouponApplied(anyString(), any(UUID.class), any(UUID.class))).thenThrow(SQLException.class);
        couponPluginApi.getCouponApplied(Constants.COUPON_CODE, UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    public void testGetCouponAppliedBySubscriptionOK() throws Exception {
        CouponsAppliedRecord couponApplied = new CouponsAppliedRecord();
        couponApplied.setCouponCode(Constants.COUPON_CODE);
        when(dao.getCouponAppliedBySubscription(any(UUID.class))).thenReturn(couponApplied);

        CouponsAppliedRecord result = couponPluginApi.getCouponAppliedBySubscription(UUID.randomUUID());

        assertEquals(result, couponApplied);
    }

    @Test(expected = SQLException.class)
    public void testGetCouponAppliedBySubscriptionSQLException() throws Exception {
        when(dao.getCouponAppliedBySubscription(any(UUID.class))).thenThrow(SQLException.class);
        couponPluginApi.getCouponAppliedBySubscription(UUID.randomUUID());
    }

    @Test
    public void testGetCouponAppliedByAccountOK() throws Exception {
        List<CouponsAppliedRecord> couponsAppliedList = new ArrayList<>();
        CouponsAppliedRecord couponApplied = new CouponsAppliedRecord();
        couponApplied.setCouponCode(Constants.COUPON_CODE);
        couponsAppliedList.add(couponApplied);
        when(dao.getCouponsAppliedByAccountId(any(UUID.class))).thenReturn(couponsAppliedList);

        List<CouponsAppliedRecord> result = couponPluginApi.getCouponsAppliedByAccountId(UUID.randomUUID());

        assertEquals(result, couponsAppliedList);
    }

    @Test
    public void testGetCouponAppliedByAccountSQLException() throws Exception {
        when(dao.getCouponsAppliedByAccountId(any(UUID.class))).thenThrow(SQLException.class);

        List<CouponsAppliedRecord> result = couponPluginApi.getCouponsAppliedByAccountId(UUID.randomUUID());

        assertEquals(result, new ArrayList<CouponsProductsRecord>());
    }

    @Test
    public void testGetProductsOfCouponOK() throws Exception {
        List<CouponsProductsRecord> couponsProductsRecordList = buildListOfCouponProducts();

        when(dao.getProductsOfCoupon(anyString())).thenReturn(couponsProductsRecordList);

        List<CouponsProductsRecord> result = couponPluginApi.getProductsOfCoupon(Constants.COUPON_CODE);

        assertEquals(result.get(0).getCouponCode(), couponsProductsRecordList.get(0).getCouponCode());
    }

    @Test
    public void testCreateCouponOK() throws Exception {
        Tenant tenant = buildMockTenant();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        couponPluginApi.createCoupon(new Coupon(), tenantContext);
    }

    @Test
    public void testApplyCouponOK() throws Exception {
        Tenant tenant = buildMockTenant();
        Account account = buildMockAccount();
        Subscription subscription = buildMockSubscription();
        CouponsRecord coupon = new CouponsRecord();
        coupon.setCouponCode(Constants.COUPON_CODE);
        coupon.setIsActive(Byte.valueOf(Constants.ACTIVE_TRUE));
        List<CouponsProductsRecord> couponProductsList = new ArrayList<>();
        CouponsProductsRecord couponsProductsRecord = new CouponsProductsRecord();
        couponsProductsRecord.setProductName("fakeName");
        couponProductsList.add(couponsProductsRecord);

        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(UUID.class), any(TenantContext.class))).thenReturn(account);
        when(osgiKillbillAPI.getSubscriptionApi()).thenReturn(subscriptionApi);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(UUID.class), any(TenantContext.class))).thenReturn(subscription);
        when(dao.getCouponByCode(anyString())).thenReturn(coupon);
        when(dao.getProductsOfCoupon(anyString())).thenReturn(couponProductsList);

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        Boolean result = couponPluginApi.applyCoupon(Constants.COUPON_CODE, UUID.randomUUID(), UUID.randomUUID(), tenantContext);

        assertTrue(result);
    }

    @Test(expected = CouponApiException.class)
    public void testApplyCouponWithCouponApiException() throws Exception {
        Tenant tenant = buildMockTenant();
        Account account = buildMockAccount();
        Subscription subscription = buildMockSubscription();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(UUID.class), any(TenantContext.class))).thenReturn(account);
        when(osgiKillbillAPI.getSubscriptionApi()).thenReturn(subscriptionApi);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(UUID.class), any(TenantContext.class))).thenReturn(subscription);
        when(dao.getCouponByCode(anyString())).thenReturn(null);

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        couponPluginApi.applyCoupon(Constants.COUPON_CODE, UUID.randomUUID(), UUID.randomUUID(), tenantContext);
    }

    @Test
    public void testApplyCouponWithNullAccount() throws Exception {
        Tenant tenant = buildMockTenant();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(osgiKillbillAPI.getSubscriptionApi()).thenReturn(subscriptionApi);

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        Boolean result = couponPluginApi.applyCoupon(Constants.COUPON_CODE, UUID.randomUUID(), null, tenantContext);

        assertFalse(result);
    }

    @Test
    public void testApplyCouponWithNullSubscription() throws Exception {
        Tenant tenant = buildMockTenant();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        Boolean result = couponPluginApi.applyCoupon(Constants.COUPON_CODE, null, UUID.randomUUID(), tenantContext);

        assertFalse(result);
    }

    @Test
    public void testGetProductsOfCouponSQLException() throws Exception {
        when(dao.getProductsOfCoupon(anyString())).thenThrow(SQLException.class);

        List<CouponsProductsRecord> result = couponPluginApi.getProductsOfCoupon(Constants.COUPON_CODE);

        assertEquals(result, new ArrayList<CouponsProductsRecord>());
    }

    @Test(expected = CouponApiException.class)
    public void testApplyCouponWithCouponApiExceptionAfterValidation() throws Exception {
        Tenant tenant = buildMockTenant();
        Account account = buildMockAccount();
        Subscription subscription = buildMockSubscription();
        CouponsRecord coupon = new CouponsRecord();
        coupon.setCouponCode(Constants.COUPON_CODE);
        coupon.setIsActive(Byte.valueOf(Constants.ACTIVE_TRUE));
        List<CouponsProductsRecord> couponProductsList = new ArrayList<>();
        CouponsProductsRecord couponsProductsRecord = new CouponsProductsRecord();
        couponsProductsRecord.setProductName("fakeOtherName");
        couponProductsList.add(couponsProductsRecord);

        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(UUID.class), any(TenantContext.class))).thenReturn(account);
        when(osgiKillbillAPI.getSubscriptionApi()).thenReturn(subscriptionApi);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(UUID.class), any(TenantContext.class))).thenReturn(subscription);
        when(dao.getCouponByCode(anyString())).thenReturn(coupon);
        when(dao.getProductsOfCoupon(anyString())).thenReturn(couponProductsList);

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        couponPluginApi.applyCoupon(Constants.COUPON_CODE, UUID.randomUUID(), UUID.randomUUID(), tenantContext);
    }

    private Subscription buildMockSubscription() {
        Subscription subscription = new Subscription() {
            @Override
            public LocalDate getBillingStartDate() {
                return null;
            }

            @Override
            public LocalDate getBillingEndDate() {
                return null;
            }

            @Override
            public LocalDate getChargedThroughDate() {
                return null;
            }

            @Override
            public String getCurrentStateForService(final String s) {
                return null;
            }

            @Override
            public List<SubscriptionEvent> getSubscriptionEvents() {
                List<SubscriptionEvent> result = new ArrayList<>();
                SubscriptionEvent event = new SubscriptionEvent() {
                    @Override
                    public UUID getId() {
                        return null;
                    }

                    @Override
                    public UUID getEntitlementId() {
                        return null;
                    }

                    @Override
                    public LocalDate getEffectiveDate() {
                        return null;
                    }

                    @Override
                    public LocalDate getRequestedDate() {
                        return null;
                    }

                    @Override
                    public SubscriptionEventType getSubscriptionEventType() {
                        return null;
                    }

                    @Override
                    public boolean isBlockedBilling() {
                        return false;
                    }

                    @Override
                    public boolean isBlockedEntitlement() {
                        return false;
                    }

                    @Override
                    public String getServiceName() {
                        return null;
                    }

                    @Override
                    public String getServiceStateName() {
                        return null;
                    }

                    @Override
                    public Product getPrevProduct() {
                        return null;
                    }

                    @Override
                    public Plan getPrevPlan() {
                        return null;
                    }

                    @Override
                    public PlanPhase getPrevPhase() {
                        return null;
                    }

                    @Override
                    public PriceList getPrevPriceList() {
                        return null;
                    }

                    @Override
                    public BillingPeriod getPrevBillingPeriod() {
                        return null;
                    }

                    @Override
                    public Product getNextProduct() {
                        Product product = new Product() {
                            @Override
                            public String getName() {
                                return "fakeName";
                            }

                            @Override
                            public boolean isRetired() {
                                return false;
                            }

                            @Override
                            public Product[] getAvailable() {
                                return new Product[0];
                            }

                            @Override
                            public Product[] getIncluded() {
                                return new Product[0];
                            }

                            @Override
                            public ProductCategory getCategory() {
                                return null;
                            }

                            @Override
                            public String getCatalogName() {
                                return null;
                            }

                            @Override
                            public Limit[] getLimits() {
                                return new Limit[0];
                            }

                            @Override
                            public boolean compliesWithLimits(final String s, final double v) {
                                return false;
                            }
                        };
                        return product;
                    }

                    @Override
                    public Plan getNextPlan() {
                        return null;
                    }

                    @Override
                    public PlanPhase getNextPhase() {
                        return null;
                    }

                    @Override
                    public PriceList getNextPriceList() {
                        return null;
                    }

                    @Override
                    public BillingPeriod getNextBillingPeriod() {
                        return null;
                    }
                };
                result.add(event);
                return result;
            }

            @Override
            public UUID getBaseEntitlementId() {
                return null;
            }

            @Override
            public UUID getBundleId() {
                return null;
            }

            @Override
            public UUID getAccountId() {
                return null;
            }

            @Override
            public String getExternalKey() {
                return null;
            }

            @Override
            public EntitlementState getState() {
                return null;
            }

            @Override
            public EntitlementSourceType getSourceType() {
                return null;
            }

            @Override
            public LocalDate getEffectiveStartDate() {
                return null;
            }

            @Override
            public LocalDate getEffectiveEndDate() {
                return null;
            }

            @Override
            public Product getLastActiveProduct() {
                return null;
            }

            @Override
            public Plan getLastActivePlan() {
                return null;
            }

            @Override
            public PlanPhase getLastActivePhase() {
                return null;
            }

            @Override
            public PriceList getLastActivePriceList() {
                return null;
            }

            @Override
            public ProductCategory getLastActiveProductCategory() {
                return null;
            }

            @Override
            public Entitlement cancelEntitlementWithDate(final LocalDate localDate, final boolean b, final CallContext callContext) throws EntitlementApiException {
                return null;
            }

            @Override
            public Entitlement cancelEntitlementWithPolicy(final EntitlementActionPolicy entitlementActionPolicy, final CallContext callContext) throws EntitlementApiException {
                return null;
            }

            @Override
            public Entitlement cancelEntitlementWithDateOverrideBillingPolicy(final LocalDate localDate, final BillingActionPolicy billingActionPolicy, final CallContext callContext) throws EntitlementApiException {
                return null;
            }

            @Override
            public Entitlement cancelEntitlementWithPolicyOverrideBillingPolicy(final EntitlementActionPolicy entitlementActionPolicy, final BillingActionPolicy billingActionPolicy, final CallContext callContext) throws EntitlementApiException {
                return null;
            }

            @Override
            public void uncancelEntitlement(final CallContext callContext) throws EntitlementApiException {

            }

            @Override
            public Entitlement changePlan(final String s, final BillingPeriod billingPeriod, final String s1, final List<PlanPhasePriceOverride> list, final CallContext callContext) throws EntitlementApiException {
                return null;
            }

            @Override
            public Entitlement changePlanWithDate(final String s, final BillingPeriod billingPeriod, final String s1, final List<PlanPhasePriceOverride> list, final LocalDate localDate, final CallContext callContext) throws EntitlementApiException {
                return null;
            }

            @Override
            public Entitlement changePlanOverrideBillingPolicy(final String s, final BillingPeriod billingPeriod, final String s1, final List<PlanPhasePriceOverride> list, final LocalDate localDate, final BillingActionPolicy billingActionPolicy, final CallContext callContext) throws EntitlementApiException {
                return null;
            }

            @Override
            public UUID getId() {
                return null;
            }

            @Override
            public DateTime getCreatedDate() {
                return null;
            }

            @Override
            public DateTime getUpdatedDate() {
                return null;
            }
        };
        return subscription;
    }

    private Account buildMockAccount() {
        Account account = new Account() {
            @Override
            public MutableAccountData toMutableAccountData() {
                return null;
            }

            @Override
            public Account mergeWithDelegate(final Account account) {
                return null;
            }

            @Override
            public String getExternalKey() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Integer getFirstNameLength() {
                return null;
            }

            @Override
            public String getEmail() {
                return null;
            }

            @Override
            public Integer getBillCycleDayLocal() {
                return null;
            }

            @Override
            public Currency getCurrency() {
                return null;
            }

            @Override
            public UUID getPaymentMethodId() {
                return null;
            }

            @Override
            public DateTimeZone getTimeZone() {
                return null;
            }

            @Override
            public String getLocale() {
                return null;
            }

            @Override
            public String getAddress1() {
                return null;
            }

            @Override
            public String getAddress2() {
                return null;
            }

            @Override
            public String getCompanyName() {
                return null;
            }

            @Override
            public String getCity() {
                return null;
            }

            @Override
            public String getStateOrProvince() {
                return null;
            }

            @Override
            public String getPostalCode() {
                return null;
            }

            @Override
            public String getCountry() {
                return null;
            }

            @Override
            public String getPhone() {
                return null;
            }

            @Override
            public Boolean isMigrated() {
                return null;
            }

            @Override
            public Boolean isNotifiedForInvoices() {
                return null;
            }

            @Override
            public UUID getId() {
                return null;
            }

            @Override
            public DateTime getCreatedDate() {
                return null;
            }

            @Override
            public DateTime getUpdatedDate() {
                return null;
            }
        };
        return account;
    }

    private List<CouponsProductsRecord> buildListOfCouponProducts() {
        List<CouponsProductsRecord> result = new ArrayList<>();
        CouponsProductsRecord couponsProductsRecord = new CouponsProductsRecord();
        couponsProductsRecord.setCouponCode(Constants.COUPON_CODE);
        result.add(couponsProductsRecord);
        return result;
    }

    private Tenant buildMockTenant() {
        Tenant result = new Tenant() {
            @Override
            public UUID getId() {
                return tenantId;
            }

            @Override
            public DateTime getCreatedDate() {
                return null;
            }

            @Override
            public DateTime getUpdatedDate() {
                return null;
            }

            @Override
            public String getExternalKey() {
                return null;
            }

            @Override
            public String getApiKey() {
                return null;
            }

            @Override
            public String getApiSecret() {
                return null;
            }
        };
        return result;
    }
}
