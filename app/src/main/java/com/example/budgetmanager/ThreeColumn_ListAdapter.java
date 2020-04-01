package com.example.budgetmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ThreeColumn_ListAdapter extends ArrayAdapter<Exchange> {

    private LayoutInflater mInflater;
    private ArrayList<Exchange> exchanges;
    private int mViewResourceId;

    public ThreeColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<Exchange> exchanges) {
        super(context, textViewResourceId, exchanges);
        this.exchanges = exchanges;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        Exchange exchange = exchanges.get(position);

        if (exchange != null) {
            TextView date = convertView.findViewById(R.id.textDate);
            TextView amount = convertView.findViewById(R.id.textAmount);
            TextView category = convertView.findViewById(R.id.textCategory);
            if (date != null) {
                date.setText(exchange.getDate());
            }
            if (amount != null) {
                amount.setText((exchange.getAmount()));
            }
            if (category != null) {
                category.setText((exchange.getCategory()));
            }
        }

        return convertView;
    }
}
