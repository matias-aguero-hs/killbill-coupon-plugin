package org.killbill.billing.plugin.coupon.model;

import java.util.UUID;

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
}
