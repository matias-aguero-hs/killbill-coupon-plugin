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
import org.killbill.billing.catalog.api.PlanPhasePriceOverride;
import org.killbill.billing.catalog.api.PlanPhaseSpecifier;
import org.killbill.billing.entitlement.plugin.api.EntitlementContext;
import org.killbill.billing.entitlement.plugin.api.OperationType;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.util.callcontext.CallOrigin;
import org.killbill.billing.util.callcontext.UserType;

/**
 * Created by maguero on 11/09/15.
 */
public class MockEntitlementContext implements EntitlementContext {

    public final static String PRODUCT_NAME = "Standard";

    @Override
    public OperationType getOperationType() {
        return OperationType.CREATE_SUBSCRIPTION;
    }

    @Override
    public UUID getAccountId() {
        return UUID.randomUUID();
    }

    @Override
    public UUID getDestinationAccountId() {
        return UUID.randomUUID();
    }

    @Override
    public UUID getBundleId() {
        return UUID.randomUUID();
    }

    @Override
    public PlanPhaseSpecifier getPlanPhaseSpecifier() {
        return new PlanPhaseSpecifier(PRODUCT_NAME, null, null, null, null);
    }

    @Override
    public String getExternalKey() {
        return null;
    }

    @Override
    public List<PlanPhasePriceOverride> getPlanPhasePriceOverride() {
        return null;
    }

    @Override
    public LocalDate getEffectiveDate() {
        return null;
    }

    @Override
    public Iterable<PluginProperty> getPluginProperties() {
        return null;
    }

    @Override
    public UUID getUserToken() {
        return null;
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public CallOrigin getCallOrigin() {
        return null;
    }

    @Override
    public UserType getUserType() {
        return null;
    }

    @Override
    public String getReasonCode() {
        return null;
    }

    @Override
    public String getComments() {
        return null;
    }

    @Override
    public DateTime getCreatedDate() {
        return null;
    }

    @Override
    public DateTime getUpdatedDate() {
        return null;
    }

    @Override
    public UUID getTenantId() {
        return UUID.randomUUID();
    }
}
