package spbpu.ponzelkoch.expensesadvisor.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import spbpu.ponzelkoch.expensesadvisor.R;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;


public class ChecksListAdapter extends RecyclerView.Adapter<ChecksListAdapter.ChecksViewHolder> {

    private ArrayList<Check> checks;

    ChecksListAdapter(ArrayList<Check> checks) {
        this.checks = checks;
    }

    @NonNull
    @Override
    public ChecksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChecksViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ChecksViewHolder extends RecyclerView.ViewHolder {

        private CalendarView card;
        private TextView date;
        private TextView sum;
        private TextView place;

        public ChecksViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.check_card_date_id);
            sum = itemView.findViewById(R.id.check_card_sum_id);
            place = itemView.findViewById(R.id.check_card_place_id);

            card = itemView.findViewById(R.id.check_card);
        }
    }

}
