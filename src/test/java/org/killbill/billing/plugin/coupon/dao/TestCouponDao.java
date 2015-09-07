///*
// * Copyright 2014-2015 Groupon, Inc
// * Copyright 2014-2015 The Billing Project, LLC
// *
// * The Billing Project licenses this file to you under the Apache License, version 2.0
// * (the "License"); you may not use this file except in compliance with the
// * License.  You may obtain a copy of the License at:
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
// * License for the specific language governing permissions and limitations
// * under the License.
// */
//
//package org.killbill.billing.plugin.coupon.dao;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import javax.sql.DataSource;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
//import org.killbill.billing.plugin.coupon.model.Constants;
//import org.mockito.Mockito;
//import org.osgi.service.log.LogService;
//
///**
// * Created by jgomez on 07/09/15.
// */
//public class TestCouponDao extends Mockito {
//
//    private CouponDao couponDao;
//    private LogService logService;
//    private DataSource dataSource;
//
//    @Before
//    public void setUp() throws SQLException {
//        logService = mock(LogService.class);
//        dataSource = mock(DataSource.class);
//        couponDao = new CouponDao(logService, dataSource);
//    }
//
//    @Test
//    public void testGetCouponByCouponCode() throws SQLException {
//        Connection connection = new Connection() {}
//        when(dataSource.getConnection()).thenReturn(connection);
//        CouponsRecord result = couponDao.getCouponByCode(Constants.COUPON_CODE);
//    }
//
//}
