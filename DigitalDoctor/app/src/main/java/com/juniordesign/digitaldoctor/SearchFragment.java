package com.juniordesign.digitaldoctor;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Console;
import java.util.ArrayList;


/**
 * Fragment class for each nav menu item
 */
public class SearchFragment extends Fragment {

    private View mContent;
    ListView listView;
    TextView questionsView;
    TextView promptsView;
    ImageButton restart;
    DatabaseHelper db;
    int level;
    String _select;
    String _tableName;
    ArrayList<String> whereColumns;
    ArrayList<String> whereMatches;
    ArrayList<String> resultsList;
    Cursor cur;

    //The ArrayAdapter and the ArrayList needs to be global variables
    ArrayAdapter tableNameArrayAdapter;
    ArrayList<String> updateList;

    public static Fragment newInstance() {
        Fragment frag = new SearchFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        level = 0;
        db = new DatabaseHelper(getActivity());
        _select = "";
        _tableName = "";
        whereColumns = new ArrayList<>();
        whereMatches = new ArrayList<>();
        resultsList = new ArrayList<>();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_search, container, false);

        // get the listView, questions, prompts to be set in future during search
        listView = (ListView)rootView.findViewById(R.id.listView);
        questionsView = (TextView) rootView.findViewById(R.id.questions);
        promptsView = (TextView) rootView.findViewById(R.id.prompts);

        //restart button
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


        //Irene - add a pre-selection into the listview for the four tables
        //1 - Body-Part Specific Symptoms
        //2 - Generalized (Whole Body) Symptoms
        //3 - Pregnancy Symptoms
        //4 - Common Childhood Symptoms
        // - unassigned, show the tables names in listView

        //first select table
        //set questions and prompts
        questionsView.setText("Which of these categories best describes your problem?");
        promptsView.setText("Select Problem Category");

        //set the listView
        ArrayList<String> tableNames = new ArrayList<>();
        tableNames.add("Body-Part Specific Symptoms");
        tableNames.add("Generalized (Whole Body) Symptoms");
        tableNames.add("Pregnancy Symptoms");
        tableNames.add("Common Childhood Symptoms");
        tableNameArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, tableNames);
        listView.setAdapter(tableNameArrayAdapter);
        updateList = new ArrayList<String>();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                //String selectedItem = (String) parent.getItemAtPosition(position);
                if (_tableName.equals("")) {
                    if (position == 0) {
                        //1 - Body-Part Specific Symptoms
                        _tableName = db.BODY_PART_TABLE;
                    } else if (position == 1) {
                        //2 - Generalized (Whole Body) Symptoms or Pregnancy Symptoms
                        _tableName = db.GENERALIZED_SYMPTOM_TABLE;
                    } else if (position == 2) {
                        //2 - Generalized (Whole Body) Symptoms or Pregnancy Symptoms
                        _tableName = db.PREGNANCY_TABLE;
                    } else if (position == 3) {
                        //4 - Common Childhood Symptoms
                        _tableName = db.CHILDHOOD_SYMPTOM_TABLE;
                    }
                }
                if (level == 0) {
                    if (_tableName.equals(db.BODY_PART_TABLE)) {
                        _select = db.PRIMARY_AREA;
                        questionsView.setText("Where is your primary symptom located?");
                        promptsView.setText("Select Symptom Location");
                    } else if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
                        _select = db.PRIMARY_SYMPTOM;
                        questionsView.setText("What is your primary symptom?");
                        promptsView.setText("Select Symptom");
                    } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
                        _select = db.PRIMARY_SYMPTOM;
                        questionsView.setText("When is this happening?");
                        promptsView.setText("Select Time Period");
                    } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
                        _select = db.PRIMARY_AREA;
                        questionsView.setText("What is the main problem?");
                        promptsView.setText("Select Appropriate Category");
                    }
                    cur = db.getData(_select, _tableName, whereColumns, whereMatches);
                    resultsList.clear();
                    while (cur.moveToNext()) {
                        resultsList.add(cur.getString(0));
                    }
                    ArrayAdapter resultArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, resultsList);
                    listView.setAdapter(resultArrayAdapter);
                } else {
                    whereColumns.add(_select);
                    whereMatches.add(listView.getItemAtPosition(position).toString());
                    if (level == 1) {
                        if (_tableName.equals(db.BODY_PART_TABLE)) {
                            _select = db.PRIMARY_SYMPTOM;
                            questionsView.setText("What is the main problem?");
                            promptsView.setText("Select Appropriate Category");
                        } else if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
                            _select = db.EXTRA_INFORMATION;
                            questionsView.setText("Please provide extra information.");
                            promptsView.setText("Select Most Similar Category");
                        } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
                            _select = db.EXTRA_INFORMATION;
                            questionsView.setText("Please provide extra information.");
                            promptsView.setText("Select Most Similar Category");
                        } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
                            _select = db.EXTRA_INFORMATION;
                            questionsView.setText("Please provide extra information.");
                            promptsView.setText("Select Most Similar Category");
                        }
                    } else if (level == 2) {
                        if (_tableName.equals(db.BODY_PART_TABLE)) {
                            _select = db.EXTRA_INFORMATION;
                            questionsView.setText("Please provide extra information.");
                            promptsView.setText("Select Most Similar Category");
                        } else if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
                            //new fragment
                        } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
                            //new fragment
                        } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
                            //new fragment
                        }
                    } else if (level == 3) {
                        if (_tableName.equals(db.BODY_PART_TABLE)) {
                            //new fragment
                        }
                    }
                    cur = db.getData(_select, _tableName, whereColumns, whereMatches);
                    resultsList.clear();
                    while (cur.moveToNext()) {
                        resultsList.add(cur.getString(0));
                    }
                    ArrayAdapter resultArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, resultsList);
                    listView.setAdapter(resultArrayAdapter);
                }
                level++;

            }
        });



            // Ester - below are a few test cases that simulate the following search sequence using db.BODY_PART_TABLE:
            // Head -> Headache -> with Stiff Neck and Fever -> Meningitis
            // uncomment each test case to go through each level of search; the results from db.getData() should populate the listview




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

            //ArrayAdapter resultArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, resultsList);

            //listView.setAdapter(resultArrayAdapter);




        return rootView;
    }

    void updateListView(ArrayAdapter arrayAdapter, ArrayList<String> arrayList) {
        arrayAdapter.clear();
        for (String string : arrayList) {
            arrayAdapter.add(string);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //For testing purposes
                Log.d("Testing", "Clicked: " + position);
            }
        });
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