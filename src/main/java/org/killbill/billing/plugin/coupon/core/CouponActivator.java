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
import org.killbill.billing.plugin.coupon.servlet.ServletRouter;
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

        // Register Servlet Router
        final ServletRouter servletRouter = new ServletRouter(couponPluginApi, logService);
        registerServlet(context, servletRouter);

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

    private void registerServlet(final BundleContext context, final HttpServlet servlet) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, Constants.PLUGIN_NAME);
        registrar.registerService(context, Servlet.class, servlet, props);
    }

    private void registerEntitlementPluginApi(final BundleContext context, final EntitlementPluginApi api) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, Constants.PLUGIN_NAME);
        registrar.registerService(context, EntitlementPluginApi.class, api, props);
    }

}
