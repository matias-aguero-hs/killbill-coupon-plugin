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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.Coupon;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.DiscountTypeEnum;
import org.killbill.billing.plugin.coupon.util.CouponContext;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.killbill.billing.tenant.api.TenantApiException;
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

        logService.log(LogService.LOG_INFO, "Getting apiKey parameter from the HTTP Request");
        String apiKey = request.getHeader(Constants.X_KILLBILL_API_KEY);

        CouponContext context = null;
        try {
            if (!apiKey.isEmpty()) {
                logService.log(LogService.LOG_INFO, "Creating Context using apiKey: " + apiKey);
                context = new CouponContext(couponPluginApi.getTenantId(apiKey));
            }
        } catch (CouponApiException ce) {
            logService.log(LogService.LOG_ERROR, "Exception trying to Create the Context. Cause: " + ce.getMessage());
            ce.printStackTrace();
        }

        Coupon coupon = null;
        try {
            logService.log(LogService.LOG_INFO, "Using JsonHelper to create an Object from the JSON Request: " + request);
            coupon = (Coupon) JsonHelper.getObjectFromRequest(request, Coupon.class, logService);
            if (null == coupon) {
                throw new CouponApiException(new Throwable("Exception during generation of the Object from JSON"), 0, "Exception during generation of the Object from JSON");
            }
        } catch (CouponApiException e) {
            logService.log(LogService.LOG_ERROR, "Exception during generation of the Object from JSON. Cause: " + e.getMessage());
            e.printStackTrace();
            buildErrorResponse(e.getCause(), response);
        }

        try {
            if (null != coupon) {
                if (coupon.getDiscountType().equals(DiscountTypeEnum.percentage)
                    && (coupon.getPercentageDiscount().doubleValue() <= 0 || coupon.getPercentageDiscount().doubleValue() > 100)) {
                    logService.log(LogService.LOG_ERROR, "Error. Percentage must be between 0 and 100");
                    buildErrorResponse(new Throwable("Error. Percentage must be between 0 and 100"), response);
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

                    // add Products to JSON response
                    logService.log(LogService.LOG_INFO, "Calling JsonHelper to add the list of Products associated with the Coupon to the JSON Response");
                    jsonResponse = JsonHelper.buildProductsAssociatedToCoupon(jsonResponse, products);

                    logService.log(LogService.LOG_INFO, "Writing JSON Response and returning OK");
                    response.setContentType(APPLICATION_JSON);
                    PrintWriter writer = response.getWriter();
                    writer.write(jsonResponse.toString());
                    writer.close();
                    buildResponse(response);
                }
                else {
                    logService.log(LogService.LOG_ERROR, "Error. Coupon not found in the DB");
                    buildErrorResponse(new Throwable("Error. Coupon not found in the DB"), response);
                }
            }
            else {
                logService.log(LogService.LOG_ERROR, "Coupon object is null");
                buildErrorResponse(new Throwable("Coupon object is null"), response);
            }
        } catch (SQLException e) {
            logService.log(LogService.LOG_ERROR, "SQL Exception. Cause: " + e.getMessage());
            e.printStackTrace();
            buildErrorResponse(new Throwable("SQL Exception. Cause: " + e.getMessage()), response);
        }
    }
}
