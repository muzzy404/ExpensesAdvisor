package spbpu.ponzelkoch.expensesadvisor.datamodels;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import spbpu.ponzelkoch.expensesadvisor.parsers.JsonParser;

/**
 @author veronika K. on 29.11.18 */
public class JsonParserTest {

    final private JsonParser parser = new JsonParser();

    @Test
    public void positiveTest()
            throws IOException {
        final String expectedJson = "{\n" +
                "  \"s\" : \"45252.2927\",\n" +
                "  \"t\" : \"20156868T86\",\n" +
                "  \"fn\" : \"8080\",\n" +
                "  \"i\" : \"68656\",\n" +
                "  \"fp\" : \"825757\"\n" +
                "}";
        final String resultJson = parser.parseToJson("t=20156868T86&s=45252.2927&fn=8080&i=68656&fp=825757&n=1");
        Assert.assertEquals(expectedJson, resultJson);
    }

    @Test
    public void negativeTest()
            throws IOException {
        try {
            parser.parseToJson("t=20156868T86&s=45252.2927&i=68656&fp=825757&n=1");
            Assert.fail("No AssertionError");
        } catch(final AssertionError e) {
            Assert.assertTrue(true);
        }
    }
}
