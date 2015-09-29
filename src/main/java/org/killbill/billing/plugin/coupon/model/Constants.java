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

import java.util.regex.Pattern;

/**
 * Created by jgomez on 02/09/15.
 */
public class Constants {

    public static final String COUPON_CODE = "couponCode";
    public static final String COUPON_NAME = "couponName";
    public static final String DISCOUNT_TYPE = "discountType";
    public static final String PERCENTAGE_DISCOUNT = "percentageDiscount";
    public static final String IS_ACTIVE = "isActive";
    public static final String DURATION = "duration";
    public static final String NUMBER_OF_INVOICES = "numberOfInvoices";
    public static final String MAX_INVOICES = "maxInvoices";
    public static final String MAX_REDEMPTIONS = "maxRedemptions";
    public static final String CREATED_DATE = "createdDate";
    public static final String NOTES = "notes";
    public static final String START_DATE = "startDate";
    public static final String EXPIRATION_DATE = "expirationDate";
    public static final String STOP_HONOURING = "stopHonouring";
    public static final String TENANT_ID = "tenantId";
    public static final String ACCOUNT_ID = "accountId";
    public static final String SUBSCRIPTION_ID = "subscriptionId";
    public static final String X_KILLBILL_API_KEY = "X-Killbill-ApiKey";
    public static final String PLUGIN_NAME = "coupon";

    public static final Pattern GET_COUPON_PATH = Pattern.compile("/" + "getCoupon");
    public static final Pattern GET_COUPON_APPLIED_PATH = Pattern.compile("/" + "getCouponApplied");
    public static final Pattern CREATE_COUPON_PATH = Pattern.compile("/" + "createCoupon");
    public static final Pattern APPLY_COUPON_PATH = Pattern.compile("/" + "applyCoupon");
    public static final Pattern GET_ALL_COUPON_PATH = Pattern.compile("/" + "getAllCoupons");
    public static final Pattern GET_ALL_COUPONS_APPLIED_PATH = Pattern.compile("/" + "getAllCouponsApplied");
    public static final Pattern GET_ALL_ACCOUNTS_WITH_COUPON_PATH = Pattern.compile("/" + "getAllAccountsWithCoupon");
    public static final Pattern DEACTIVATE_COUPON_PATH = Pattern.compile("/" + "deactivateCoupon");
    public static final Pattern DELETE_COUPON_PATH = Pattern.compile("/" + "deleteCoupon");
    public static final Pattern CHANGE_COUPON_PATH = Pattern.compile("/" + "changeCoupon");

    public static final String ADMIN_USER = "admin";
    public static final String ADMIN_PASSWORD = "password";
    public static final String PRODUCTS = "products";
    public static final String PLAN_PHASES = "planPhases";
    public static final String COUPON_LIST = "coupons";
    public static final String COUPONS_APPLIED_LIST = "couponsApplied";
    public static final String INFO = "Info";
    public static final String ERROR = "Error";
    public static final String BYTE_TRUE = "1";
    public static final String BYTE_FALSE = "0";
    public static final String COUPON_TEST_CODE = "couponTestCode";
    public static final String COUPON_TEST_NAME = "couponTestName";
    public static final String SUBSCRIPTION_TEST_ID = "5c90a2f6-c351-404c-aea2-f87b6e88826b";
}
