
package com.example.diabetestracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomWeightList extends BaseAdapter {
    private Activity context;
    ArrayList<Weight> weightEntries;

    public CustomWeightList(Activity context, ArrayList weightEntries) {
        this.context = context;
        this.weightEntries = weightEntries;
    }

    public static class ViewHolder {
        TextView date;
        TextView time;
        TextView weight;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.weight_row_item, null, true);
            vh.date = (TextView) row.findViewById(R.id.date);
            vh.time = (TextView) row.findViewById(R.id.time);
            vh.weight = (TextView) row.findViewById(R.id.weight);
            row.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (weightEntries.size() > position) {
            vh.date.setText(weightEntries.get(position).getDate());
            vh.time.setText(weightEntries.get(position).getTime());
            vh.weight.setText(weightEntries.get(position).getWeight()+" kg");
            return row;
        } else {
            Toast toast = Toast.makeText(context, "No record available, enter data.", Toast.LENGTH_SHORT);
            toast.show();
            return row;
        }
    }

    @Override
    public int getCount() {
        if (weightEntries.size() <= 0) {
            return 1;
        }
        return weightEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}