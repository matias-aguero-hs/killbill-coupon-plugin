package org.killbill.billing.plugin.coupon.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.killbill.billing.plugin.coupon.CouponJson;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.osgi.service.log.LogService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.killbill.billing.plugin.coupon.dao.gen.tables.Coupons.COUPONS;

/**
 * Created by maguero on 01/09/15.
 */
public class JsonHelper {

    /**
     * Convert servlet request (in json string format) into a java object
     *
     * @param request
     * @param clazz
     * @return
     */
    public static Object getObjectFromRequest(HttpServletRequest request, Class clazz) {

        try {

            // 1. get json from request
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

            String jsonString = "";
            String line = null;
            if(br != null){
                while ((line = br.readLine()) != null) {
                    jsonString += line;
                }
            }

            // 2. initiate jackson mapper
            ObjectMapper mapper = new ObjectMapper();

            // 3. Convert received JSON to Article
            Object jsonObject = mapper.readValue(jsonString, clazz);

            return jsonObject;

        } catch (Exception e) {
            // TODO use logger info
            e.printStackTrace();
        }

        return null;

    }

    public static JSONObject buildCouponJsonResponse(CouponsRecord coupon) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("couponCode", coupon.getValue(COUPONS.COUPON_CODE));
        jsonResponse.put("couponName", coupon.getValue(COUPONS.COUPON_NAME));
        jsonResponse.put("discountType", coupon.getValue(COUPONS.DISCOUNT_TYPE));
        jsonResponse.put("percentageDiscount", coupon.getValue(COUPONS.PERCENTAGE_DISCOUNT));
        jsonResponse.put("tenantId", coupon.getValue(COUPONS.KB_TENANT_ID));
        return jsonResponse;
    }
}
