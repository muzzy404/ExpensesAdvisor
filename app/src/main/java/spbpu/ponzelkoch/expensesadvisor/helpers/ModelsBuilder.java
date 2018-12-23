package spbpu.ponzelkoch.expensesadvisor.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Item;

public class ModelsBuilder {

    public static ArrayList<Check> buildChecksFromJSON(final JSONObject json)
            throws JSONException, ParseException {
        final String ID_FIELD = "id";
        final String DATE_FIELD = "date";
        final String SHOP_FIELD = "shop";
        final String SUM_FIELD = "sum";
        final String ARRAY_NAME = "checks";

        ArrayList<Check> models = new ArrayList<>();
        JSONArray checksArray = json.getJSONArray(ARRAY_NAME);

        for(int i = 0; i < checksArray.length(); ++i) {
            JSONObject checkJSON = checksArray.getJSONObject(i);
            Check check = new Check(checkJSON.getLong(ID_FIELD),
                                    checkJSON.getString(DATE_FIELD),
                                    checkJSON.getString(SHOP_FIELD),
                                    checkJSON.getDouble(SUM_FIELD));
            models.add(check);
        }

        return models;
    }

    public static ArrayList<Item> buildItemsFromJSON(final JSONObject json) throws JSONException {
        final String ID_FIELD = "id";
        final String NAME_FIELD = "name";
        final String SUM_FIELD = "price";
        final String QUANTITY_FIELD = "quantity";
        final String CATEGORY_FIELD = "category";
        final String ARRAY_NAME = "items";

        ArrayList<Item> models = new ArrayList<>();
        JSONArray itemsArray = json.getJSONArray(ARRAY_NAME);

        for(int i = 0; i < itemsArray.length(); ++i) {
            JSONObject itemJSON = itemsArray.getJSONObject(i);
            Item item = new Item(itemJSON.getLong(ID_FIELD),
                                 itemJSON.getString(NAME_FIELD),
                                 itemJSON.getDouble(SUM_FIELD),
                                 itemJSON.getDouble(QUANTITY_FIELD),
                                 itemJSON.getString(CATEGORY_FIELD));
            models.add(item);
        }

        return models;
    }

    public static ArrayList<String> getCategoriesFromJSON(final JSONObject json) throws JSONException {
        final String ARRAY_NAME = "categories";

        ArrayList<String> categories = new ArrayList<>();
        JSONArray categoriesArray = json.getJSONArray(ARRAY_NAME);

        for(int i = 0; i < categoriesArray.length(); ++i) {
            String category = categoriesArray.getString(i);
            categories.add(category);
        }

        return categories;
    }

}
