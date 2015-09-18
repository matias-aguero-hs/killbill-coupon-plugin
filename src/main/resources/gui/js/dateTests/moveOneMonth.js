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
function initializeMoveOneMonthForm() {
    $("#currentUtcTime").val("");
    $("#timeZone").val("");
    $("#localDate").val("");
};
function moveOneMonth() {
    var url = "http://localhost:8080/1.0/kb/test/clock?months=1&timeZone=UTC&timeoutSec=5";
    $.ajax({
        type: "PUT",
        url: url,
        success: function (data) {
            buildResponse(data);
        },
        error: function () {
            alert("Error moving 1 Month");
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
function buildResponse(dateResponse) {
    $("#currentUtcTime").val(dateResponse.currentUtcTime);
    $("#timeZone").val(dateResponse.timeZone);
    $("#localDate").val(dateResponse.localDate);
}