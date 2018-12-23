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

//        loadTestCategories();
//        items = loadTestItems();
//
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
//                                                                 android.R.layout.simple_spinner_item,
//                                                                 categories);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        categorySpinner.setAdapter(spinnerAdapter);

        recyclerView = findViewById(R.id.items_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new ItemsListAdapter(items, categories, this));

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
        Log.d(DEBUG_TAG, "onStart");
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

        while (categories.size() == 0) { }

        list.add(new Item(1, "ОГО Завтр.МЮС.зап.сух.яблок350г", 81.9, 1.0, categories.get(1)));
        list.add(new Item(2, "MILKA Шок.BUBBLES кокос.нач.97г", 49.99, 1.0, categories.get(1)));
        list.add(new Item(3, "OR.Резин.BUB.БЕЛОСНЕЖ.жев.13,6г", 24.99, 2.0, categories.get(1)));
        list.add(new Item(4, "Цыпленок - Бройлер 1 кат охл, 1 кг", 199.39, 1.734, categories.get(1)));
        list.add(new Item(5, "Зубная счетка splat", 70.00, 1.0, categories.get(1)));
        list.add(new Item(6, "Свитер красный ostin", 2599.00, 1.0, categories.get(1)));

        return list;
    }
}
