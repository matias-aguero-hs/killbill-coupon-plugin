package org.killbill.billing.plugin.coupon;

import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.killbill.billing.plugin.coupon.model.DiscountTypeEnum;

/**
 * Created by maguero on 31/08/15.
 */
public class CouponJson {

    private String couponCode;
    private String couponName;
    private DiscountTypeEnum discountType;
    private Double percentageDiscount;
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
        CouponJson rhs = (CouponJson) obj;
        return new EqualsBuilder()
                .append(this.couponCode, rhs.couponCode)
                .append(this.couponName, rhs.couponName)
                .append(this.discountType, rhs.discountType)
                .append(this.percentageDiscount, rhs.percentageDiscount)
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
                .append("tenantId", tenantId)
                .toString();
    }
}
