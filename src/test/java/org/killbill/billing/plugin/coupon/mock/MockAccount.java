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

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.MutableAccountData;
import org.killbill.billing.catalog.api.Currency;

/**
 * Created by maguero on 09/09/15.
 */
public class MockAccount implements Account {

    private UUID id;
    private String externalKey;

    public MockAccount(final UUID accountId, final String externalKey) {
        this.id = accountId;
        this.externalKey = externalKey;
    }

    @Override
    public MutableAccountData toMutableAccountData() {
        return null;
    }

    @Override
    public Account mergeWithDelegate(final Account account) {
        return null;
    }

    @Override
    public String getExternalKey() {
        return externalKey;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getFirstNameLength() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public Integer getBillCycleDayLocal() {
        return null;
    }

    @Override
    public Currency getCurrency() {
        return Currency.USD;
    }

    @Override
    public UUID getPaymentMethodId() {
        return null;
    }

    @Override
    public DateTimeZone getTimeZone() {
        return null;
    }

    @Override
    public String getLocale() {
        return null;
    }

    @Override
    public String getAddress1() {
        return null;
    }

    @Override
    public String getAddress2() {
        return null;
    }

    @Override
    public String getCompanyName() {
        return null;
    }

    @Override
    public String getCity() {
        return null;
    }

    @Override
    public String getStateOrProvince() {
        return null;
    }

    @Override
    public String getPostalCode() {
        return null;
    }

    @Override
    public String getCountry() {
        return null;
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public Boolean isMigrated() {
        return null;
    }

    @Override
    public Boolean isNotifiedForInvoices() {
        return null;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public DateTime getCreatedDate() {
        return null;
    }

    @Override
    public DateTime getUpdatedDate() {
        return null;
    }
}
