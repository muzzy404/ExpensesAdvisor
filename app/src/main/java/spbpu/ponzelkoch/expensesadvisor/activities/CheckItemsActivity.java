package spbpu.ponzelkoch.expensesadvisor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;
import spbpu.ponzelkoch.expensesadvisor.fragments.ChecksFragment;
import spbpu.ponzelkoch.expensesadvisor.R;
import spbpu.ponzelkoch.expensesadvisor.adapters.ItemsListAdapter;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Item;
import spbpu.ponzelkoch.expensesadvisor.helpers.ModelsBuilder;
import spbpu.ponzelkoch.expensesadvisor.helpers.RestClient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class CheckItemsActivity extends AppCompatActivity implements ItemsListAdapter.ItemCategoryCallback {

    private RecyclerView recyclerView;
    private ItemsListAdapter itemsListAdapter;
    private Spinner categorySpinner;
    private TextView checkTitleDate;
    private TextView checkTitlePlace;
    private ProgressBar progressBar;

    private ArrayList<Item> items;
    ArrayList<String> categories = new ArrayList<>();

    private long checkId;
    private boolean categoriesChanged = false;
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
        progressBar = findViewById(R.id.items_progress_bar);

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCategories(success -> {
            if (success)
                loadItems();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(DEBUG_TAG, "onDestroy");
    }

    /**
     * Method to load categories from server.
     * @param callback Callback to make next actions after getting of categories.
     */
    private void loadCategories(CategoriesCallback callback) {
        CheckItemsActivity context = this;
        makeRestGetRequest(RestClient.CATEGORIES_URL, (success, response) -> {
            if (!success) {
                Toast.makeText(context, LOAD_CATEGORIES_FAIL, Toast.LENGTH_SHORT).show();
                // TODO: finish activity
            } else {
                try {
                    categories = ModelsBuilder.getCategoriesFromJSON(response);
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item,
                            categories);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(spinnerAdapter);
                    categorySpinner.setOnItemSelectedListener(new OnCommonCategorySelected());
                } catch (JSONException e) {
                    success = false;
                    Log.d(DEBUG_TAG, "Load categories parsing fail");
                }
            }
            callback.onLoadedCategories(success);
        });
    }

    /**
     * Method to load items from server.
     */
    private void loadItems() {
        CheckItemsActivity context = this;
        makeRestGetRequest(String.format(Locale.getDefault(),
                           RestClient.ITEMS_URL, checkId), (success, response) -> {
            if (!success) {
                Log.d(DEBUG_TAG, "Load items fail");
                Toast.makeText(context, LOAD_ITEMS_FAIL, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    items = ModelsBuilder.buildItemsFromJSON(response);
                    itemsListAdapter = new ItemsListAdapter(items, categories, context);
                    recyclerView.setAdapter(itemsListAdapter);
                } catch (JSONException e) {
                    Log.d(DEBUG_TAG, "Load items parsing fail");
                }
            }
            progressBar.setVisibility(View.INVISIBLE);  // set loading progress bar to invisible
        });
    }

    /**
     * Interface to handle JSON only after getting of response.
     */
    private interface OnJSONResponseCallback {
        void onJSONResponse(boolean success, JSONObject response);
    }

    /**
     * Interface to make actions only when categories loading is finished.
     */
    private interface CategoriesCallback {
        void onLoadedCategories(boolean success);
    }

    private void makeRestGetRequest(String url, OnJSONResponseCallback callback) {
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

    class OnCommonCategorySelected implements AdapterView.OnItemSelectedListener {
        private boolean firstSelection = true;

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (firstSelection) {
                firstSelection = false;
                return;
            }
            categoriesChanged = true;

            final String selectedCategory = categories.get(position);
            for(Item item: items) {
                item.setCategory(selectedCategory);
            }
            itemsListAdapter.itemsChanges(items);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    }

    @Override
    public void onItemCategorySelected(boolean firstSelection, int itemPosition, int categoryPosition) {
        if (firstSelection)
            return;

        categoriesChanged = true;
        Item item = items.get(itemPosition);
        String selectedCategory = categories.get(categoryPosition);
        item.setCategory(selectedCategory);

        Log.d(DEBUG_TAG, "Category: " + items.get(itemPosition).getCategory());
    }

}
