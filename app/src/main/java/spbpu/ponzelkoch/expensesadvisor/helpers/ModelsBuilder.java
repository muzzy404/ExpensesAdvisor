package spbpu.ponzelkoch.expensesadvisor.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;

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

}
