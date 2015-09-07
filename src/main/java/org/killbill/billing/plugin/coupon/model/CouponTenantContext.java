package org.killbill.billing.plugin.coupon.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.killbill.billing.util.callcontext.TenantContext;

import java.util.UUID;

/**
 * Created by maguero on 01/09/15.
 */
public class CouponTenantContext implements TenantContext {

    private final UUID tenantId;

    public CouponTenantContext(final UUID tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public UUID getTenantId() {
        return tenantId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        CouponTenantContext rhs = (CouponTenantContext) obj;
        return new EqualsBuilder()
                .append(this.tenantId, rhs.tenantId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(tenantId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("tenantId", tenantId)
                .toString();
    }
}