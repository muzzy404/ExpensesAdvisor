package spbpu.ponzelkoch.expensesadvisor;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import spbpu.ponzelkoch.expensesadvisor.adapters.ChecksListAdapter;
import spbpu.ponzelkoch.expensesadvisor.datamodels.Check;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChecksFragment extends Fragment implements ChecksListAdapter.ChecksFragmentCallback {

    public static String CHECK_TITLE = "check title";

    private ArrayList<Check> checks;
    private RecyclerView recyclerView;
    private ChecksListAdapter adapter;

    public ChecksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: change to request for real checks
        try {
            checks = getTestChecksSet();
        } catch (ParseException e) {
            Log.d("ERROR", e.getMessage());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checks, container, false);

        recyclerView = view.findViewById(R.id.checks_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ChecksListAdapter(checks, this));

        return view;
    }

    @Override
    public void onCardClick(int id, String title) {
        // Toast.makeText(getContext(), Integer.toString(id), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), CheckItemsActivity.class);
        intent.putExtra(CHECK_TITLE, title);
        startActivity(intent);
    }

    // TODO: remove later
    private ArrayList<Check> getTestChecksSet() throws ParseException {
        ArrayList<Check> checks = new ArrayList<>();

        checks.add(new Check(111, "2018-11-20T11:14:21", "Place 1", 2345.56));
        checks.add(new Check(111, "2018-12-21T11:14:22", "Place 2", 2.56));
        checks.add(new Check(111, "2018-10-22T11:07:22", "Place 3", 25555.56));
        checks.add(new Check(111, "2018-04-23T11:54:42", "Place 4", 234.00));
        checks.add(new Check(111, "2018-05-24T11:46:52", "Place 5", 45.56));
        checks.add(new Check(111, "2018-13-25T11:34:26", "Place 6", 45.56));
        checks.add(new Check(111, "2018-11-26T11:25:23", "Place 7", 222.546));
        checks.add(new Check(111, "2018-12-27T11:14:42", "Place 8", 235.556));
        checks.add(new Check(111, "2018-13-28T11:24:22", "Place 9", 245.56));
        checks.add(new Check(111, "2018-14-29T11:34:32", "Place 10", 45.56));

        return checks;
    }
}
