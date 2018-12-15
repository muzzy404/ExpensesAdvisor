package spbpu.ponzelkoch.expensesadvisor.helpers;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

public class RestClient {

    public static final String LOGIN_URL = "/user/login";
    public static final String SEND_QR_URL = "/sendQRcode";
    private static final String BASE_URL = "https://expenses-advisor.herokuapp.com";

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

//    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.post(getAbsoluteUrl(url), params, responseHandler);
//    }

    public static void post(Context context, String url, JSONObject json, ResponseHandlerInterface responseHandler)
            throws UnsupportedEncodingException {
        AsyncHttpClient client = new AsyncHttpClient();

        String contentType = "application/json";
        StringEntity entity = new StringEntity(json.toString());
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void post(Context context, String url, JSONObject json,
                            String username, String password, ResponseHandlerInterface responseHandler)
            throws UnsupportedEncodingException {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(username, password);

        String contentType = "application/json";
        StringEntity entity = new StringEntity(json.toString());
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
