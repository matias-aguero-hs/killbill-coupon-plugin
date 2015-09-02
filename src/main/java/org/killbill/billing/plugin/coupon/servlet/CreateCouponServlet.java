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

import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.CouponJson;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
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

        String apiKey = request.getHeader("X-Killbill-ApiKey");

        CouponContext context = null;
        try {
            if (!apiKey.isEmpty()) {
                context = new CouponContext(couponPluginApi.getTenantId(apiKey));
            }
        } catch (TenantApiException e) {
            e.printStackTrace();
        }

        CouponJson coupon = (CouponJson) JsonHelper.getObjectFromRequest(request, CouponJson.class);

        try {
            if (null != coupon) {
                if (coupon.getDiscountType().equals(DiscountTypeEnum.percentage)
                    && (coupon.getPercentageDiscount() <= 0 || coupon.getPercentageDiscount() <= 100)) {
                    // TODO: Error! percentage must be between 0 and 100
                }
                couponPluginApi.createCoupon(coupon, context);
                // 4. Set response type to JSON
                response.setContentType(APPLICATION_JSON);
                // 5. send response
                buildResponse(response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
