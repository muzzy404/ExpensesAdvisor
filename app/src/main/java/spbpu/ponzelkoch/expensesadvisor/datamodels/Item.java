package spbpu.ponzelkoch.expensesadvisor.datamodels;

import java.util.Locale;

import androidx.annotation.NonNull;


public class Item {
    private long id;
    private String name;
    private double sum;
    private double quantity;
    private String category;

    public Item(long id, String name, double sum, double quantity, String category) {
        this.id = id;
        this.name = name;
        this.sum = sum;
        this.quantity = quantity;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSum() {
        return String.format(Locale.US, "%.2f", sum);
    }

    public String getQuantity()
    {
        int fraction = (int)(100 * quantity) % 100;
        if (fraction == 0)
            return Integer.toString((int) quantity);
        else
            return String.format(Locale.US, "%.2f", quantity);
    }

    public String getCategory() {
        return category;
    }

    @NonNull
    @Override
    public String toString() {
        final String sep = " | ";
        return getId() + sep + getName() + sep + getSum() + sep + getQuantity() + sep + getCategory();
    }
}
