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

package org.killbill.billing.plugin.coupon.mock;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.killbill.billing.catalog.api.BillingActionPolicy;
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.Plan;
import org.killbill.billing.catalog.api.PlanPhase;
import org.killbill.billing.catalog.api.PlanPhasePriceOverride;
import org.killbill.billing.catalog.api.PriceList;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.catalog.api.ProductCategory;
import org.killbill.billing.entitlement.api.Entitlement;
import org.killbill.billing.entitlement.api.EntitlementApiException;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.util.callcontext.CallContext;

/**
 * Created by maguero on 11/09/15.
 */
public class MockEntitlement implements Entitlement {

    private UUID id = UUID.randomUUID();

    @Override
    public UUID getBaseEntitlementId() {
        return UUID.randomUUID();
    }

    @Override
    public UUID getBundleId() {
        return UUID.randomUUID();
    }

    @Override
    public UUID getAccountId() {
        return UUID.randomUUID();
    }

    @Override
    public String getExternalKey() {
        return "any";
    }

    @Override
    public EntitlementState getState() {
        return null;
    }

    @Override
    public EntitlementSourceType getSourceType() {
        return null;
    }

    @Override
    public LocalDate getEffectiveStartDate() {
        return null;
    }

    @Override
    public LocalDate getEffectiveEndDate() {
        return null;
    }

    @Override
    public Product getLastActiveProduct() {
        return null;
    }

    @Override
    public Plan getLastActivePlan() {
        return null;
    }

    @Override
    public PlanPhase getLastActivePhase() {
        return null;
    }

    @Override
    public PriceList getLastActivePriceList() {
        return null;
    }

    @Override
    public ProductCategory getLastActiveProductCategory() {
        return null;
    }

    @Override
    public Entitlement cancelEntitlementWithDate(final LocalDate localDate, final boolean b, final Iterable<PluginProperty> iterable, final CallContext callContext) throws EntitlementApiException {
        return null;
    }

    @Override
    public Entitlement cancelEntitlementWithPolicy(final EntitlementActionPolicy entitlementActionPolicy, final Iterable<PluginProperty> iterable, final CallContext callContext) throws EntitlementApiException {
        return null;
    }

    @Override
    public Entitlement cancelEntitlementWithDateOverrideBillingPolicy(final LocalDate localDate, final BillingActionPolicy billingActionPolicy, final Iterable<PluginProperty> iterable, final CallContext callContext) throws EntitlementApiException {
        return null;
    }

    @Override
    public Entitlement cancelEntitlementWithPolicyOverrideBillingPolicy(final EntitlementActionPolicy entitlementActionPolicy, final BillingActionPolicy billingActionPolicy, final Iterable<PluginProperty> iterable, final CallContext callContext) throws EntitlementApiException {
        return null;
    }

    @Override
    public void uncancelEntitlement(final Iterable<PluginProperty> iterable, final CallContext callContext) throws EntitlementApiException {

    }

    @Override
    public Entitlement changePlan(final String s, final BillingPeriod billingPeriod, final String s1, final List<PlanPhasePriceOverride> list, final Iterable<PluginProperty> iterable, final CallContext callContext) throws EntitlementApiException {
        return null;
    }

    @Override
    public Entitlement changePlanWithDate(final String s, final BillingPeriod billingPeriod, final String s1, final List<PlanPhasePriceOverride> list, final LocalDate localDate, final Iterable<PluginProperty> iterable, final CallContext callContext) throws EntitlementApiException {
        return null;
    }

    @Override
    public Entitlement changePlanOverrideBillingPolicy(final String s, final BillingPeriod billingPeriod, final String s1, final List<PlanPhasePriceOverride> list, final LocalDate localDate, final BillingActionPolicy billingActionPolicy, final Iterable<PluginProperty> iterable, final CallContext callContext) throws EntitlementApiException {
        return null;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public DateTime getCreatedDate() {
        return null;
    }

    @Override
    public DateTime getUpdatedDate() {
        return null;
    }
}
