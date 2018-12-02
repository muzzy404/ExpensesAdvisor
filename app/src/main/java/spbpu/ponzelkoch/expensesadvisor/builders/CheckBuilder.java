package spbpu.ponzelkoch.expensesadvisor.builders;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;
import spbpu.ponzelkoch.expensesadvisor.parsers.JsonParser;


public class CheckBuilder {

    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String SHOP = "shop";
    private static final String SUM = "sum";
    private static final List<String> KEYS = Arrays.asList(ID, DATE, SHOP, SUM);

    // TODO: return ArrayList of checks from json (in one json can be more than one check!!!)
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

    private static boolean isValid(final Map map) {
        return map.keySet().containsAll(KEYS);
    }

}
