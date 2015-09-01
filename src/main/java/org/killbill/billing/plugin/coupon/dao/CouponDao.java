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

package org.killbill.billing.plugin.coupon.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jooq.impl.DSL;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.dao.PluginDao;

import static org.killbill.billing.plugin.coupon.dao.gen.tables.Coupons.COUPONS;

public class CouponDao extends PluginDao {

    public CouponDao(final DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    public CouponsRecord getCouponByCode(final String couponCode) throws SQLException {
        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<CouponsRecord>() {
                           @Override
                           public CouponsRecord withConnection(final Connection conn) throws SQLException {
                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(COUPONS)
                                         .where(COUPONS.COUPON_CODE.equal(couponCode))
                                         .fetchOne();
                           }
                       });
    }

}
