package com.example.budgetmanager;

public class Exchange {

    // fields
    private String date, amount, category;
    // constructors
    public Exchange() {}
    public Exchange(String date, String amount, String category) {
        this.date = date;
        this.amount = amount;
        this.category = category;
    }
    // properties
    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return this.date;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getAmount() {
        return this.amount;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return this.category;
    }
}

