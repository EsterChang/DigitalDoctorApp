package com.juniordesign.digitaldoctor;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Fragment class for each nav menu item
 */
public class DetailFragment extends Fragment {
    private ImageView icon;
    private TextView emergency;
    private TextView name;
    private TextView description;
    private ImageButton restartButton;
    private ImageButton backButton;
    private Button mapButton;
    String select;
    String tableName;
    ArrayList<String> resultsList;
    ArrayList<String> whereColumns;
    ArrayList<String> whereMatches;
    int level;

    //Creates a new Fragment, keeps track of parameters needed to populate the
    //detail page as well as information for recreating the last search fragment
    public static Fragment newInstance(String name, String emergency, String text,
                                       ArrayList<String> resultsList, int level,
                                       String _select, String _tableName,
                                       ArrayList<String> whereColumns,
                                       ArrayList<String> whereMatches) {
        Fragment frag = new DetailFragment();

        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("emergency", emergency);
        args.putString("text", text);
        args.putStringArrayList("resultsList", resultsList);
        args.putInt("level", level);
        args.putString("_select", _select);
        args.putString("_tableName", _tableName);
        args.putStringArrayList("whereColumns", whereColumns);
        args.putStringArrayList("whereMatches", whereMatches);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        String diagnosisName = getArguments().getString("name", getResources().getString(R.string.error_name));
        String emergencyIconCaption = getArguments().getString("emergency", getResources().getString(R.string.error_emergency));
        String pageText = getArguments().getString("text", getResources().getString(R.string.error_text));
        resultsList = getArguments().getStringArrayList("resultsList");
        level = getArguments().getInt("level");
        select = getArguments().getString("_select");
        tableName = getArguments().getString("_tableName");
        whereColumns = getArguments().getStringArrayList("whereColumns");
        whereMatches = getArguments().getStringArrayList("whereMatches");

        //restart button functionality
        restartButton = rootView.findViewById(R.id.restart_button_search);
        restartButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Fragment frag = SearchFragment.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, frag);
                ft.commit();
            }
        });

        //back button functionality
        backButton = rootView.findViewById(R.id.back_button_search);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = SearchFragment.newInstance(resultsList, level,
                        select, tableName, whereColumns, whereMatches);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, frag);
                ft.commit();
            }
        });

        // sets the map button to open a google map url
        mapButton = rootView.findViewById(R.id.mapsIntent);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=emergency+room&zoom=11"));
                startActivity(intent);
            }
        });

        // populate page information
        icon = rootView.findViewById(R.id.detail_icon);
        emergency = rootView.findViewById(R.id.detail_emergency);
        name = rootView.findViewById(R.id.detail_name);
        description = rootView.findViewById(R.id.detail_description);
        name.setText(diagnosisName);
        description.setText(pageText);
        emergency.setText(emergencyIconCaption);

        // sets icon and button states
        if (emergencyIconCaption.equalsIgnoreCase(getResources().getString(R.string.emergency))) {
            icon.setImageResource(R.drawable.emergency_icon);
        } else if (emergencyIconCaption.equalsIgnoreCase(getResources().getString(R.string.caution))) {
            icon.setImageResource(R.drawable.caution_icon);
        } else if (emergencyIconCaption.equalsIgnoreCase(getResources().getString(R.string.not_an_emergency))) {
            icon.setImageResource(R.drawable.not_an_emergency);
            mapButton.setVisibility(View.GONE);
        } else {
            icon.setImageResource(R.drawable.emergency_icon);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}