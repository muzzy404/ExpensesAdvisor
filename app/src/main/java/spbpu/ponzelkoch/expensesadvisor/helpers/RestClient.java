package spbpu.ponzelkoch.expensesadvisor.helpers;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

public class RestClient {

    private static final String BASE_URL = "https://expenses-advisor.herokuapp.com";

    public static final String LOGIN_URL = "/user/login";
    public static final String SEND_QR_URL = "/sendQRcode";
    public static final String RECENT_CHECKS_URL = "/checks/getRecent";
    public static final String CATEGORIES_URL = "/categories";
    public static final String ITEMS_URL = "/items/checkID/%d";
    public static final String UPDATE_CATEGORY_URL = "/items/updateCategory";
    public static final String STATISTICS_PIE_CHART_URL = "/statistics/categories";

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void get(String url, String username, String password, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(username, password);
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    public static void post(Context context, String url, JSONObject json, AsyncHttpResponseHandler responseHandler)
            throws UnsupportedEncodingException {
        AsyncHttpClient client = new AsyncHttpClient();

        String contentType = "application/json";
        StringEntity entity = new StringEntity(json.toString());
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void post(Context context, String url, JSONObject json,
                            String username, String password, AsyncHttpResponseHandler responseHandler)
            throws UnsupportedEncodingException {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(username, password);

        String contentType = "application/json";
        StringEntity entity = new StringEntity(json.toString());
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void put(Context context, String url, JSONObject json,
                           String username, String password, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(username, password);

        String contentType = "application/json";
        StringEntity entity = new StringEntity(json.toString(), "UTF-8");
        client.put(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
