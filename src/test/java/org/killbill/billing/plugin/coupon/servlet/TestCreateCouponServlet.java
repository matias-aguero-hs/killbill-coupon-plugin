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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.ServletException;
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
import org.killbill.billing.plugin.coupon.util.CouponContext;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

import static org.junit.Assert.assertTrue;

/**
 * Created by jgomez on 03/09/15.
 */
public class TestCreateCouponServlet extends Mockito {

    public static final String COUPON_TEST_CODE = "couponTestCode";
    public static final String TEST_API_KEY = "hootsuite";
    private CreateCouponServlet createCouponServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private LogService logService;
    private CouponPluginApi couponPluginApi;
    private JsonHelper jsonHelper;

    @Before
    public void setUp() {
        logService = mock(LogService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        couponPluginApi = mock(CouponPluginApi.class);
        jsonHelper = mock(JsonHelper.class);
        createCouponServlet = new CreateCouponServlet(logService, couponPluginApi);
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

        createCouponServlet.doPost(request, response);

        assertTrue(stringWriter.toString().contains(COUPON_TEST_CODE));
    }

    @Test
    public void testCreateCouponServletCouponApiException() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        Coupon coupon = buildSuccessfulCoupon(randomTenantId);
        CouponsRecord couponRecord = buildSuccessfulCouponRecord();

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenThrow(CouponApiException.class);
        when(response.getWriter()).thenReturn(writer);

        createCouponServlet.doPost(request, response);

        assertTrue(stringWriter.toString().contains("CouponApiException"));
    }

    private Coupon buildSuccessfulCoupon(UUID randomTenantId) {
        Coupon result = new Coupon();
        result.setCouponCode(COUPON_TEST_CODE);
        result.setCouponName("couponTestName");
        result.setDiscountType(DiscountTypeEnum.percentage);
        result.setPercentageDiscount(20d);
        result.setTenantId(randomTenantId);
        return result;
    }

    private CouponsRecord buildSuccessfulCouponRecord() {
        CouponsRecord result = new CouponsRecord();
        result.setCouponCode(COUPON_TEST_CODE);
        result.setCouponName("couponTestName");
        result.setDiscountType("percentage");
        result.setPercentageDiscount(20d);
        result.setKbTenantId(UUID.randomUUID().toString());
        return result;
    }

    //    @Test
//    public void testGetCouponServletWithNullCoupon() throws Exception {
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//
//        when(request.getParameter(Constants.COUPON_CODE)).thenReturn(COUPON_TEST_CODE);
//        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(null);
//        when(couponPluginApi.getProductsOfCoupon(anyString())).thenReturn(new ArrayList());
//        when(response.getWriter()).thenReturn(writer);
//
//        createCouponServlet.doGet(request, response);
//
//        assertTrue(stringWriter.toString().contains("Error. Coupon not found in the DB"));
//    }
//
//    @Test
//    public void testGetCouponServletWithSQLException() throws IOException, SQLException, ServletException {
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//
//        when(response.getWriter()).thenReturn(writer);
//        when(request.getParameter(Constants.COUPON_CODE)).thenReturn(COUPON_TEST_CODE);
//        when(couponPluginApi.getCouponByCode(anyString())).thenThrow(SQLException.class);
//
//        createCouponServlet.doGet(request, response);
//
//        assertTrue(stringWriter.toString().contains("SQL Exception"));
//    }
//
//    @Test
//    public void testGetCouponServletWithEmptyCoupon() throws Exception {
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//
//        when(request.getParameter(Constants.COUPON_CODE)).thenReturn("");
//        when(response.getWriter()).thenReturn(writer);
//
//        createCouponServlet.doGet(request, response);
//
//        assertTrue(stringWriter.toString().contains("Coupon code is empty or not valid"));
//    }
//
//    private CouponsRecord buildSuccessfulCouponRecord() {
//        CouponsRecord result = new CouponsRecord();
//        result.setCouponCode(COUPON_TEST_CODE);
//        result.setCouponName("couponTestName");
//        result.setDiscountType("percentage");
//        result.setPercentageDiscount(20d);
//        result.setKbTenantId(UUID.randomUUID().toString());
//        return result;
//    }
}
