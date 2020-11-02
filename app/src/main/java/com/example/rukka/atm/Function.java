package com.example.rukka.atm;

public class Function {
    private int itemImageResource;
    private String itemName;

    public Function(int itemImageResource, String itemName) {
        this.itemImageResource = itemImageResource;
        this.itemName = itemName;
    }

    public int getItemImageResource() {
        return itemImageResource;
    }

    public void setItemImageResource(int itemImageResource) {
        this.itemImageResource = itemImageResource;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
