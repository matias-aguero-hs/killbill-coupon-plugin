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

package org.killbill.billing.plugin.coupon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.killbill.billing.plugin.core.PluginServlet;
import org.osgi.service.log.LogService;

public class CreateCouponServlet extends PluginServlet {

    private final LogService logService;
    JSONObject json = new JSONObject();

    public CreateCouponServlet(final LogService logService) {
        this.logService = logService;
    }

    public void init() throws ServletException {
        json.put("city", "Parana");
        json.put("country", "Arg");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Find me on http://127.0.0.1:8080/plugins/killbill-helloworld
        logService.log(LogService.LOG_INFO, "Hello Javi!");

        response.setContentType("application/json");
        String output = json.toString();
        PrintWriter writer = response.getWriter();
        writer.write(output);
        writer.close();

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
