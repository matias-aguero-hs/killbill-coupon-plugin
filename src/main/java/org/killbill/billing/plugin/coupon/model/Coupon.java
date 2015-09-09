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
    private boolean active;
    private DurationTypeEnum duration;
    private Integer numberOfInvoices;
    private Date startDate;
    private Date expirationDate;
    private List<String> products;
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
                .append(this.active, rhs.active)
                .append(this.duration, rhs.duration)
                .append(this.numberOfInvoices, rhs.numberOfInvoices)
                .append(this.startDate, rhs.startDate)
                .append(this.expirationDate, rhs.expirationDate)
                .append(this.products, rhs.products)
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
                .append(active)
                .append(duration)
                .append(numberOfInvoices)
                .append(startDate)
                .append(expirationDate)
                .append(products)
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
                .append("active", active)
                .append("duration", duration)
                .append("numberOfInvoices", numberOfInvoices)
                .append("startDate", startDate)
                .append("expirationDate", expirationDate)
                .append("products", products)
                .append("tenantId", tenantId)
                .toString();
    }
}
