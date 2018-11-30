package spbpu.ponzelkoch.expensesadvisor.datamodels;

import java.util.Locale;

public class Item {
    private int id;
    private String name;
    private double sum;
    private double quantity;
    private String category;

    public Item(int id, String name, double sum, double quantity, String category) {
        this.id = id;
        this.name = name;
        this.sum = sum;
        this.quantity = quantity;
        this.category = category;
    }

    public int getId() {
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
}
