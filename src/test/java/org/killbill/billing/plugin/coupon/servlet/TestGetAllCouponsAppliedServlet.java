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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

import static org.junit.Assert.assertTrue;

/**
 * Created by jgomez on 03/09/15.
 */
public class TestGetAllCouponsAppliedServlet extends Mockito {

    private GetAllCouponsAppliedServlet getAllCouponsAppliedServlet;
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
        getAllCouponsAppliedServlet = new GetAllCouponsAppliedServlet(logService, couponPluginApi);
    }

    @Test
    public void testGetAllCouponsAppliedServletOK() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        List<CouponsAppliedRecord> couponsAppliedList = buildSuccessfulCouponAppliedRecordList();

        when(couponPluginApi.getAllCouponsApplied()).thenReturn(couponsAppliedList);
        when(response.getWriter()).thenReturn(writer);

        getAllCouponsAppliedServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains(Constants.COUPON_TEST_CODE));
    }

    @Test
    public void testGetAllCouponsAppliedServletWithNullList() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(couponPluginApi.getAllCouponsApplied()).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        getAllCouponsAppliedServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("Response: null object"));
    }

    @Test
    public void testGetAllCouponsAppliedServletWithEmptyList() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(couponPluginApi.getAllCouponsApplied()).thenReturn(new ArrayList<>());
        when(response.getWriter()).thenReturn(writer);

        getAllCouponsAppliedServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("[]"));
    }

    @Test
    public void testGetAllCouponsAppliedServletWithSQLException() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
        when(couponPluginApi.getAllCouponsApplied()).thenThrow(SQLException.class);

        getAllCouponsAppliedServlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("SQL Exception"));
    }

    private List<CouponsAppliedRecord> buildSuccessfulCouponAppliedRecordList() {
        List<CouponsAppliedRecord> result = new ArrayList<>();
        CouponsAppliedRecord couponsAppliedRecord = new CouponsAppliedRecord();
        couponsAppliedRecord.setCouponCode(Constants.COUPON_TEST_CODE);
        couponsAppliedRecord.setKbAccountId(UUID.randomUUID().toString());
        couponsAppliedRecord.setKbSubscriptionId(UUID.randomUUID().toString());
        couponsAppliedRecord.setKbTenantId(UUID.randomUUID().toString());
        result.add(couponsAppliedRecord);
        return result;
    }
}
