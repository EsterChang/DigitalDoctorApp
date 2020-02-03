package com.juniordesign.digitaldoctor;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Fragment class for each nav menu item
 */
public class SearchFragment extends Fragment {

    private View mContent;
    ListView listView;
    ImageButton restart;
    DatabaseHelper db;

    public static Fragment newInstance() {
        Fragment frag = new SearchFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_search, container, false);
        // example listView
        listView = (ListView)rootView.findViewById(R.id.listView);
        restart = (ImageButton)rootView.findViewById(R.id.restart_button);
        restart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                SearchFragment fragment = (SearchFragment)
                        getFragmentManager().findFragmentById(R.id.fragment_container);
                getFragmentManager().beginTransaction()
                        .detach(fragment)
                        .attach(fragment)
                        .commit();
            }
        });

        // Ester - below are a few test cases that simulate the following search sequence using db.BODY_PART_TABLE:
        // Head -> Headache -> with Stiff Neck and Fever -> Meningitis
        // uncomment each test case to go through each level of search; the results from db.getData() should populate the listview

        db = new DatabaseHelper(getActivity());
        ArrayList<String> whereColumns = new ArrayList<>();
        ArrayList<String> whereMatches = new ArrayList<>();
        Cursor cur;
        ArrayList<String> resultsList = new ArrayList<>();

//        //test 0 - populates listview with all items in the PRIMARY_AREA column
//        whereColumns = new ArrayList<>();
//        whereMatches = new ArrayList<>();
//        resultsList = new ArrayList<>();
//        cur = db.getData(db.PRIMARY_AREA, db.BODY_PART_TABLE, whereColumns, whereMatches);
//        while (cur.moveToNext()) {
//            resultsList.add(cur.getString(0));
//        }

//        //test 1 - populates listview with all unique PRIMARY_SYMPTOM where PRIMARY_AREA = Head
//        whereColumns.add(db.PRIMARY_AREA);
//        whereMatches.add("Head");
//        cur = db.getData(db.PRIMARY_SYMPTOM, db.BODY_PART_TABLE, whereColumns, whereMatches);
//        while (cur.moveToNext()) {
//            resultsList.add(cur.getString(0));
//        }
//
//        //test 2 - populates listview with all unique EXTRA_INFORMATION where PRIMARY_AREA = Head and PRIMARY_SYMPTOM = Headache
//        whereColumns = new ArrayList<>();
//        whereMatches = new ArrayList<>();
//        resultsList = new ArrayList<>();
//        whereColumns.add(db.PRIMARY_AREA);
//        whereColumns.add(db.PRIMARY_SYMPTOM);
//        whereMatches.add("Head");
//        whereMatches.add("Headache");
//        cur = db.getData(db.EXTRA_INFORMATION, db.BODY_PART_TABLE, whereColumns, whereMatches);
//        while (cur.moveToNext()) {
//            resultsList.add(cur.getString(0));
//        }
//
//        //test 3 - populates listview with all unique SYMPTOM_NAME where PRIMARY_AREA = Head
//        // and PRIMARY_SYMPTOM = Headache and EXTRA_INFORMATION = "with Stiff Neck and Fever" (result should be Meningitis)

//        whereColumns = new ArrayList<>();
//        whereMatches = new ArrayList<>();
//        resultsList = new ArrayList<>();
//        whereColumns.add(db.PRIMARY_AREA);
//        whereColumns.add(db.PRIMARY_SYMPTOM);
//        whereColumns.add(db.EXTRA_INFORMATION);
//        whereMatches.add("Head");
//        whereMatches.add("Headache");
//        whereMatches.add("with Stiff Neck and Fever");
//        cur = db.getData(db.SYMPTOM_NAME, db.BODY_PART_TABLE, whereColumns, whereMatches);
//        while (cur.moveToNext()) {
//            resultsList.add(cur.getString(0));
//        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, resultsList);

        listView.setAdapter(arrayAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text and color from bundle or savedInstanceState
        mContent = view.findViewById(R.id.fragment_content_search);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}