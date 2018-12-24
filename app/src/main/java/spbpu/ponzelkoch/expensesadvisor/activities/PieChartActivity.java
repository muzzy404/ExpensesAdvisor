package spbpu.ponzelkoch.expensesadvisor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import cz.msebera.android.httpclient.Header;
import spbpu.ponzelkoch.expensesadvisor.R;
import spbpu.ponzelkoch.expensesadvisor.helpers.RestClient;

public class PieChartActivity extends AppCompatActivity {

    private PieChart pieChart;
    private ProgressBar progressBar;

    private static final String STATISTICS_KEY = "statistics";
    private static final String CATEGORY_FIELD = "category";
    private static final String SUM_FIELD = "sum";

    private static final String DEBUG_TAG = "DebugPieChart";
    private static final String EXPENSES_TITLE = "Расходы";
    private static final String NO_DATA_TITLE = "Нет данных";
    private static final String NO_LOADED_DATA_TITLE = "Подождите, данные загружаются...";
    private static final String DATA_LOAD_FAIL = "При загрузке данных произошла ошибка";
    private static final String DATA_PARSING_FAIL = "Ошибка сервера";

    private static final int CHART_ANIMATION_DURATION = 1_000;
    private static final float CHART_HOLE_RADIUS = 35f;
    private static final float CHART_TRANSPARENT_RADIUS = 40f;
    private static final float CHART_SLICES_GAP = 0f;

    private static final float CHART_LEGEND_TEXT_SIZE = 15f;
    private static final float CHART_VALUE_TEXT_SIZE = 12f;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        pieChart = findViewById(R.id.pie_chart);
        progressBar = findViewById(R.id.pie_chart_progress_bar);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USERNAME);
        password = intent.getStringExtra(LoginActivity.PASSWORD);

        setChartProperties();
        drawChart();
    }

    private void setChartProperties() {
        int textColor = ContextCompat.getColor(this, R.color.color_primary_dark);

        pieChart.setUsePercentValues(true);
        pieChart.animateY(CHART_ANIMATION_DURATION, Easing.EaseInOutCubic);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setNoDataText(NO_LOADED_DATA_TITLE);
        pieChart.setNoDataTextColor(textColor);

        pieChart.setCenterText(EXPENSES_TITLE);
        pieChart.setCenterTextColor(textColor);
        pieChart.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        pieChart.setHoleRadius(CHART_HOLE_RADIUS);
        pieChart.setTransparentCircleRadius(CHART_TRANSPARENT_RADIUS);

        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setTextSize(CHART_LEGEND_TEXT_SIZE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setForm(Legend.LegendForm.CIRCLE);
    }

    private void drawChart() {
        PieChartActivity context = this;

        loadChartData((success, data) -> {
            progressBar.setVisibility(View.INVISIBLE);

            if (!success) {
                Log.d(DEBUG_TAG, DATA_LOAD_FAIL);
                Toast.makeText(context, DATA_LOAD_FAIL, Toast.LENGTH_SHORT).show();
                onDestroy();
                return;
            }

            ArrayList<PieEntry> chartData;
            try {
                chartData = new ArrayList<>();
                for (int i = 0; i < data.length(); ++i) {
                    JSONObject json = (JSONObject) data.get(i);
                    String category = json.getString(CATEGORY_FIELD);
                    float sum = (float) json.getDouble(SUM_FIELD);

                    chartData.add(new PieEntry(sum, category));
                }
            } catch (JSONException e) {
                Log.d(DEBUG_TAG, DATA_PARSING_FAIL);
                Toast.makeText(context, DATA_PARSING_FAIL, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // draw chart
            PieDataSet pieDataSet = new PieDataSet(chartData, "");
            pieDataSet.setColors(loadColors());
            pieDataSet.setSliceSpace(CHART_SLICES_GAP);

            PieData pieData = new PieData(pieDataSet);
            pieData.setValueTextColor(Color.WHITE);
            pieData.setValueTextSize(CHART_VALUE_TEXT_SIZE);
            pieData.setValueFormatter(new PercentFormatter());

            if (chartData.size() == 0)
                pieChart.setCenterText(NO_DATA_TITLE);
            pieChart.setData(pieData);
            pieChart.invalidate();
        });
    }

    private void loadChartData(OnDataLoadCallback callback) {
        RestClient.get(RestClient.STATISTICS_PIE_CHART_URL,
                username, password,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray(STATISTICS_KEY);
                            callback.onJSONResponse(true, data);
                        } catch (JSONException e) {
                            callback.onJSONResponse(false, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        callback.onJSONResponse(false, null);
                    }
                });
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

    private interface OnDataLoadCallback {
        void onJSONResponse(boolean success, JSONArray data);
    }
}
