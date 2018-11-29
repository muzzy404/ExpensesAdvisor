package spbpu.ponzelkoch.expensesadvisor.datamodels;

import android.os.Build;


import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;

/**
 * @author veronika K. on 26.11.18
 */
public class JsonParser
        implements Parser {

    //t=...&s=...&fn=...&i=...&fp=...&n=...
    private List<String> requiredFields = Arrays.asList("t", "s", "fn", "i", "fp");

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String parseToJson(final String line, final List<String> requiredFields)
            throws IOException {
        final Map<String, String> items = new HashMap<>();
        this.requiredFields = requiredFields;
        try {
            Arrays.asList(line.split("&"))
                    .forEach(item -> {
                        final String k = item.split("=")[0];
                        final String v = item.split("=")[1];
                        if (requiredFields.contains(k)) {
                            items.put(k, v);
                        }
                    });
        } catch (final IndexOutOfBoundsException ex) {
            throw new AssertionError("Illegal line: \"" + line + "\"");
        }
        if (!isValid(items)) {
            throw new AssertionError("Illegal line: \"" + line + "\"\n" + "CAUSE: Not enough fields");
        }
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(items);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String parseToJson(final String line)
            throws IOException {
        return parseToJson(line, requiredFields);
    }

    @Override
    public Map parseToMap(final String json)
            throws IOException {
        return new ObjectMapper().readValue(json, HashMap.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isValid(final Map<String, String> items) {
        return items.keySet().containsAll(requiredFields);
    }
}
