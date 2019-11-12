package com.example.a1dproject_campustradingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Test for firebase connection.
        // To be deleted later.
        Toast.makeText(MainActivity.this, "Firebase connection success", Toast.LENGTH_LONG).show();
    }
}
