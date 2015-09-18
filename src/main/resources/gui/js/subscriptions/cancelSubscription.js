/**
 * Created by jgomez on 18/09/15.
 */
function initializeCancelSubscriptionForm() {
    $("#subscriptionId").val("");
    $("#response").remove();
};

function cancelSubscription() {
    var subscriptionId = document.getElementById("subscriptionId");
    var url = "http://127.0.0.1:8080/1.0/kb/subscriptions/" + subscriptionId.value + "?callCompletion=false&callTimeoutSec=5&useRequestedDateForBilling=false";
    $.ajax({
        type: "DELETE",
        url: url,
        success: function (data) {
            $("#response").remove();
            $("#cancelSubscriptionButton").hide();
            $("#cancelSubscriptionContainer").append(
                "<input type='text' readonly='true' id='response' style='width:800px; height:50px;'/>"
                + "<br/>"
                + "<input type='button' id='cancelAnotherSubscriptionButton' value='Cancel another Subscription' onclick='initializeCancelSubscriptionForm()'/>"
            );
            $("#response").val("Subscription succesfully cancelled");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $("#response").remove();
            if (jqXHR.status == '200') {
                $("#cancelSubscriptionButton").hide();
                $("#cancelSubscriptionContainer").append(
                    "<input type='text' readonly='true' id='response' style='width:800px; height:50px;'/>"
                    + "<br/>"
                    + "<input type='button' id='cancelAnotherSubscriptionButton' value='Cancel another Subscription' onclick='initializeCancelSubscriptionForm()'/>"
                );
                $("#response").val("Subscription succesfully cancelled");
            }
            else {
                var responseText = $.parseJSON(jqXHR.responseText);
                $("#cancelSubscriptionContainer").append(
                    "<textarea readonly='true' id='response' style='width:500px; height:50px;'>"
                    + "Response Code Status: " + jqXHR.status + " | Message: " + responseText.message
                    + "</textarea>"
                );
            }
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