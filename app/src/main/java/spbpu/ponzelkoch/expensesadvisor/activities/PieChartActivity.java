package spbpu.ponzelkoch.expensesadvisor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import spbpu.ponzelkoch.expensesadvisor.R;

public class PieChartActivity extends AppCompatActivity {

    private static final String CATEGORY_FIELD = "category";
    private static final String SUM_FIELD = "sum";
    private static final String DEBUG_TAG = "DebugPieChart";
    private static final String EXPENSES_TITLE = "Расходы";

    private PieChart pieChart;

    private static final int CHART_ANIMATION_DURATION = 1_000;
    private static final float CHART_HOLE_RADIUS = 35f;
    private static final float CHART_TRANSPARENT_RADIUS = 40f;

    private static final float CHART_LEGEND_TEXT_SIZE = 12f;
    private static final float CHART_VALUE_TEXT_SIZE = 20f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        pieChart = findViewById(R.id.pie_chart);

        setChartProperties();
        drawChart();
    }

    private void setChartProperties() {
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText(EXPENSES_TITLE);
        pieChart.setCenterTextColor(ContextCompat.getColor(this, R.color.color_primary_dark));
        pieChart.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        pieChart.animateY(CHART_ANIMATION_DURATION, Easing.EaseInOutCubic);
        pieChart.setHoleRadius(CHART_HOLE_RADIUS);
        pieChart.setTransparentCircleRadius(CHART_TRANSPARENT_RADIUS);

        pieChart.setDrawEntryLabels(false);

        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setTextSize(CHART_LEGEND_TEXT_SIZE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setYEntrySpace(5f);

        pieChart.getDescription().setEnabled(false);
    }

    private void drawChart() {
        // TODO: loading from real server (use callback)
        ArrayList<PieEntry> chartData;

        try {
            JSONArray chartDataJSONArray = loadTestData();

            chartData = new ArrayList<>();
            for(int i = 0; i < chartDataJSONArray.length(); ++i) {
                JSONObject json = (JSONObject) chartDataJSONArray.get(i);
                String category = json.getString(CATEGORY_FIELD);
                float sum = (float) json.getDouble(SUM_FIELD);

                chartData.add(new PieEntry(sum, category));
            }

        } catch (JSONException e) {
            Log.d(DEBUG_TAG, "data loading failed");
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            return;
        }

        PieDataSet pieDataSet = new PieDataSet(chartData, "");
        pieDataSet.setColors(loadColors());

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(CHART_VALUE_TEXT_SIZE);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private ArrayList<Integer> loadColors() {
        ArrayList<Integer> colorResources = new ArrayList<>();
        colorResources.add(R.color.color_pie_chart_1);
        colorResources.add(R.color.color_pie_chart_2);
        colorResources.add(R.color.color_pie_chart_3);
        colorResources.add(R.color.color_pie_chart_4);
        colorResources.add(R.color.color_pie_chart_5);
        colorResources.add(R.color.color_pie_chart_6);

        ArrayList<Integer> colors = new ArrayList<>();
        for(Integer color: colorResources) {
            colors.add(ContextCompat.getColor(getBaseContext(), color));
        }
        return colors;
    }

    private static JSONArray loadTestData() throws JSONException {
        JSONArray data = new JSONArray();

        for(int i = 0; i < 5; ++i) {
            JSONObject json = new JSONObject();
            json.put(CATEGORY_FIELD, String.format("Category %d", i + 1));
            json.put(SUM_FIELD, 10.0 * (i + 1));
            data.put(json);
        }

        return data;
    }
}
