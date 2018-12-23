package spbpu.ponzelkoch.expensesadvisor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;
import spbpu.ponzelkoch.expensesadvisor.adapters.ItemsListAdapter;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Item;
import spbpu.ponzelkoch.expensesadvisor.helpers.ModelsBuilder;
import spbpu.ponzelkoch.expensesadvisor.helpers.RestClient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class CheckItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner categorySpinner;
    private TextView checkTitleDate;
    private TextView checkTitlePlace;

    private ArrayList<Item> items;
    ArrayList<String> categories = new ArrayList<>();

    private long checkId;
    private static long NO_ID = -1;

    private String username;
    private String password;

    private static final String DEBUG_TAG = "DebugItems";
    private static final String LOAD_CATEGORIES_FAIL = "При загрузке категорий товаров произошла ошибка";
    private static final String LOAD_ITEMS_FAIL = "При загрузке позиций чека произошла ошибка";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_items);

        categorySpinner = findViewById(R.id.common_category_spinner);
        checkTitleDate = findViewById(R.id.items_list_check_title_date);
        checkTitlePlace = findViewById(R.id.items_list_check_title_place);

        Intent intent = getIntent();
        checkTitleDate.setText(intent.getStringExtra(ChecksFragment.CHECK_TITLE_DATE));
        checkTitlePlace.setText(intent.getStringExtra(ChecksFragment.CHECK_TITLE_PLACE));
        username = intent.getStringExtra(LoginActivity.USERNAME);
        password = intent.getStringExtra(LoginActivity.PASSWORD);

        recyclerView = findViewById(R.id.items_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkId = intent.getLongExtra(ChecksFragment.CHECK_ID, NO_ID);
        if (checkId == NO_ID) {
            Toast.makeText(this, "Invalid check id", Toast.LENGTH_SHORT).show();
            // TODO: return to checks fragment
        }
        Toast.makeText(this, "ID чека " + String.valueOf(checkId), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCategories();
        loadItems();
    }

    private void loadCategories() {
        Log.d(DEBUG_TAG, "loadCategories started");
        CheckItemsActivity context = this;

        getJSONObj(RestClient.CATEGORIES_URL, (success, response) -> {
            if (!success) {
                Log.d(DEBUG_TAG, "Load categories fail");
                Toast.makeText(context, LOAD_CATEGORIES_FAIL, Toast.LENGTH_SHORT).show();
                // TODO: finish activity
                return;
            }
            try {
                categories = ModelsBuilder.getCategoriesFromJSON(response);
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_spinner_item,
                        categories);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(spinnerAdapter);
            } catch (JSONException e) {
                Log.d(DEBUG_TAG, "Load categories parsing fail");
            }
        });

        Log.d(DEBUG_TAG, "loadCategories finished");
    }

    private void loadItems() {
        Log.d(DEBUG_TAG, "loadItems started");
        Log.d(DEBUG_TAG, String.format("loadItems categories size = %d", categories.size()));
        for(String category: categories) {
            Log.d(DEBUG_TAG, "loadItems: category = " + category);
        }

        CheckItemsActivity context = this;
        getJSONObj(String.format(Locale.getDefault(),
                                 RestClient.ITEMS_URL, checkId), (success, response) -> {
            if (!success) {
                Log.d(DEBUG_TAG, "Load items fail");
                Toast.makeText(context, LOAD_ITEMS_FAIL, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                items = ModelsBuilder.buildItemsFromJSON(response);
                recyclerView.setAdapter(new ItemsListAdapter(items, categories, context));
            } catch (JSONException e) {
                Log.d(DEBUG_TAG, "Load items parsing fail");
            }
        });

        Log.d(DEBUG_TAG, "loadItems finished");
    }

    // TODO: remove or replace with loading of categories from server/local-db
    private void loadTestCategories() {
        categories.add("Продукты");
        categories.add("Продукты 2");
        categories.add("Продукты 3");
    }

    // TODO: remove or replace with loading of categories from server/local-db
    private ArrayList<Item> loadTestItems() {
        ArrayList<Item> list = new ArrayList<>();

        list.add(new Item(1, "ОГО Завтр.МЮС.зап.сух.яблок350г", 81.9, 1.0, categories.get(1)));
        list.add(new Item(2, "MILKA Шок.BUBBLES кокос.нач.97г", 49.99, 1.0, categories.get(1)));
        list.add(new Item(3, "OR.Резин.BUB.БЕЛОСНЕЖ.жев.13,6г", 24.99, 2.0, categories.get(1)));
        list.add(new Item(4, "Цыпленок - Бройлер 1 кат охл, 1 кг", 199.39, 1.734, categories.get(1)));
        list.add(new Item(5, "Зубная счетка splat", 70.00, 1.0, categories.get(1)));
        list.add(new Item(6, "Свитер красный ostin", 2599.00, 1.0, categories.get(1)));

        return list;
    }

    private interface OnJSONResponseCallback {
        public void onJSONResponse(boolean success, JSONObject response);
    }

    private void getJSONObj(String url, OnJSONResponseCallback callback) {
        CheckItemsActivity context = this;

        RestClient.get(url, context.username, context.password, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                callback.onJSONResponse(true, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                callback.onJSONResponse(false, errorResponse);
            }
        });
    }
}
