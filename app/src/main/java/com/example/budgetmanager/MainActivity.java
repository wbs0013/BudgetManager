package com.example.budgetmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.*;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    MyDBHandler db;

    TableLayout tableLayout;

    EditText date, amount, category, balanceAmount, minDate, maxDate, minPrice, maxPrice;

    CheckBox onlyAdded, onlySubtracted;

    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");

    final DecimalFormat df = new DecimalFormat("###.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDBHandler(this);
        minDate = findViewById(R.id.minDate);
        maxDate = findViewById(R.id.maxDate);
        minPrice = findViewById(R.id.minPrice);
        maxPrice = findViewById(R.id.maxPrice);

        date = findViewById(R.id.date);
        amount = findViewById(R.id.amount);
        category = findViewById(R.id.category);
        balanceAmount = findViewById(R.id.balanceAmount);
        Button add = findViewById(R.id.add);
        Button minus = findViewById(R.id.minus);
        tableLayout = findViewById(R.id.tableLayout);
        String temp;

        Button maxDateBtn = findViewById(R.id.maxDateBtn);
        Button minDateBtn = findViewById(R.id.minDateBtn);
        Button minAmtBtn = findViewById(R.id.minAmtBtn);
        Button maxAmtBtn = findViewById(R.id.maxAmtBtn);
        Button dateRangeBtn = findViewById(R.id.dateRangeBtn);
        Button amountRangeBtn = findViewById(R.id.amountRangeBtn);
        Button overallBtn = findViewById(R.id.overallBtn);
        Button resetBtn = findViewById(R.id.resetBtn);
        onlyAdded = findViewById(R.id.onlyAdded);
        onlySubtracted = findViewById(R.id.onlySubtracted);
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

        maxDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((maxDate.getText().length() == 0)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Empty Field", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    while (tableLayout.getChildCount() > 1) {
                        tableLayout.removeView(tableLayout.getChildAt(tableLayout.getChildCount() - 1));
                    }
                    try {
                        applyMaxDate();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                maxDate.setText("");
            }
        });

        minDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((minDate.getText().length() == 0)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Empty Field", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    while (tableLayout.getChildCount() > 1) {
                        tableLayout.removeView(tableLayout.getChildAt(tableLayout.getChildCount() - 1));
                    }
                    try {
                        applyMinDate();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                minDate.setText("");
            }
        });

        minAmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((minPrice.getText().length() == 0)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Empty Field", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    while (tableLayout.getChildCount() > 1) {
                        tableLayout.removeView(tableLayout.getChildAt(tableLayout.getChildCount() - 1));
                    }
                    applyMinAmt();
                }
                minPrice.setText("");
            }
        });
        maxAmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((maxPrice.getText().length() == 0)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Empty Field", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    while (tableLayout.getChildCount() > 1) {
                        tableLayout.removeView(tableLayout.getChildAt(tableLayout.getChildCount() - 1));
                    }
                    applyMaxAmt();
                }
                maxPrice.setText("");
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (tableLayout.getChildCount() > 1) {
                    tableLayout.removeView(tableLayout.getChildAt(tableLayout.getChildCount() - 1));
                }
                reset();
                minDate.setText("");
                minPrice.setText("");
                maxDate.setText("");
                maxPrice.setText("");
            }
        });

        dateRangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((maxDate.getText().length() == 0) || minDate.getText().length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Empty Field", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    while (tableLayout.getChildCount() > 1) {
                        tableLayout.removeView(tableLayout.getChildAt(tableLayout.getChildCount() - 1));
                    }
                    try {
                        applyDateRange();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                maxDate.setText("");
                minDate.setText("");
            }
        });
        amountRangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((maxPrice.getText().length() == 0) || minPrice.getText().length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Empty Field", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    while (tableLayout.getChildCount() > 1) {
                        tableLayout.removeView(tableLayout.getChildAt(tableLayout.getChildCount() - 1));
                    }
                    applyAmountRange();

                }
                minPrice.setText("");
                maxPrice.setText("");
            }
        });

        overallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((maxPrice.getText().length() == 0) && minPrice.getText().length() == 0
                    && minDate.getText().length() == 0 && maxDate.getText().length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Empty Field(s)", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    while (tableLayout.getChildCount() > 1) {
                        tableLayout.removeView(tableLayout.getChildAt(tableLayout.getChildCount() - 1));
                    }
                    try {
                        applyOverallRange();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                minPrice.setText("");
                maxPrice.setText("");
                minDate.setText("");
                maxDate.setText("");
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

    public void applyMaxDate() throws ParseException {
        Date maximumDate = formatter.parse(maxDate.getText().toString());

        Cursor newCursor = db.getData();
        newCursor.moveToFirst();
        while (newCursor.moveToNext()) {
            Double actualAmount = Double.parseDouble(newCursor.getString(2));
            Date actualDate = formatter.parse(newCursor.getString(1));
            if (onlyAdded.isChecked()) {
                if ((actualDate.before(maximumDate)) && (actualAmount > 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            if (onlySubtracted.isChecked()) {
                if ((actualDate.before(maximumDate)) && (actualAmount < 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            if (!onlyAdded.isChecked() && !onlySubtracted.isChecked()) {
                if ((actualDate.before(maximumDate))) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
        }
        }

    public void applyMinDate() throws ParseException {
        Date minimumDate = formatter.parse(minDate.getText().toString());

        Cursor newCursor = db.getData();
        newCursor.moveToFirst();
        while (newCursor.moveToNext()) {
            Double actualAmount = Double.parseDouble(newCursor.getString(2));
            Date actualDate = formatter.parse(newCursor.getString(1));
            if (onlyAdded.isChecked()) {
                if ((actualDate.after(minimumDate)) && (actualAmount > 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            if (onlySubtracted.isChecked()) {
                if ((actualDate.after(minimumDate)) && (actualAmount < 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            if (!onlyAdded.isChecked() && !onlySubtracted.isChecked()) {
                if ((actualDate.after(minimumDate))) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
        }
    }

    public void applyMinAmt() {
        Double minimum = Double.parseDouble(minPrice.getText().toString());

        Cursor newCursor = db.getData();
        newCursor.moveToFirst();
        while (newCursor.moveToNext()) {
            Double actualPrice = Double.parseDouble(newCursor.getString(2));
            if (onlyAdded.isChecked()) {
                if ((actualPrice >= minimum) && (actualPrice > 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            else if (onlySubtracted.isChecked()) {
                Double temp = 0.0;
                if (actualPrice < 0) {
                    temp = actualPrice * -1;
                }
                if ((temp >= minimum) && (actualPrice < 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            else if (!onlySubtracted.isChecked() && !onlyAdded.isChecked()) {
                if (actualPrice < 0) {
                    actualPrice = actualPrice * -1;
                }
                if ((actualPrice <= minimum)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
        }
    }

    public void applyMaxAmt() {
        Double maximumPrice = Double.parseDouble(maxPrice.getText().toString());

        Cursor newCursor = db.getData();
        newCursor.moveToFirst();
        while (newCursor.moveToNext()) {
            Double actualPrice = Double.parseDouble(newCursor.getString(2));
            if (onlyAdded.isChecked()) {
                if ((actualPrice <= maximumPrice) && (actualPrice > 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            else if (onlySubtracted.isChecked()) {
                Double temp = 0.0;
                if (actualPrice < 0) {
                    temp = actualPrice * -1;
                }
                if ((temp <= maximumPrice) && (actualPrice < 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            else if (!onlySubtracted.isChecked() && !onlyAdded.isChecked()) {
                if (actualPrice < 0) {
                    actualPrice = actualPrice * -1;
                }
                if ((actualPrice <= maximumPrice)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
        }
    }

    public void reset() {
        Cursor resetCursor = db.getData();
        resetCursor.moveToPosition(0);
        while (resetCursor.moveToNext()) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView myDate = new TextView(this);
            myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView amt = new TextView(this);
            amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView cat = new TextView(this);
            cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            myDate.setText(resetCursor.getString(1));
            myDate.setTextColor(Color.parseColor("#FFFFFF"));
            amt.setText(resetCursor.getString(2));
            amt.setTextColor(Color.parseColor("#FFFFFF"));
            cat.setText(resetCursor.getString(3));
            cat.setTextColor(Color.parseColor("#FFFFFF"));
            row.addView(myDate);
            row.addView(amt);
            row.addView(cat);
            tableLayout.addView(row);
        }
    }

    public void applyDateRange() throws ParseException {
        Date minimumDate = formatter.parse(minDate.getText().toString());
        Date maximumDate = formatter.parse(maxDate.getText().toString());
        Cursor newCursor = db.getData();
        newCursor.moveToFirst();
        while (newCursor.moveToNext()) {
            Double actualAmount = Double.parseDouble(newCursor.getString(2));
            Date actualDate = formatter.parse(newCursor.getString(1));
            if (onlyAdded.isChecked()) {
                if (((actualDate.after(minimumDate)) && (actualDate.before(maximumDate))) && (actualAmount > 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            if (onlySubtracted.isChecked()) {
                if (((actualDate.after(minimumDate)) && (actualDate.before(maximumDate))) && (actualAmount < 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            if (!onlySubtracted.isChecked() && !onlyAdded.isChecked()) {
                if ((actualDate.after(minimumDate)) && (actualDate.before(maximumDate))) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
        }

    }

    public void applyAmountRange() {
        Double minimum = Double.parseDouble(minPrice.getText().toString());
        Double maximum = Double.parseDouble(maxPrice.getText().toString());
        Cursor newCursor = db.getData();
        newCursor.moveToFirst();
        while (newCursor.moveToNext()) {
            Double actualPrice = Double.parseDouble(newCursor.getString(2));
            if (onlyAdded.isChecked()) {
                if (((actualPrice >= minimum) && (actualPrice <= maximum)) && (actualPrice > 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            else if (onlySubtracted.isChecked()) {
                Double temp = 0.0;
                if (actualPrice < 0) {
                    temp = actualPrice * -1;
                }
                if (((temp >= minimum) && (temp <= maximum)) && (actualPrice < 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            else if (!onlySubtracted.isChecked() && !onlyAdded.isChecked()) {
                if (actualPrice < 0) {
                    actualPrice = actualPrice * -1;
                }
                if ((actualPrice >= minimum) && (actualPrice <= maximum)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
        }
    }

    public void applyOverallRange() throws ParseException {
        Double minimum = Double.parseDouble(minPrice.getText().toString());
        Double maximum = Double.parseDouble(maxPrice.getText().toString());
        Date minimumDate = formatter.parse(minDate.getText().toString());
        Date maximumDate = formatter.parse(maxDate.getText().toString());
        Cursor newCursor = db.getData();
        newCursor.moveToFirst();
        while (newCursor.moveToNext()) {
            Date actualDate = formatter.parse(newCursor.getString(1));
            Double actualPrice = Double.parseDouble(newCursor.getString(2));
            if (onlyAdded.isChecked()) {
                if (((actualPrice >= minimum) && (actualPrice <= maximum))
                        && ((actualDate.after(minimumDate)) && actualDate.before(maximumDate)) && (actualPrice > 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            else if (onlySubtracted.isChecked()) {
                Double temp = 0.0;
                if (actualPrice < 0) {
                    temp = actualPrice * -1;
                }
                if (((temp >= minimum) && (temp <= maximum))
                        && ((actualDate.after(minimumDate)) && actualDate.before(maximumDate)) && (actualPrice < 0)) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
            else if (!onlySubtracted.isChecked() && !onlyAdded.isChecked()) {
                if (actualPrice < 0) {
                    actualPrice = actualPrice * -1;
                }
                if (((actualPrice >= minimum) && (actualPrice <= maximum))
                        && ((actualDate.after(minimumDate)) && actualDate.before(maximumDate))) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    TextView myDate = new TextView(this);
                    myDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView amt = new TextView(this);
                    amt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView cat = new TextView(this);
                    cat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    myDate.setText(newCursor.getString(1));
                    myDate.setTextColor(Color.parseColor("#FFFFFF"));
                    amt.setText(newCursor.getString(2));
                    amt.setTextColor(Color.parseColor("#FFFFFF"));
                    cat.setText(newCursor.getString(3));
                    cat.setTextColor(Color.parseColor("#FFFFFF"));
                    row.addView(myDate);
                    row.addView(amt);
                    row.addView(cat);
                    tableLayout.addView(row);
                }
            }
        }
    }

}
