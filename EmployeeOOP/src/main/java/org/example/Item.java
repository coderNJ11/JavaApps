package org.example;

import java.math.BigDecimal;

public class Item {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Item(String name, int qty, BigDecimal price) {
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    private int qty;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private BigDecimal price;

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
