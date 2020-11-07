package com.example.rukka.atm;

import org.json.JSONException;
import org.json.JSONObject;

public class TransEntity {
    private String account;
    private String date;
    private int amount;
    private int type;

    public TransEntity(JSONObject object) {
        try {
            account = object.getString("account");
            date = object.getString("date");
            amount = object.getInt("amount");
            type = object.getInt("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
