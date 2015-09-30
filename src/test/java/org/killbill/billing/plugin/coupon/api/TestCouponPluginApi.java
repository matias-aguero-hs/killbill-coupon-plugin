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

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.account.api.AccountUserApi;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.entitlement.api.Subscription;
import org.killbill.billing.entitlement.api.SubscriptionApi;
import org.killbill.billing.entitlement.api.SubscriptionApiException;
import org.killbill.billing.plugin.coupon.dao.CouponDao;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.mock.MockAccount;
import org.killbill.billing.plugin.coupon.mock.MockProduct;
import org.killbill.billing.plugin.coupon.mock.MockSubscription;
import org.killbill.billing.plugin.coupon.mock.MockTenant;
import org.killbill.billing.plugin.coupon.mock.TestCouponHelper;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.Coupon;
import org.killbill.billing.plugin.coupon.model.CouponTenantContext;
import org.killbill.billing.plugin.coupon.model.DiscountTypeEnum;
import org.killbill.billing.plugin.coupon.util.CouponHelper;
import org.killbill.billing.tenant.api.Tenant;
import org.killbill.billing.tenant.api.TenantApiException;
import org.killbill.billing.tenant.api.TenantUserApi;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    public void setUp() throws SQLException {
        osgiKillbillAPI = mock(OSGIKillbillAPI.class);
        dao = mock(CouponDao.class);
        logService = mock(LogService.class);
        tenantUserApi = mock(TenantUserApi.class);
        accountUserApi = mock(AccountUserApi.class);
        subscriptionApi = mock(SubscriptionApi.class);
        tenantId = UUID.randomUUID();
        couponPluginApi = new CouponPluginApi(logService, dao, osgiKillbillAPI);

        when(dao.getActiveCouponAppliedBySubscription(any())).thenReturn(null);
        when(dao.getCouponAppliedByCodeAndSubscription(any(), any())).thenReturn(null);
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
    public void testGetCouponsAppliedByCouponCodeOK() throws Exception {
        when(dao.getCouponsAppliedByCouponCode(anyString())).thenReturn(new ArrayList<>());

        List<CouponsAppliedRecord> result = couponPluginApi.getCouponsAppliedByCouponCode(Constants.COUPON_CODE);

        assertTrue(result.isEmpty());
    }

    @Test(expected = SQLException.class)
    public void testGetCouponsAppliedByCouponCodeSQLException() throws Exception {
        when(dao.getCouponsAppliedByCouponCode(anyString())).thenThrow(SQLException.class);

        couponPluginApi.getCouponsAppliedByCouponCode(Constants.COUPON_CODE);
    }

    @Test
    public void testGetCouponByCodeOK() throws Exception {
        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
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
        Tenant tenant = new MockTenant();

        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);

        UUID result = couponPluginApi.getTenantId("apiKey");

        assertEquals(result, tenant.getId());
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
    public void testGetCouponAppliedByCouponCodeAndSubscriptionOK() throws Exception {
        CouponsAppliedRecord couponApplied = new CouponsAppliedRecord();
        couponApplied.setCouponCode(Constants.COUPON_CODE);
        when(dao.getCouponApplied(anyString(), any(UUID.class))).thenReturn(couponApplied);

        CouponsAppliedRecord result = couponPluginApi.getCouponApplied(Constants.COUPON_CODE, UUID.randomUUID());

        assertEquals(result, couponApplied);
    }

    @Test(expected = SQLException.class)
    public void testGetCouponAppliedByCouponCodeAndSubscriptionSQLException() throws Exception {
        when(dao.getCouponApplied(anyString(), any(UUID.class))).thenThrow(SQLException.class);
        couponPluginApi.getCouponApplied(Constants.COUPON_CODE, UUID.randomUUID());
    }

    @Test
    public void testGetCouponAppliedBySubscriptionOK() throws Exception {
        CouponsAppliedRecord couponApplied = new CouponsAppliedRecord();
        couponApplied.setCouponCode(Constants.COUPON_CODE);
        when(dao.getActiveCouponAppliedBySubscription(any(UUID.class))).thenReturn(couponApplied);

        CouponsAppliedRecord result = couponPluginApi.getActiveCouponAppliedBySubscription(UUID.randomUUID());

        assertEquals(result, couponApplied);
    }

    @Test(expected = SQLException.class)
    public void testGetCouponAppliedBySubscriptionSQLException() throws Exception {
        when(dao.getActiveCouponAppliedBySubscription(any(UUID.class))).thenThrow(SQLException.class);
        couponPluginApi.getActiveCouponAppliedBySubscription(UUID.randomUUID());
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
    public void testGetActiveCouponsAppliedByCouponCodeOK() throws Exception {
        List<CouponsAppliedRecord> couponsAppliedList = new ArrayList<>();
        CouponsAppliedRecord couponApplied = new CouponsAppliedRecord();
        couponApplied.setCouponCode(Constants.COUPON_CODE);
        couponsAppliedList.add(couponApplied);
        when(dao.getActiveCouponsAppliedByCouponCode(anyString())).thenReturn(couponsAppliedList);

        List<CouponsAppliedRecord> result = couponPluginApi.getActiveCouponsAppliedByCouponCode(Constants.COUPON_CODE);

        assertEquals(result, couponsAppliedList);
    }

    @Test
    public void testGetActiveCouponsAppliedByCouponCodeSQLException() throws Exception {
        when(dao.getActiveCouponsAppliedByCouponCode(anyString())).thenThrow(SQLException.class);

        List<CouponsAppliedRecord> result = couponPluginApi.getActiveCouponsAppliedByCouponCode(Constants.COUPON_CODE);

        assertEquals(result, new ArrayList<CouponsProductsRecord>());
    }

    @Test
    public void testGetActiveCouponsAppliedByAccountIdAndProductOK() throws Exception {
        List<CouponsAppliedRecord> couponsAppliedList = new ArrayList<>();
        CouponsAppliedRecord couponApplied = new CouponsAppliedRecord();
        couponApplied.setCouponCode(Constants.COUPON_CODE);
        couponsAppliedList.add(couponApplied);
        when(dao.getActiveCouponsAppliedByAccountIdAndProduct(any(), anyString())).thenReturn(couponsAppliedList);

        List<CouponsAppliedRecord> result = couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(UUID.randomUUID(), Constants.COUPON_CODE);

        assertEquals(result, couponsAppliedList);
    }

    @Test
    public void testGetActiveCouponsAppliedByAccountIdAndProductSQLException() throws Exception {
        when(dao.getActiveCouponsAppliedByAccountIdAndProduct(any(), anyString())).thenThrow(SQLException.class);

        List<CouponsAppliedRecord> result = couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(UUID.randomUUID(), Constants.COUPON_CODE);

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
        Tenant tenant = new MockTenant();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        couponPluginApi.createCoupon(new Coupon(), tenantContext);
    }

    // ------------------------------------------------
    //      APPLY COUPON
    // ------------------------------------------------

    @Test
    public void testApplyCouponOK() throws Exception {
        Tenant tenant = new MockTenant();
        Account account = new MockAccount(UUID.randomUUID(), "key");
        Subscription subscription = new MockSubscription();
        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
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
        Boolean result = couponPluginApi.applyCoupon(Constants.COUPON_CODE, null, UUID.randomUUID(), UUID.randomUUID(), tenantContext);

        assertTrue(result);
    }

    @Test
    public void testApplyCouponValidateActiveSubscription() throws Exception {
        Tenant tenant = new MockTenant();
        Account account = new MockAccount(UUID.randomUUID(), "key");
        Subscription subscription = new MockSubscription();
        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
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
        when(dao.getActiveCouponAppliedBySubscription(any())).thenReturn(TestCouponHelper.createBaseCouponApplied(UUID.randomUUID(), UUID.randomUUID()));

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        try {
            couponPluginApi.applyCoupon(Constants.COUPON_CODE, null, UUID.randomUUID(), UUID.randomUUID(), tenantContext);
       } catch (CouponApiException e) {
            assertTrue(e.getMessage().contains("already has an active applied coupon"));
            return;
        }

        fail();
    }

    @Test
    public void testApplyCouponValidateAlreadyAppliedCouponToSubscription() throws Exception {
        Tenant tenant = new MockTenant();
        Account account = new MockAccount(UUID.randomUUID(), "key");
        Subscription subscription = new MockSubscription();
        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
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
        when(dao.getActiveCouponAppliedBySubscription(any())).thenReturn(null);
        when(dao.getCouponAppliedByCodeAndSubscription(any(), any())).thenReturn(TestCouponHelper.createBaseCouponApplied(UUID.randomUUID(), UUID.randomUUID()));

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        try {
            couponPluginApi.applyCoupon(Constants.COUPON_CODE, null, UUID.randomUUID(), UUID.randomUUID(), tenantContext);
        } catch (CouponApiException e) {
            assertTrue(e.getMessage().contains("already had applied coupon"));
            return;
        }

        fail();
    }

    @Test(expected = CouponApiException.class)
    public void testApplyCouponWithCouponApiException() throws Exception {
        Tenant tenant = new MockTenant();
        Account account = new MockAccount(UUID.randomUUID(), "key");
        Subscription subscription = new MockSubscription();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(UUID.class), any(TenantContext.class))).thenReturn(account);
        when(osgiKillbillAPI.getSubscriptionApi()).thenReturn(subscriptionApi);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(UUID.class), any(TenantContext.class))).thenReturn(subscription);
        when(dao.getCouponByCode(anyString())).thenReturn(null);

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        couponPluginApi.applyCoupon(Constants.COUPON_CODE, null, UUID.randomUUID(), UUID.randomUUID(), tenantContext);
    }

    @Test
    public void testApplyCouponWithNullAccount() throws Exception {
        Tenant tenant = new MockTenant();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(osgiKillbillAPI.getSubscriptionApi()).thenReturn(subscriptionApi);

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        Boolean result = couponPluginApi.applyCoupon(Constants.COUPON_CODE, null, UUID.randomUUID(), null, tenantContext);

        assertFalse(result);
    }

    @Test
    public void testApplyCouponWithNullSubscription() throws Exception {
        Tenant tenant = new MockTenant();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);

        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));
        Boolean result = couponPluginApi.applyCoupon(Constants.COUPON_CODE, null, null, UUID.randomUUID(), tenantContext);

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
        Tenant tenant = new MockTenant();
        Account account = new MockAccount(UUID.randomUUID(), "key");
        Subscription subscription = new MockSubscription();
        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
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
        couponPluginApi.applyCoupon(Constants.COUPON_CODE, null, UUID.randomUUID(), UUID.randomUUID(), tenantContext);
    }

    @Test(expected = SQLException.class)
    public void testDeactivateCouponSQLException() throws Exception {
        doThrow(new SQLException()).when(dao).deactivateCouponByCode(anyString());
        couponPluginApi.deactivateCouponByCode(Constants.COUPON_CODE);
    }

    @Test(expected = SQLException.class)
    public void testDeleteCouponSQLException() throws Exception {
        doThrow(new SQLException()).when(dao).deleteCouponByCode(anyString());
        couponPluginApi.deleteCouponByCode(Constants.COUPON_CODE);
    }

    @Test(expected = SQLException.class)
    public void testIncreaseNumberOfInvoicesAndSetActiveStatusSQLException() throws Exception {
        doThrow(new SQLException()).when(dao).increaseNumberOfInvoicesAffected(anyString(), anyInt(), anyByte(), any(UUID.class));
        couponPluginApi.increaseNumberOfInvoicesAndSetActiveStatus(Constants.COUPON_CODE, 0, true, UUID.randomUUID());
    }

    @Test(expected = SQLException.class)
    public void testIncreaseNumberOfInvoicesAndSetActiveStatusWithFalseDeactivateSQLException() throws Exception {
        doThrow(new SQLException()).when(dao).increaseNumberOfInvoicesAffected(anyString(), anyInt(), anyByte(), any(UUID.class));
        couponPluginApi.increaseNumberOfInvoicesAndSetActiveStatus(Constants.COUPON_CODE, 0, false, UUID.randomUUID());
    }

    @Test(expected = SQLException.class)
    public void testDeactivateApplicationsOfCouponSQLException() throws Exception {
        doThrow(new SQLException()).when(dao).deactivateApplicationsOfCoupon(anyString());
        couponPluginApi.deactivateApplicationsOfCoupon(Constants.COUPON_CODE);
    }

    @Test(expected = SQLException.class)
    public void testDeactivateApplicationOfCouponByCodeAndSubscriptionSQLException() throws Exception {
        doThrow(new SQLException()).when(dao).deactivateApplicationOfCouponByCodeAndSubscription(anyString(), any(UUID.class));
        couponPluginApi.deactivateApplicationOfCouponByCodeAndSubscription(Constants.COUPON_CODE, UUID.randomUUID());
    }

    private List<CouponsProductsRecord> buildListOfCouponProducts() {
        List<CouponsProductsRecord> result = new ArrayList<>();
        CouponsProductsRecord couponsProductsRecord = new CouponsProductsRecord();
        couponsProductsRecord.setCouponCode(Constants.COUPON_CODE);
        result.add(couponsProductsRecord);
        return result;
    }

    // -----------------------------------------
    //       Validate Apply Coupon
    // public boolean validateCoupon(String couponCode, UUID accountId, String subscriptionProduct, TenantContext context)
    // -----------------------------------------

    @Test
    public void testValidateNullCoupon() throws SQLException {

        when(dao.getCouponByCode(any())).thenReturn(null);

        try {
            couponPluginApi.validateCoupon("invalid", null, null, null);
        } catch (CouponApiException e) {
            assertTrue(e.getMessage().contains("does not exist"));
            return;
        }

        fail();

    }

    @Test
    public void testValidateCouponSQLException() throws SQLException {

        when(dao.getCouponByCode(any())).thenThrow(SQLException.class);

        try {
            couponPluginApi.validateCoupon("invalid", null, null, null);
        } catch (CouponApiException e) {
            assertTrue(e.getMessage().contains("There is an error trying"));
            return;
        }

        fail();

    }

    @Test
    public void testValidateNullAccount() throws SQLException, AccountApiException {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();

        when(dao.getCouponByCode(any())).thenReturn(coupon);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(), any())).thenReturn(null);

        try {
            couponPluginApi.validateCoupon(coupon.getCouponCode(), null, null, null);
        } catch (CouponApiException e) {
            assertTrue(e.getMessage().contains("does not exist"));
            return;
        }

        fail();

    }

    @Test
    public void testValidateExceptionAccount() throws SQLException, AccountApiException {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();

        when(dao.getCouponByCode(any())).thenReturn(coupon);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(), any())).thenThrow(AccountApiException.class);

        try {
            couponPluginApi.validateCoupon(coupon.getCouponCode(), null, null, null);
        } catch (CouponApiException e) {
            assertTrue(e.getMessage().contains("There is an error trying to get Account"));
            return;
        }

        fail();

    }

    @Test
    public void testValidateInactiveCoupon() throws SQLException, AccountApiException {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
        coupon.setIsActive(Byte.valueOf("0"));
        Account account = new MockAccount(UUID.randomUUID(), "external");

        when(dao.getCouponByCode(any())).thenReturn(coupon);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(), any())).thenReturn(account);

        try {
            couponPluginApi.validateCoupon(coupon.getCouponCode(), account.getId(), null, null);
        } catch (CouponApiException e) {
            assertTrue(e.getMessage().contains("is not active"));
            return;
        }

        fail();

    }

    @Test
    public void testValidateCouponWithoutProducts() throws SQLException, AccountApiException {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();

        Account account = new MockAccount(UUID.randomUUID(), "external");

        when(dao.getCouponByCode(any())).thenReturn(coupon);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(), any())).thenReturn(account);

        try {
            couponPluginApi.validateCoupon(coupon.getCouponCode(), account.getId(), null, null);
        } catch (CouponApiException e) {
            fail();
        }

    }

    @Test
    public void testValidateCouponProductsSQLException() throws SQLException, AccountApiException {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();

        Account account = new MockAccount(UUID.randomUUID(), "external");

        when(dao.getCouponByCode(any())).thenReturn(coupon);
        when(dao.getProductsOfCoupon(any())).thenThrow(SQLException.class);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(), any())).thenReturn(account);

        try {
            couponPluginApi.validateCoupon(coupon.getCouponCode(), account.getId(), null, null);
        } catch (CouponApiException e) {
            assertTrue(e.getMessage().contains("is not active"));
            return;
        }

        // TODO fail();
    }

    @Test
    public void testValidateCouponWithProducts() throws SQLException, AccountApiException {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
        CouponsProductsRecord product = new CouponsProductsRecord();
        product.setProductName("Standard");
        List<CouponsProductsRecord> products = new ArrayList<CouponsProductsRecord>();
        products.add(product);

        Account account = new MockAccount(UUID.randomUUID(), "external");

        when(dao.getCouponByCode(any())).thenReturn(coupon);
        when(dao.getProductsOfCoupon(any())).thenReturn(products);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(), any())).thenReturn(account);

        try {
            couponPluginApi.validateCoupon(coupon.getCouponCode(), account.getId(), "Standard", null);
        } catch (CouponApiException e) {
            fail();
        }

    }

    @Test(expected = CouponApiException.class)
    public void testValidateCouponWithoutRedemptionsLeft() throws SQLException, AccountApiException, CouponApiException {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
        coupon.setMaxRedemptions(1);
        List<CouponsAppliedRecord> couponsApplied = new ArrayList<CouponsAppliedRecord>();
        couponsApplied.add(new CouponsAppliedRecord());

        Account account = new MockAccount(UUID.randomUUID(), "external");

        when(dao.getCouponByCode(any())).thenReturn(coupon);
        when(dao.getCouponsAppliedByCouponCode(any())).thenReturn(couponsApplied);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(), any())).thenReturn(account);

        couponPluginApi.validateCoupon(coupon.getCouponCode(), account.getId(), "Standard", null);
    }

    @Test(expected = CouponApiException.class)
    public void testValidateCouponWithDifferentCurrencies() throws SQLException, AccountApiException, CouponApiException {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
        coupon.setAmountCurrency(Currency.EUR.toString());
        coupon.setAmountDiscount(10d);
        coupon.setDiscountType(DiscountTypeEnum.amount.toString());

        List<CouponsAppliedRecord> couponsApplied = new ArrayList<CouponsAppliedRecord>();
        couponsApplied.add(new CouponsAppliedRecord());

        Account account = new MockAccount(UUID.randomUUID(), "external");

        when(dao.getCouponByCode(any())).thenReturn(coupon);
        when(dao.getCouponsAppliedByCouponCode(any())).thenReturn(couponsApplied);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(), any())).thenReturn(account);

        couponPluginApi.validateCoupon(coupon.getCouponCode(), account.getId(), "Standard", null);
    }

    @Test
    public void testValidateCouponWithInvalidProducts() throws SQLException, AccountApiException {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
        CouponsProductsRecord product = new CouponsProductsRecord();
        product.setProductName("Standard");
        List<CouponsProductsRecord> products = new ArrayList<CouponsProductsRecord>();
        products.add(product);

        Account account = new MockAccount(UUID.randomUUID(), "external");

        when(dao.getCouponByCode(any())).thenReturn(coupon);
        when(dao.getProductsOfCoupon(any())).thenReturn(products);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(accountUserApi.getAccountById(any(), any())).thenReturn(account);

        try {
            couponPluginApi.validateCoupon(coupon.getCouponCode(), account.getId(), "Super", null);
        } catch (CouponApiException e) {
            assertTrue(e.getMessage().contains("it has not product"));
            return;
        }

        fail();

    }

    @Test
    public void testUpdateCouponWithProducts() throws SQLException, TenantApiException, CouponApiException, SubscriptionApiException {
        List<CouponsProductsRecord> oldProducts = new ArrayList<CouponsProductsRecord>();
        CouponsProductsRecord oldProduct = new CouponsProductsRecord();
        oldProduct.setCouponCode(Constants.COUPON_TEST_CODE);
        oldProduct.setProductName("oldProduct");
        oldProducts.add(oldProduct);

        Coupon coupon = new Coupon();
        List<String> newProducts = new ArrayList<String>();
        newProducts.add("newProduct");
        coupon.setProducts(newProducts);
        coupon.setCouponCode(Constants.COUPON_TEST_CODE);

        List<CouponsAppliedRecord> activeCouponApplications = new ArrayList<CouponsAppliedRecord>();
        CouponsAppliedRecord couponsAppliedRecord = new CouponsAppliedRecord();
        couponsAppliedRecord.setCouponCode(Constants.COUPON_TEST_CODE);
        couponsAppliedRecord.setKbSubscriptionId(UUID.randomUUID().toString());
        activeCouponApplications.add(couponsAppliedRecord);

        MockSubscription subscription = new MockSubscription() {
            @Override
            public Product getLastActiveProduct() {
                Product product = new MockProduct() {
                    @Override
                    public String getName() {
                        return "newProduct";
                    }
                };
                return product;
            }
        };

        Tenant tenant = new MockTenant();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(dao.getActiveCouponsAppliedByCouponCode(anyString())).thenReturn(activeCouponApplications);
        when(osgiKillbillAPI.getSubscriptionApi()).thenReturn(subscriptionApi);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(), any())).thenReturn(subscription);
        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));

        couponPluginApi.updateCoupon(oldProducts, coupon, tenantContext);
    }

    @Test(expected = CouponApiException.class)
    public void testUpdateCouponWithCouponApiException() throws SQLException, TenantApiException, CouponApiException, SubscriptionApiException {
        List<CouponsProductsRecord> oldProducts = new ArrayList<CouponsProductsRecord>();
        CouponsProductsRecord oldProduct = new CouponsProductsRecord();
        oldProduct.setCouponCode(Constants.COUPON_TEST_CODE);
        oldProduct.setProductName("oldProduct");
        oldProducts.add(oldProduct);

        Coupon coupon = new Coupon();
        List<String> newProducts = new ArrayList<String>();
        newProducts.add("newProduct");
        coupon.setProducts(newProducts);
        coupon.setCouponCode(Constants.COUPON_TEST_CODE);

        List<CouponsAppliedRecord> activeCouponApplications = new ArrayList<CouponsAppliedRecord>();
        CouponsAppliedRecord couponsAppliedRecord = new CouponsAppliedRecord();
        couponsAppliedRecord.setCouponCode(Constants.COUPON_TEST_CODE);
        couponsAppliedRecord.setKbSubscriptionId(UUID.randomUUID().toString());
        activeCouponApplications.add(couponsAppliedRecord);

        MockSubscription subscription = new MockSubscription() {
            @Override
            public Product getLastActiveProduct() {
                Product product = new MockProduct() {
                    @Override
                    public String getName() {
                        return "oldProduct";
                    }
                };
                return product;
            }
        };

        Tenant tenant = new MockTenant();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(dao.getActiveCouponsAppliedByCouponCode(anyString())).thenReturn(activeCouponApplications);
        when(osgiKillbillAPI.getSubscriptionApi()).thenReturn(subscriptionApi);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(), any())).thenReturn(subscription);
        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));

        couponPluginApi.updateCoupon(oldProducts, coupon, tenantContext);
    }

    @Test(expected = CouponApiException.class)
    public void testUpdateCouponWithSubscriptionApiException() throws SQLException, TenantApiException, CouponApiException, SubscriptionApiException {
        List<CouponsProductsRecord> oldProducts = new ArrayList<CouponsProductsRecord>();
        CouponsProductsRecord oldProduct = new CouponsProductsRecord();
        oldProduct.setCouponCode(Constants.COUPON_TEST_CODE);
        oldProduct.setProductName("oldProduct");
        oldProducts.add(oldProduct);

        Coupon coupon = new Coupon();
        List<String> newProducts = new ArrayList<String>();
        newProducts.add("newProduct");
        coupon.setProducts(newProducts);
        coupon.setCouponCode(Constants.COUPON_TEST_CODE);

        List<CouponsAppliedRecord> activeCouponApplications = new ArrayList<CouponsAppliedRecord>();
        CouponsAppliedRecord couponsAppliedRecord = new CouponsAppliedRecord();
        couponsAppliedRecord.setCouponCode(Constants.COUPON_TEST_CODE);
        couponsAppliedRecord.setKbSubscriptionId(UUID.randomUUID().toString());
        activeCouponApplications.add(couponsAppliedRecord);

        Tenant tenant = new MockTenant();
        when(osgiKillbillAPI.getTenantUserApi()).thenReturn(tenantUserApi);
        when(tenantUserApi.getTenantByApiKey(anyString())).thenReturn(tenant);
        when(dao.getActiveCouponsAppliedByCouponCode(anyString())).thenReturn(activeCouponApplications);
        when(osgiKillbillAPI.getSubscriptionApi()).thenReturn(subscriptionApi);
        when(subscriptionApi.getSubscriptionForEntitlementId(any(), any())).thenThrow(SubscriptionApiException.class);
        TenantContext tenantContext = new CouponTenantContext(couponPluginApi.getTenantId("apiKey"));

        couponPluginApi.updateCoupon(oldProducts, coupon, tenantContext);
    }
}
