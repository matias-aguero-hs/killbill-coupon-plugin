/*
 * Copyright 2010-2014 Ning, Inc.
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

package org.killbill.billing.plugin.coupon.listener;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.invoice.api.Invoice;
import org.killbill.billing.invoice.api.InvoiceApiException;
import org.killbill.billing.invoice.api.InvoiceItem;
import org.killbill.billing.notification.plugin.api.ExtBusEvent;
import org.killbill.billing.notification.plugin.api.ExtBusEventType;
import org.killbill.billing.plugin.api.PluginCallContext;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.core.CouponActivator;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.util.CouponContext;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillEventDispatcher.OSGIKillbillEventHandler;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillLogService;
import org.osgi.service.log.LogService;

public class CouponListener implements OSGIKillbillEventHandler {

    private final LogService logService;
    private final OSGIKillbillAPI osgiKillbillAPI;
    private final CouponPluginApi couponPluginApi;

    public CouponListener(final OSGIKillbillLogService logService, final OSGIKillbillAPI killbillAPI, final CouponPluginApi couponPluginApi) {
        this.logService = logService;
        this.osgiKillbillAPI = killbillAPI;
        this.couponPluginApi = couponPluginApi;
    }

    @Override
    public void handleKillbillEvent(final ExtBusEvent killbillEvent) {

        // catch only invoice creations events
        if (ExtBusEventType.INVOICE_CREATION.equals(killbillEvent.getEventType())) {
            logEvent(killbillEvent);

            logService.log(LogService.LOG_INFO, "Event catched");

            // TODO add logs
            try {
                applyDiscounts(killbillEvent);
            } catch (InvoiceApiException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void logEvent(final ExtBusEvent killbillEvent) {
        logService.log(LogService.LOG_INFO, "-------------------------------------------------");
        logService.log(LogService.LOG_INFO, "Received event " + killbillEvent.getEventType() +
                                            " for object id " + killbillEvent.getObjectId() +
                                            " of type " + killbillEvent.getObjectType());

        logService.log(LogService.LOG_INFO, "-------------------------------------------------");
        logService.log(LogService.LOG_INFO, "" + killbillEvent.getEventType());
        logService.log(LogService.LOG_INFO, "" + killbillEvent.getObjectId());
        logService.log(LogService.LOG_INFO, "" + killbillEvent.getObjectType());
        logService.log(LogService.LOG_INFO, "" + killbillEvent.getMetaData());
        logService.log(LogService.LOG_INFO, "" + killbillEvent.getTenantId());
        logService.log(LogService.LOG_INFO, "-------------------------------------------------");
    }

    private void applyDiscounts(ExtBusEvent killbillEvent) throws InvoiceApiException, SQLException {

        UUID accountId = killbillEvent.getAccountId();
        UUID invoiceId = killbillEvent.getObjectId();
        UUID tenantId = killbillEvent.getTenantId();

        logService.log(LogService.LOG_INFO, "getting coupons applied for account " + accountId);
        //for (CouponsAppliedRecord cApplied : couponPluginApi.getCouponsApplied(accountId)) {
            //CouponsRecord coupon = couponPluginApi.getCouponByCode(cApplied.getCouponCode());
            //if (coupon != null) {

                // apply discount
                Invoice invoice = osgiKillbillAPI.getInvoiceUserApi().getInvoice(invoiceId, new CouponContext(tenantId));

                // TODO validate if the coupon should be applied to this invoice

                osgiKillbillAPI.getSecurityApi().login("admin", "password");
                PluginCallContext context = new PluginCallContext(CouponActivator.PLUGIN_NAME, DateTime.now(), tenantId);
                InvoiceItem item = osgiKillbillAPI.getInvoiceUserApi().insertCreditForInvoice(accountId, invoiceId, BigDecimal.TEN, LocalDate.now(), Currency.USD, context);

                logService.log(LogService.LOG_INFO, "new invoice item added. ID: " + item.getId());

            //}
        //}




    }

}
