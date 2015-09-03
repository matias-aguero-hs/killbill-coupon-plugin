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
    private UUID subscriptionId;
    private String couponCode;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
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
                .append(this.subscriptionId, rhs.subscriptionId)
                .append(this.couponCode, rhs.couponCode)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(accountId)
                .append(subscriptionId)
                .append(couponCode)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accountId", accountId)
                .append("subscriptionId", subscriptionId)
                .append("couponCode", couponCode)
                .toString();
    }
}
