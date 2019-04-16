package spbpu.ponzelkoch.expensesadvisor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import spbpu.ponzelkoch.expensesadvisor.datamodels.Item;
import spbpu.ponzelkoch.expensesadvisor.helpers.CommonHelper;
import spbpu.ponzelkoch.expensesadvisor.helpers.ModelsBuilder;


public class CommonHelperTests {

    @Test
    public void commonHelper_CorrectCheckString_ReturnsCorrectJSON() throws JSONException {
        String QRString = "t=20181205T210300&s=479.00&fn=8710000101734113&i=36006&fp=113914245&n=1";
        String actual = CommonHelper.QRStringToJSON(QRString).toString();
        String expected = "{\"s\":\"47900\"," +
                          "\"t\":\"20181205T210300\"," +
                          "\"fn\":\"8710000101734113\"," +
                          "\"fp\":\"113914245\"," +
                          "\"fd\":\"36006\"," +
                          "\"n\":\"1\"}";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void commonHelper_ItemsList_ReturnsUpdatingToAllCategoriesJSON() throws JSONException {
        ArrayList<Item> items = ModelsBuilder.buildItemsFromJSON(new JSONObject(Const.TWO_ITEMS_JSON_STRING));

        final String newCategory = "test category";
        items.get(0).setCategory(newCategory);
        items.get(1).setCategory(newCategory);

        JSONArray idsArray = new JSONArray();
        idsArray.put(Const.ITEM_ID_1);
        idsArray.put(Const.ITEM_ID_2);

        JSONObject itemsUpdate = new JSONObject();
        itemsUpdate.put("category", newCategory);
        itemsUpdate.put("ids", idsArray);

        ArrayList<JSONObject> expected = new ArrayList<>();
        expected.add(itemsUpdate);
        ArrayList<JSONObject> actual = CommonHelper.getUpdateItemsCategoriesJSON(items);

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void commonHelper_ItemsList_ReturnsUpdatingToOneCategoriesJSON() throws JSONException {
        ArrayList<Item> items = ModelsBuilder.buildItemsFromJSON(new JSONObject(Const.TWO_ITEMS_JSON_STRING));

        final String newCategory = "test category";
        final String oldCategory = items.get(0).getCategory();
        items.get(1).setCategory(newCategory);

        JSONObject oldCategoriesItems = new JSONObject();
        oldCategoriesItems.put("category", oldCategory);
        oldCategoriesItems.put("ids", new JSONArray(Collections.singletonList(Const.ITEM_ID_1)));

        JSONObject newCategoriesItems = new JSONObject();
        newCategoriesItems.put("category", newCategory);
        newCategoriesItems.put("ids", new JSONArray(Collections.singletonList(Const.ITEM_ID_2)));

        ArrayList<JSONObject> expected = new ArrayList<>();
        expected.add(oldCategoriesItems);
        expected.add(newCategoriesItems);
        ArrayList<JSONObject> actual = CommonHelper.getUpdateItemsCategoriesJSON(items);

        Assert.assertEquals(expected.toString(), actual.toString());
    }

}
