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
 * Created by jgomez on 17/09/15.
 */
function initializeChangeSubscriptionForm() {
    $("#accountId").val("");
    $("#externalKey").val("");
    $("#productName").val("");
    $("#productCategory").val("");
    $("#billingPeriod").val("MONTHLY");
    $("#priceList").val("");
    $("#changeSubscriptionButton").show();
    $("#changeAnotherSubscriptionButton").remove();
    $("#changeSubscriptionContainer h4").remove();
    $("#response").remove();
};
function changeSubscription() {
    var subscriptionId = document.getElementById("subscriptionId");
    var url = "http://127.0.0.1:8080/1.0/kb/subscriptions/" + subscriptionId.value + "?callCompletion=false&callTimeoutSec=3";

    var accountId = $("#accountId").val();
    var externalKey = $("#externalKey").val();
    var productName = $("#productName").val();
    var productCategory = $("#productCategory").val();
    var billingPeriod = $("#billingPeriod").val();
    var priceList = $("#priceList").val();

    var body = '{' +
            '"accountId": "' + accountId +
            '", "externalKey": "' + externalKey +
            '", "productName": "' + productName +
            '", "productCategory": "' + productCategory +
            '", "billingPeriod": "' + billingPeriod +
            '", "priceList": "' + priceList +
            '"}';

    $.ajax({
            type: "PUT",
            url: url,
            data: body,
            success: function(det) {
                $("#response").remove();
                $("#changeSubscriptionButton").hide();
                $("#changeSubscriptionContainer").append(
                    "<input type='text' readonly='true' id='response' style='width:800px; height:50px;'/>"
                    + "<br/>"
                    + "<input type='button' id='changeAnotherSubscriptionButton' value='Change another Subscription' onclick='initializeChangeSubscriptionForm()'/>"
                );
                $("#response").val("Subscription changed succesfully");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $("#response").remove();
                var responseText = $.parseJSON(jqXHR.responseText);
                $("#changeSubscriptionContainer").append(
                    "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                    + "Response Code Status: " + jqXHR.status + " | Message: " + responseText.message
                    + "</textarea>"
                );
            },
            dataType: "json",
            contentType: "application/json",
            headers: {
                "Authorization": "Basic YWRtaW46cGFzc3dvcmQ=",
                "X-Killbill-CreatedBy": "admin",
                "X-Killbill-ApiKey": "hootsuite",
                "X-Killbill-ApiSecret": "hootsuite"
            }
        });
}