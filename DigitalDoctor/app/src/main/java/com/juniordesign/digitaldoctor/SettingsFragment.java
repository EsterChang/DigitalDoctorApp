package com.juniordesign.digitaldoctor;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


//Red Chu - Settings fragment class, rewrote this using SettingsActivity
public class SettingsFragment extends Fragment {

    Button onTermsAndConditionsPressed;
    Button onLiabilityPressed;
    ImageButton backButton;

    public static Fragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        //handle liability warning button pressed
        onLiabilityPressed = rootView.findViewById(R.id.liability_button);
        onLiabilityPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLiabilityDialog();
            }
        });

        //handle terms and conditions pressed
        onTermsAndConditionsPressed = rootView.findViewById(R.id.terms_and_conditions_button);
        onTermsAndConditionsPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTermsDialog();
            }
        });

        //handle Back button pressed
        backButton = rootView.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new HomeFragment());
                fr.commit();
            }
        });

        return rootView;
    }

    public void showTermsDialog() {
        String message;

        try {
            // reads the raw resource file and converts it to a string
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.terms);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int i;
            i = in_s.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = in_s.read();
            }

            in_s.close();

            message = byteArrayOutputStream.toString();
        } catch (Exception e) {
            message = this.getResources().getString(R.string.terms_and_conditions_error);;
        }

        new AlertDialog.Builder(getActivity())
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
            // reads the raw resource file and converts it to a string
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.liability);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int i;
            i = in_s.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = in_s.read();
            }

            in_s.close();

            message = byteArrayOutputStream.toString();
        } catch (Exception e) {
            message = this.getResources().getString(R.string.liability_error);;
        }

        new AlertDialog.Builder(getActivity())
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