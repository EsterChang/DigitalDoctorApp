package com.juniordesign.digitaldoctor;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class HomeActivity extends AppCompatActivity {
    DatabaseHelper db;
    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    // keeps the index of the currently selected item.
    private int mSelectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = new DatabaseHelper(this);

        mBottomNav = findViewById(R.id.navigationView);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        // physically changes the selected item on app load to the home screen
        MenuItem selectedItem;
        if (savedInstanceState != null) {
            // Justin -- I do not believe we use this method since I do not think we keep track of states in a meaningful way
            // Note that if we use states later, we will need to change mBottomNav.getMenu().findItem(R.id.navigation_home).setChecked(true) below;
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            // Justin -- sets the selected icon to the home screen on application load. Otherwise, it would default to book.
            selectedItem = mBottomNav.getMenu().getItem(1);
        }
        // Justin -- runs the select fragment method on either the saved state or the home screen
        selectFragment(selectedItem);

        // since we only ever run the select fragment on the home screen currently, always set it checked on application load
        mBottomNav.getMenu().findItem(R.id.navigation_home).setChecked(true);

        handleFirstStart();
    }

    // Justin -- saved instance state saves the currently selected icon. We do not currently use this
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    // Justin -- if android back button is pressed in the application, send back to home screen fragment
    // if on the home screen, do the default for the android system
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(1);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
            mBottomNav.getMenu().findItem(R.id.navigation_home).setChecked(true);
        } else {
            super.onBackPressed();
        }
    }

    // Justin -- find the item id on the menu and assigns the frag variable to a new instance of the correct fragment
    private void selectFragment(MenuItem item) {
        Fragment frag = null;

        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.navigation_home:
                frag = HomeFragment.newInstance();
                break;
            case R.id.navigation_search:
                frag = SearchFragment.newInstance();
                break;
            case R.id.navigation_book:
                frag = BookFragment.newInstance();
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

        // Justin -- if the fragment is not null, then add the fragment to the fragment container xml block
        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, frag);
            ft.commit();
        }
    }

    private void handleFirstStart() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showTermsDialog();

            //this just prevents running double insertions on the edge case the user closes out of
            //the app without agreeing to both the Terms and the Liability Warning
            db.deleteAllData();

            Resources res = getResources();

            //Kyle -- this reads in the resource file to allow loading into the database
            InputStream in_s = res.openRawResource(R.raw.body_part_table_information);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in_s));

            //currently loads all data from our body part table
            db.loadAllData(reader, DatabaseHelper.BODY_PART_TABLE);

            in_s = res.openRawResource(R.raw.generalized_symptom_table_information);
            reader = new BufferedReader(new InputStreamReader(in_s));

            db.loadAllData(reader, DatabaseHelper.GENERALIZED_SYMPTOM_TABLE);

            in_s = res.openRawResource(R.raw.childhood_table_information);
            reader = new BufferedReader(new InputStreamReader(in_s));

            db.loadAllData(reader, DatabaseHelper.CHILDHOOD_SYMPTOM_TABLE);

            in_s = res.openRawResource(R.raw.pregnancy_table_information);
            reader = new BufferedReader(new InputStreamReader(in_s));

            db.loadAllData(reader, DatabaseHelper.PREGNANCY_TABLE);

            in_s = res.openRawResource(R.raw.diagnosis_table_information);
            reader = new BufferedReader(new InputStreamReader(in_s));

            db.loadAllData(reader, DatabaseHelper.DIAGNOSIS_TABLE);
        }
    }

    private void showTermsDialog() {
        String message;

        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.terms);

            byte[] b = new byte[in_s.available()];
            message = new String(b);
        } catch (Exception e) {
            message = this.getResources().getString(R.string.terms_and_conditions_error);
        }

        new AlertDialog.Builder(this)
            .setTitle(R.string.terms_and_conditions)
            .setMessage(message)
            .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    showLiabilityDialog();
                }
            })
            .setCancelable(false)
            .create()
            .show();
    }

    private void showLiabilityDialog() {
        String message;

        try {
            // reads the raw resource file and converts it to a string
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.liability);

            byte[] b = new byte[in_s.available()];
            message = new String(b);
        } catch (Exception e) {
            message = this.getResources().getString(R.string.liability_error);
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.liability)
                .setMessage(message)
                .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstStart", false);
                        editor.apply();
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }
}