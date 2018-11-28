package spbpu.ponzelkoch.expensesadvisor.datamodels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckRoot {
    private int id;
    private Date date;
    private String place;
    private int sum;

    private static String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_PATTERN, Locale.US);

    CheckRoot(int id, String date, String place, int sum) throws ParseException {
        this.id = id;
        this.date = formatter.parse(date);  // date sample: "2018-11-28T11:54:22.047Z"
        this.place = place;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public int getSum() {
        return sum;
    }
}
