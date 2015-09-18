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
function initializeGetInvoicesForm() {
    $('#invoicesTable').html('');
};
function getInvoices() {
    var url = "http://localhost:8080/1.0/kb/invoices/pagination?offset=0&limit=100&withItems=true&audit=NONE";
    $.ajax({
        type: "GET",
        url: url,
        success: function (data) {
            buildTable(data);
        },
        error: function () {
            alert("Error getting Invoices");
        },
        dataType: "json",
        headers: {
            "Authorization": "Basic YWRtaW46cGFzc3dvcmQ=",
            "X-Killbill-CreatedBy": "admin",
            "X-Killbill-ApiKey": "hootsuite",
            "X-Killbill-ApiSecret": "hootsuite"
        }
    });
}

function buildTable(data) {
    var r = new Array(), j = -1;
    r[++j] ='<tr><td>&nbsp;</td>';
    r[++j] ='<td style="border:2px outset; text-align:center;"><h5>Invoice Number</h5></td>';
    r[++j] ='<td style="border:2px outset; text-align:center;"><h5>Invoice Date</h5></td>';
    r[++j] ='<td style="border:2px outset; text-align:center;"><h5>Invoice Id</h5></td>';
    r[++j] ='<td style="border:2px outset; text-align:center;"><h5>Details</h5></td></tr>';
    for (var key=0, size=data.length; key<size; key++){
        r[++j] ='<tr><td style="border:2px outset; text-align:center;"><h5>#'+ [key+1] + '</h5></td>';
        r[++j] ='<td style="border:2px outset; text-align:center;">';
        r[++j] = data[key].invoiceNumber;
        r[++j] = '</td>';
        r[++j] ='<td style="border:2px outset; text-align:center;">';
        r[++j] = data[key].invoiceDate;
        r[++j] = '</td>';
        r[++j] ='<td style="border:2px outset; text-align:center;" id="invoiceId' + [key] + '">';
        r[++j] = data[key].invoiceId;
        r[++j] = '</td>';
        r[++j] ='<td style="border:2px outset; text-align:center;"><input type="button" onclick="openInvoiceDetailsOverlay(this);" id="getInfo' + [key] + '" value="Get Details"/></td></tr>';
    }
    $('#invoicesTable').html(r.join(''));
}

function openInvoiceDetailsOverlay(inputDetails) {
    var id = $(inputDetails).attr('id').substring(7, $(inputDetails).attr('id').length);
    var invoiceId = $("#invoiceId" + id).html();
    importHtmlWithInvoiceId("views/invoices/getInvoiceById.html", invoiceId);
}