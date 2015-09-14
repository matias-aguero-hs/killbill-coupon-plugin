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

package org.killbill.billing.plugin.coupon.listener;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.UUID;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.account.api.AccountUserApi;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.invoice.api.Invoice;
import org.killbill.billing.invoice.api.InvoiceItem;
import org.killbill.billing.invoice.api.InvoiceItemType;
import org.killbill.billing.invoice.api.InvoiceUserApi;
import org.killbill.billing.mock.api.MockExtBusEvent;
import org.killbill.billing.notification.plugin.api.ExtBusEvent;
import org.killbill.billing.notification.plugin.api.ExtBusEventType;
import org.killbill.billing.payment.MockInvoice;
import org.killbill.billing.payment.MockRecurringInvoiceItem;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.mock.MockAccount;
import org.killbill.billing.plugin.coupon.mock.TestCouponHelper;
import org.killbill.billing.plugin.coupon.model.DiscountTypeEnum;
import org.killbill.billing.plugin.coupon.model.DurationTypeEnum;
import org.killbill.billing.security.api.SecurityApi;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillLogService;
import org.mockito.Mockito;

/**
 * Created by maguero on 09/09/15.
 */
public class TestCouponListener extends Mockito {

    public static final String COUPON_CODE = "test";
    private CouponPluginApi couponPluginApi;
    private OSGIKillbillAPI osgiKillbillAPI;
    private InvoiceUserApi invoiceUserApi;
    private AccountUserApi accountUserApi;
    private SecurityApi securityApi;
    private OSGIKillbillLogService logService;
    private UUID tenantId;
    private CouponListener couponListener;

    private Invoice invoice;
    private Account account;
    private UUID subscriptionId;
    private UUID accountId;
    private ExtBusEvent event;

    @Before
    public void setUp() throws Exception {

        osgiKillbillAPI = mock(OSGIKillbillAPI.class);
        logService = mock(OSGIKillbillLogService.class);
        accountUserApi = mock(AccountUserApi.class);
        invoiceUserApi = mock(InvoiceUserApi.class);
        securityApi = mock(SecurityApi.class);
        tenantId = UUID.randomUUID();
        couponPluginApi = mock(CouponPluginApi.class);
        couponListener = new CouponListener(logService, osgiKillbillAPI, couponPluginApi);

        // -------------

        when(osgiKillbillAPI.getInvoiceUserApi()).thenReturn(invoiceUserApi);
        when(osgiKillbillAPI.getAccountUserApi()).thenReturn(accountUserApi);
        when(osgiKillbillAPI.getSecurityApi()).thenReturn(securityApi);

        // properties

        final BigDecimal requestedAmount = BigDecimal.TEN;
        subscriptionId = UUID.randomUUID();
        accountId = UUID.randomUUID();
        final LocalDate now = new LocalDate();

        invoice = new MockInvoice(UUID.randomUUID(), now, now, Currency.USD);
        account = new MockAccount(accountId, "user");

        InvoiceItem invoiceItem = new MockRecurringInvoiceItem(invoice.getId(), account.getId(),
                                                               subscriptionId,
                                                               UUID.randomUUID(),
                                                               "test plan",
                                                               "test phase", null,
                                                               now,
                                                               now.plusMonths(1),
                                                               requestedAmount,
                                                               new BigDecimal("1.0"),
                                                               Currency.USD);

        invoice.addInvoiceItem(invoiceItem);

        when(invoiceUserApi.getInvoice(any(), any())).thenReturn(invoice);
        when(accountUserApi.getAccountById(any(), any())).thenReturn(account);
        doNothing().when(couponPluginApi).increaseNumberOfInvoicesAndSetActiveStatus(any(), any(), any(), any());
        when(invoiceUserApi.insertInvoiceItemAdjustment(any(), any(), any(), any(), any(), any(), any())).thenReturn(invoiceItem);
        doNothing().when(securityApi).login(any(), any());

        event = new MockExtBusEvent(ExtBusEventType.INVOICE_CREATION, null, invoice.getId(),
                            account.getId(), UUID.randomUUID());

    }

