package org.killbill.billing.plugin.coupon.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.plugin.core.PluginServlet;
import org.killbill.billing.plugin.coupon.CouponJson;
import org.killbill.billing.plugin.coupon.api.CouponPluginApi;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.model.ApplyCouponRequest;
import org.killbill.billing.plugin.coupon.util.CouponContext;
import org.killbill.billing.plugin.coupon.util.JsonHelper;
import org.killbill.billing.tenant.api.TenantApiException;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.osgi.service.log.LogService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

import static org.killbill.billing.plugin.coupon.dao.gen.tables.Coupons.COUPONS;

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

        String apikey = request.getHeader("X-Killbill-ApiKey");

        CouponContext context = null;
        try {
            context = new CouponContext(couponPluginApi.getTenantId(apikey));
        } catch (TenantApiException e) {
            e.printStackTrace();
        }

        ApplyCouponRequest req = (ApplyCouponRequest) JsonHelper.getObjectFromRequest(request, ApplyCouponRequest.class);

        try {
            couponPluginApi.applyCoupon(req.getCouponCode(), req.getAccountId(), context);

            response.setContentType("application/json");
            buildCreatedResponse("http://localhost:8080/plugins/coupon-plugin/applied", response);

        } catch (SQLException e) {
            e.printStackTrace();
            buildErrorResponse(e, response);
        } catch (AccountApiException e) {
            e.printStackTrace();
            buildErrorResponse(e, response);
        }


    }
}