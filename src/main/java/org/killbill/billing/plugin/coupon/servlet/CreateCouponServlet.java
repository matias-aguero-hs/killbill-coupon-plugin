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
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsPlansRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.Coupon;
import org.killbill.billing.plugin.coupon.model.CouponTenantContext;
import org.killbill.billing.plugin.coupon.model.DiscountTypeEnum;
import org.killbill.billing.plugin.coupon.model.DurationTypeEnum;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.killbill.billing.plugin.coupon.util.ServletHelper;
import org.osgi.service.log.LogService;

public class CreateCouponServlet extends PluginServlet {

    private final LogService logService;
    private final CouponPluginApi couponPluginApi;

    public CreateCouponServlet(final LogService logService, final CouponPluginApi couponPluginApi)
    {
        this.couponPluginApi = couponPluginApi;
        this.logService = logService;
    }

    /**
     * Method doPost will handle the createCoupon operation
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(APPLICATION_JSON);
        logService.log(LogService.LOG_INFO, "Getting apiKey parameter from the HTTP Request");
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

        Coupon coupon = null;
        try {
            logService.log(LogService.LOG_INFO, "Using JsonHelper to create an Object from the JSON Request: " + request);
            coupon = (Coupon) couponPluginApi.getObjectFromJsonRequest(request, logService, Coupon.class);
            if (null == coupon || (null == coupon.getCouponCode() || coupon.getCouponCode().isEmpty())
                    || (null == coupon.getCouponName() || coupon.getCouponName().isEmpty())
                    || (null == coupon.getDiscountType())
                    || (coupon.getDiscountType().equals(DiscountTypeEnum.percentage) && null == coupon.getPercentageDiscount())
                    || (coupon.getDiscountType().equals(DiscountTypeEnum.amount) && (null == coupon.getAmountDiscount() || null == coupon.getAmountCurrency()))
                    || (null == coupon.getDuration())) {
                throw new CouponApiException(new Throwable("Exception during generation of the Object from JSON. Missing or invalid required parameters."), 0, "Exception during generation of the Object from JSON. Missing or invalid required parameters.");
            }
            else {
                if (coupon.getDiscountType().equals(DiscountTypeEnum.percentage)
                    && (coupon.getPercentageDiscount().doubleValue() <= 0 || coupon.getPercentageDiscount().doubleValue() > 100)) {
                    logService.log(LogService.LOG_ERROR, "Error. Percentage must be between 0 and 100");
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("Error", "Percentage must be between 0 and 100");
                    ServletHelper.writeResponseToJson(response, errorMessage.toString());
                    buildResponse(response);
                }
                else if (coupon.getDiscountType().equals(DiscountTypeEnum.amount)
                        && coupon.getAmountDiscount() < 0) {
                    logService.log(LogService.LOG_ERROR, "Error. Amount can't be a negative value");
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("Error", "Amount can't be a negative value");
                    ServletHelper.writeResponseToJson(response, errorMessage.toString());
                    buildResponse(response);
                }
                else if (coupon.getDuration().equals(DurationTypeEnum.multiple)
                        && (null == coupon.getNumberOfInvoices() || coupon.getNumberOfInvoices() < 1)) {
                    logService.log(LogService.LOG_ERROR, "Error. The number of Invoices affected must be greater than 0 if the Duration is 'multiple'");
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("Error", "Must specify the number of Invoices affected if the Duration is 'multiple'");
                    ServletHelper.writeResponseToJson(response, errorMessage.toString());
                    buildResponse(response);
                }
                else {
                    // if not specified start date, use current date
                    if (null == coupon.getStartDate()) {
                        Calendar c = Calendar.getInstance();
                        coupon.setStartDate(new Date(c.getTimeInMillis()));
                    }

                    logService.log(LogService.LOG_INFO, "Calling CouponPluginAPI to Create a new Coupon");
                    couponPluginApi.createCoupon(coupon, context);

                    logService.log(LogService.LOG_INFO, "Getting recently created Coupon using couponCode: " + coupon.getCouponCode());
                    CouponsRecord couponCreated = couponPluginApi.getCouponByCode(coupon.getCouponCode());

                    if (null != couponCreated) {
                        // add Coupon to JSON response
                        logService.log(LogService.LOG_INFO, "Calling JsonHelper to build JSON Response using the Coupon created");
                        JSONObject jsonResponse = JsonHelper.buildCouponJsonResponse(couponCreated);

                        logService.log(LogService.LOG_INFO, "Calling the CouponPluginAPI to get the list of Products associated with the Coupon");
                        List<CouponsProductsRecord> products = couponPluginApi.getProductsOfCoupon(coupon.getCouponCode());

                        logService.log(LogService.LOG_INFO, "Calling the CouponPluginAPI to get the list of Plan Phases associated with the Coupon");
                        List<CouponsPlansRecord> planPhases = couponPluginApi.getPlanPhasesOfCoupon(coupon.getCouponCode());

                        // add Products and Plans to JSON response
                        logService.log(LogService.LOG_INFO, "Calling JsonHelper to add the list of Products associated with the Coupon to the JSON Response");
                        jsonResponse = JsonHelper.buildProductsAndPlansAssociatedToCoupon(jsonResponse, products, planPhases);

                        logService.log(LogService.LOG_INFO, "Writing JSON Response and returning OK");
                        ServletHelper.writeResponseToJson(response, jsonResponse.toString());
                        buildResponse(response);
                    }
                    else {
                        logService.log(LogService.LOG_ERROR, "Error. Coupon not found in the DB");
                        JSONObject errorMessage = new JSONObject();
                        errorMessage.put("Error", "Coupon not found in the DB");
                        ServletHelper.writeResponseToJson(response, errorMessage.toString());
                        buildResponse(response);
                    }
                }
            }
        } catch (CouponApiException e) {
            logService.log(LogService.LOG_ERROR, "Exception during generation of the Object from JSON. Cause: " + e.getMessage());
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "CouponApiException. Cause: " + e.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        } catch (SQLException e) {
            logService.log(LogService.LOG_ERROR, "SQL Exception. Cause: " + e.getMessage());
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "SQLException. Cause: " + e.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        }
    }
}
