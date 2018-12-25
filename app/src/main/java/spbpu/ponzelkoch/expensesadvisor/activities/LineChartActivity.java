package spbpu.ponzelkoch.expensesadvisor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import spbpu.ponzelkoch.expensesadvisor.R;

public class LineChartActivity extends AppCompatActivity {

    private LineChart lineChart;
    private ProgressBar progressBar;

    private static final String DAY_FIELD = "day";
    private static final String SUM_FIELD = "sum";

    private String username;
    private String password;

    private static final String NO_LOADED_DATA_TITLE = "Подождите, данные загружаются...";
    private static final String NO_DATA_TITLE = "Нет данных";

    private static final int CHART_ANIMATION_DURATION = 1_000;

    private static String DATETIME_PATTERN_1 = "yyyy-MM-dd";
    private static String DATETIME_PATTERN_2 = "d.MM (EEE)";
    private SimpleDateFormat stringToDate = new SimpleDateFormat(DATETIME_PATTERN_1, Locale.US);
    private SimpleDateFormat dateToString = new SimpleDateFormat(DATETIME_PATTERN_2, Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        lineChart = findViewById(R.id.line_chart);
        progressBar = findViewById(R.id.line_chart_progress_bar);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USERNAME);
        password = intent.getStringExtra(LoginActivity.PASSWORD);

        setChartProperties();
        drawChart();
    }

    private void setChartProperties() {
        int textColor = ContextCompat.getColor(this, R.color.color_primary_dark);

        lineChart.animateY(CHART_ANIMATION_DURATION, Easing.EaseInOutCubic);
        lineChart.setNoDataText(NO_LOADED_DATA_TITLE);
        lineChart.setNoDataTextColor(textColor);
        lineChart.getDescription().setEnabled(false);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yLeftAxis = lineChart.getAxisLeft();
        yLeftAxis.setLabelCount(10);

        YAxis yRightAxis = lineChart.getAxisRight();
        yRightAxis.setEnabled(false);
    }

    private void drawChart() {
        int fillColor = ContextCompat.getColor(this, R.color.color_pie_chart_3);
        int highLightColor = ContextCompat.getColor(this, R.color.color_pie_chart_5);

        JSONArray data = new JSONArray();

        try {
            String dateTemplate = "2018-12-%d";
            for (int i = 15; i < 27; ++i) {
                String date = String.format(dateTemplate, i);
                double sum = Math.random() * 5000;
                JSONObject json = new JSONObject();
                json.put(DAY_FIELD, date);
                json.put(SUM_FIELD, sum);
                data.put(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Entry> chartData = new ArrayList<>();
        ArrayList<Date> days = new ArrayList<>();
        try {
            for (int i = 0; i < data.length(); ++i) {
                JSONObject json = (JSONObject) data.get(i);
                float x = (float) i;
                float y = (float) json.getDouble(SUM_FIELD);
                chartData.add(new Entry(x, y));

                String day = json.getString(DAY_FIELD);
                days.add(stringToDate.parse(day));
            }

            // set date strings
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter((value, axis) -> {
                int integerValue = (int) value;
                Date dayDate = days.get(integerValue);
                return dateToString.format(dayDate);
            });
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        LineDataSet lineDataSet = new LineDataSet(chartData, "");
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(fillColor);
        lineDataSet.setColor(fillColor);
        lineDataSet.setHighLightColor(highLightColor);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}
