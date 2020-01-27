package com.juniordesign.digitaldoctor;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
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
    private int mSelectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = new DatabaseHelper(this);

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigationView);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(1);
        }
        selectFragment(selectedItem);

        mBottomNav.getMenu().findItem(R.id.navigation_home).setChecked(true);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

      //uncomment to make easy edits to first load
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", true);
        editor.apply();

        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showTermsDialog();

            // to be removed once we have all data loaded, and first start uncommented
            db.deleteAllData();

            Resources res = getResources();

            InputStream in_s = res.openRawResource(R.raw.body_part_table_information);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in_s));

            //currently loads all data from our body part table
            db.loadAllData(reader, db.BODY_PART_TABLE);

            in_s = res.openRawResource(R.raw.generalized_symptom_table_information);
            reader = new BufferedReader(new InputStreamReader(in_s));

            db.loadAllData(reader, db.GENERALIZED_SYMPTOM_TABLE);

            in_s = res.openRawResource(R.raw.childhood_table_information);
            reader = new BufferedReader(new InputStreamReader(in_s));

            db.loadAllData(reader, db.CHILDHOOD_SYMPTOM_TABLE);

            in_s = res.openRawResource(R.raw.pregnancy_table_information);
            reader = new BufferedReader(new InputStreamReader(in_s));

            db.loadAllData(reader, db.PREGNANCY_TABLE);

            in_s = res.openRawResource(R.raw.diagnosis_table_information);
            reader = new BufferedReader(new InputStreamReader(in_s));

            db.loadAllData(reader, db.DIAGNOSIS_TABLE);


            // note from Kyle --> this is for database testing purposes, do not delete

//            Cursor cur = db.getAllData(db.BODY_PART_TABLE);
//            StringBuffer buffer = new StringBuffer();
//            while (cur.moveToNext()) {
//                buffer.append("Area: " + cur.getString(0) + "\n");
//                buffer.append("Symptom: " + cur.getString(1) + "\n");
//                buffer.append("Extra Info: " + cur.getString(2) + "\n");
//                buffer.append("Name: " + cur.getString(3) + "\n");
//            }
//            new AlertDialog.Builder(this)
//                    .setTitle("Database")
//                    .setMessage(buffer.toString())
//                    .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    })
//                    .setCancelable(false)
//                    .create()
//                    .show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(1);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

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

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, frag);
            ft.commit();
        }
    }

    private void showTermsDialog() {
        String message;

        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.terms);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            message = new String(b);
        } catch (Exception e) {
            // e.printStackTrace();
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
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.liability);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            message = new String(b);
        } catch (Exception e) {
            // e.printStackTrace();
            message = this.getResources().getString(R.string.liability_error);
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.liability)
                .setMessage(message)
                .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
}