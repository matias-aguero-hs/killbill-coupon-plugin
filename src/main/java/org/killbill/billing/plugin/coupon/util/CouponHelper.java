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

package org.killbill.billing.plugin.coupon.util;

import java.util.Date;
import java.util.Calendar;

import org.joda.time.LocalDate;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.DurationTypeEnum;

/**
 * Created by maguero on 10/09/15.
 */
public class CouponHelper {

    // --------------------------------------------------------
    //                          COUPON
    // --------------------------------------------------------

    /**
     * Check if the coupon is applicable.
     * @param coupon
     * @return
     */
    public static boolean isApplicable(CouponsRecord coupon) {
        return isActive(coupon) && !hasExpired(coupon);
    }

    /**
     * Check if the coupon is active.
     * @param coupon
     * @return
     */
    public static boolean isActive(CouponsRecord coupon) {
        return coupon.getIsActive().equals(Byte.valueOf(Constants.BYTE_TRUE));
    }

    /**
     * Check if the coupon's start date is today or in the past.
     * @param coupon
     * @return
     */
    public static boolean isStarted(CouponsRecord coupon, LocalDate currentDate) {
        return !getZeroTimeDate(coupon.getStartDate()).after(getZeroTimeDate(currentDate.toDate()));
    }

    /**
     * Check if the coupon still has redemptions to be applied.
     * @param coupon
     * @return
     */
    public static boolean hasRedemptions(CouponsRecord coupon, Integer numberOfApplications) {
        return (coupon.getMaxRedemptions().equals(0) || coupon.getMaxRedemptions() > numberOfApplications);
    }

    /**
     * Check if the coupon has expired.
     * @param coupon
     * @return
     */
    public static boolean hasExpired(CouponsRecord coupon) {
        return (coupon.getExpirationDate() != null)
               && coupon.getExpirationDate().before(getZeroTimeDate(new Date()));
    }

    /**
     * Return a date without time.
     * @param fecha
     * @return
     */
    public static Date getZeroTimeDate(Date fecha) {
        Date res = fecha;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( fecha );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }


    // --------------------------------------------------------
    //                     COUPON APPLIED
    // --------------------------------------------------------


    /**
     * Check if the applied coupon is active.
     * @param couponApplied
     * @return
     */
    public static boolean isCouponAppliedActive(CouponsAppliedRecord couponApplied) {
        return couponApplied.getIsActive().equals(Byte.valueOf(Constants.BYTE_TRUE));
    }

    /**
     * Checks if the coupon can be applied comparing duration between Coupon and CouponApplied
     *
     * @param cApplied
     * @param coupon
     * @return
     */
    public static boolean canCouponCanBeAppliedByDuration(final CouponsAppliedRecord cApplied, final CouponsRecord coupon) {
        return (coupon.getDuration().equals(DurationTypeEnum.once.toString())
                && cApplied.getNumberOfInvoices().equals(0))
               || (coupon.getDuration().equals(DurationTypeEnum.forever.toString()))
               || (coupon.getDuration().equals(DurationTypeEnum.multiple.toString())
                   && coupon.getNumberOfInvoices() > cApplied.getNumberOfInvoices());
    }

    /**
     * Determines if the applied coupon should be disabled because it finished the application.
     *
     * @param cApplied
     * @param coupon
     * @return
     */
    public static boolean shouldDeactivateCouponApplied(final CouponsAppliedRecord cApplied, final CouponsRecord coupon) {
        return coupon.getDuration().equals(DurationTypeEnum.once.toString())
               || (coupon.getDuration().equals(DurationTypeEnum.multiple.toString())
                   && cApplied.getMaxInvoices() <= cApplied.getNumberOfInvoices());
    }

    /**
     * Determine if both Currencies are equal and the Coupon can be applied
     * @param couponCurrency
     * @param accountCurrency
     * @return
     */
    public static boolean validateCurrencies(final String couponCurrency, final String accountCurrency) {
        return couponCurrency.equalsIgnoreCase(accountCurrency);
    }
}
