package com.juniordesign.digitaldoctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    BottomNavigationView mBottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mBottomNav = findViewById(R.id.navigationView);
        mBottomNav.getMenu().findItem(R.id.navigation_home).setChecked(true);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
    }

    public void onBackButtonPressed(View view) {
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
    }
}
