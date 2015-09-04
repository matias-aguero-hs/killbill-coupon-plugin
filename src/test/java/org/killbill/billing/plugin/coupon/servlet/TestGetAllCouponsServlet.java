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
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

import static org.junit.Assert.assertTrue;

/**
 * Created by jgomez on 03/09/15.
 */
public class TestGetAllCouponsServlet extends Mockito {

    public static final String COUPON_TEST_CODE = "couponTestCode";
    private GetAllCouponsServlet getAllCouponsServlet;
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
        getAllCouponsServlet = new GetAllCouponsServlet(logService, couponPluginApi);
    }

    @Test
    public void testGetAllCouponsServletOK() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        List<CouponsRecord> couponList = buildSuccessfulCouponRecordList();

        when(couponPluginApi.getAllCoupons()).thenReturn(couponList);
        when(couponPluginApi.getProductsOfCoupon(anyString())).thenReturn(new ArrayList());
        when(response.getWriter()).thenReturn(writer);

        getAllCouponsServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains(COUPON_TEST_CODE));
    }

    @Test
    public void testGetAllCouponsServletWithNullList() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(couponPluginApi.getAllCoupons()).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        getAllCouponsServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("Response: null object"));
    }

    @Test
    public void testGetAllCouponsServletWithEmptyList() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(couponPluginApi.getAllCoupons()).thenReturn(new ArrayList<>());
        when(response.getWriter()).thenReturn(writer);

        getAllCouponsServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("[]"));
    }

    @Test
    public void testGetAllCouponsServletWithSQLException() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
        when(couponPluginApi.getAllCoupons()).thenThrow(SQLException.class);

        getAllCouponsServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("SQL Exception"));
    }

    private List<CouponsRecord> buildSuccessfulCouponRecordList() {
        List<CouponsRecord> result = new ArrayList<>();
        CouponsRecord couponsRecord = new CouponsRecord();
        couponsRecord.setCouponCode(COUPON_TEST_CODE);
        couponsRecord.setCouponName("couponTestName");
        couponsRecord.setDiscountType("percentage");
        couponsRecord.setPercentageDiscount(20d);
        couponsRecord.setKbTenantId(UUID.randomUUID().toString());
        result.add(couponsRecord);
        return result;
    }
}
