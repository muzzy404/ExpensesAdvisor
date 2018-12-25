package spbpu.ponzelkoch.expensesadvisor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import spbpu.ponzelkoch.expensesadvisor.R;
import spbpu.ponzelkoch.expensesadvisor.activities.LoginActivity;
import spbpu.ponzelkoch.expensesadvisor.activities.MainActivity;
import spbpu.ponzelkoch.expensesadvisor.activities.PieChartActivity;

import static spbpu.ponzelkoch.expensesadvisor.activities.LoginActivity.USERNAME;


public class StatisticsFragment extends Fragment {

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        final MainActivity activity = (MainActivity) getActivity();

        // set listener to pie chart card to open pie chart activity by click
        root.findViewById(R.id.statistics_pie_chart_card).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PieChartActivity.class);
            intent.putExtra(LoginActivity.USERNAME, activity.getUsername());
            intent.putExtra(LoginActivity.PASSWORD, activity.getPassword());
            startActivity(intent);
        });

        // set listener to line chart card to open pie line activity by click
        root.findViewById(R.id.statistics_line_chart_card).setOnClickListener(v -> {
//            TODO: open line chart
//            Intent intent = new Intent(getContext(), LineChartActivity.class);
//            startActivity(intent);
        });

        return root;
    }

}
