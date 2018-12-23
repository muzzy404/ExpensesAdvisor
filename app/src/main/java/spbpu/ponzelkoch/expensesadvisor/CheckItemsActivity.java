package spbpu.ponzelkoch.expensesadvisor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import spbpu.ponzelkoch.expensesadvisor.adapters.ItemsListAdapter;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Item;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CheckItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner categorySpinner;
    private TextView checkTitleDate;
    private TextView checkTitlePlace;

    private ArrayList<Item> items;
    private ArrayList<String> categories;

    private long checkId;
    private static long NO_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_items);

        categorySpinner = findViewById(R.id.common_category_spinner);
        checkTitleDate = findViewById(R.id.items_list_check_title_date);
        checkTitlePlace = findViewById(R.id.items_list_check_title_place);

        // TODO: set sum or place to title???
        Intent intent = getIntent();
        checkTitleDate.setText(intent.getStringExtra(ChecksFragment.CHECK_TITLE_DATE));
        checkTitlePlace.setText(intent.getStringExtra(ChecksFragment.CHECK_TITLE_PLACE));

        categories = loadTestCategories();
        items = loadTestItems();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                                                                 android.R.layout.simple_spinner_item,
                                                                 categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        recyclerView = findViewById(R.id.items_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ItemsListAdapter(items, categories, this));

        checkId = intent.getLongExtra(ChecksFragment.CHECK_ID, NO_ID);
        if (checkId == NO_ID) {
            Toast.makeText(this, "Invalid check id", Toast.LENGTH_SHORT).show();
            // TODO: return to checks fragment
        }
        Toast.makeText(this, "ID чека " + String.valueOf(checkId), Toast.LENGTH_SHORT).show();
    }

    // TODO: remove or replace with loading of categories from server/local-db
    private ArrayList<String> loadTestCategories() {
        ArrayList<String> list = new ArrayList<>();

        list.add("Продукты");
        list.add("Пром. товары");
        list.add("Одежда");
        list.add("Питание");
        list.add("Другое");

        return list;
    }

    // TODO: remove or replace with loading of categories from server/local-db
    private ArrayList<Item> loadTestItems() {
        ArrayList<Item> list = new ArrayList<>();

        list.add(new Item(1, "ОГО Завтр.МЮС.зап.сух.яблок350г", 81.9, 1.0, categories.get(1)));
        list.add(new Item(2, "MILKA Шок.BUBBLES кокос.нач.97г", 49.99, 1.0, categories.get(1)));
        list.add(new Item(3, "OR.Резин.BUB.БЕЛОСНЕЖ.жев.13,6г", 24.99, 2.0, categories.get(1)));
        list.add(new Item(4, "Цыпленок - Бройлер 1 кат охл, 1 кг", 199.39, 1.734, categories.get(1)));
        list.add(new Item(5, "Зубная счетка splat", 70.00, 1.0, categories.get(2)));
        list.add(new Item(6, "Свитер красный ostin", 2599.00, 1.0, categories.get(3)));

        return list;
    }
}
