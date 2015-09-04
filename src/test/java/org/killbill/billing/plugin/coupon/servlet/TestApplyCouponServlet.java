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
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.ApplyCouponRequest;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

import static org.junit.Assert.assertTrue;

/**
 * Created by jgomez on 03/09/15.
 */
public class TestApplyCouponServlet extends Mockito {

    public static final String COUPON_TEST_CODE = "couponTestCode";
    public static final String TEST_API_KEY = "hootsuite";
    private ApplyCouponServlet applyCouponServlet;
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
        applyCouponServlet = new ApplyCouponServlet(logService, couponPluginApi);
    }

    @Test
    public void testApplyCouponServletOK() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        ApplyCouponRequest applyCouponRequest = buildSuccessfulApplyCouponRequest(randomTenantId);
        CouponsAppliedRecord couponAppliedRecord = buildSuccessfulCouponAppliedRecord();

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(applyCouponRequest);
        when(couponPluginApi.getCouponApplied(anyString(), any(UUID.class), any(UUID.class))).thenReturn(couponAppliedRecord);
        when(response.getWriter()).thenReturn(writer);

        applyCouponServlet.doPost(request, response);

        assertTrue(stringWriter.toString().contains(COUPON_TEST_CODE));
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

        applyCouponServlet.doPost(request, response);

        assertTrue(stringWriter.toString().contains("CouponApiException"));
    }

    @Test
    public void testCreateCouponServletCouponSQLException() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        ApplyCouponRequest applyCouponRequest = buildSuccessfulApplyCouponRequest(randomTenantId);

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(applyCouponRequest);
        when(couponPluginApi.getCouponApplied(anyString(), any(UUID.class), any(UUID.class))).thenThrow(SQLException.class);
        when(response.getWriter()).thenReturn(writer);

        applyCouponServlet.doPost(request, response);

        assertTrue(stringWriter.toString().contains("SQLException"));
    }

    @Test
    public void testCreateCouponServletCouponCouponApiException() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        ApplyCouponRequest applyCouponRequest = buildSuccessfulApplyCouponRequest(randomTenantId);

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(applyCouponRequest);
        when(couponPluginApi.getCouponApplied(anyString(), any(UUID.class), any(UUID.class))).thenThrow(CouponApiException.class);
        when(response.getWriter()).thenReturn(writer);

        applyCouponServlet.doPost(request, response);

        assertTrue(stringWriter.toString().contains("Coupon cannot be applied"));
    }

    @Test
    public void testCreateCouponServletCouponException() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UUID randomTenantId = UUID.randomUUID();
        ApplyCouponRequest applyCouponRequest = buildSuccessfulApplyCouponRequest(randomTenantId);

        when(request.getHeader(Constants.X_KILLBILL_API_KEY)).thenReturn(TEST_API_KEY);
        when(couponPluginApi.getTenantId(anyString())).thenReturn(randomTenantId);
        when(couponPluginApi.getObjectFromJsonRequest(any(HttpServletRequest.class), any(LogService.class), any(Class.class))).thenReturn(applyCouponRequest);
        when(couponPluginApi.getCouponApplied(anyString(), any(UUID.class), any(UUID.class))).thenThrow(Exception.class);
        when(response.getWriter()).thenReturn(writer);

        applyCouponServlet.doPost(request, response);

        assertTrue(stringWriter.toString().contains("API Exception"));
    }

    private ApplyCouponRequest buildSuccessfulApplyCouponRequest(UUID randomTenantId) {
        ApplyCouponRequest result = new ApplyCouponRequest();
        result.setCouponCode(COUPON_TEST_CODE);
        result.setAccountId(randomTenantId);
        result.setSubscriptionId(randomTenantId);
        return result;
    }

    private CouponsAppliedRecord buildSuccessfulCouponAppliedRecord() {
        CouponsAppliedRecord result = new CouponsAppliedRecord();
        result.setCouponCode(COUPON_TEST_CODE);
        result.setKbAccountId(UUID.randomUUID().toString());
        result.setKbSubscriptionId(UUID.randomUUID().toString());
        result.setKbTenantId(UUID.randomUUID().toString());
        return result;
    }
}
