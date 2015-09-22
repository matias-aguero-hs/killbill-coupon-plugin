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
function initializeChangeCouponForm() {
    $("#couponCode").val("");
    $("#couponName").val("");
    $("#maxRedemptions").val("");
    $("#startDate").val("");
    $("#expirationDate").val("");
    $("#product").val("");
    $("#changeCouponButton").show();
    $("#changeAnotherCouponButton").remove();
    $("#changeCouponContainer h4").remove();
    $("#response").remove();
};
function changeCoupon() {
    var couponCode = $("#couponCode").val();
    var url = defaultUrl + "changecoupon/";

    var couponName = $("#couponName").val();
    var maxRedemptions = $("#maxRedemptions").val();
    var startDate = $("#startDate").val();
    var expirationDate = $("#expirationDate").val();
    var product = $("#product").val();

    if (product && product !== '') {
        var body = '{' +
            '"couponCode": "' + couponCode +
            '", "couponName": "' + couponName +
            '", "maxRedemptions": "' + maxRedemptions +
            '", "startDate": "' + startDate +
            '", "expirationDate": "' + expirationDate +
            '", "products": ["' + product + '"]' +
            '}';
    }
    else {
        var body = '{' +
            '"couponCode": "' + couponCode +
            '", "couponName": "' + couponName +
            '", "maxRedemptions": "' + maxRedemptions +
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
                    $("#changeCouponContainer").append(
                        "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                        + det.Error
                        + "</textarea>"
                    );
                }
                else {
                    $("#changeCouponButton").hide();
                    $("#changeCouponContainer").append(
                        "<input type='text' readonly='true' id='response' style='width:800px; height:50px;'/>"
                        + "<br/>"
                        + "<input type='button' id='changeAnotherCouponButton' value='Change another Coupon' onclick='initializeChangeCouponForm()'/>"
                    );
                    $("#response").val("Coupon updated succesfully");
                }
            },
            error: function() {
                alert("Error Changing Coupon");
            },
            dataType: "json",
            contentType: "application/json",
            headers: {
                "X-Killbill-ApiKey" : "hootsuite"
            }
        });

}