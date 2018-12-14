package spbpu.ponzelkoch.expensesadvisor.tests;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;

import spbpu.ponzelkoch.expensesadvisor.helpers.ModelsBuilder;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;

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

}
