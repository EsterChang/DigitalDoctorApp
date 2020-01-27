package com.juniordesign.digitaldoctor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.InputStream;

public class  SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
    }

    public void onBackButtonPressed(View view) {
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
    }

    public void onShowTermsandConditionButtonPressed(View view){
        showTermsDialog();
    }

    public void onShowLiabilityButtonPressed(View view){
        showLiabilityDialog();
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
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    protected void showLiabilityDialog() {
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
    }
}
