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
function initializeCreateSubscriptionForm() {
    $("#accountIdCreate").val("");
    $("#externalKeyCreate").val("");
    $("#productNameCreate").val("");
    $("#productCategoryCreate").val("BASE");
    $("#billingPeriodCreate").val("MONTHLY");
    $("#priceListCreate").val("DEFAULT");
    $("#createSubscriptionButton").show();
    $("#createNewSubscriptionButton").remove();
    $("#createSubscriptionContainer h4").remove();
    $("#response").remove();
};
function createSubscription() {
    var url = "http://127.0.0.1:8080/1.0/kb/subscriptions";

    var accountId = $("#accountIdCreate").val();
    var externalKey = $("#externalKeyCreate").val();
    var productName = $("#productNameCreate").val();
    var productCategory = $("#productCategoryCreate").val();
    var billingPeriod = $("#billingPeriodCreate").val();
    var priceList = $("#priceListCreate").val();

    var body = '{' +
            '"accountId": "' + accountId +
            '", "externalKey": "' + externalKey +
            '", "productName": "' + productName +
            '", "productCategory": "' + productCategory +
            '", "billingPeriod": "' + billingPeriod +
            '", "priceList": "' + priceList +
            '"}';

    $.ajax({
            type: "POST",
            url: url,
            data: body,
            success: function(det) {
                $("#response").remove();
                $("#createSubscriptionButton").hide();
                $("#createSubscriptionContainer").append(
                    "<input type='text' readonly='true' id='response' style='width:800px; height:50px;'/>"
                    + "<br/>"
                    + "<input type='button' id='createNewSubscriptionButton' value='Create a new Subscription' onclick='initializeCreateSubscriptionForm()'/>"
                );
                $("#response").val("Subscription created succesfully");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $("#response").remove();
                if (jqXHR.status == '201') {
                    $("#createSubscriptionButton").hide();
                    $("#createSubscriptionContainer").append(
                        "<input type='text' readonly='true' id='response' style='width:800px; height:50px;'/>"
                        + "<br/>"
                        + "<input type='button' id='createNewSubscriptionButton' value='Create a new Subscription' onclick='initializeCreateSubscriptionForm()'/>"
                    );
                    $("#response").val("Subscription created succesfully");
                }
                else {
                    var responseText = $.parseJSON(jqXHR.responseText);
                    $("#createSubscriptionContainer").append(
                        "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                        + "Response Code Status: " + jqXHR.status + " | Message: " + responseText.message
                        + "</textarea>"
                    );
                }
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