/*
 * Copyright 2010-2014 Ning, Inc.
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

package org.killbill.billing.plugin.coupon.core;

import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.killbill.billing.entitlement.plugin.api.EntitlementPluginApi;
import org.killbill.billing.osgi.api.OSGIPluginProperties;
import org.killbill.billing.plugin.coupon.api.CouponEntitlementPluginApi;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.CouponDao;
import org.killbill.billing.plugin.coupon.listener.CouponListener;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.servlet.ApplyCouponServlet;
import org.killbill.billing.plugin.coupon.servlet.CreateCouponServlet;
import org.killbill.billing.plugin.coupon.servlet.DeactivateCouponServlet;
import org.killbill.billing.plugin.coupon.servlet.DeleteCouponServlet;
import org.killbill.billing.plugin.coupon.servlet.GetAllAccountsWithCouponServlet;
import org.killbill.billing.plugin.coupon.servlet.GetAllCouponsAppliedServlet;
import org.killbill.billing.plugin.coupon.servlet.GetAllCouponsServlet;
import org.killbill.billing.plugin.coupon.servlet.GetCouponAppliedServlet;
import org.killbill.billing.plugin.coupon.servlet.GetCouponServlet;
import org.killbill.killbill.osgi.libs.killbill.KillbillActivatorBase;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillEventDispatcher.OSGIKillbillEventHandler;
import org.osgi.framework.BundleContext;

public class CouponActivator extends KillbillActivatorBase {


    private OSGIKillbillEventHandler couponListener;

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);

        final CouponDao dao = new CouponDao(logService, dataSource.getDataSource());

        // Register Plugin API
        final CouponPluginApi couponPluginApi = new CouponPluginApi(logService, dao, killbillAPI);
        registerCouponPluginApi(context, couponPluginApi);

        // register a CouponEntitlementPluginApi
        final EntitlementPluginApi entitlementPluginApi = new CouponEntitlementPluginApi(configProperties.getProperties(),
                                                                                         killbillAPI, logService, couponPluginApi);
        registerEntitlementPluginApi(context, entitlementPluginApi);

        // Register an event listener
        couponListener = new CouponListener(logService, killbillAPI, couponPluginApi);
        dispatcher.registerEventHandler(couponListener);

        // Register Get Coupon Servlet
        final GetCouponServlet getCouponServlet = new GetCouponServlet(logService, couponPluginApi);
        registerServlet(context, getCouponServlet, Constants.GET_COUPON_PATH);

        // Register Get Coupon Applied Servlet
        final GetCouponAppliedServlet getCouponAppliedServlet = new GetCouponAppliedServlet(logService, couponPluginApi);
        registerServlet(context, getCouponAppliedServlet, Constants.GET_COUPON_APPLIED_PATH);

        // Register Create Coupon Servlet
        final CreateCouponServlet createCouponServlet = new CreateCouponServlet(logService, couponPluginApi);
        registerServlet(context, createCouponServlet, Constants.CREATE_COUPON_PATH);

        // Register Apply Coupon Servlet
        final ApplyCouponServlet applyCouponServlet = new ApplyCouponServlet(logService, couponPluginApi);
        registerServlet(context, applyCouponServlet, Constants.APPLY_COUPON_PATH);

        // Register Get All Coupons Servlet
        final GetAllCouponsServlet getAllCouponServlet = new GetAllCouponsServlet(logService, couponPluginApi);
        registerServlet(context, getAllCouponServlet, Constants.GET_ALL_COUPON_PATH);

        // Register Get All Coupons Applied Servlet
        final GetAllCouponsAppliedServlet getAllCouponsAppliedServlet = new GetAllCouponsAppliedServlet(logService, couponPluginApi);
        registerServlet(context, getAllCouponsAppliedServlet, Constants.GET_ALL_COUPONS_APPLIED_PATH);

        // Register Get All Accounts With Coupon Servlet
        final GetAllAccountsWithCouponServlet getAllAccountsWithCouponServlet = new GetAllAccountsWithCouponServlet(logService, couponPluginApi);
        registerServlet(context, getAllAccountsWithCouponServlet, Constants.GET_ALL_ACCOUNTS_WITH_COUPON_PATH);

        // Register Deactivate Coupon Servlet
        final DeactivateCouponServlet deactivateCouponServlet = new DeactivateCouponServlet(logService, couponPluginApi);
        registerServlet(context, deactivateCouponServlet, Constants.DEACTIVATE_COUPON_PATH);

        // Register Delete Coupon Servlet
        final DeleteCouponServlet deleteCouponServlet = new DeleteCouponServlet(logService, couponPluginApi);
        registerServlet(context, deleteCouponServlet, Constants.DELETE_COUPON_PATH);
    }

    private void registerCouponPluginApi(final BundleContext context, final CouponPluginApi couponPluginApi) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, Constants.PLUGIN_NAME);
        registrar.registerService(context, CouponPluginApi.class, couponPluginApi, props);
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        super.stop(context);
    }

    @Override
    public OSGIKillbillEventHandler getOSGIKillbillEventHandler() {
        return couponListener;
    }

    private void registerServlet(final BundleContext context, final HttpServlet servlet, String subPath) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, Constants.PLUGIN_NAME + subPath);
        registrar.registerService(context, Servlet.class, servlet, props);
    }

    private void registerEntitlementPluginApi(final BundleContext context, final EntitlementPluginApi api) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, Constants.PLUGIN_NAME);
        registrar.registerService(context, EntitlementPluginApi.class, api, props);
    }

}
