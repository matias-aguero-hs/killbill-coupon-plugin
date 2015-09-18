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
function initializeGetSubscriptionByIdForm() {
    $("#subscriptionId").val("");
    $("#accountId").val("");
    $("#bundleId").val("");
    $("#externalKey").val("");
    $("#startDate").val("");
    $("#productName").val("");
    $("#productCategory").val("");
    $("#billingPeriod").val("");
    $("#phaseType").val("");
    $("#priceList").val("");
    $("#state").val("");
    $("#sourceType").val("");
    $("#billingStartDate").val("");
    $("#response").remove();
};

function getSubscriptionById() {
    var subscriptionId = document.getElementById("subscriptionId");
    var url = "http://127.0.0.1:8080/1.0/kb/subscriptions/" + subscriptionId.value;
    $.ajax({
        type: "GET",
        url: url,
        success: function (data) {
            buildSubscriptionResponse(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            initializeGetSubscriptionByIdForm();
            $("#getSubscriptionByIdContainer").append(
                "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                + "Status: " + textStatus + " || Error thrown: " + errorThrown + " || JQ Status text: " + jqXHR.statusText
                + " || JQ Response text: " + jqXHR.responseText + " || JQ Status: " + jqXHR.status
                + "</textarea>"
            );
        },
        dataType: "json",
        headers: {
            "Authorization": "Basic YWRtaW46cGFzc3dvcmQ=",
            "X-Killbill-CreatedBy": "admin",
            "X-Killbill-ApiKey": "hootsuite",
            "X-Killbill-ApiSecret": "hootsuite"
        }
    });
};

function buildSubscriptionResponse(dataSubscription) {
    $("#response").remove();
    $("#subscriptionId").val(dataSubscription.subscriptionId);
    $("#accountId").val(dataSubscription.accountId);
    $("#bundleId").val(dataSubscription.bundleId);
    $("#externalKey").val(dataSubscription.externalKey);
    $("#startDate").val(dataSubscription.startDate);
    $("#productName").val(dataSubscription.productName);
    $("#productCategory").val(dataSubscription.productCategory);
    $("#billingPeriod").val(dataSubscription.billingPeriod);
    $("#phaseType").val(dataSubscription.phaseType);
    $("#priceList").val(dataSubscription.priceList);
    $("#state").val(dataSubscription.state);
    $("#sourceType").val(dataSubscription.sourceType);
    $("#billingStartDate").val(dataSubscription.billingStartDate);
    buildSubscriptionEventsTable(dataSubscription.events);
};

function buildSubscriptionEventsTable(events) {
    var r = new Array(), j = -1;
    for (var key=0, size=events.length; key<size; key++){
        r[++j] ='<tr><td><h4>Subscription Event #'+ [key+1] + '</h4></td></tr>';
        r[++j] ='<tr><td>Subscription Event Id:</td><td><input type="text" style="width:280px;" readonly="true" id="eventId' + [key] + '" value="';
        r[++j] = events[key].eventId;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Billing Period:</td><td><input type="text" readonly="true" id="billingPeriod' + [key] + '" value="';
        r[++j] = events[key].billingPeriod;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Product:</td><td><input type="text" readonly="true" id="product' + [key] + '" value="';
        r[++j] = events[key].product;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Price List:</td><td><input type="text" readonly="true" id="priceList' + [key] + '" value="';
        r[++j] = events[key].priceList;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Event Type:</td><td><input type="text" readonly="true" id="eventType' + [key] + '" value="';
        r[++j] = events[key].eventType;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Is Blocked Billing:</td><td><input type="text" readonly="true" id="isBlockedBilling' + [key] + '" value="';
        r[++j] = events[key].isBlockedBilling;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Is Blocked Entitlement:</td><td><input type="text" readonly="true" id="isBlockedEntitlement' + [key] + '" value="';
        r[++j] = events[key].isBlockedEntitlement;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Service Name:</td><td><input type="text" readonly="true" id="serviceName' + [key] + '" value="';
        r[++j] = events[key].serviceName;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Service State Name:</td><td><input type="text" readonly="true" id="serviceStateName' + [key] + '" value="';
        r[++j] = events[key].serviceStateName;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Phase:</td><td><input type="text" readonly="true" id="phase' + [key] + '" value="';
        r[++j] = events[key].phase;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Requested Date:</td><td><input type="text" readonly="true" id="requestedDate' + [key] + '" value="';
        r[++j] = events[key].requestedDate;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Effective Date:</td><td><input type="text" readonly="true" id="effectiveDate' + [key] + '" value="';
        r[++j] = events[key].effectiveDate;
        r[++j] = '"/></td></tr><tr><td>&nbsp;</td></tr>';
    }
    $('#subscriptionEventTable').html(r.join(''));
}