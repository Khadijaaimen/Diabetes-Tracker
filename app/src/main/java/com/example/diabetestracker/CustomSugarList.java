
package com.example.diabetestracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomSugarList extends BaseAdapter {
    private Activity context;
    ArrayList<Sugar> sugarEntries;

    public CustomSugarList(Activity context,ArrayList sugarEntries)
    {
        this.context=context;
        this.sugarEntries=sugarEntries;
    }

    public static class ViewHolder
    {
        TextView date;
        TextView time;
        TextView measured;
        TextView concent;
        TextView unit;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater=context.getLayoutInflater();
        ViewHolder vh;
        if(convertView==null)
        {
            vh=new ViewHolder();
            row=inflater.inflate(R.layout.sugar_row_item,null,true);
            vh.date=(TextView)row.findViewById(R.id.date);
            vh.time=(TextView)row.findViewById(R.id.time);
            vh.measured=(TextView)row.findViewById(R.id.measured);
            vh.concent=(TextView)row.findViewById(R.id.concent);
            vh.unit=(TextView)row.findViewById(R.id.unit);
            row.setTag(vh);
        }
        else
        {
            vh=(ViewHolder)convertView.getTag();
        }
        if(sugarEntries.size() > position) {
            vh.date.setText(sugarEntries.get(position).getDate());
            vh.time.setText(sugarEntries.get(position).getTime());
            vh.measured.setText("Measured At " + sugarEntries.get(position).getMeasured());
            vh.concent.setText(String.valueOf(sugarEntries.get(position).getConcentration()));
            vh.unit.setText("mmol/L");
            return row;
        }
        else{
            return row;
        }
    }
    @Override
    public int getCount() {
        if(sugarEntries.size()<=0)
        {
            return 1;
        }
        return sugarEntries.size();
    }

    @Override
    public Object getItem(int position) { return position; }

    @Override
    public long getItemId(int position) { return position; }

}