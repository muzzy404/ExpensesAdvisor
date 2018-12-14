package spbpu.ponzelkoch.expensesadvisor.tests;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import spbpu.ponzelkoch.expensesadvisor.helpers.CommonHelper;

public class CommonHelperTests {

    @Test
    public void QRStringToJSONPositiveTest() throws JSONException {
        String QRString = "t=20181205T210300&s=479.00&fn=8710000101734113&i=36006&fp=113914245&n=1";
        String actual = CommonHelper.QRStringToJSON(QRString).toString();
        String expected = "{\"s\":\"479.00\"," +
                          "\"t\":\"20181205T210300\"," +
                          "\"fn\":\"8710000101734113\"," +
                          "\"i\":\"36006\"," +
                          "\"fp\":\"113914245\"," +
                          "\"n\":\"1\"}";
        Assert.assertEquals(expected, actual);
    }

}
