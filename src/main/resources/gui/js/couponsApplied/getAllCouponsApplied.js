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
function initializeGetAllCouponsAppliedForm() {
    $('#allCouponsAppliedTable').html('');
};
function getAllCouponsApplied() {
    var url = defaultUrl + "getAllCouponsApplied";
    xmlhttp.open('GET',url,true);
    xmlhttp.send(null);
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4) {
            if ( xmlhttp.status == 200) {
                var det = eval( "(" +  xmlhttp.responseText + ")");
                $("#response").remove();
                if (det.Error) {
                    $("#getAllCouponsAppliedContainer").append(
                        "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                        + det.Error
                        + "</textarea>"
                    );
                } else {
                    var r = new Array(), j = -1;
                    r[++j] ='<tr><td>&nbsp;</td>';
                    r[++j] ='<td style="border:2px outset; text-align:center;"><h5>Coupon Code</h5></td>';
                    r[++j] ='<td style="border:2px outset; text-align:center;"><h5>Subscription Id</h5></td>';
                    r[++j] ='<td style="border:2px outset; text-align:center;"><h5>Details</h5></td></tr>';

                    for (var key=0, size=det.couponsApplied.length; key<size; key++){
                        r[++j] ='<tr><td style="border:2px outset; text-align:center;"><h5>#'+ [key+1] + '</h5></td>';
                        r[++j] ='<td style="border:2px outset; text-align:center;" id="couponCode' + [key] + '">';
                        r[++j] = det.couponsApplied[key].couponCode;
                        r[++j] = '</td>';
                        r[++j] ='<td style="border:2px outset; text-align:center;" id="subscriptionId' + [key] + '">';
                        r[++j] = det.couponsApplied[key].subscriptionId;
                        r[++j] = '</td>';
                        r[++j] ='<td style="border:2px outset; text-align:center;"><input type="button" onclick="openCouponAppliedOverlay(this);" id="getInfo' + [key] + '" value="Get Details"/></td></tr>';
                    }
                    $('#allCouponsAppliedTable').html(r.join(''));
                }
            }
            else
                alert("Error ->" + xmlhttp.responseText);
        }
    };
}

function openCouponAppliedOverlay(inputDetails) {
    var id = $(inputDetails).attr('id').substring(7, $(inputDetails).attr('id').length);
    var couponCode = $("#couponCode" + id).html();
    var subscriptionId = $("#subscriptionId" + id).html();
    importHtmlWithCouponAppliedCodeAndSubscription("views/couponsApplied/getCouponAppliedByCouponCodeAndSubscriptionId.html", couponCode, subscriptionId);
}