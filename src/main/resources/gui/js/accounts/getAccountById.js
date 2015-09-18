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
function initializeGetAccountByIdForm() {
    $("#accountId").val("");
    $("#name").val("");
    $("#externalKey").val("");
    $("#email").val("");
    $("#currency").val("");
    $("#paymentMethodId").val("");
    $("#billCycleDayLocal").val("");
    $("#response").remove();
};

function getAccountById() {
    var accountId = document.getElementById("accountId");
    var url = "http://localhost:8080/1.0/kb/accounts/" + accountId.value + "/?accountWithBalance=false&accountWithBalanceAndCBA=false&audit=NONE";

    $.ajax({
        type: "GET",
        url: url,
        success: function (data) {
            buildAccountResponse(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            initializeGetAccountByIdForm();
            $("#getAccountByIdContainer").append(
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

function buildAccountResponse(dataAccount) {
    $("#response").remove();
    $("#accountId").val(dataAccount.accountId);
    $("#name").val(dataAccount.name);
    $("#externalKey").val(dataAccount.externalKey);
    $("#email").val(dataAccount.email);
    $("#currency").val(dataAccount.currency);
    $("#paymentMethodId").val(dataAccount.paymentMethodId);
    $("#billCycleDayLocal").val(dataAccount.billCycleDayLocal);
};