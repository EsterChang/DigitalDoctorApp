package com.juniordesign.digitaldoctor;

import android.database.Cursor;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Fragment class for each nav menu item
 */
public class SearchFragment extends Fragment {

    private View mContent;
    ListView listView;
    TextView promptView;
    TextView directiveView;
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
    boolean detailBackPressed = false;

    //The ArrayAdapter and the ArrayList needs to be global variables
    ArrayAdapter tableNameArrayAdapter;
    ArrayList<String> updateList;

    public static Fragment newInstance() {
        return new SearchFragment();
    }

    public static Fragment newInstance(ArrayList<String> resultsList, int level, String _select, String _tableName, ArrayList<String> whereColumns, ArrayList<String> whereMatches) {

        Fragment frag = new SearchFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("resultsList", resultsList);
        args.putInt("level", level);
        args.putString("_select", _select);
        args.putString("_tableName", _tableName);
        args.putStringArrayList("whereColumns", whereColumns);
        args.putStringArrayList("whereMatches", whereMatches);
        frag.setArguments(args);

        return frag;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getActivity());
        if (getArguments() != null) {
            detailBackPressed = true;
            resultsList = getArguments().getStringArrayList("resultsList");
            level = getArguments().getInt("level");
            _select = getArguments().getString("_select");
            _tableName = getArguments().getString("_tableName");
            whereColumns = getArguments().getStringArrayList("whereColumns");
            whereMatches = getArguments().getStringArrayList("whereMatches");
        } else {
            level = 0;
            _select = "";
            _tableName = "";
            whereColumns = new ArrayList<>();
            whereMatches = new ArrayList<>();
            resultsList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_search, container, false);

        // get the listView, questions, prompts to be set in future during search
        listView = (ListView)rootView.findViewById(R.id.listView);
        promptView = (TextView) rootView.findViewById(R.id.questions);
        directiveView = (TextView) rootView.findViewById(R.id.prompts);

