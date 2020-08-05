
package com.example.diabetestracker.customList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabetestracker.R;
import com.example.diabetestracker.javaClass.Medicine;

import java.util.ArrayList;

public class CustomMedList extends BaseAdapter {
    private Activity context;
    ArrayList<Medicine> medEntries;

    public CustomMedList(Activity context,ArrayList medEntries)
    {
        this.context=context;
        this.medEntries=medEntries;
    }

    public static class ViewHolder
    {
        TextView date;
        TextView time;
        TextView unit;
        TextView dose;
        TextView med;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater=context.getLayoutInflater();
        ViewHolder vh;
        if(convertView==null)
        {
            vh=new ViewHolder();
            row=inflater.inflate(R.layout.med_row_item,null,true);
            vh.date=(TextView)row.findViewById(R.id.date);
            vh.time=(TextView)row.findViewById(R.id.time);
            vh.unit=(TextView)row.findViewById(R.id.unit);
            vh.med=(TextView)row.findViewById(R.id.med_name);
            vh.dose=(TextView)row.findViewById(R.id.dose);
            row.setTag(vh);
        }
        else
        {
            vh=(ViewHolder)convertView.getTag();
        }

        if(medEntries.size() > position) {
            vh.date.setText(medEntries.get(position).getDate());
            vh.time.setText(medEntries.get(position).getTime());
            vh.med.setText("Medication: " + medEntries.get(position).getMedication());
            vh.dose.setText(String.valueOf(medEntries.get(position).getDosage()));
            vh.unit.setText(medEntries.get(position).getUnit());
            return row;
        }
        else {
            Toast toast = Toast.makeText(context, "No record available, enter data.", Toast.LENGTH_SHORT);
            toast.show();
            return row;
        }
    }
    @Override
    public int getCount() {
        if(medEntries.size()<=0)
        {
            return 1;
        }
        return medEntries.size();
    }

    @Override
    public Object getItem(int position) { return position; }

    @Override
    public long getItemId(int position) { return position; }

}