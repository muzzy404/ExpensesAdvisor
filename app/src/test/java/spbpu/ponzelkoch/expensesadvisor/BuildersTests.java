package spbpu.ponzelkoch.expensesadvisor;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;

import spbpu.ponzelkoch.expensesadvisor.datamodels.Item;
import spbpu.ponzelkoch.expensesadvisor.helpers.ModelsBuilder;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;

// TODO: change inner structure of tests
public class BuildersTests {

    @Test
    public void ChecksBuildPositiveTest() throws JSONException, ParseException {
        String rawData =
                "{\n" +
                "  \"checks\": [\n" +
                "    {\n" +
                "      \"id\": 73,\n" +
                "      \"date\": \"2018-09-27T18:01:00\",\n" +
                "      \"shop\": \"Агроторг ООО\",\n" +
                "      \"sum\": 328.77\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 16,\n" +
                "      \"date\": \"2018-11-08T14:33:00\",\n" +
                "      \"shop\": \"ООО \\\"ТК Прогресс\\\"\",\n" +
                "      \"sum\": 1512.0\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ArrayList<String> expected = new ArrayList<>();
        expected.add("73 | Thu 27 Sep 2018 | Агроторг ООО | 328.77");
        expected.add("16 | Thu 8 Nov 2018 | ООО \"ТК Прогресс\" | 1512.00");

        ArrayList<Check> actual = ModelsBuilder.buildChecksFromJSON(new JSONObject(rawData));

        int i = 0;
        for(Check check: actual) {
            Assert.assertEquals(expected.get(i++), check.toString());
        }
    }

    @Test
    public void ItemsBuildPositiveTest() throws JSONException {
        String rawData =
                "{\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"id\": 85,\n" +
                "      \"name\": \"Нектар Добрый апельсин 2л т/п\",\n" +
                "      \"price\": 94.99,\n" +
                "      \"quantity\": 1.0,\n" +
                "      \"category\": \"Продукты\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 93,\n" +
                "      \"name\": \"Томаты кг\",\n" +
                "      \"price\": 133.1,\n" +
                "      \"quantity\": 1.702,\n" +
                "      \"category\": \"Продукты\"\n" +
                "    }" +
                "  ]\n" +
                "}";

        ArrayList<String> expected = new ArrayList<>();
        expected.add("85 | Нектар Добрый апельсин 2л т/п | 94.99 | 1 | Продукты");
        expected.add("93 | Томаты кг | 133.10 | 1.70 | Продукты");

        ArrayList<Item> actual = ModelsBuilder.buildItemsFromJSON(new JSONObject(rawData));

        int i = 0;
        for(Item item: actual) {
            Assert.assertEquals(expected.get(i++), item.toString());
        }
    }

    @Test
    public void CategoriesBuildPositiveTest() throws JSONException {
        String rawData = "{\n" +
                         "  \"categories\": [\n" +
                         "    \"Продукты\",\n" +
                         "    \"Услуги\"\n" +
                         "  ]\n" +
                         "}";

        ArrayList<String> expected = new ArrayList<>();
        expected.add("Продукты");
        expected.add("Услуги");

        ArrayList<String> actual = ModelsBuilder.getCategoriesFromJSON(new JSONObject(rawData));

        int i = 0;
        for(String category: actual) {
            Assert.assertEquals(expected.get(i++), category);
        }
    }
}
