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
function initializeApplyCouponForm() {
    $("#couponCodeApply").val("");
    $("#accountIdApply").val("");
    $("#subscriptionIdApply").val("");
    $("#applyCouponButton").show();
    $("#applyNewCouponButton").remove();
    $("#applyCouponContainer h4").remove();
    $("#response").remove();
};
function applyCoupon() {
    var url = defaultUrl + "applyCoupon";
    var couponCode = $("#couponCodeApply").val();
    var accountId = $("#accountIdApply").val();
    var subscriptionId = $("#subscriptionIdApply").val();

    var body = '{' +
            '"couponCode": "' + couponCode +
            '", "accountId": "' + accountId +
            '", "subscriptionId": "' + subscriptionId +
        '"}';

    $.ajax({
            type: "POST",
            url: url,
            data: body,
            success: function(det) {
                $("#response").remove();
                if (det.Error) {
                    $("#applyCouponContainer").append(
                        "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                        + det.Error
                        + "</textarea>"
                    );
                } else {
                    $("#applyCouponButton").hide();
                    $("#applyCouponContainer").append(
                        "<input type='text' readonly='true' id='response' style='width:800px; height:50px;'/>"
                        + "<br/>"
                        + "<input type='button' id='applyNewCouponButton' value='Apply a new Coupon' onclick='initializeApplyCouponForm()'/>"
                    );
                    $("#response").val("Coupon applied succesfully");
                }
            },
            error: function() {
                alert("Error Applying Coupon");
            },
            dataType: "json",
            headers: {
                "X-Killbill-ApiKey" : "hootsuite"
            }
        });
}