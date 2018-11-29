package spbpu.ponzelkoch.expensesadvisor.datamodels;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;


public class CheckBuilderTest {

    private static final int ID = 11;
    private static final String DATE = "2018-11-28T11:54:22.047Z";
    private static final String SHOP = "SomeShop";
    private static final double SUM = 23.55;

    private static final String json = "{\n" +
            "  \"id\" : \"" + ID + "\",\n" +
            "  \"date\" : \"" + DATE + "\",\n" +
            "  \"shop\" : \"" + SHOP + "\",\n" +
            "  \"sum\" : \"" + SUM + "\"\n" +
            "}";


    @Test
    public void test() {
        try {
            final Check expectedValue = new Check(ID, DATE, SHOP, SUM);
            final Check actualValue = CheckBuilder.buildFromJson(json);
            Assert.assertEquals(true, areEquals(expectedValue, actualValue));
        } catch (final Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private boolean areEquals(final Check c1, final Check c2) {
        if (c1.getId() != c2.getId()) {
            return false;
        }
        if (c1.getDate().compareTo(c2.getDate()) != 0) {
            return false;
        }
        if (!c1.getPlace().equals(c2.getPlace())) {
            return false;
        }
        if (!c1.getSum().equals(c2.getSum())) {
            return false;
        }
        return true;
    }

}
