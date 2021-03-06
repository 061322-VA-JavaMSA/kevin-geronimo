package com.revature.model;

import java.util.Objects;

public class Item {

    public enum Stock {
        AVAILABLE, OWNED
    }

    private int id;
    private String itemName;
    private Stock stock;

    public Item() {
        super();
    }

    public Item(int id, String itemName, Stock stock) {
        this.id = id;
        this.itemName = itemName;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && Objects.equals(itemName, item.itemName) && stock == item.stock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemName, stock);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", stock=" + stock +
                '}';
    }
}
