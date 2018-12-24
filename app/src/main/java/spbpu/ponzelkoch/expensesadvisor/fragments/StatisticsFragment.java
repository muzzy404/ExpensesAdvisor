package spbpu.ponzelkoch.expensesadvisor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import spbpu.ponzelkoch.expensesadvisor.R;
import spbpu.ponzelkoch.expensesadvisor.activities.PieChartActivity;

import static spbpu.ponzelkoch.expensesadvisor.activities.LoginActivity.USERNAME;


public class StatisticsFragment extends Fragment {

    public static final String TITLE = "title";

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        // set listener to pie chart card to open pie chart activity by click
        root.findViewById(R.id.statistics_pie_chart_card).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PieChartActivity.class);
            startActivity(intent);
        });

        // set listener to line chart card to open pie line activity by click
        root.findViewById(R.id.statistics_line_chart_card).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PieChartActivity.class);
            startActivity(intent);
        });

        return root;
    }

}
