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
import org.killbill.billing.invoice.api.Invoice;
import org.killbill.billing.invoice.api.InvoiceApiException;
import org.killbill.billing.invoice.api.InvoiceItem;
import org.killbill.billing.invoice.api.InvoiceItemType;
import org.killbill.billing.notification.plugin.api.ExtBusEvent;
import org.killbill.billing.notification.plugin.api.ExtBusEventType;
import org.killbill.billing.plugin.api.PluginCallContext;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.model.DiscountTypeEnum;
import org.killbill.billing.plugin.coupon.model.CouponTenantContext;
import org.killbill.billing.plugin.coupon.model.DurationTypeEnum;
import org.killbill.billing.plugin.coupon.util.CouponHelper;
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

            try {
                applyDiscounts(killbillEvent);
            } catch (Exception e) {
                logService.log(LogService.LOG_ERROR,
                               "There is an error trying to validate and apply a coupon discount", e);
            }

        }
    }

    private void logEvent(final ExtBusEvent killbillEvent) {
        logService.log(LogService.LOG_INFO, "-------------------------------------------------");
        logService.log(LogService.LOG_INFO, "Received event " + killbillEvent.getEventType() +
                                            " for object id " + killbillEvent.getObjectId() +
                                            " of type " + killbillEvent.getObjectType());
        logService.log(LogService.LOG_INFO, "-------------------------------------------------");

    }

    /**
     * Validate and apply discounts to the invoice
     *
     * @param killbillEvent
     * @throws InvoiceApiException
     * @throws SQLException
     */
    private void applyDiscounts(ExtBusEvent killbillEvent) throws InvoiceApiException, SQLException {

        UUID accountId = killbillEvent.getAccountId();
        UUID invoiceId = killbillEvent.getObjectId();
        UUID tenantId = killbillEvent.getTenantId();

        // login TODO figure out where credential are pick up from...
        osgiKillbillAPI.getSecurityApi().login(Constants.ADMIN_USER, Constants.ADMIN_PASSWORD);

        // get invoice
        Invoice invoice = osgiKillbillAPI.getInvoiceUserApi().getInvoice(invoiceId, new CouponTenantContext(tenantId));

        for (InvoiceItem item : invoice.getInvoiceItems()) {
            if (InvoiceItemType.RECURRING.equals(item.getInvoiceItemType())) {

                // TODO validate phase plan (EVERGREEN) ?
                // item.getPhaseName().contains("evergreen");

                logService.log(LogService.LOG_INFO, "RECURRING item " + item.getId() + " found for invoice " + invoice.getId());

                // get coupon applied by subscription id
                UUID subscriptionId = item.getSubscriptionId();
                logService.log(LogService.LOG_INFO, "getting coupons applied for subscription " + subscriptionId);
                CouponsAppliedRecord cApplied = couponPluginApi.getActiveCouponAppliedBySubscription(subscriptionId);
                if (cApplied == null) {
                    logService.log(LogService.LOG_INFO, "Subscription " + subscriptionId + " does not have active coupon applied.");
                    continue;
                }

                // get coupon info from DB
                CouponsRecord coupon = couponPluginApi.getCouponByCode(cApplied.getCouponCode());

                // check if Coupon's application is still valid
                if (validateCouponApplication(cApplied, coupon)) {
                    BigDecimal discountAmount = calculateDiscountAmount(item, cApplied);

                    PluginCallContext context = new PluginCallContext(Constants.PLUGIN_NAME, DateTime.now(), tenantId);
                    InvoiceItem invoiceItemAdjustment = osgiKillbillAPI.getInvoiceUserApi().insertInvoiceItemAdjustment(accountId, invoiceId, item.getId(),
                                                                                                                        item.getStartDate(), discountAmount,
                                                                                                                        item.getCurrency(), context);
                    if (null != invoiceItemAdjustment) {
                        // add 1 to the number of Invoices affected
                        cApplied.setNumberOfInvoices(cApplied.getNumberOfInvoices() + 1);
                        // check if now the duration is completed (after adding the last discount)

                        couponPluginApi.increaseNumberOfInvoicesAndSetActiveStatus(
                                cApplied.getCouponCode(),
                                cApplied.getNumberOfInvoices(),
                                CouponHelper.shouldDeactivateCouponApplied(cApplied, coupon),
                                subscriptionId);

                        logService.log(LogService.LOG_INFO, "Invoice Item Adjustment added. ID: " + invoiceItemAdjustment.getId());
                    }
                    else {
                        logService.log(LogService.LOG_ERROR, "Error: Invoice Item Adjustment not added.");
                    }
                }
            } else {
                logService.log(LogService.LOG_INFO, "Skipping invoice item " + item.getId() + "/" + item.getInvoiceItemType());
            }
        }
    }

    private boolean validateCouponApplication(final CouponsAppliedRecord cApplied, final CouponsRecord coupon) {
        if (CouponHelper.isCouponAppliedActive(cApplied)) {
            // now check if it has not completed its duration yet
            if (CouponHelper.canCouponCanBeAppliedByDuration(cApplied, coupon)) {
                // coupon has not completed its duration and could be applied
                return true;
            }
            else {
                // coupon has completed its duration cannot be applied
                logService.log(LogService.LOG_ERROR, "Error: Coupon Application has completed its duration and the discount cannot be applied again.");
            }
        }
        else {
            // coupon application is not active and the discount can't be applied
            logService.log(LogService.LOG_ERROR, "Error: Coupon Application is not active and the discount cannot be applied again.");
        }
        return false;
    }

    /**
     * Calculate the discount amount based on the DiscountType
     *
     * @param item
     * @param cApplied
     * @return
     */
    private BigDecimal calculateDiscountAmount(final InvoiceItem item, final CouponsAppliedRecord cApplied)
            throws SQLException {

        CouponsRecord coupon = couponPluginApi.getCouponByCode(cApplied.getCouponCode());

        if ((coupon != null) && (coupon.getDiscountType() != null)
            && (coupon.getDiscountType().equals(DiscountTypeEnum.percentage.toString()))) {
            BigDecimal discountAmount = (item.getAmount()
                                   .multiply(BigDecimal.valueOf(coupon.getPercentageDiscount()))
                                   .divide(BigDecimal.valueOf(100)));
            logService.log(LogService.LOG_INFO, "Discount calculated: " + discountAmount);
            return discountAmount;
        } // TODO complete when implement DiscountTypeEnum.amount

        logService.log(LogService.LOG_WARNING, "No discount type was found for coupon " + cApplied.getCouponCode());
        return BigDecimal.ZERO;
    }

}