    @Test
    public void testNonInvoiceCreation() throws Exception {

        ExtBusEvent event = new MockExtBusEvent(ExtBusEventType.INVOICE_NOTIFICATION, null, null, null, null);
        couponListener.handleKillbillEvent(event);
    }

    @Test
    public void testInvalidExtBusEvent() throws Exception {

        ExtBusEvent event = new MockExtBusEvent(ExtBusEventType.INVOICE_CREATION, null, null, null, null);
        couponListener.handleKillbillEvent(event);
    }

    @Test
    public void testInvoiceItemCreation() throws Exception {

        CouponsAppliedRecord couponApplied = TestCouponHelper.createBaseCouponApplied(subscriptionId, accountId);
        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();

        // mocks
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(coupon);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any(UUID.class))).thenReturn(couponApplied);

        // test
        couponListener.handleKillbillEvent(event);
    }

    @Test
    public void testInvoiceItemCreationMultiple() throws Exception {

        CouponsAppliedRecord couponApplied = TestCouponHelper.createBaseCouponApplied(subscriptionId, accountId);
        couponApplied.setNumberOfInvoices(0);

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
        coupon.setDuration(DurationTypeEnum.multiple.toString());
        coupon.setNumberOfInvoices(1);

        // mocks
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(coupon);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any(UUID.class))).thenReturn(couponApplied);

        // test
        couponListener.handleKillbillEvent(event);
    }

    @Test
    public void testInvoiceItemCreationInactiveCouponApplied() throws Exception {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();

        CouponsAppliedRecord couponApplied = TestCouponHelper.createBaseCouponApplied(subscriptionId, accountId);
        couponApplied.setIsActive(Byte.valueOf("0"));

        // mocks
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(coupon);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any(UUID.class))).thenReturn(couponApplied);

        // test
        couponListener.handleKillbillEvent(event);
    }

    @Test
    public void testInvoiceItemCreationFinishedCouponApplied() throws Exception {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();
        coupon.setDuration(DurationTypeEnum.once.toString());
        coupon.setNumberOfInvoices(0);

        CouponsAppliedRecord couponApplied = TestCouponHelper.createBaseCouponApplied(subscriptionId, accountId);
        couponApplied.setNumberOfInvoices(1);

        // mocks
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(coupon);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any(UUID.class))).thenReturn(couponApplied);

        // test
        couponListener.handleKillbillEvent(event);
    }

    @Test
    public void testInvoiceItemCreationWithoutCouponApplied() throws Exception {

        CouponsRecord coupon = TestCouponHelper.createBaseCoupon();

        // mocks
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(coupon);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any(UUID.class))).thenReturn(null);

        // test
        couponListener.handleKillbillEvent(event);
    }

    @Test
    public void testInvoiceItemCreationWithoutCoupon() throws Exception {

        // mocks
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(null);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any(UUID.class))).thenReturn(null);

        // test
        couponListener.handleKillbillEvent(event);
    }

    @Test
    public void testNonRecurringInvoiceItem() throws Exception {

        InvoiceItem invoiceItem = new MockRecurringInvoiceItem(invoice.getId(), account.getId(),
                                                               subscriptionId,
                                                               UUID.randomUUID(),
                                                               "test plan",
                                                               "test phase", null,
                                                               null,
                                                               null,
                                                               null,
                                                               new BigDecimal("1.0"),
                                                               Currency.USD) {
            @Override
            public InvoiceItemType getInvoiceItemType() {
                return InvoiceItemType.FIXED;
            }
        };

        invoice.getInvoiceItems().clear();
        invoice.addInvoiceItem(invoiceItem);

        // mocks
        when(invoiceUserApi.getInvoice(any(), any())).thenReturn(invoice);
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(null);
        when(couponPluginApi.getActiveCouponAppliedBySubscription(any(UUID.class))).thenReturn(null);

        // test
        couponListener.handleKillbillEvent(event);
    }

}
