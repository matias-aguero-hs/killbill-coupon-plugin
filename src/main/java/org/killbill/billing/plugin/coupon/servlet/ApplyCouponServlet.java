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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.ApplyCouponRequest;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.CouponTenantContext;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.killbill.billing.plugin.coupon.util.ServletHelper;
import org.osgi.service.log.LogService;

/**
 * Created by maguero on 01/09/15.
 */
public class ApplyCouponServlet extends PluginServlet {


    private final LogService logService;
    private final CouponPluginApi couponPluginApi;

    public ApplyCouponServlet(final LogService logService,
                              final CouponPluginApi couponPluginApi) {
        this.couponPluginApi = couponPluginApi;
        this.logService = logService;
    }

    /**
     * Method doPost will handle the applyCoupon operation
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(APPLICATION_JSON);
        String apiKey = request.getHeader(Constants.X_KILLBILL_API_KEY);

        CouponTenantContext context = null;
        try {
            if (!apiKey.isEmpty()) {
                logService.log(LogService.LOG_INFO, "Creating Context using apiKey: " + apiKey);
                context = new CouponTenantContext(couponPluginApi.getTenantId(apiKey));
            }
        } catch (CouponApiException ce) {
            logService.log(LogService.LOG_ERROR, "Exception trying to Create the Context. Cause: " + ce.getMessage());
            ce.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "CouponApiException. Cause: " + ce.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        }

        ApplyCouponRequest applyCouponRequest = null;
        try {
            logService.log(LogService.LOG_INFO, "Using JsonHelper to create an Object from the JSON Request: " + request);
            applyCouponRequest = (ApplyCouponRequest) couponPluginApi.getObjectFromJsonRequest(request, logService, ApplyCouponRequest.class);
            if (null == applyCouponRequest) {
                throw new CouponApiException(new Throwable("Exception during generation of the Object from JSON. Missing or invalid required parameters."), 0, "Exception during generation of the Object from JSON. Missing or invalid required parameters.");
            }
        } catch (CouponApiException e) {
            logService.log(LogService.LOG_ERROR, "Exception during generation of the Object from JSON. Cause: " + e.getMessage());
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "CouponApiException. Cause: " + e.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
            return;
        }

        try {
            logService.log(LogService.LOG_INFO, "Calling CouponPluginAPI to Apply a Coupon");
            couponPluginApi.applyCoupon(applyCouponRequest.getCouponCode(), null, applyCouponRequest.getSubscriptionId(), applyCouponRequest.getAccountId(), context);

            logService.log(LogService.LOG_INFO, "Getting recently created Coupon Applied using couponCode: "
                                                + applyCouponRequest.getCouponCode()
                                                + " and accountId: " + applyCouponRequest.getAccountId());
            CouponsAppliedRecord couponApplied =
                    couponPluginApi.getCouponApplied(applyCouponRequest.getCouponCode(), applyCouponRequest.getSubscriptionId(), applyCouponRequest.getAccountId());

            if (null != couponApplied) {
                // add Coupon to JSON response
                logService.log(LogService.LOG_INFO, "Calling JsonHelper to build JSON Response using the Coupon Applied created");
                JSONObject jsonResponse = JsonHelper.buildCouponAppliedJsonResponse(couponApplied);
                logService.log(LogService.LOG_INFO, "Writing JSON Response and returning OK");
                ServletHelper.writeResponseToJson(response, jsonResponse.toString());
                buildResponse(response);
            }
            else {
                logService.log(LogService.LOG_ERROR, "Error. Coupon Applied not found in the DB");
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("Error", "Coupon Applied not found in the DB");
                ServletHelper.writeResponseToJson(response, errorMessage.toString());
                buildResponse(response);
            }
        } catch (SQLException e) {
            logService.log(LogService.LOG_ERROR, "SQL Exception. Cause: " + e.getMessage());
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "SQLException. Cause: " + e.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        } catch (CouponApiException e) {
            logService.log(LogService.LOG_ERROR, "Coupon cannot be applied. Cause: " + e.getMessage());
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "Coupon cannot be applied. Cause: " + e.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        } catch (Exception e) {
            logService.log(LogService.LOG_ERROR, "API Exception. Cause: " + e.getMessage());
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "API Exception. Cause: " + e.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        }
    }
}