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
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.killbill.billing.plugin.coupon.util.ServletHelper;
import org.osgi.service.log.LogService;

public class GetCouponAppliedServlet extends PluginServlet {

    private final LogService logService;
    private final CouponPluginApi couponPluginApi;

    public GetCouponAppliedServlet(final LogService logService, final CouponPluginApi couponPluginApi)
    {
        this.couponPluginApi = couponPluginApi;
        this.logService = logService;
    }

    /**
     * Method doGet will handle the getCouponApplied operations
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(APPLICATION_JSON);

        logService.log(LogService.LOG_INFO, "Getting couponCode and subscriptionId from the Parameters of the Request");
        String couponCode = request.getParameter(Constants.COUPON_CODE);
        String subscriptionId = request.getParameter(Constants.SUBSCRIPTION_ID);

        if (null != couponCode && !couponCode.isEmpty() && null != subscriptionId && !subscriptionId.isEmpty()) {
            try {
                logService.log(LogService.LOG_INFO, "Calling CouponPluginAPI to get a Coupon Applied with couponCode: " + couponCode
                              + " and subscriptionId: " + subscriptionId);
                CouponsAppliedRecord couponApplied = couponPluginApi.getCouponApplied(couponCode, UUID.fromString(subscriptionId));

                if (null != couponApplied) {
                    logService.log(LogService.LOG_INFO, "Calling JsonHelper to build JSON Response using the Coupon Applied obtained");
                    JSONObject jsonResponse = JsonHelper.buildCouponAppliedJsonResponse(couponApplied);
                    logService.log(LogService.LOG_INFO, "Writing JSON Response and returning OK");
                    ServletHelper.writeResponseToJson(response, jsonResponse.toString());
                    buildResponse(response);
                }
                else {
                    logService.log(LogService.LOG_ERROR, "Error. Coupon Applied not found in the DB");
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put(Constants.ERROR, "Coupon Applied not found in the DB");
                    ServletHelper.writeResponseToJson(response, errorMessage.toString());
                    buildResponse(response);
                }
            } catch (SQLException e) {
                logService.log(LogService.LOG_ERROR, "SQL Exception. Cause: " + e.getMessage());
                e.printStackTrace();
                JSONObject errorMessage = new JSONObject();
                errorMessage.put(Constants.ERROR, "SQL Exception. Cause: " + e.getMessage());
                ServletHelper.writeResponseToJson(response, errorMessage.toString());
                buildResponse(response);
            }
        }
        else {
            logService.log(LogService.LOG_ERROR, "Coupon code and/or Subscription Id are empty or not valid");
            JSONObject errorMessage = new JSONObject();
            errorMessage.put(Constants.ERROR, "Coupon code and/or Subscription Id are empty or not valid");
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        }
    }
}
