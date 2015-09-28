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
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

import static org.junit.Assert.assertTrue;

/**
 * Created by jgomez on 03/09/15.
 */
public class TestDeleteCouponServlet extends Mockito {

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
    public void testDeleteCouponServletOK() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        CouponsRecord couponRecord = buildSuccessfulCouponRecord();

        when(request.getParameter(Constants.COUPON_CODE)).thenReturn(Constants.COUPON_TEST_CODE);
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(couponRecord);
        when(couponPluginApi.getActiveCouponsAppliedByCouponCode(anyString())).thenReturn(new ArrayList());
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.DELETE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("GET");

        servletRouter.doGet(request, response);

        assertTrue(stringWriter.toString().contains("has been successfully deleted"));
    }

    @Test
    public void testDeleteCouponServletWithNullCoupon() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getParameter(Constants.COUPON_CODE)).thenReturn(Constants.COUPON_TEST_CODE);
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.DELETE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("GET");

        servletRouter.doGet(request, response);

        assertTrue(stringWriter.toString().contains("Coupon not found in the DB"));
    }

    @Test
    public void testDeleteCouponServletWithSQLException() throws IOException, SQLException, ServletException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(Constants.COUPON_CODE)).thenReturn(Constants.COUPON_TEST_CODE);
        when(couponPluginApi.getCouponByCode(anyString())).thenThrow(SQLException.class);
        when(request.getPathInfo()).thenReturn(Constants.DELETE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("GET");

        servletRouter.doGet(request, response);

        assertTrue(stringWriter.toString().contains("SQL Exception"));
    }

    @Test
    public void testDeleteCouponServletWithEmptyCoupon() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getParameter(Constants.COUPON_CODE)).thenReturn("");
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.DELETE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("GET");

        servletRouter.doGet(request, response);

        assertTrue(stringWriter.toString().contains("Coupon code is empty or not valid"));
    }

    @Test
    public void testDeleteCouponServletWithCouponApplicationActive() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        List<CouponsAppliedRecord> mockListOfActiveCouponsApplied = buildListOfActiveCouponsApplied();
        CouponsRecord couponRecord = buildSuccessfulCouponRecord();

        when(request.getParameter(Constants.COUPON_CODE)).thenReturn(Constants.COUPON_TEST_CODE);
        when(couponPluginApi.getCouponByCode(anyString())).thenReturn(couponRecord);
        when(couponPluginApi.getActiveCouponsAppliedByCouponCode(anyString())).thenReturn(mockListOfActiveCouponsApplied);
        when(response.getWriter()).thenReturn(writer);
        when(request.getPathInfo()).thenReturn(Constants.DELETE_COUPON_PATH.toString());
        when(request.getMethod()).thenReturn("GET");

        servletRouter.doGet(request, response);

        assertTrue(stringWriter.toString().contains("has currently active applications and cannot be deleted"));
    }

    private CouponsRecord buildSuccessfulCouponRecord() {
        CouponsRecord result = new CouponsRecord();
        result.setCouponCode(Constants.COUPON_TEST_CODE);
        result.setCouponName(Constants.COUPON_TEST_NAME);
        result.setDiscountType("percentage");
        result.setPercentageDiscount(20d);
        result.setIsActive(Byte.valueOf(Constants.BYTE_TRUE));
        result.setKbTenantId(UUID.randomUUID().toString());
        return result;
    }

    private List<CouponsAppliedRecord> buildListOfActiveCouponsApplied() {
        List<CouponsAppliedRecord> result = new ArrayList<>();
        CouponsAppliedRecord activeCouponApplied = new CouponsAppliedRecord();
        result.add(activeCouponApplied);
        return result;
    }
}