        //restart button
        restart = (ImageButton)rootView.findViewById(R.id.restart_button);
        restart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Fragment frag = SearchFragment.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, frag);
                ft.commit();
            }
        });

        back = (ImageButton)rootView.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (level > 0) {
                    level--;
                }

                if (level == 0) {
                    levelZeroBackHelper();
                } else {
                    whereColumns.remove(whereColumns.size() - 1);
                    whereMatches.remove(whereMatches.size() - 1);

                    switch (level) {
                        case 1:
                            levelOneBackHelper();
                            break;
                        case 2:
                            levelTwoBackHelper();
                            break;
                        case 3:
                            levelThreeBackHelper();
                            break;
                        case 4:
                            if (_tableName.equals(db.BODY_PART_TABLE)) {
                                //new fragment
                            }
                            break;
                    }
                    setSelect(level - 1, _tableName);
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

        if (!detailBackPressed) {
            //first select table
            //set questions and prompts
            promptView.setText(R.string.initial_search_prompt);
            directiveView.setText(R.string.initial_search_directive);

            //set the listView

            resultsList.add(getResources().getString(R.string.body_part_specific));
            resultsList.add(getResources().getString(R.string.generalized_symptoms));
            resultsList.add(getResources().getString(R.string.pregnancy_symptoms));
            resultsList.add(getResources().getString(R.string.childhood_symptoms));


        } else {
            setPromptText(level - 1, _tableName);
            setSelect(level - 1, _tableName);
            if (whereColumns.size() > 0) {
                whereColumns.remove(whereColumns.size() - 1);
                whereMatches.remove(whereMatches.size() - 1);
            }
            cur = db.getData(_select, _tableName, whereColumns, whereMatches);
            resultsList.clear();
            while (cur.moveToNext()) {
                resultsList.add(cur.getString(0));
            }
        }
        tableNameArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, resultsList);
        listView.setAdapter(tableNameArrayAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                //String selectedItem = (String) parent.getItemAtPosition(position);

                //searchDone - whether or not we reached the end of a search sequence
                boolean searchDone = false;
                if (level == 0) {
                    switch (position) {
                        case 0:
                            //1 - Body-Part Specific Symptoms
                            _tableName = db.BODY_PART_TABLE;
                            break;
                        case 1:
                            //2 - Generalized (Whole Body) Symptoms or Pregnancy Symptoms
                            _tableName = db.GENERALIZED_SYMPTOM_TABLE;
                            break;
                        case 2:
                            //2 - Generalized (Whole Body) Symptoms or Pregnancy Symptoms
                            _tableName = db.PREGNANCY_TABLE;
                            break;
                        case 3:
                            //4 - Common Childhood Symptoms
                            _tableName = db.CHILDHOOD_SYMPTOM_TABLE;
                            break;
                }
                searchHelper();
                } else {
                    whereColumns.add(_select);
                    whereMatches.add(listView.getItemAtPosition(position).toString());
                    setPromptText(level, _tableName);
                    setSelect(level, _tableName);
                    if (level == 2) {
                        if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
                            createDetailFragment(_tableName, whereColumns, whereMatches);
                            searchDone = true;
                        } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
                            createDetailFragment(_tableName, whereColumns, whereMatches);
                            searchDone = true;
                        } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
                            createDetailFragment(_tableName, whereColumns, whereMatches);
                            searchDone = true;
                        }
                    } else if (level == 3) {
                        if (_tableName.equals(db.BODY_PART_TABLE)) {
                            createDetailFragment(_tableName, whereColumns, whereMatches);
                            searchDone = true;
                        }
                    }
                    setSearchList(searchDone);
                }
                level++;

            }
        });

        return rootView;
    }

    //Sets the correct text based on current level of search and tableName
    private void setPromptText(int level, String _tableName) {
        if (level == 0) {
            if (_tableName.equals(db.BODY_PART_TABLE)) {
                _select = db.PRIMARY_AREA;
                promptView.setText(R.string.primary_symptom_location_prompt);
                directiveView.setText(R.string.primary_symptom_location_directive);
            } else if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
                _select = db.PRIMARY_SYMPTOM;
                promptView.setText(R.string.primary_symptom_identification_prompt);
                directiveView.setText(R.string.primary_symptom_identification_directive);
            } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
                _select = db.PRIMARY_SYMPTOM;
                promptView.setText(R.string.time_period_prompt);
                directiveView.setText(R.string.time_period_directive);
            } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
                _select = db.PRIMARY_AREA;
                promptView.setText(R.string.general_problem_prompt);
                directiveView.setText(R.string.general_problem_directive);
            }
        } else if (level == 1) {
            if (_tableName.equals(db.BODY_PART_TABLE)) {
                _select = db.PRIMARY_SYMPTOM;
                promptView.setText(R.string.general_problem_prompt);
                directiveView.setText(R.string.general_problem_directive);
            } else if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
                _select = db.EXTRA_INFORMATION;
                promptView.setText(R.string.extra_info_prompt);
                directiveView.setText(R.string.extra_info_directive);
            } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
                _select = db.EXTRA_INFORMATION;
                promptView.setText(R.string.extra_info_prompt);
                directiveView.setText(R.string.extra_info_directive);
            } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
                _select = db.EXTRA_INFORMATION;
                promptView.setText(R.string.extra_info_prompt);
                directiveView.setText(R.string.extra_info_directive);
            }
        } else if (level == 2) {
            if (_tableName.equals(db.BODY_PART_TABLE)) {
                _select = db.EXTRA_INFORMATION;
                promptView.setText(R.string.extra_info_prompt);
                directiveView.setText(R.string.extra_info_directive);
            }
        }
    }

    //Sets the _select variable based on current level of search and tableName
    private void setSelect(int level, String _tableName) {
        if (level == 0) {
            if (_tableName.equals(db.BODY_PART_TABLE)) {
                _select = db.PRIMARY_AREA;
            } else if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
                _select = db.PRIMARY_SYMPTOM;
            } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
                _select = db.PRIMARY_SYMPTOM;
            } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
                _select = db.PRIMARY_AREA;
            }
        } else if (level == 1) {
            if (_tableName.equals(db.BODY_PART_TABLE)) {
                _select = db.PRIMARY_SYMPTOM;
            } else if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
                _select = db.EXTRA_INFORMATION;
            } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
                _select = db.EXTRA_INFORMATION;
            } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
                _select = db.EXTRA_INFORMATION;
            }
        } else if (level == 2) {
            if (_tableName.equals(db.BODY_PART_TABLE)) {
                _select = db.EXTRA_INFORMATION;
            }
        }
    }

    private void createDetailFragment(String _tableName, ArrayList whereColumns, ArrayList whereMatches) {
        String name = null;
        Cursor populate = db.getData(db.SYMPTOM_NAME, _tableName, whereColumns, whereMatches);
        while (populate.moveToNext()) {
            name = (populate.getString(0));
        }
        ArrayList<String> symptomname = new ArrayList<>();
        ArrayList<String> column = new ArrayList<>();
        if (symptomname != null) {
            symptomname.add(name);
            column.add(db.SYMPTOM_NAME);
        }

        String emergency = null;
        populate = db.getData(db.SYMPTOM_SEVERITY, db.DIAGNOSIS_TABLE, column, symptomname);
        while (populate.moveToNext()) {
            emergency = (populate.getString(0));
        }
        String text = null;
        populate = db.getData(db.SYMPTOM_INFORMATION, db.DIAGNOSIS_TABLE, column, symptomname);
        while (populate.moveToNext()) {
            text = (populate.getString(0));
        }
        Fragment frag = DetailFragment.newInstance(name, emergency, text, resultsList, level , _select, _tableName, whereColumns, whereMatches);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, frag);
        ft.commit();
    }

    private void levelZeroBackHelper() {
        promptView.setText(R.string.initial_search_prompt);
        directiveView.setText(R.string.initial_search_directive);

        //set the listView
        final ArrayList<String> tableNames = new ArrayList<>();
        tableNames.add(getResources().getString(R.string.body_part_specific));
        tableNames.add(getResources().getString(R.string.generalized_symptoms));
        tableNames.add(getResources().getString(R.string.pregnancy_symptoms));
        tableNames.add(getResources().getString(R.string.childhood_symptoms));
        tableNameArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, tableNames);
        listView.setAdapter(tableNameArrayAdapter);
        _tableName = "";
        _select = "";
        whereMatches.clear();
        whereColumns.clear();
    }

    private void levelOneBackHelper() {
        if (_tableName.equals(db.BODY_PART_TABLE)) {
            _select = db.PRIMARY_AREA;
            promptView.setText(R.string.primary_symptom_location_prompt);
            directiveView.setText(R.string.primary_symptom_location_directive);
        } else if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
            _select = db.PRIMARY_SYMPTOM;
            promptView.setText(R.string.primary_symptom_identification_prompt);
            directiveView.setText(R.string.primary_symptom_identification_directive);
        } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
            _select = db.PRIMARY_SYMPTOM;
            promptView.setText(R.string.time_period_prompt);
            directiveView.setText(R.string.time_period_directive);
        } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
            _select = db.PRIMARY_AREA;
            promptView.setText(R.string.general_problem_prompt);
            directiveView.setText(R.string.general_problem_directive);
        }
    }

    private void levelTwoBackHelper() {
        if (_tableName.equals(db.BODY_PART_TABLE)) {
            _select = db.PRIMARY_SYMPTOM;
            promptView.setText(R.string.general_problem_prompt);
            directiveView.setText(R.string.general_problem_directive);
        } else if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
            _select = db.EXTRA_INFORMATION;
            promptView.setText(R.string.extra_info_prompt);
            directiveView.setText(R.string.extra_info_directive);
        } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
            _select = db.EXTRA_INFORMATION;
            promptView.setText(R.string.extra_info_prompt);
            directiveView.setText(R.string.extra_info_directive);
        } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
            _select = db.EXTRA_INFORMATION;
            promptView.setText(R.string.extra_info_prompt);
            directiveView.setText(R.string.extra_info_directive);
        }
    }

    private void levelThreeBackHelper() {
        if (_tableName.equals(db.BODY_PART_TABLE)) {
            _select = db.EXTRA_INFORMATION;
            promptView.setText(R.string.extra_info_prompt);
            directiveView.setText(R.string.extra_info_directive);
        } else if (_tableName.equals(db.GENERALIZED_SYMPTOM_TABLE)) {
            //new fragment
        } else if (_tableName.equals(db.PREGNANCY_TABLE)) {
            //new fragment
        } else if (_tableName.equals(db.CHILDHOOD_SYMPTOM_TABLE)) {
            //new fragment
        }
    }

    private void searchHelper() {
        setPromptText(level, _tableName);
        setSelect(level, _tableName);

        cur = db.getData(_select, _tableName, whereColumns, whereMatches);
        resultsList.clear();
        while (cur.moveToNext()) {
            resultsList.add(cur.getString(0));
        }
        ArrayAdapter resultArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, resultsList);
        listView.setAdapter(resultArrayAdapter);
    }

    private void setSearchList(boolean searchDone) {
        cur = db.getData(_select, _tableName, whereColumns, whereMatches);
        resultsList.clear();
        while (cur.moveToNext()) {
            resultsList.add(cur.getString(0));
        }

        if (!searchDone) {
            ArrayAdapter resultArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, resultsList);
            listView.setAdapter(resultArrayAdapter);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text and color from bundle or savedInstanceState
        mContent = view.findViewById(R.id.fragment_content_search);
    }

}