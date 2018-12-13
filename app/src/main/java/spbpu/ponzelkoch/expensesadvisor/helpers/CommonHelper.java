package spbpu.ponzelkoch.expensesadvisor.helpers;

import org.json.JSONException;
import org.json.JSONObject;

public class CommonHelper {

    public static JSONObject QRStringToJSON(String string) throws JSONException {
        JSONObject result = new JSONObject();
        String[] items = string.split("&");
        for(String item: items) {
            final String field = item.split("=")[0];
            final String value = item.split("=")[1];
            result.put(field, value);
        }

        return result;
    }

}
