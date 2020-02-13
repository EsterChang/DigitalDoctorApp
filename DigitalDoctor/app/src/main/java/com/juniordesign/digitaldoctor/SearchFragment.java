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
    ImageButton back;
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

        back = (ImageButton)rootView.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level--;
                if (level < 0) {
                    level = 0;
                }
                if (level == 0) {
                    questionsView.setText("Which of these categories best describes your problem?");
                    promptsView.setText("Select Problem Category");

                    //set the listView
                    final ArrayList<String> tableNames = new ArrayList<>();
                    tableNames.add("Body-Part Specific Symptoms");
                    tableNames.add("Generalized (Whole Body) Symptoms");
                    tableNames.add("Pregnancy Symptoms");
                    tableNames.add("Common Childhood Symptoms");
                    tableNameArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, tableNames);
                    listView.setAdapter(tableNameArrayAdapter);
                    _tableName = "";
                    _select = "";
                    whereMatches.clear();
                    whereColumns.clear();
                } else {
                    whereColumns.remove(whereColumns.size() - 1);
                    whereMatches.remove(whereMatches.size() - 1);
                    if (level == 1) {
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
                    } else if (level == 2) {
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
                    } else if (level == 3) {
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
                    } else if (level == 4) {
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
        final ArrayList<String> tableNames = new ArrayList<>();
        tableNames.add("Body-Part Specific Symptoms");
        tableNames.add("Generalized (Whole Body) Symptoms");
        tableNames.add("Pregnancy Symptoms");
        tableNames.add("Common Childhood Symptoms");
        tableNameArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, tableNames);
        listView.setAdapter(tableNameArrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                //String selectedItem = (String) parent.getItemAtPosition(position);
                if (level == 0) {
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