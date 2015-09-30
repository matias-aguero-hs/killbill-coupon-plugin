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
    $("#percentageDiscountCreate").show();
    $("#percentageDiscountLabel").show();
    $("#amountDiscountCreate").val("");
    $("#amountCurrencyCreate").val("");
    $("#amountDiscountCreate").hide();
    $("#amountCurrencyCreate").hide();
    $("#amountDiscountLabel").hide();
    $("#amountCurrencyLabel").hide();
    $("#durationCreate").val("");
    $("#numberOfInvoicesCreate").val("");
    $("#maxRedemptionsCreate").val("");
    $("#startDateCreate").val("");
    $("#expirationDateCreate").val("");
    $("#productCreate").val("");
    $("#planCreate").val("evergreen");
    $("#createCouponButton").show();
    $("#createNewCouponButton").remove();
    $("#createCouponContainer h4").remove();
    $("#response").remove();
};

function changeDiscountType() {
    if ($("#discountTypeCreate").val() === "percentage") {
        // show percentage discount amount field
        $("#percentageDiscountCreate").show();
        $("#percentageDiscountLabel").show();
        $("#amountDiscountCreate").hide();
        $("#amountDiscountLabel").hide();
        $("#amountCurrencyCreate").hide();
        $("#amountCurrencyLabel").hide();
    }
    else {
        // show discount amount field
        $("#amountDiscountCreate").show();
        $("#amountDiscountLabel").show();
        $("#amountCurrencyCreate").show();
        $("#amountCurrencyLabel").show();
        $("#percentageDiscountCreate").hide();
        $("#percentageDiscountLabel").hide();
    }
}

function changeDurationType() {
    if ($("#durationCreate").val() === "multiple") {
        $("#numberOfInvoicesCreate").show();
        $("#numberOfInvoicesLabel").show();
    }
    else {
        $("#numberOfInvoicesCreate").hide();
        $("#numberOfInvoicesLabel").hide();
    }
}

function createCoupon() {
    var url = defaultUrl + "createCoupon";
    var couponCode = $("#couponCodeCreate").val();
    var couponName = $("#couponNameCreate").val();
    var discountType = $("#discountTypeCreate").val();
    var percentageDiscount = $("#percentageDiscountCreate").val();
    var amountDiscount = $("#amountDiscountCreate").val();
    var amountCurrency = $("#amountCurrencyCreate").val();
    var duration = $("#durationCreate").val();
    var numberOfInvoices = $("#numberOfInvoicesCreate").val();
    var maxRedemptions = $("#maxRedemptionsCreate").val();
    var startDate = $("#startDateCreate").val();
    var expirationDate = $("#expirationDateCreate").val();
    var product = $("#productCreate").val();
    var plan = $("#planCreate").val();

    var lineProduct = "";
    var linePlan = "";

    if (product && product !== '') {
        // split products
        var products = product.split(",");
        var jsonProducts = '"';
        for (i = 0; i < products.length; i++) {
            if (products.length-1 === (i)) {
                jsonProducts+= products[i].trim() + '"';
            }
            else {
                jsonProducts+= products[i].trim() + '", "';
            }
        }

        lineProduct = ', "products": [' + jsonProducts + ']';

    }

    if (plan && plan !== '') {
        // split plans
        var plans = plan.split(",");
        var jsonPlans = '"';
        for (i = 0; i < plans.length; i++) {
            if (plans.length-1 === (i)) {
                jsonPlans+= plans[i].trim() + '"';
            }
            else {
                jsonPlans+= plans[i].trim() + '", "';
            }
        }

        linePlan = ', "planPhases": [' + jsonPlans + ']';

    }

    var body = '{' +
        '"couponCode": "' + couponCode +
        '", "couponName": "' + couponName +
        '", "discountType": "' + discountType +
        '", "percentageDiscount": "' + percentageDiscount +
        '", "amountDiscount": "' + amountDiscount +
        '", "amountCurrency": "' + amountCurrency +
        '", "duration": "' + duration +
        '", "numberOfInvoices": "' + numberOfInvoices +
        '", "maxRedemptions": "' + maxRedemptions +
        '", "startDate": "' + startDate +
        '", "expirationDate": "' + expirationDate + '"' +
        lineProduct + linePlan +
        '}';

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