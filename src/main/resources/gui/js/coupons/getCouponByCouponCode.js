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
    $("#percentageDiscount").show();
    $("#percentageDiscountLabel").show();
    $("#amountDiscount").val("");
    $("#amountCurrency").val("");
    $("#amountDiscount").hide();
    $("#amountCurrency").hide();
    $("#amountDiscountLabel").hide();
    $("#amountCurrencyLabel").hide();
    $("#isActive").val("");
    $("#duration").val("");
    $("#numberOfInvoices").val("");
    $("#startDate").val("");
    $("#expirationDate").val("");
    $("#products").val("");
    $("#plans").val("");
};

function changeDiscount(e) {
    if (e === "percentage") {
        // show percentage discount amount field
        $("#percentageDiscount").show();
        $("#percentageDiscountLabel").show();
        $("#amountDiscount").hide();
        $("#amountDiscountLabel").hide();
        $("#amountCurrency").hide();
        $("#amountCurrencyLabel").hide();
    }
    else {
        // show discount amount field
        $("#amountDiscount").show();
        $("#amountDiscountLabel").show();
        $("#amountCurrency").show();
        $("#amountCurrencyLabel").show();
        $("#percentageDiscount").hide();
        $("#percentageDiscountLabel").hide();
    }
}

function getCouponByCouponCode() {
    var couponCode = document.getElementById("couponCode");
    var url = defaultUrl + "getCoupon?couponCode=" + couponCode.value;
    xmlhttp.open('GET',url,true);
    xmlhttp.send(null);
    xmlhttp.onreadystatechange = function() {
        var couponName =  document.getElementById("couponName");
        var isActive =  document.getElementById("isActive");
        var discountType =  document.getElementById("discountType");
        var percentageDiscount =  document.getElementById("percentageDiscount");
        var amountDiscount =  document.getElementById("amountDiscount");
        var amountCurrency =  document.getElementById("amountCurrency");
        var duration =  document.getElementById("duration");
        var startDate =  document.getElementById("startDate");
        var expirationDate =  document.getElementById("expirationDate");
        var numberOfInvoices =  document.getElementById("numberOfInvoices");
        var maxRedemptions =  document.getElementById("maxRedemptions");
        var products =  document.getElementById("products");
        var plans =  document.getElementById("plans");

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
                    if (isActive) {
                        isActive.value = det.isActive;
                    }
                    if (discountType) {
                        discountType.value = det.discountType;
                        changeDiscount(det.discountType);
                    }
                    if (percentageDiscount) {
                        percentageDiscount.value = det.percentageDiscount;
                    }
                    if (amountDiscount) {
                        amountDiscount.value = det.amountDiscount;
                    }
                    if (amountCurrency) {
                        amountCurrency.value = det.amountCurrency;
                    }
                    if (duration) {
                        duration.value = det.duration;
                    }
                    startDate.value = det.startDate;
                    expirationDate.value = det.expirationDate;
                    maxRedemptions.value = det.maxRedemptions;
                    if (numberOfInvoices) {
                        numberOfInvoices.value = det.numberOfInvoices;
                    }
                    if (products) {
                        products.value = det.products;
                    }
                    if (plans) {
                        plans.value = det.planPhases;
                    }
                }
            }
            else
                alert("Error ->" + xmlhttp.responseText);
        }
    };
}