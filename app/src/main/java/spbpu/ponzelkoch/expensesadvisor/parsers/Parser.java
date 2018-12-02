package spbpu.ponzelkoch.expensesadvisor.parsers;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author veronika K. on 26.11.18
 */

public interface Parser {

    // parse line 't=...&s=...&fn=...&i=...&fp=...&n=...' to pretty Json
    JSONObject parseToJson(final String line)
            throws IOException;

    // parse with fields for Json from line 't=...&s=...&fn=...&i=...&fp=...&n=...'
    JSONObject parseToJson(final String line, final List<String> requiredFields)
            throws IOException;

    // parse Json to Map<String,String>
    Map parseToMap(final String json)
            throws IOException;

}
