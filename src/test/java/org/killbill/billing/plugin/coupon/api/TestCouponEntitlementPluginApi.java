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

package org.killbill.billing.plugin.coupon.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.entitlement.api.Entitlement;
import org.killbill.billing.entitlement.api.EntitlementApi;
import org.killbill.billing.entitlement.api.EntitlementApiException;
import org.killbill.billing.entitlement.api.SubscriptionApiException;
import org.killbill.billing.entitlement.plugin.api.EntitlementPluginApiException;
import org.killbill.billing.entitlement.plugin.api.OnFailureEntitlementResult;
import org.killbill.billing.entitlement.plugin.api.OnSuccessEntitlementResult;
import org.killbill.billing.entitlement.plugin.api.OperationType;
import org.killbill.billing.entitlement.plugin.api.PriorEntitlementResult;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.mock.MockEntitlement;
import org.killbill.billing.plugin.coupon.mock.MockEntitlementContext;
import org.killbill.billing.plugin.coupon.mock.TestCouponHelper;
import org.killbill.billing.plugin.coupon.model.DefaultPriorEntitlementResult;
import org.killbill.billing.plugin.coupon.model.ErrorPriorEntitlementResult;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillLogService;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by maguero on 11/09/15.
 */
public class TestCouponEntitlementPluginApi extends Mockito {

    private CouponEntitlementPluginApi entitlementPluginApi;
    private CouponPluginApi couponPluginApi;
    private OSGIKillbillAPI osgiKillbillAPI;
    private EntitlementApi entitlementApi;
    private OSGIKillbillLogService logService;
    private MockEntitlementContext context;
    private MockEntitlement entitlement;
    private List<PluginProperty> properties;
    private PluginProperty pluginProperty;

    @Before
    public void setUp() {
        osgiKillbillAPI = mock(OSGIKillbillAPI.class);
        logService = mock(OSGIKillbillLogService.class);
        couponPluginApi = mock(CouponPluginApi.class);
        entitlementPluginApi = new CouponEntitlementPluginApi(null, osgiKillbillAPI, logService, couponPluginApi);
        entitlementApi = mock(EntitlementApi.class);

        context = new MockEntitlementContext();
        entitlement = new MockEntitlement();
        pluginProperty = new PluginProperty(CouponEntitlementPluginApi.COUPON_PROPERTY, "10", false);
        properties = new ArrayList<PluginProperty>();
        properties.add(pluginProperty);

        when(osgiKillbillAPI.getEntitlementApi()).thenReturn(entitlementApi);

    }

    // ----------------------
    //     FAILURE CALL
    // ----------------------

    @Test
    public void testOnFailureCall() throws EntitlementPluginApiException {

        OnFailureEntitlementResult result = entitlementPluginApi.onFailureCall(null, null);
        assertTrue(result == null);

    }

    // ----------------------
    //     PRIOR CALL
    // ----------------------

    @Test
    public void testPriorCallOtherOperation() throws EntitlementPluginApiException {

        MockEntitlementContext otherContext = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CANCEL_SUBSCRIPTION;
            }
        };
        PriorEntitlementResult result = entitlementPluginApi.priorCall(otherContext, null);
        assertTrue(result instanceof DefaultPriorEntitlementResult);

    }

    @Test
    public void testPriorCallNoCoupon() throws EntitlementPluginApiException {

        PriorEntitlementResult result = entitlementPluginApi.priorCall(context, null);
        assertTrue(result instanceof DefaultPriorEntitlementResult);

    }

    @Test
    public void testPriorCallInvalidCoupon() throws EntitlementPluginApiException, CouponApiException {

        when(couponPluginApi.validateCoupon(any(), any(), any(), any())).thenThrow(CouponApiException.class);

        PriorEntitlementResult result = entitlementPluginApi.priorCall(context, properties);
        assertTrue(result instanceof ErrorPriorEntitlementResult);
    }

    @Test
    public void testPriorCallValidCoupon() throws EntitlementPluginApiException, CouponApiException {

        when(couponPluginApi.validateCoupon(any(), any(), any(), any())).thenReturn(true);

        PriorEntitlementResult result = entitlementPluginApi.priorCall(context, properties);
        assertTrue(result instanceof DefaultPriorEntitlementResult);
    }

    // ----------------------
    //     SUCCESS CALL
    // ----------------------

    @Test
    public void testOnSuccessCallOtherOperation() throws EntitlementPluginApiException {

        MockEntitlementContext context = new MockEntitlementContext() {
            @Override
            public OperationType getOperationType() {
                return OperationType.CANCEL_SUBSCRIPTION;
            }
        };
        OnSuccessEntitlementResult result = entitlementPluginApi.onSuccessCall(context, null);
        assertTrue(result == null);

    }

    @Test
    public void testOnSuccessCallNoCoupon() throws EntitlementPluginApiException {

        OnSuccessEntitlementResult result = entitlementPluginApi.onSuccessCall(context, null);
        assertTrue(result == null);

    }

    @Test
    public void testOnSuccessCallCouponNoEntitlement() throws EntitlementApiException {

        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(null);

        OnSuccessEntitlementResult result = null;
        try {
            result = entitlementPluginApi.onSuccessCall(context, properties);
        } catch (EntitlementPluginApiException e) {
            assertTrue(e.getMessage().contains("There are no Entitlements"));
        }
        assertTrue(result == null);

    }

    @Test
    public void testOnSuccessCallInvalidCoupon() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException {

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);

        when(couponPluginApi.getCouponByCode(any())).thenReturn(TestCouponHelper.createBaseCoupon());
        when(couponPluginApi.getActiveCouponsAppliedByAccountIdAndProduct(any(), any())).thenReturn(null);
        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(couponPluginApi.applyCoupon(any(), any(), any(), any(), any())).thenThrow(CouponApiException.class);

        OnSuccessEntitlementResult result = null;
        try {
            result = entitlementPluginApi.onSuccessCall(context, properties);
        } catch (EntitlementPluginApiException e) {
            assertTrue(result == null);
            return;
        }

        fail();

    }

    @Test
    public void testOnSuccessCallValidCoupon() throws EntitlementApiException, SQLException, AccountApiException, CouponApiException, SubscriptionApiException {

        List<Entitlement> entitlements = new ArrayList<Entitlement>();
        entitlements.add(entitlement);

        when(entitlementApi.getAllEntitlementsForAccountIdAndExternalKey(any(), any(), any())).thenReturn(entitlements);
        when(couponPluginApi.applyCoupon(any(), any(), any(), any(), any())).thenReturn(true);

        OnSuccessEntitlementResult result = null;
        try {
            result = entitlementPluginApi.onSuccessCall(context, properties);
        } catch (EntitlementPluginApiException e) {
            fail();
        }
        assertTrue(result == null);

    }


}
