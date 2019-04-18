package spbpu.ponzelkoch.expensesadvisor.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import spbpu.ponzelkoch.expensesadvisor.datamodels.Item;

public class CommonHelper {

    /**
     * Method ro parse string from QR code to JSONObject for request.
     * @param string QR string
     * @return JSONObject for request
     * @throws JSONException
     */
    public static JSONObject QRStringToJSON(String string) throws JSONException {
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

    /**
     * Method to get JSONObjects for updating items categories.
     * @param items items array
     * @return JSONObjects for updating items request
     * @throws JSONException
     */
    public static ArrayList<JSONObject> getUpdateItemsCategoriesJSON(ArrayList<Item> items)
            throws JSONException {
        final String CATEGORY_FILED = "category";
        final String CATEGORY_IDS = "ids";

        // sorting by categories (see compareTo method of Item)
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
