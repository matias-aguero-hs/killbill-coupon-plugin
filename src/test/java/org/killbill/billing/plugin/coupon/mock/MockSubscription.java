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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.killbill.billing.catalog.api.BillingActionPolicy;
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.Limit;
import org.killbill.billing.catalog.api.Plan;
import org.killbill.billing.catalog.api.PlanPhase;
import org.killbill.billing.catalog.api.PlanPhasePriceOverride;
import org.killbill.billing.catalog.api.PriceList;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.catalog.api.ProductCategory;
import org.killbill.billing.entitlement.api.Entitlement;
import org.killbill.billing.entitlement.api.EntitlementApiException;
import org.killbill.billing.entitlement.api.Subscription;
import org.killbill.billing.entitlement.api.SubscriptionEvent;
import org.killbill.billing.entitlement.api.SubscriptionEventType;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.util.callcontext.CallContext;

/**
 * Created by maguero on 11/09/15.
 */
public class MockSubscription implements Subscription {

    private UUID id = UUID.randomUUID();

    @Override
    public LocalDate getBillingStartDate() {
        return null;
    }

    @Override
    public LocalDate getBillingEndDate() {
        return null;
    }

    @Override
    public LocalDate getChargedThroughDate() {
        return null;
    }

    @Override
    public String getCurrentStateForService(final String s) {
        return null;
    }

    @Override
    public List<SubscriptionEvent> getSubscriptionEvents() {
        List<SubscriptionEvent> result = new ArrayList<>();
        SubscriptionEvent event = new SubscriptionEvent() {
            @Override
            public UUID getId() {
                return UUID.randomUUID();
            }

            @Override
            public UUID getEntitlementId() {
                return UUID.randomUUID();
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
                Product product = new Product() {
                    @Override
                    public String getName() {
                        return "fakeName";
                    }

                    @Override
                    public boolean isRetired() {
                        return false;
                    }

                    @Override
                    public Product[] getAvailable() {
                        return new Product[0];
                    }

                    @Override
                    public Product[] getIncluded() {
                        return new Product[0];
                    }

                    @Override
                    public ProductCategory getCategory() {
                        return null;
                    }

                    @Override
                    public String getCatalogName() {
                        return null;
                    }

                    @Override
                    public Limit[] getLimits() {
                        return new Limit[0];
                    }

                    @Override
                    public boolean compliesWithLimits(final String s, final double v) {
                        return false;
                    }
                };
                return product;
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
        };
        result.add(event);
        return result;
    }

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
        return null;
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
    public Entitlement cancelEntitlementWithDate(final LocalDate effectiveDate, final boolean overrideBillingEffectiveDate, final Iterable<PluginProperty> properties, final CallContext context) throws EntitlementApiException {
        return null;
    }

    @Override
    public Entitlement cancelEntitlementWithPolicy(final EntitlementActionPolicy policy, final Iterable<PluginProperty> properties, final CallContext context) throws EntitlementApiException {
        return null;
    }

    @Override
    public Entitlement cancelEntitlementWithDateOverrideBillingPolicy(final LocalDate effectiveDate, final BillingActionPolicy billingPolicy, final Iterable<PluginProperty> properties, final CallContext context) throws EntitlementApiException {
        return null;
    }

    @Override
    public Entitlement cancelEntitlementWithPolicyOverrideBillingPolicy(final EntitlementActionPolicy policy, final BillingActionPolicy billingPolicy, final Iterable<PluginProperty> properties, final CallContext context) throws EntitlementApiException {
        return null;
    }

    @Override
    public void uncancelEntitlement(final Iterable<PluginProperty> properties, final CallContext context) throws EntitlementApiException {

    }

    @Override
    public Entitlement changePlan(final String productName, final BillingPeriod billingPeriod, final String priceList, final List<PlanPhasePriceOverride> overrides, final Iterable<PluginProperty> properties, final CallContext context) throws EntitlementApiException {
        return null;
    }

    @Override
    public Entitlement changePlanWithDate(final String productName, final BillingPeriod billingPeriod, final String priceList, final List<PlanPhasePriceOverride> overrides, final LocalDate effectiveDate, final Iterable<PluginProperty> properties, final CallContext context) throws EntitlementApiException {
        return null;
    }

    @Override
    public Entitlement changePlanOverrideBillingPolicy(final String productName, final BillingPeriod billingPeriod, final String priceList, final List<PlanPhasePriceOverride> overrides, final LocalDate effectiveDate, final BillingActionPolicy billingPolicy, final Iterable<PluginProperty> properties, final CallContext context) throws EntitlementApiException {
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
