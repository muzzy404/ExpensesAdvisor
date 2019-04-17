package spbpu.ponzelkoch.expensesadvisor;

// TODO: decide - use Assert or Truth
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.google.common.truth.Truth;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import spbpu.ponzelkoch.expensesadvisor.datamodels.Item;
import spbpu.ponzelkoch.expensesadvisor.helpers.ModelsBuilder;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;

public class ModelsBuilderTest {
    // TODO: follow https://developer.android.com/training/testing/unit-testing/local-unit-tests.html

    @Test
    public void modelsBuilder_CorrectCheckJSON_ReturnsCorrectCheckModel() throws JSONException, ParseException {
        // expected from constant strings
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(Const.CHECK_ITEM_REFERENCE_1,
                                                                   Const.CHECK_ITEM_REFERENCE_2,
                                                                   Const.CHECK_ITEM_REFERENCE_3));
        Collections.sort(expected);  // do not forget to sort

        // get string representation of actual checks
        ArrayList<Check> checks = ModelsBuilder.buildChecksFromJSON(new JSONObject(Const.CHECK_JSON_STRING));
        ArrayList<String> actual = checks.stream().map(Check::toString).collect(Collectors.toCollection(ArrayList::new));

        Truth.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void modelsBuilder_CorrectItemJSON_ReturnsCorrectItemModel() throws JSONException {
        // expected from constant strings
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(Const.ITEM_REFERENCE_1,
                                                                   Const.ITEM_REFERENCE_2));
        Collections.sort(expected);  // do not forget to sort

        // get string representation of actual checks
        ArrayList<Item> items = ModelsBuilder.buildItemsFromJSON(new JSONObject(Const.TWO_ITEMS_JSON_STRING));
        ArrayList<String> actual = items.stream().map(Item::toString).collect(Collectors.toCollection(ArrayList::new));

        Truth.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void modelBuilder_CorrectCategoriesJSON_ReturnsCorrectCategoriesList() throws JSONException {
        // expected from constant strings
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(Const.CATEGORY_REFERENCE_1,
                                                                   Const.CATEGORY_REFERENCE_2));
        Collections.sort(expected);  // do not forget to sort

        // get string representation of actual checks
        ArrayList<String> actual = ModelsBuilder.getCategoriesFromJSON(new JSONObject(Const.CATEGORIES_JSON));

        Truth.assertThat(actual).isEqualTo(expected);
    }
}


// TODO: save in some file or smth like that
class Const {
    public static final String CHECK_JSON_STRING = "{\n" +
                    "  \"checks\": [\n" +
                    "    {\n" +
                    "      \"id\": 73,\n" +
                    "      \"date\": \"2018-09-27T18:01:00\",\n" +
                    "      \"shop\": \"Агроторг ООО\",\n" +
                    "      \"sum\": 328.77\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 22,\n" +
                    "      \"date\": \"2018-09-30T15:30:00\",\n" +
                    "      \"shop\": \"\",\n" +
                    "      \"sum\": 555.00\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 16,\n" +
                    "      \"date\": \"2018-11-08T14:33:00\",\n" +
                    "      \"shop\": \"ООО \\\"ТК Прогресс\\\"\",\n" +
                    "      \"sum\": 1512.0\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
    public static final String CHECK_ITEM_REFERENCE_1 = "73 | 27 September 2018, Thu, 18:01 | Агроторг ООО | 328.77";
    public static final String CHECK_ITEM_REFERENCE_2 = "22 | 30 September 2018, Sun, 15:30 | Неизвестно | 555.00";
    public static final String CHECK_ITEM_REFERENCE_3 = "16 | 8 November 2018, Thu, 14:33 | ООО \"ТК Прогресс\" | 1512.00";

    public static final String TWO_ITEMS_JSON_STRING = "{\n" +
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
    public static final String ITEM_REFERENCE_1 = "85 | Нектар Добрый апельсин 2л т/п | 94.99 | 1 | Продукты";
    public static final String ITEM_REFERENCE_2 = "93 | Томаты кг | 133.10 | 1.70 | Продукты";
    public static final long ITEM_ID_1 = 85;
    public static final long ITEM_ID_2 = 93;

    public static final String CATEGORIES_JSON = "{\n" +
            "  \"categories\": [\n" +
            "    \"Продукты\",\n" +
            "    \"Услуги\"\n" +
            "  ]\n" +
            "}";
    public static final String CATEGORY_REFERENCE_1 = "Продукты";
    public static final String CATEGORY_REFERENCE_2 = "Услуги";
}
