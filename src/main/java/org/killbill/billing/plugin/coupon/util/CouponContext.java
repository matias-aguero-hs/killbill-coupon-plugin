package org.killbill.billing.plugin.coupon.util;

import org.killbill.billing.util.callcontext.TenantContext;

import java.util.UUID;

/**
 * Created by maguero on 01/09/15.
 */
public class CouponContext implements TenantContext {

    private final UUID tenantId;

    public CouponContext(final UUID tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public UUID getTenantId() {
        return tenantId;
    }
}