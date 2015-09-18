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

/**
 * Created by jgomez on 11/09/15.
 */
function initializeGetCouponByCouponCodeForm() {
    $("#couponCode").val('');
    $("#couponName").val("");
    $("#discountType").val("percentage");
    $("#percentageDiscount").val("");
    $("#isActive").val("");
    $("#duration").val("");
    $("#numberOfInvoices").val("");
    $("#startDate").val("");
    $("#expirationDate").val("");
    $("#products").val("");
};
function getCouponByCouponCode() {
    var couponCode = document.getElementById("couponCode");
    var url = defaultUrl + "getcoupon?couponCode=" + couponCode.value;
    xmlhttp.open('GET',url,true);
    xmlhttp.send(null);
    xmlhttp.onreadystatechange = function() {
        var couponName =  document.getElementById("couponName");
        var isActive =  document.getElementById("isActive");
        var discountType =  document.getElementById("discountType");
        var percentageDiscount =  document.getElementById("percentageDiscount");
        var duration =  document.getElementById("duration");
        var startDate =  document.getElementById("startDate");
        var expirationDate =  document.getElementById("expirationDate");
        var numberOfInvoices =  document.getElementById("numberOfInvoices");
        var products =  document.getElementById("products");
        if (xmlhttp.readyState == 4) {
            if ( xmlhttp.status == 200) {
                var det = eval( "(" +  xmlhttp.responseText + ")");
                $("#response").remove();
                if (det.Error) {
                    $("#getCouponByCouponCodeContainer").append(
                        "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                        + det.Error
                        + "</textarea>"
                    );
                }
                else {
                    couponName.value = det.couponName;
                    isActive.value = det.isActive;
                    discountType.value = det.discountType;
                    percentageDiscount.value = det.percentageDiscount;
                    duration.value = det.duration;
                    startDate.value = det.startDate;
                    expirationDate.value = det.expirationDate;
                    numberOfInvoices.value = det.numberOfInvoices;
                    products.value = det.products;
                }
            }
            else
                alert("Error ->" + xmlhttp.responseText);
        }
    };
}