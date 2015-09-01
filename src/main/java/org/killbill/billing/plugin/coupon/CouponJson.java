package org.killbill.billing.plugin.coupon;

/**
 * Created by maguero on 31/08/15.
 */
public class CouponJson {

    private String couponCode;

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    private String couponName;


    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }


}
