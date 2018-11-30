package spbpu.ponzelkoch.expensesadvisor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import spbpu.ponzelkoch.expensesadvisor.ChecksFragment;
import spbpu.ponzelkoch.expensesadvisor.R;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;


public class ChecksListAdapter extends RecyclerView.Adapter<ChecksListAdapter.ChecksViewHolder> {

    private ArrayList<Check> checks;
    private ChecksFragment fragment;

    public ChecksListAdapter(ArrayList<Check> checks, ChecksFragment fragment) {
        this.checks = checks;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ChecksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_check,
                                                                     parent, false);
        return new ChecksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChecksViewHolder holder, int position) {
        final Check check = checks.get(position);

        holder.date.setText(check.getDateString());
        holder.sum.setText(check.getSum());
        holder.place.setText(check.getPlace());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onCardClick(holder.card.getId(), check.getDateString());
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return checks.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return checks.size();
    }

    static class ChecksViewHolder extends RecyclerView.ViewHolder {

        private CardView card;
        private TextView date;
        private TextView sum;
        private TextView place;

        ChecksViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.check_card);
            date = itemView.findViewById(R.id.check_card_date);
            sum = itemView.findViewById(R.id.check_card_sum);
            place = itemView.findViewById(R.id.check_card_place_id);
        }
    }

    public interface ChecksFragmentCallback {
        void onCardClick(final int id, final String title);
    }
}
