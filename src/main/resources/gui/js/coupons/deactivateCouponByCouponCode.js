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
function initializeDeactivateCouponByCouponCodeForm() {
    $("#deactivateCouponCode").val('');
    $("#stopHonouring").val('false');
    $("#response").remove();
};
function deactivateCouponByCouponCode() {
    var couponCode = document.getElementById("deactivateCouponCode");
    var stopHonouring = document.getElementById("stopHonouring");
    var url = defaultUrl + "deactivateCoupon?couponCode=" + couponCode.value + "&stopHonouring=" + stopHonouring.value;
    xmlhttp.open('GET',url,true);
    xmlhttp.send(null);
    xmlhttp.onreadystatechange = function() {
        var response =  document.getElementById("response");
        if (xmlhttp.readyState == 4) {
            if ( xmlhttp.status == 200) {
                var det = eval( "(" +  xmlhttp.responseText + ")");
                if (det.Error) {
                    $("#response").remove();
                    $("#deactivateCouponByCouponCodeContainer").append(
                        "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                        + det.Error
                        + "</textarea>"
                    );
                } else {
                    $("#response").remove();
                    $("#deactivateCouponByCouponCodeContainer").append(
                        "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                        + det.Info
                        + "</textarea>"
                    );
                }
            }
            else
                alert("Error ->" + xmlhttp.responseText);
        }
    };
}