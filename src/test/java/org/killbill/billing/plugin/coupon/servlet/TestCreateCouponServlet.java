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

package org.killbill.billing.plugin.coupon.servlet;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.Coupon;
import org.killbill.billing.plugin.coupon.model.DiscountTypeEnum;
import org.killbill.billing.plugin.coupon.model.DurationTypeEnum;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

import static org.junit.Assert.assertTrue;

/**
 * Created by jgomez on 03/09/15.
 */
public class TestCreateCouponServlet extends Mockito {

    public static final String TEST_API_KEY = "hootsuite";
    private ServletRouter servletRouter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private LogService logService;
    private CouponPluginApi couponPluginApi;

    @Before
    public void setUp() {
        logService = mock(LogService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        couponPluginApi = mock(CouponPluginApi.class);
        servletRouter = new ServletRouter(couponPluginApi, logService);
    }

    @Test
    public void testCreateCouponServletOK() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        Coupon coupon = buildSuccessfulCoupon(randomTenantId);
        CouponsRecord couponRecord = buildSuccessfulCouponRecord();

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(coupon);
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(couponRecord);
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.CREATE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("POST");

        servletRouter.doPost(request, response);

        assertTrue(stringWriter.toString().contains(Constants.COUPON_TEST_CODE));
    }

    @Test
    public void testCreateCouponServletWithNullRequest() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.CREATE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("POST");

        servletRouter.doPost(request, response);

        assertTrue(stringWriter.toString().contains("CouponApiException"));
    }

    @Test
    public void testCreateCouponServletWithBadPercentage() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        Coupon coupon = buildSuccessfulCoupon(randomTenantId);
        coupon.setPercentageDiscount(-10d);

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(coupon);
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.CREATE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("POST");

        servletRouter.doPost(request, response);

        assertTrue(stringWriter.toString().contains("Percentage must be between 0 and 100"));
    }

    @Test
    public void testCreateCouponServletWithNullCouponCreated() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        Coupon coupon = buildSuccessfulCoupon(randomTenantId);
        coupon.setStartDate(null);

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(coupon);
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.CREATE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("POST");

        servletRouter.doPost(request, response);

        assertTrue(stringWriter.toString().contains("Coupon not found in the DB"));
    }

    @Test
    public void testCreateCouponServletCouponSQLException() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        Coupon coupon = buildSuccessfulCoupon(randomTenantId);

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(coupon);
        when(couponPluginApi.getCouponByCode(anyString())).thenThrow(SQLException.class);
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.CREATE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("POST");

        servletRouter.doPost(request, response);

        assertTrue(stringWriter.toString().contains("SQLException"));
    }

    @Test
    public void testCreateCouponServletCouponApiException() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenThrow(CouponApiException.class);
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.CREATE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("POST");

        servletRouter.doPost(request, response);

        assertTrue(stringWriter.toString().contains("CouponApiException"));
    }

    @Test
    public void testCreateCouponServletInvalidDuration() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        Coupon coupon = buildSuccessfulCoupon(randomTenantId);
        coupon.setDuration(DurationTypeEnum.multiple);
        coupon.setNumberOfInvoices(0);
        CouponsRecord couponRecord = buildSuccessfulCouponRecord();

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(coupon);
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(couponRecord);
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.CREATE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("POST");

        servletRouter.doPost(request, response);

        assertTrue(stringWriter.toString().contains("Must specify the number of Invoices"));
    }

    @Test
    public void testCreateCouponServletWithNegativeAmountDiscount() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        Coupon coupon = buildSuccessfulCoupon(randomTenantId);
        coupon.setDiscountType(DiscountTypeEnum.amount);
        coupon.setAmountDiscount(-30d);
        coupon.setAmountCurrency("USD");
        CouponsRecord couponRecord = buildSuccessfulCouponRecord();
        couponRecord.setDiscountType("amount");
        couponRecord.setAmountDiscount(-30d);
        couponRecord.setAmountCurrency("USD");

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(coupon);
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.CREATE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("POST");

        servletRouter.doPost(request, response);

        assertTrue(stringWriter.toString().contains("Amount can't be a negative value"));
    }

    private Coupon buildSuccessfulCoupon(UUID randomTenantId) {
        Coupon result = new Coupon();
        result.setCouponCode(Constants.COUPON_TEST_CODE);
        result.setCouponName(Constants.COUPON_TEST_NAME);
        result.setDiscountType(DiscountTypeEnum.percentage);
        result.setPercentageDiscount(20d);
        result.setDuration(DurationTypeEnum.forever);
        Calendar c = Calendar.getInstance();
        result.setStartDate(new Date(c.getTimeInMillis()));
        c.add(Calendar.DATE, 30);
        result.setExpirationDate(new Date(c.getTimeInMillis()));
        result.setTenantId(randomTenantId);
        return result;
    }

    private CouponsRecord buildSuccessfulCouponRecord() {
        CouponsRecord result = new CouponsRecord();
        result.setCouponCode(Constants.COUPON_TEST_CODE);
        result.setCouponName(Constants.COUPON_TEST_NAME);
        result.setDiscountType("percentage");
        result.setPercentageDiscount(20d);
        result.setIsActive(Byte.valueOf(Constants.BYTE_TRUE));
        result.setDuration(DurationTypeEnum.forever.toString());
        result.setKbTenantId(UUID.randomUUID().toString());
        return result;
    }
}
