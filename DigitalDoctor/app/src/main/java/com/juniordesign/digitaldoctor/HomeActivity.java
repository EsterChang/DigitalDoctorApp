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

import java.io.InputStream;


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
            db.loadAllData();
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
        // only home fragment is currently set-up, but everything else works similarly
        switch (item.getItemId()) {
            case R.id.navigation_home:
                frag = HomeFragment.newInstance();
                break;
            case R.id.navigation_search:
                frag = null;
                break;
            case R.id.navigationView:
                frag = null;
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
            ft.add(R.id.container, frag, frag.getTag());
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