package com.example.myneeds.model;

public class Item {
    private int id;
    private String itemName;
    private String itemQuantity;
    private float itemPrice;
    private String itemSize;
    private String dateAdded;

    public Item() {
    }

    public Item(String itemName, String itemQuantity, float itemPrice, String itemSize, String dateAdded) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemSize = itemSize;
        this.dateAdded = dateAdded;
    }

    public Item(int id, String itemName, String itemQuantity, float itemPrice, String itemSize, String dateAdded) {
        this.id = id;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemSize = itemSize;
        this.dateAdded = dateAdded;
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

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
