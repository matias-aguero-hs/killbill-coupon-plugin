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

import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.Constants;

/**
 * Created by maguero on 10/09/15.
 */
public class CouponHelper {

    /**
     * Check if the coupon is active.
     * @param coupon
     * @return
     */
    public static boolean isActive(CouponsRecord coupon) {
        return coupon.getIsActive().equals(Byte.valueOf(Constants.BYTE_TRUE))
               && !getZeroTimeDate(coupon.getStartDate()).after(getZeroTimeDate(new Date()));
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


}
