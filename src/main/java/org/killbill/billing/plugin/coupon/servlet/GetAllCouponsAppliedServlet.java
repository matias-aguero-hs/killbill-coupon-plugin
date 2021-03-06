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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.killbill.billing.plugin.coupon.util.ServletHelper;
import org.osgi.service.log.LogService;

public class GetAllCouponsAppliedServlet extends PluginServlet {

    private final LogService logService;
    private final CouponPluginApi couponPluginApi;

    public GetAllCouponsAppliedServlet(final LogService logService, final CouponPluginApi couponPluginApi)
    {
        this.couponPluginApi = couponPluginApi;
        this.logService = logService;
    }

    /**
     * Method doGet will handle the get All Coupons Applied operation
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            logService.log(LogService.LOG_INFO, "Calling CouponPluginAPI to get all Coupons Applied. ");
            List<CouponsAppliedRecord> couponsApplied = couponPluginApi.getAllCouponsApplied();

            if (null != couponsApplied) {
                List<JSONObject> jsonCoupons = new ArrayList<JSONObject>();
                for (CouponsAppliedRecord couponApplied : couponsApplied) {
                    logService.log(LogService.LOG_INFO, "Calling JsonHelper to build JSON Response using the Coupon Applied");

                    JSONObject jsonCoupon = JsonHelper.buildCouponAppliedJsonResponse(couponApplied);
                    jsonCoupons.add(jsonCoupon);
                }

                JSONObject jsonResponse = JsonHelper.buildCouponsAppliedListJsonResponse(jsonCoupons);

                logService.log(LogService.LOG_INFO, "Writing JSON Response and returning OK");
                response.setContentType(APPLICATION_JSON);
                PrintWriter writer = response.getWriter();
                writer.write(jsonResponse.toString());
                writer.close();
                buildResponse(response);
            }
            else {
                logService.log(LogService.LOG_ERROR, "Error getting List of Coupons Applied from the DB");
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("Error", "Can't get List of Coupons Applied from the DB. Response: null object");
                ServletHelper.writeResponseToJson(response, errorMessage.toString());
                buildResponse(response);
            }
        } catch (SQLException e) {
            logService.log(LogService.LOG_ERROR, "SQL Exception. Cause: " + e.getMessage());
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "SQL Exception. Cause: " + e.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        }

    }
}
