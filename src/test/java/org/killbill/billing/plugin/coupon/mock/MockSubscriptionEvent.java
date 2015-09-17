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

import java.util.UUID;

import org.joda.time.LocalDate;
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.Plan;
import org.killbill.billing.catalog.api.PlanPhase;
import org.killbill.billing.catalog.api.PriceList;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.entitlement.api.SubscriptionEvent;
import org.killbill.billing.entitlement.api.SubscriptionEventType;

/**
 * Created by jgomez on 17/09/15.
 */
public class MockSubscriptionEvent implements SubscriptionEvent {

    @Override
    public UUID getId() {
        return null;
    }

    @Override
    public UUID getEntitlementId() {
        return null;
    }

    @Override
    public LocalDate getEffectiveDate() {
        return null;
    }

    @Override
    public LocalDate getRequestedDate() {
        return null;
    }

    @Override
    public SubscriptionEventType getSubscriptionEventType() {
        return null;
    }

    @Override
    public boolean isBlockedBilling() {
        return false;
    }

    @Override
    public boolean isBlockedEntitlement() {
        return false;
    }

    @Override
    public String getServiceName() {
        return null;
    }

    @Override
    public String getServiceStateName() {
        return null;
    }

    @Override
    public Product getPrevProduct() {
        return null;
    }

    @Override
    public Plan getPrevPlan() {
        return null;
    }

    @Override
    public PlanPhase getPrevPhase() {
        return null;
    }

    @Override
    public PriceList getPrevPriceList() {
        return null;
    }

    @Override
    public BillingPeriod getPrevBillingPeriod() {
        return null;
    }

    @Override
    public Product getNextProduct() {
        return null;
    }

    @Override
    public Plan getNextPlan() {
        return null;
    }

    @Override
    public PlanPhase getNextPhase() {
        return null;
    }

    @Override
    public PriceList getNextPriceList() {
        return null;
    }

    @Override
    public BillingPeriod getNextBillingPeriod() {
        return null;
    }
}
