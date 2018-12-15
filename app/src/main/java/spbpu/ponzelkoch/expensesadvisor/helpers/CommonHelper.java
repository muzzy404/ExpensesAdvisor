package spbpu.ponzelkoch.expensesadvisor.helpers;

import org.json.JSONException;
import org.json.JSONObject;

public class CommonHelper {

    public static JSONObject QRStringToJSON(String string) throws JSONException {
        // TODO: add checking for wrong QRs
        JSONObject result = new JSONObject();
        String[] items = string.split("&");
        for(String item: items) {
            String field = item.split("=")[0];
            String value = item.split("=")[1];

            // rename "i" field to "fd"
            if (field.equals("i"))
                field = "fd";

            // remove '.' from sum string
            if (field.equals("s")) {
                if (value.contains("."))
                    value = value.replace(".", "");
            }

            result.put(field, value);
        }

        return result;
    }

}
