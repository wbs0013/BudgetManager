package com.example.budgetmanager;

public class BudgetModel {
    public int mId;
    public String date, amount, category;

    public String toSQL() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(mId).append(",");
        sb.append("\"").append(date).append("\"").append(",");
        sb.append("\"").append(amount).append("\"").append(",");
        sb.append("\"").append(category).append("\"").append(",");
        return sb.toString();
    }

}
