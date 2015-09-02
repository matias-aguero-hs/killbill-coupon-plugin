package org.killbill.billing.plugin.coupon.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.model.ApplyCouponRequest;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.killbill.billing.plugin.coupon.util.CouponContext;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.killbill.billing.tenant.api.TenantApiException;
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO refactor
        String apiKey = request.getHeader(Constants.X_KILLBILL_API_KEY);

        CouponContext context = null;
        try {
            if (!apiKey.isEmpty()) {
                context = new CouponContext(couponPluginApi.getTenantId(apiKey));
            }
        } catch (TenantApiException e) {
            e.printStackTrace();
        }

        ApplyCouponRequest applyCouponRequest = (ApplyCouponRequest) JsonHelper.getObjectFromRequest(request, ApplyCouponRequest.class);

        try {
            if (null != applyCouponRequest) {
                couponPluginApi.applyCoupon(applyCouponRequest.getCouponCode(), applyCouponRequest.getAccountId(), context);

                CouponsAppliedRecord couponApplied = couponPluginApi.getCouponApplied(applyCouponRequest.getCouponCode(), applyCouponRequest.getAccountId());
                JSONObject jsonResponse = JsonHelper.buildCouponAppliedJsonResponse(couponApplied);

                PrintWriter writer = response.getWriter();
                writer.write(jsonResponse.toString());
                writer.close();
                response.setContentType(APPLICATION_JSON);
                buildResponse(response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            buildErrorResponse(e, response);
        } catch (AccountApiException e) {
            e.printStackTrace();
            buildErrorResponse(e, response);
        }
    }
}