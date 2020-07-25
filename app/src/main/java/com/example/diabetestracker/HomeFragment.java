package com.example.diabetestracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;

public class HomeFragment extends Fragment {

    //button
    Button b1, b2, b3, b4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //sugar log
        b1 = v.findViewById(R.id.sugar);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SugarLog.class);
                startActivity(i);
            }
        });

        //medication log
        b2 = v.findViewById(R.id.med);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MedLog.class);
                startActivity(i);
            }
        });

        //weight log
        b3 = v.findViewById(R.id.weight);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), WeightLog.class);
                startActivity(i);
            }
        });

        //statistics log
        b4 = v.findViewById(R.id.stat);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Statistics.class);
                startActivity(i);
            }
        });

        return v;
    }
}
