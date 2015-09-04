package org.killbill.billing.plugin.coupon.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.ApplyCouponRequest;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.util.CouponContext;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.killbill.billing.plugin.coupon.util.ServletHelper;
import org.osgi.service.log.LogService;

/**
 * Created by maguero on 01/09/15.
 */
public class ApplyCouponServlet extends PluginServlet {


    private final LogService logService;
    private final CouponPluginApi couponPluginApi;

    public ApplyCouponServlet(final LogService logService,
                              final CouponPluginApi couponPluginApi) {
        this.couponPluginApi = couponPluginApi;
        this.logService = logService;
    }

    /**
     * Method doPost will handle the applyCoupon operation
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(APPLICATION_JSON);
        String apiKey = request.getHeader(Constants.X_KILLBILL_API_KEY);

        CouponContext context = null;
        try {
            if (!apiKey.isEmpty()) {
                logService.log(LogService.LOG_INFO, "Creating Context using apiKey: " + apiKey);
                context = new CouponContext(couponPluginApi.getTenantId(apiKey));
            }
        } catch (CouponApiException ce) {
            logService.log(LogService.LOG_ERROR, "Exception trying to Create the Context. Cause: " + ce.getMessage());
            ce.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "CouponApiException. Cause: " + ce.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        }

        ApplyCouponRequest applyCouponRequest = null;
        try {
            logService.log(LogService.LOG_INFO, "Using JsonHelper to create an Object from the JSON Request: " + request);
            applyCouponRequest = (ApplyCouponRequest) couponPluginApi.getObjectFromJsonRequest(request, logService, ApplyCouponRequest.class);
            if (null == applyCouponRequest) {
                throw new CouponApiException(new Throwable("Exception during generation of the Object from JSON"), 0, "Exception during generation of the Object from JSON");
            }
        } catch (CouponApiException e) {
            logService.log(LogService.LOG_ERROR, "Exception during generation of the Object from JSON. Cause: " + e.getMessage());
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "CouponApiException. Cause: " + e.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        }

        try {
            if (null != applyCouponRequest) {
                logService.log(LogService.LOG_INFO, "Calling CouponPluginAPI to Apply a Coupon");
                couponPluginApi.applyCoupon(applyCouponRequest.getCouponCode(), applyCouponRequest.getSubscriptionId(), applyCouponRequest.getAccountId(), context);

                logService.log(LogService.LOG_INFO, "Getting recently created Coupon Applied using couponCode: "
                                                    + applyCouponRequest.getCouponCode()
                                                    + " and accountId: " + applyCouponRequest.getAccountId());
                CouponsAppliedRecord couponApplied =
                        couponPluginApi.getCouponApplied(applyCouponRequest.getCouponCode(), applyCouponRequest.getSubscriptionId(),
                                                         applyCouponRequest.getAccountId());

                if (null != couponApplied) {
                    // add Coupon to JSON response
                    logService.log(LogService.LOG_INFO, "Calling JsonHelper to build JSON Response using the Coupon Applied created");
                    JSONObject jsonResponse = JsonHelper.buildCouponAppliedJsonResponse(couponApplied);
                    logService.log(LogService.LOG_INFO, "Writing JSON Response and returning OK");
                    ServletHelper.writeResponseToJson(response, jsonResponse.toString());
                    buildResponse(response);
                }
                else {
                    logService.log(LogService.LOG_ERROR, "Error. Coupon Applied not found in the DB");
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("Error", "Coupon Applied not found in the DB");
                    ServletHelper.writeResponseToJson(response, errorMessage.toString());
                    buildResponse(response);
                }
            }
            else {
                logService.log(LogService.LOG_ERROR, "Apply Coupon Request object is null");
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("Error", "Apply Coupon Request object is null");
                ServletHelper.writeResponseToJson(response, errorMessage.toString());
                buildResponse(response);
            }
        } catch (SQLException e) {
            logService.log(LogService.LOG_ERROR, "SQL Exception. Cause: " + e.getMessage());
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "SQLException. Cause: " + e.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        } catch (AccountApiException e) {
            logService.log(LogService.LOG_ERROR, "Account API Exception. Cause: " + e.getMessage());
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("Error", "AccountApiException. Cause: " + e.getMessage());
            ServletHelper.writeResponseToJson(response, errorMessage.toString());
            buildResponse(response);
        }
    }
}