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
function initializeCreateCouponForm() {
    $("#couponCodeCreate").val("");
    $("#couponNameCreate").val("");
    $("#discountTypeCreate").val("percentage");
    $("#percentageDiscountCreate").val("");
    $("#durationCreate").val("");
    $("#numberOfInvoicesCreate").val("");
    $("#startDateCreate").val("");
    $("#expirationDateCreate").val("");
    $("#productCreate").val("");
    $("#createCouponButton").show();
    $("#createNewCouponButton").remove();
    $("#createCouponContainer h4").remove();
    $("#response").remove();
};
function createCoupon() {
    var url = defaultUrl + "createcoupon/";
    var couponCode = $("#couponCodeCreate").val();
    var couponName = $("#couponNameCreate").val();
    var discountType = $("#discountTypeCreate").val();
    var percentageDiscount = $("#percentageDiscountCreate").val();
    var duration = $("#durationCreate").val();
    var numberOfInvoices = $("#numberOfInvoicesCreate").val();
    var startDate = $("#startDateCreate").val();
    var expirationDate = $("#expirationDateCreate").val();
    var product = $("#productCreate").val();

    if (product && product !== '') {
        var body = '{' +
            '"couponCode": "' + couponCode +
            '", "couponName": "' + couponName +
            '", "discountType": "' + discountType +
            '", "percentageDiscount": "' + percentageDiscount +
            '", "duration": "' + duration +
            '", "numberOfInvoices": "' + numberOfInvoices +
            '", "startDate": "' + startDate +
            '", "expirationDate": "' + expirationDate +
            '", "products": ["' + product + '"]' +
            '}';
    }
    else {
        var body = '{' +
            '"couponCode": "' + couponCode +
            '", "couponName": "' + couponName +
            '", "discountType": "' + discountType +
            '", "percentageDiscount": "' + percentageDiscount +
            '", "duration": "' + duration +
            '", "numberOfInvoices": "' + numberOfInvoices +
            '", "startDate": "' + startDate +
            '", "expirationDate": "' + expirationDate +
            '"}';
    }

    $.ajax({
            type: "POST",
            url: url,
            data: body,
            success: function(det) {
                $("#response").remove();
                if (det.Error) {
                    $("#createCouponContainer").append(
                        "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                        + det.Error
                        + "</textarea>"
                    );
                }
                else {
                    $("#createCouponButton").hide();
                    $("#createCouponContainer").append(
                        "<input type='text' readonly='true' id='response' style='width:800px; height:50px;'/>"
                        + "<br/>"
                        + "<input type='button' id='createNewCouponButton' value='Create a new Coupon' onclick='initializeCreateCouponForm()'/>"
                    );
                    $("#response").val("Coupon created succesfully");
                }
            },
            error: function() {
                alert("Error Creating Coupon");
            },
            dataType: "json",
            contentType: "application/json",
            headers: {
                "X-Killbill-ApiKey" : "hootsuite"
            }
        });

}