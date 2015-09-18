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
function initializeGetInvoiceByIdForm() {
    $("#invoiceId").val('');
    $("#amount").val("");
    $("#currency").val("");
    $("#creditAdj").val("");
    $("#refundAdj").val("");
    $("#gotInvoiceId").val("");
    $("#invoiceDate").val("");
    $("#targetDate").val("");
    $("#invoiceNumber").val("");
    $("#balance").val("");
    $("#accountId").val("");
    $("#response").remove();
};

function getInvoiceById() {
    var invoiceId = document.getElementById("invoiceId");
    var url = "http://localhost:8080/1.0/kb/invoices/" + invoiceId.value + "/?withItems=true&audit=NONE";
    $.ajax({
        type: "GET",
        url: url,
        success: function (data) {
            buildInvoiceResponse(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            initializeGetInvoiceByIdForm();
            $("#getInvoiceByIdContainer").append(
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

function buildInvoiceResponse(dataInvoice) {
    $("#response").remove();
    $("#invoiceId").val(dataInvoice.invoiceId);
    $("#amount").val(dataInvoice.amount);
    $("#currency").val(dataInvoice.currency);
    $("#creditAdj").val(dataInvoice.creditAdj);
    $("#refundAdj").val(dataInvoice.refundAdj);
    $("#gotInvoiceId").val(dataInvoice.invoiceId);
    $("#invoiceDate").val(dataInvoice.invoiceDate);
    $("#targetDate").val(dataInvoice.targetDate);
    $("#invoiceNumber").val(dataInvoice.invoiceNumber);
    $("#balance").val(dataInvoice.balance);
    $("#accountId").val(dataInvoice.accountId);
    buildInvoiceItemsTable(dataInvoice.items);
};

function buildInvoiceItemsTable(items) {
    var r = new Array(), j = -1;
    for (var key=0, size=items.length; key<size; key++){
        r[++j] ='<tr><td><h4>Invoice Item #'+ [key+1] + '</h4></td></tr>';
        r[++j] ='<tr><td>Invoice Item Id:</td><td><input type="text" style="width:280px;" readonly="true" id="invoiceItemId' + [key] + '" value="';
        r[++j] = items[key].invoiceItemId;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Invoice Id:</td><td><input type="text" style="width:280px;" readonly="true" id="invoiceId' + [key] + '" value="';
        r[++j] = items[key].invoiceId;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Account Id:</td><td><input type="text" style="width:280px;" readonly="true" id="accountId' + [key] + '" value="';
        r[++j] = items[key].accountId;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Bundle Id:</td><td><input type="text" style="width:280px;" readonly="true" id="bundleId' + [key] + '" value="';
        r[++j] = items[key].bundleId;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Subscription Id:</td><td><input type="text" style="width:280px;" readonly="true" id="subscriptionId' + [key] + '" value="';
        r[++j] = items[key].subscriptionId;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Plan Name:</td><td><input type="text" readonly="true" id="planName' + [key] + '" value="';
        r[++j] = items[key].planName;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Phase Name:</td><td><input type="text" readonly="true" id="phaseName' + [key] + '" value="';
        r[++j] = items[key].phaseName;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Usage Name:</td><td><input type="text" readonly="true" id="usageName' + [key] + '" value="';
        r[++j] = items[key].usageName;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Item Type:</td><td><input type="text" readonly="true" id="itemType' + [key] + '" value="';
        r[++j] = items[key].itemType;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Description:</td><td><input type="text" readonly="true" id="description' + [key] + '" value="';
        r[++j] = items[key].description;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Start Date:</td><td><input type="text" readonly="true" id="startDate' + [key] + '" value="';
        r[++j] = items[key].startDate;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>End Date:</td><td><input type="text" readonly="true" id="endDate' + [key] + '" value="';
        r[++j] = items[key].endDate;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Amount:</td><td><input type="text" readonly="true" id="amount' + [key] + '" value="';
        r[++j] = items[key].amount;
        r[++j] = '"/></td></tr>';
        r[++j] ='<tr><td>Currency:</td><td><input type="text" readonly="true" id="currency' + [key] + '" value="';
        r[++j] = items[key].currency;
        r[++j] = '"/></td></tr><tr><td>&nbsp;</td></tr>';
    }
    $('#invoiceItemTable').html(r.join(''));
}