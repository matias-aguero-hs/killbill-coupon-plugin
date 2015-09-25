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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.osgi.service.log.LogService;

public class ServletRouter extends BaseServlet {

    private GetCouponServlet getCouponServlet;
    private ApplyCouponServlet applyCouponServlet;
    private ChangeCouponServlet changeCouponServlet;
    private CreateCouponServlet createCouponServlet;
    private DeactivateCouponServlet deactivateCouponServlet;
    private DeleteCouponServlet deleteCouponServlet;
    private GetAllAccountsWithCouponServlet getAllAccountsWithCouponServlet;
    private GetAllCouponsServlet getAllCouponsServlet;
    private GetAllCouponsAppliedServlet getAllCouponsAppliedServlet;
    private GetCouponAppliedServlet getCouponAppliedServlet;


    public ServletRouter(final CouponPluginApi couponPluginApi, final LogService logService) {
        super(couponPluginApi, logService);
        getCouponServlet = new GetCouponServlet(logService, couponPluginApi);
        applyCouponServlet = new ApplyCouponServlet(logService, couponPluginApi);
        changeCouponServlet = new ChangeCouponServlet(logService, couponPluginApi);
        createCouponServlet = new CreateCouponServlet(logService, couponPluginApi);
        deactivateCouponServlet = new DeactivateCouponServlet(logService, couponPluginApi);
        deleteCouponServlet = new DeleteCouponServlet(logService, couponPluginApi);
        getAllAccountsWithCouponServlet = new GetAllAccountsWithCouponServlet(logService, couponPluginApi);
        getAllCouponsServlet = new GetAllCouponsServlet(logService, couponPluginApi);
        getAllCouponsAppliedServlet = new GetAllCouponsAppliedServlet(logService, couponPluginApi);
        getCouponAppliedServlet = new GetCouponAppliedServlet(logService, couponPluginApi);
    }

    @Override
    protected void doOptions(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        setCrossSiteScriptingHeaders(resp);
        forward(req, resp);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        setCrossSiteScriptingHeaders(resp);
        forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        setCrossSiteScriptingHeaders(resp);
        forward(req, resp);
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        setCrossSiteScriptingHeaders(resp);
        forward(req, resp);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        setCrossSiteScriptingHeaders(resp);
        forward(req, resp);
    }

    // Lame - we should rather use the built-in forward mechanism but I'm not sure how to create
    // the dispatchers without a web.xml: getServletContext().getNamedDispatcher("...").forward(req, resp);
    private void forward(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        final String pathInfo = req.getPathInfo();
        Matcher matcher;

        // ---------- coupons -------------
        matcher = Constants.CREATE_COUPON_PATH.matcher(pathInfo);
        if (matcher.matches()) {
            createCouponServlet.service(req, resp);
            return;
        }

        matcher = Constants.GET_COUPON_PATH.matcher(pathInfo);
        if (matcher.matches()) {
            getCouponServlet.service(req, resp);
            return;
        }

        matcher = Constants.GET_ALL_COUPON_PATH.matcher(pathInfo);
        if (matcher.matches()) {
            getAllCouponsServlet.service(req, resp);
            return;
        }

        matcher = Constants.DELETE_COUPON_PATH.matcher(pathInfo);
        if (matcher.matches()) {
            deleteCouponServlet.service(req, resp);
            return;
        }

        matcher = Constants.DEACTIVATE_COUPON_PATH.matcher(pathInfo);
        if (matcher.matches()) {
            deactivateCouponServlet.service(req, resp);
            return;
        }

        matcher = Constants.CHANGE_COUPON_PATH.matcher(pathInfo);
        if (matcher.matches()) {
            changeCouponServlet.service(req, resp);
            return;
        }

        // ---------- applied coupons -------------

        matcher = Constants.GET_COUPON_APPLIED_PATH.matcher(pathInfo);
        if (matcher.matches()) {
            getCouponAppliedServlet.service(req, resp);
            return;
        }

        matcher = Constants.APPLY_COUPON_PATH.matcher(pathInfo);
        if (matcher.matches()) {
            applyCouponServlet.service(req, resp);
            return;
        }

        matcher = Constants.GET_ALL_COUPONS_APPLIED_PATH.matcher(pathInfo);
        if (matcher.matches()) {
            getAllCouponsAppliedServlet.service(req, resp);
            return;
        }

        matcher = Constants.GET_ALL_ACCOUNTS_WITH_COUPON_PATH.matcher(pathInfo);
        if (matcher.matches()) {
            getAllAccountsWithCouponServlet.service(req, resp);
            return;
        }

        resp.sendError(404, "Resource " + pathInfo + " not found");
    }

}
