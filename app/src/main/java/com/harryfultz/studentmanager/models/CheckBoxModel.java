package com.harryfultz.studentmanager.models;


public class CheckBoxModel {
    private String itemName;
    private int value;

    public CheckBoxModel(String itemName, int value) {
        this.itemName = itemName;
        this.value = value;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
