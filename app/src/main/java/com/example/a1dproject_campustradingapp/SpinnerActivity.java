package com.example.a1dproject_campustradingapp;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DatabaseReference;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference databaseReference;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
