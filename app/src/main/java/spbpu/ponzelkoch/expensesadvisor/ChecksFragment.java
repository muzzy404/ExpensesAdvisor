package spbpu.ponzelkoch.expensesadvisor;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;
import spbpu.ponzelkoch.expensesadvisor.adapters.ChecksListAdapter;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;
import spbpu.ponzelkoch.expensesadvisor.helpers.ModelsBuilder;
import spbpu.ponzelkoch.expensesadvisor.helpers.RestClient;


public class ChecksFragment extends Fragment implements ChecksListAdapter.ChecksFragmentCallback {

    public static String CHECK_TITLE = "check title";

    private final String SUCCESS = "Успешно";
    private final String FAIL = "Ошибка";

    private final String GET_CHECKS_FAIL = "Ошибка при получнии списка чеков с сервреа";
    private final String GET_CHECKS_SUCCESS = "Чеки успешно загружены";
    private final String CHECKS_PARSING_FAIL = "Ошибка при парсинге списка чеков с сервреа (1)";

    private ArrayList<Check> checks = new ArrayList<>();

    private RecyclerView recyclerView;
    private ChecksListAdapter adapter;

    private static final String DEBUG_TAG = "DebugChecks";

    public ChecksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getChecksFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checks, container, false);

        adapter = new ChecksListAdapter(checks, this);
        recyclerView = view.findViewById(R.id.checks_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCardClick(int id, String title) {
        // Toast.makeText(getContext(), Integer.toString(id), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), CheckItemsActivity.class);
        intent.putExtra(CHECK_TITLE, title);
        startActivity(intent);
    }

    // TODO: remove later
    private ArrayList<Check> getTestChecksSet() throws ParseException {
        ArrayList<Check> checks = new ArrayList<>();

        checks.add(new Check(111, "2018-11-20T11:14:21", "Place 1", 2345.56));
        checks.add(new Check(111, "2018-12-21T11:14:22", "Place 2", 2.56));
        checks.add(new Check(111, "2018-10-22T11:07:22", "Place 3", 25555.56));
        checks.add(new Check(111, "2018-04-23T11:54:42", "Place 4", 234.00));
        checks.add(new Check(111, "2018-05-24T11:46:52", "Place 5", 45.56));
        checks.add(new Check(111, "2018-13-25T11:34:26", "Place 6", 45.56));
        checks.add(new Check(111, "2018-11-26T11:25:23", "Place 7", 222.546));
        checks.add(new Check(111, "2018-12-27T11:14:42", "Place 8", 235.556));
        checks.add(new Check(111, "2018-13-28T11:24:22", "Place 9", 245.56));
        checks.add(new Check(111, "2018-14-29T11:34:32", "Place 10", 45.56));

        return checks;
    }

    private void getChecksFromServer() {
        final MainActivity activity = (MainActivity) getActivity();
        Log.d(DEBUG_TAG, "getChecksFromServer");

        RestClient.get(RestClient.RECENT_CHECKS_URL,
                       activity.getUsername(), activity.getPassword(),
                       new JsonHttpResponseHandler() {

                           @Override
                           public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                               Log.d(DEBUG_TAG, "got checks JSONObject");
                               Log.d(DEBUG_TAG, response.toString());
                               try {
                                   checks = ModelsBuilder.buildChecksFromJSON(response);
                                   adapter.newChecks(checks);
                                   Log.d(DEBUG_TAG, GET_CHECKS_SUCCESS);
                                   Toast.makeText(activity, GET_CHECKS_SUCCESS, Toast.LENGTH_SHORT).show();
                               } catch (JSONException | ParseException e) {
                                   Log.d(DEBUG_TAG, e.getMessage());
                                   Toast.makeText(activity, CHECKS_PARSING_FAIL, Toast.LENGTH_SHORT).show();
                               }
                               Log.d(DEBUG_TAG, "parsing finished");
                           }

                           @Override
                           public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                               Log.d(DEBUG_TAG, GET_CHECKS_FAIL);
                               Toast.makeText(activity, GET_CHECKS_FAIL, Toast.LENGTH_SHORT).show();
                           }
                       });
    }
}
