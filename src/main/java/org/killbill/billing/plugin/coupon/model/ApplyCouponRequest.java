package org.killbill.billing.plugin.coupon.model;

import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Object used to apply a coupon to an existing account
 *
 * Created by maguero on 01/09/15.
 */
public class ApplyCouponRequest {

    private UUID accountId;
    private String couponCode;

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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
        ApplyCouponRequest rhs = (ApplyCouponRequest) obj;
        return new EqualsBuilder()
                .append(this.accountId, rhs.accountId)
                .append(this.couponCode, rhs.couponCode)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(accountId)
                .append(couponCode)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accountId", accountId)
                .append("couponCode", couponCode)
                .toString();
    }
}
