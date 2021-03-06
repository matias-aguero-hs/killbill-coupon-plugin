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

package org.killbill.billing.plugin.coupon.model;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by maguero on 31/08/15.
 */
public class Coupon {

    private String couponCode;
    private String couponName;
    private DiscountTypeEnum discountType;
    private Double percentageDiscount;
    private Double amountDiscount;
    private String amountCurrency;
    private boolean active;
    private DurationTypeEnum duration;
    private Integer numberOfInvoices;
    private Integer maxRedemptions;
    private Date startDate;
    private Date expirationDate;
    private List<String> products;
    private List<String> planPhases;
    private UUID tenantId;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public DiscountTypeEnum getDiscountType() {
        return discountType;
    }

    public void setDiscountType(final DiscountTypeEnum discountType) {
        this.discountType = discountType;
    }

    public Double getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(final Double percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(final UUID tenantId) {
        this.tenantId = tenantId;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(final List<String> products) {
        this.products = products;
    }

    public List<String> getPlanPhases() { return planPhases; }

    public void setPlanPhases(final List<String> planPhases) { this.planPhases = planPhases;}

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public DurationTypeEnum getDuration() {
        return duration;
    }

    public void setDuration(final DurationTypeEnum duration) {
        this.duration = duration;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getNumberOfInvoices() {
        return numberOfInvoices;
    }

    public void setNumberOfInvoices(final Integer numberOfInvoices) {
        this.numberOfInvoices = numberOfInvoices;
    }

    public Integer getMaxRedemptions() {
        return maxRedemptions;
    }

    public void setMaxRedemptions(final Integer maxRedemptions) {
        this.maxRedemptions = maxRedemptions;
    }

    public Double getAmountDiscount() {
        return amountDiscount;
    }

    public void setAmountDiscount(final Double amountDiscount) {
        this.amountDiscount = amountDiscount;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(final String amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Coupon rhs = (Coupon) obj;
        return new EqualsBuilder()
                .append(this.couponCode, rhs.couponCode)
                .append(this.couponName, rhs.couponName)
                .append(this.discountType, rhs.discountType)
                .append(this.percentageDiscount, rhs.percentageDiscount)
                .append(this.amountDiscount, rhs.amountDiscount)
                .append(this.amountCurrency, rhs.amountCurrency)
                .append(this.active, rhs.active)
                .append(this.duration, rhs.duration)
                .append(this.numberOfInvoices, rhs.numberOfInvoices)
                .append(this.maxRedemptions, rhs.maxRedemptions)
                .append(this.startDate, rhs.startDate)
                .append(this.expirationDate, rhs.expirationDate)
                .append(this.products, rhs.products)
                .append(this.planPhases, rhs.planPhases)
                .append(this.tenantId, rhs.tenantId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(couponCode)
                .append(couponName)
                .append(discountType)
                .append(percentageDiscount)
                .append(amountDiscount)
                .append(amountCurrency)
                .append(active)
                .append(duration)
                .append(numberOfInvoices)
                .append(maxRedemptions)
                .append(startDate)
                .append(expirationDate)
                .append(products)
                .append(planPhases)
                .append(tenantId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("couponCode", couponCode)
                .append("couponName", couponName)
                .append("discountType", discountType)
                .append("percentageDiscount", percentageDiscount)
                .append("amountDiscount", amountDiscount)
                .append("amountCurrency", amountCurrency)
                .append("active", active)
                .append("duration", duration)
                .append("numberOfInvoices", numberOfInvoices)
                .append("maxRedemptions", maxRedemptions)
                .append("startDate", startDate)
                .append("expirationDate", expirationDate)
                .append("products", products)
                .append("planPhases", planPhases)
                .append("tenantId", tenantId)
                .toString();
    }
}
