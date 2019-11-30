package com.example.a1dproject_campustradingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomBar = findViewById(R.id.bottom_nav);
        bottomBar.setOnNavigationItemSelectedListener(barListener);

        // make news feed page as default page
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_section, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener barListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    // navigate through the bottom navigation
                    int id = menuItem.getItemId();
                    if (id == R.id.nav_home) {
                        selectedFragment = new HomeFragment();
                    } else if (id == R.id.nav_account) {
                        selectedFragment = new AccountFragment();
                    }

                    // selectedFragment = new HomeFragment();
                    // replace the empty fragment with the main news feed page
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_section, selectedFragment).commit();
                    return true;
                }
            };
}
