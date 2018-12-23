package spbpu.ponzelkoch.expensesadvisor.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import spbpu.ponzelkoch.expensesadvisor.datamodels.Item;

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

    public static ArrayList<JSONObject> getUpdateItemsCategoriesJSON(ArrayList<Item> items)
            throws JSONException {
        final String CATEGORY_FILED = "category";
        final String CATEGORY_IDS = "ids";

        Collections.sort(items);
        String currentCategory = items.get(0).getCategory();

        ArrayList<JSONObject> jsons = new ArrayList<>();
        JSONArray array = new JSONArray();

        for(Item item: items) {
            String itemCategory = item.getCategory();
            if (!itemCategory.equals(currentCategory)) {
                JSONObject json = new JSONObject();
                json.put(CATEGORY_FILED, currentCategory);
                json.put(CATEGORY_IDS, array);
                jsons.add(json);

                array = new JSONArray();
                currentCategory = itemCategory;
            }
            array.put(item.getId());
        }
        // last json
        JSONObject json = new JSONObject();
        json.put(CATEGORY_FILED, currentCategory);
        json.put(CATEGORY_IDS, array);
        jsons.add(json);

        return jsons;
    }

}
