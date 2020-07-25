package com.example.diabetestracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ProfileFragment extends Fragment {

    TextView tvname,tvemail,tvdob,tvgender,tvpassword;
    String name,email,DOB,gender,password;
    Button edit;
    public ProfileFragment()
    {}

    public ProfileFragment(String name1,String email1,String DOB1,String gender1,String password1)
    {
        this.name=name1;
        this.email=email1;
        this.DOB=DOB1;
        this.gender=gender1;
        this.password=password1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvname = (TextView) v.findViewById(R.id.name);
        tvname.setText(name);
        tvemail=(TextView)v.findViewById(R.id.email);
        tvemail.setText(email);
        tvdob=(TextView)v.findViewById(R.id.dob);
        tvdob.setText(DOB);
        tvgender=(TextView)v.findViewById(R.id.gender);
        tvgender.setText(gender);
        tvpassword=(TextView)v.findViewById(R.id.password);
        tvpassword.setText(password);

        edit = v.findViewById(R.id.b1);
        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfile.class);
                i.putExtra("name",name);
                i.putExtra("dob",DOB);
                i.putExtra("gender",gender);
                i.putExtra("password",password);
                startActivity(i);
            }
        });


        return v;
    }


}

