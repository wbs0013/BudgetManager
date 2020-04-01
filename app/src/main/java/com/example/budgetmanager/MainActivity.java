package com.example.budgetmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.*;
import android.os.Bundle;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    MyDBHandler db;

    TableLayout tableLayout;

    EditText date, amount, category, balanceAmount;
    final DecimalFormat df = new DecimalFormat("###.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDBHandler(this);

        date = findViewById(R.id.date);
        amount = findViewById(R.id.amount);
        category = findViewById(R.id.category);
        balanceAmount = findViewById(R.id.balanceAmount);
        Button add = findViewById(R.id.add);
        Button minus = findViewById(R.id.minus);
        tableLayout = findViewById(R.id.tableLayout);
        String temp;

        Cursor cursor = db.getData();

        double myBalance = 0;

        if (cursor.getCount() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "No Data Yet.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            while (cursor.moveToNext()) {
                temp = cursor.getString(2);
                myBalance += Double.parseDouble(temp);

                TableRow row = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                TextView myDate = new TextView(this);
                myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView amt = new TextView(this);
                amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView cat = new TextView(this);
                cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                myDate.setText(cursor.getString(1));
                myDate.setTextColor(Color.parseColor("#FFFFFF"));
                amt.setText(cursor.getString(2));
                amt.setTextColor(Color.parseColor("#FFFFFF"));
                cat.setText(cursor.getString(3));
                cat.setTextColor(Color.parseColor("#FFFFFF"));
                row.addView(myDate);
                row.addView(amt);
                row.addView(cat);
                tableLayout.addView(row);
            }
            balanceAmount.setText(df.format(myBalance));
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((date.getText().length() > 0) && (amount.getText().length() > 0) && (category.getText().length() > 0)) {
                    boolean insert = db.insertData(date.getText().toString(), amount.getText().toString(), category.getText().toString());
                    if (insert) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Added", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    updateBalance();
                    date.setText("");
                    amount.setText("");
                    category.setText("");
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((date.getText().length() > 0) && (amount.getText().length() > 0) && (category.getText().length() > 0)) {
                    boolean insert = db.insertData(date.getText().toString(), "-" + amount.getText().toString(), category.getText().toString());
                    if (insert) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Subtracted", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    updateBalance();
                    date.setText("");
                    amount.setText("");
                    category.setText("");
                }
            }
        });

    }

    public void updateBalance() {
        String temp;

        Cursor cursor = db.getData();

        double myBalance = 0;

        if (cursor.getCount() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "No Data Yet.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            while (cursor.moveToNext()) {
                temp = cursor.getString(2);
                myBalance += Double.parseDouble(temp);


            }
            balanceAmount.setText(df.format(myBalance));
        }
        cursor.moveToLast();
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TextView myDate = new TextView(this);
        myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView amt = new TextView(this);
        amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView cat = new TextView(this);
        cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        myDate.setText(cursor.getString(1));
        myDate.setTextColor(Color.parseColor("#FFFFFF"));
        amt.setText(cursor.getString(2));
        amt.setTextColor(Color.parseColor("#FFFFFF"));
        cat.setText(cursor.getString(3));
        cat.setTextColor(Color.parseColor("#FFFFFF"));
        row.addView(myDate);
        row.addView(amt);
        row.addView(cat);
        tableLayout.addView(row);
    }
}
