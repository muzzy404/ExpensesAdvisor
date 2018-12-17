package spbpu.ponzelkoch.expensesadvisor.datamodels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

public class Check implements Comparable<Check> {
    private long id;
    private Date date;
    private String place;
    private double sum;

    private static String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"; // "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private static String DATETIME_TITLE = "EEE d MMM yyyy";

    private SimpleDateFormat toDateFormatter = new SimpleDateFormat(DATETIME_PATTERN, Locale.US);
    private SimpleDateFormat toTitleFormatter = new SimpleDateFormat("EEE d MMM yyyy");

    public Check(long id, String date, String place, double sum) throws ParseException {
        this.id = id;
        this.date = toDateFormatter.parse(date);  // date sample: "2018-11-28T11:54:22.047Z"
        this.place = place.trim();
        this.sum = sum;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        return toTitleFormatter.format(date);
    }

    public String getPlace() {
        return place;
    }

    public String getSum() {
        return String.format(Locale.US, "%.2f", sum);
    }

    @NonNull
    @Override
    public String toString() {
        final String sep = " | ";
        return getId() + sep + getDateString() + sep + getPlace() + sep + getSum();
    }

    @Override
    public int compareTo(Check check) {
        return check.getDate().compareTo(this.getDate());
    }

}
