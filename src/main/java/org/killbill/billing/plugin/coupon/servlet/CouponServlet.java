/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014 The Billing Project, LLC
 *
 * Ning licenses this file to you under the Apache License, version 2.0
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.CouponJson;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.osgi.service.log.LogService;

import static org.killbill.billing.plugin.coupon.dao.gen.tables.Coupons.COUPONS;

public class CouponServlet extends PluginServlet {

    private final LogService logService;
    private final CouponPluginApi couponPluginApi;

    public CouponServlet(final LogService logService, final CouponPluginApi couponPluginApi)
    {
        this.couponPluginApi = couponPluginApi;
        this.logService = logService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String couponCode = request.getParameter("couponCode");

        logService.log(LogService.LOG_INFO, "Hello Javi!");

        try {
            CouponsRecord coupon = couponPluginApi.getCouponByCode(couponCode);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("name", coupon.getValue(COUPONS.COUPON_NAME));
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(jsonResponse.toString());
            writer.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. get received JSON data from request
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonString = "";
        String line = null;
        if(br != null){
            while ((line = br.readLine()) != null) {
                jsonString += line;
            }
        }

        logService.log(LogService.LOG_INFO, jsonString);

        // 2. initiate jackson mapper
        ObjectMapper mapper = new ObjectMapper();

        // 3. Convert received JSON to Article
        CouponJson article = mapper.readValue(jsonString, CouponJson.class);

        // 4. Set response type to JSON
        response.setContentType("application/json");

        // 5. send response
        buildCreatedResponse("http://localhost:8080/plugins/killbill-coupon/", response);

    }
}
