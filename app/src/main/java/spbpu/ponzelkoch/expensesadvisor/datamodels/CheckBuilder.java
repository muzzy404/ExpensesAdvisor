package spbpu.ponzelkoch.expensesadvisor.datamodels;

import android.os.Build;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;

public class CheckBuilder {

    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String SHOP = "shop";
    private static final String SUM = "sum";
    private static final List<String> KEYS = Arrays.asList(ID, DATE, SHOP, SUM);

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Check buildFromJson(final String json) throws IOException, ParseException {
        final Map<String, String> map = new JsonParser().parseToMap(json);
        if (!isValid(map)) {
            throw new AssertionError();
        }
        return new Check(
                Integer.valueOf(map.get(ID)),
                map.get(DATE),
                map.get(SHOP),
                Double.valueOf(map.get(SUM)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static boolean isValid(final Map map) {
        return map.keySet().containsAll(KEYS);
    }

}
