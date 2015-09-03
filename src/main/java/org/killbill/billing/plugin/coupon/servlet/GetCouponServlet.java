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
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.osgi.service.log.LogService;

public class GetCouponServlet extends PluginServlet {

    private final LogService logService;
    private final CouponPluginApi couponPluginApi;

    public GetCouponServlet(final LogService logService, final CouponPluginApi couponPluginApi)
    {
        this.couponPluginApi = couponPluginApi;
        this.logService = logService;
    }

    /**
     * Method doGet will handle the getCoupon operations
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logService.log(LogService.LOG_INFO, "Getting couponCode from the Parameters of the Request");
        String couponCode = request.getParameter(Constants.COUPON_CODE);

        if (!couponCode.isEmpty()) {
            try {
                logService.log(LogService.LOG_INFO, "Calling CouponPluginAPI to get a Coupon with couponCode: " + couponCode);
                CouponsRecord coupon = couponPluginApi.getCouponByCode(couponCode);

                if (null != coupon) {
                    logService.log(LogService.LOG_INFO, "Calling JsonHelper to build JSON Response using the Coupon obtained");
                    JSONObject jsonResponse = JsonHelper.buildCouponJsonResponse(coupon);
                    logService.log(LogService.LOG_INFO, "Calling the CouponPluginAPI to get the list of Products associated with the Coupon");
                    List<CouponsProductsRecord> products = couponPluginApi.getProductsOfCoupon(coupon.getCouponCode());
                    logService.log(LogService.LOG_INFO, "Calling JsonHelper to add the list of Products associated with the Coupon to the JSON Response");
                    // add Products to JSON response
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
            } catch (SQLException e) {
                logService.log(LogService.LOG_ERROR, "SQL Exception. Cause: " + e.getMessage());
                e.printStackTrace();
                buildErrorResponse(new Throwable("SQL Exception. Cause: " + e.getMessage()), response);
            }
        }
        else {
            logService.log(LogService.LOG_ERROR, "Coupon code is empty or not valid");
            buildErrorResponse(new Throwable("Coupon code is empty or not valid"), response);
        }
    }
}
