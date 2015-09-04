package org.killbill.billing.plugin.coupon.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.killbill.billing.plugin.coupon.dao.gen.tables.Coupons;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsAppliedRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsProductsRecord;
import org.killbill.billing.plugin.coupon.dao.gen.tables.records.CouponsRecord;
import org.killbill.billing.plugin.coupon.exception.CouponApiException;
import org.killbill.billing.plugin.coupon.model.Constants;
import org.osgi.service.log.LogService;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.killbill.billing.plugin.coupon.dao.gen.tables.Coupons.COUPONS;
import static org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsApplied.COUPONS_APPLIED;
import static org.killbill.billing.plugin.coupon.dao.gen.tables.CouponsProducts.COUPONS_PRODUCTS;

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
    public static Object getObjectFromRequest(HttpServletRequest request, Class clazz, LogService logService) throws CouponApiException {
        try {
            // 1. get json from request
            logService.log(LogService.LOG_INFO, "Getting JSON from Request");
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

            // 3. Convert received JSON to Object
            logService.log(LogService.LOG_INFO, "Converting JSON to Object: " + clazz.toString());
            Object jsonObject = mapper.readValue(jsonString, clazz);

            logService.log(LogService.LOG_INFO, "Returning generated Java Object from JSON");
            return jsonObject;

        } catch (Exception e) {
            logService.log(LogService.LOG_INFO, "Exception during generation of the Object from JSON. Cause: " + e.getMessage());
            e.printStackTrace();
            throw new CouponApiException(new Throwable(e.getMessage()), 0, "Exception during generation of the Object from JSON");
        }
    }

    public static JSONObject buildCouponJsonResponse(CouponsRecord coupon) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put(Constants.COUPON_CODE, coupon.getValue(COUPONS.COUPON_CODE));
        jsonResponse.put(Constants.COUPON_NAME, coupon.getValue(COUPONS.COUPON_NAME));
        jsonResponse.put(Constants.DISCOUNT_TYPE, coupon.getValue(COUPONS.DISCOUNT_TYPE));
        jsonResponse.put(Constants.PERCENTAGE_DISCOUNT, coupon.getValue(COUPONS.PERCENTAGE_DISCOUNT));
        jsonResponse.put(Constants.TENANT_ID, coupon.getValue(COUPONS.KB_TENANT_ID));
        return jsonResponse;
    }

    public static JSONObject buildCouponAppliedJsonResponse(CouponsAppliedRecord couponAppliedRecord) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put(Constants.COUPON_CODE, couponAppliedRecord.getValue(COUPONS_APPLIED.COUPON_CODE));
        jsonResponse.put(Constants.ACCOUNT_ID, couponAppliedRecord.getValue(COUPONS_APPLIED.KB_ACCOUNT_ID));
        jsonResponse.put(Constants.SUBSCRIPTION_ID, couponAppliedRecord.getValue(COUPONS_APPLIED.KB_SUBSCRIPTION_ID));
        jsonResponse.put(Constants.TENANT_ID, couponAppliedRecord.getValue(COUPONS_APPLIED.KB_TENANT_ID));
        return jsonResponse;
    }

    public static JSONObject buildProductsAssociatedToCoupon(JSONObject json, List<CouponsProductsRecord> products) {
        List<String> productsAsString = buildProductList(products);
        json.put(Constants.PRODUCTS, productsAsString);
        return json;
    }

    public static JSONObject buildCouponListJsonResponse(List<JSONObject> coupons) {
        JSONObject json = new JSONObject();
        json.put(Constants.COUPON_LIST, coupons);
        return json;
    }

    public static JSONObject buildCouponsAppliedListJsonResponse(List<JSONObject> couponsApplied) {
        JSONObject json = new JSONObject();
        json.put(Constants.COUPONS_APPLIED_LIST, couponsApplied);
        return json;
    }

    private static List<String> buildProductList(final List<CouponsProductsRecord> products) {
        List<String> productsAsString = new ArrayList<String>();
        for (CouponsProductsRecord product : products) {
            String productAsString = product.getProductName();
            productsAsString.add(productAsString);
        }
        return productsAsString;
    }
}
