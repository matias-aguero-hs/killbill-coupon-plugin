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

public class Coupon {

    private final String couponCode;
    private final String couponName;

    public Coupon(final String couponCode, final String couponName) {
        this.couponCode = couponCode;
        this.couponName = couponName;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public String getCouponName() {
        return couponName;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null || !(other instanceof Coupon)) {
            return false;
        } else {
            final Coupon typedOther = (Coupon) other;
            return couponCode == typedOther.getCouponCode()
                    && couponName.equals(typedOther.getCouponName());
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (couponCode != null ? couponCode.hashCode() : 0);
        result = 31 * result + (couponName != null ? couponName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Coupon(couponCode %d, couponName %s)",
                couponCode, couponName);
    }
}
