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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.osgi.service.log.LogService;

public class GetCouponServlet extends PluginServlet {

    public static final String COUPON_CODE = "couponCode";
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
        String couponCode = request.getParameter(COUPON_CODE);

        try {
            CouponsRecord coupon = couponPluginApi.getCouponByCode(couponCode);

            JSONObject jsonResponse = JsonHelper.buildCouponJsonResponse(coupon);

            response.setContentType(APPLICATION_JSON);
            PrintWriter writer = response.getWriter();
            writer.write(jsonResponse.toString());
            writer.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
