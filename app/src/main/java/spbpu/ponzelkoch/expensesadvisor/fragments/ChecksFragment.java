package spbpu.ponzelkoch.expensesadvisor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;
import spbpu.ponzelkoch.expensesadvisor.R;
import spbpu.ponzelkoch.expensesadvisor.activities.CheckItemsActivity;
import spbpu.ponzelkoch.expensesadvisor.activities.LoginActivity;
import spbpu.ponzelkoch.expensesadvisor.activities.MainActivity;
import spbpu.ponzelkoch.expensesadvisor.adapters.ChecksListAdapter;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;
import spbpu.ponzelkoch.expensesadvisor.helpers.ModelsBuilder;
import spbpu.ponzelkoch.expensesadvisor.helpers.RestClient;


public class ChecksFragment extends Fragment implements ChecksListAdapter.ChecksFragmentCallback {

    public static String CHECK_TITLE_DATE = "check title date";
    public static String CHECK_TITLE_PLACE = "check title place";
    public static String CHECK_ID = "check title";

    private final String GET_CHECKS_FAIL = "Ошибка при получнии списка чеков с сервреа";
    private final String GET_CHECKS_SUCCESS = "Чеки успешно загружены";
    private final String CHECKS_PARSING_FAIL = "Ошибка при парсинге списка чеков с сервреа (1)";

    private final static int CHECKS_NUMBER = 100;

    private ArrayList<Check> checks = new ArrayList<>();

    private RecyclerView recyclerView;
    private ChecksListAdapter adapter;
    private ProgressBar progressBar;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checks, container, false);

        adapter = new ChecksListAdapter(checks, this);
        recyclerView = view.findViewById(R.id.checks_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        progressBar = view.findViewById(R.id.checks_progress_bar);

        return view;
    }

    @Override
    public void onCardClick(int id, Check check) {
        final MainActivity activity = (MainActivity) getActivity();

        Intent intent = new Intent(getContext(), CheckItemsActivity.class);
        intent.putExtra(CHECK_TITLE_DATE, check.getDateString());
        intent.putExtra(CHECK_TITLE_PLACE, check.getPlace());
        intent.putExtra(CHECK_ID, check.getId());
        intent.putExtra(LoginActivity.USERNAME, activity.getUsername());
        intent.putExtra(LoginActivity.PASSWORD, activity.getPassword());

        startActivity(intent);
    }

    /**
     * Method to load checks from server.
     */
    private void getChecksFromServer() {
        final MainActivity activity = (MainActivity) getActivity();

        RestClient.get(String.format(RestClient.RECENT_CHECKS_URL, CHECKS_NUMBER),
                       activity.getUsername(), activity.getPassword(),
                       new JsonHttpResponseHandler() {

                           @Override
                           public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                               Log.d(DEBUG_TAG, "got checks: " + response.toString());
                               try {
                                   // build checks models from JSON response and set them to adapter
                                   checks = ModelsBuilder.buildChecksFromJSON(response);
                                   adapter.checksChanges(checks);
                                   Log.d(DEBUG_TAG, GET_CHECKS_SUCCESS);
                               } catch (JSONException | ParseException e) {
                                   Log.d(DEBUG_TAG, e.getMessage());
                                   Toast.makeText(activity, CHECKS_PARSING_FAIL, Toast.LENGTH_SHORT).show();
                               }
                               // set progress bar to invisible when loading is finished
                               progressBar.setVisibility(View.INVISIBLE);
                           }

                           @Override
                           public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                               Log.d(DEBUG_TAG, GET_CHECKS_FAIL);
                               Toast.makeText(activity, GET_CHECKS_FAIL, Toast.LENGTH_SHORT).show();
                               progressBar.setVisibility(View.INVISIBLE);
                           }
                       });

    }

}
