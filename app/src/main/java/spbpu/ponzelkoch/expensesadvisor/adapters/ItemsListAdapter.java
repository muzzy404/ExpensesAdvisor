package spbpu.ponzelkoch.expensesadvisor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import spbpu.ponzelkoch.expensesadvisor.CheckItemsActivity;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Item;
import spbpu.ponzelkoch.expensesadvisor.R;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ItemsViewHolder> {

    private ArrayList<Item> items;
    private ArrayList<String> categories;
    private CheckItemsActivity activity;

    public ItemsListAdapter(ArrayList<Item> items, ArrayList<String> categories, CheckItemsActivity activity) {
        this.items = items;
        this.categories = categories;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,
                                                                     parent, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        Item item = items.get(position);

        holder.name.setText(item.getName());
        holder.sum.setText(item.getSum());
        holder.quantity.setText(item.getQuantity());

        // create and set adapter to spiner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(activity,
                                                                 android.R.layout.simple_spinner_item,
                                                                 categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.categorySpinner.setAdapter(spinnerAdapter);
        holder.categorySpinner.setSelection(categories.indexOf(item.getCategory()) - 1);

        // TODO: listener or something else for selection of items category
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class ItemsViewHolder extends RecyclerView.ViewHolder {

        private CardView card;
        private TextView name;
        private TextView sum;
        private TextView quantity;
        private Spinner categorySpinner;

        ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.item_card);
            name = itemView.findViewById(R.id.item_card_name);
            sum = itemView.findViewById(R.id.item_card_sum);
            quantity = itemView.findViewById(R.id.item_quantity);
            categorySpinner = itemView.findViewById(R.id.item_card_category_spinner);
        }
    }

}
