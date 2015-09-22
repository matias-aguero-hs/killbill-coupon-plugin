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

import java.sql.Date;
import java.util.Calendar;
import java.util.UUID;

import org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsApplied;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.DiscountTypeEnum;
import org.killbill.billing.plugin.coupon.model.DurationTypeEnum;

/**
 * Created by maguero on 10/09/15.
 */
public class TestCouponHelper {

    public static CouponsRecord createBaseCoupon() {

        CouponsRecord coupon = new CouponsRecord();
        coupon.setCouponCode(Constants.COUPON_CODE);
        coupon.setDiscountType(DiscountTypeEnum.percentage.toString());
        coupon.setPercentageDiscount(5.0);
        coupon.setStartDate(new Date(Calendar.getInstance().getTimeInMillis()));
        coupon.setDuration(DurationTypeEnum.forever.toString());
        coupon.setIsActive(Byte.valueOf("1"));
        coupon.setNumberOfInvoices(0);
        coupon.setMaxRedemptions(0);

        return coupon;
    }

    public static CouponsAppliedRecord createBaseCouponApplied(UUID subscriptionId, UUID accountId) {

        if (subscriptionId == null) {
            subscriptionId = UUID.randomUUID();
        }

        if (accountId == null) {
            accountId = UUID.randomUUID();
        }

        CouponsAppliedRecord couponApplied = new CouponsAppliedRecord();
        couponApplied.setCouponCode(Constants.COUPON_CODE);
        couponApplied.setKbSubscriptionId(subscriptionId.toString());
        couponApplied.setKbAccountId(accountId.toString());
        couponApplied.setNumberOfInvoices(0);
        couponApplied.setMaxInvoices(6);
        couponApplied.setIsActive(Byte.valueOf("1"));

        return couponApplied;
    }

}
