package com.juniordesign.digitaldoctor;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Fragment class for the search screen. Contains a couple buttons and a
 * listview that updates on user input.
 */
public class SearchFragment extends Fragment {
    private ListView listView;
    private TextView promptView;
    private TextView directiveView;
    private ImageButton restartButton;
    private ImageButton backButton;

    // used when creating a page from the back button on the detailFragment.
    boolean detailBackPressed = false;

    // the following variables are used to search for a diagnosis.
    DatabaseHelper db;
    Cursor cur;
    int level;
    String select;
    String tableName;
    ArrayList<String> whereColumns;
    ArrayList<String> whereMatches;
    ArrayList<String> resultsList;

    /**
     * This is a custom array adapter made to fix the padding bug
     */
    class CustomAdapter extends ArrayAdapter<String> {
        String[] options;

        CustomAdapter(Context c, ArrayList<String> options) {
            super(c, R.layout.row, R.id.customTextView, options);

            this.options = new String[options.size()];
            Object[] objArr = options.toArray();

            int i = 0;
            for (Object obj : objArr) {
                this.options[i++] = (String)obj;
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater =
                            (LayoutInflater)getActivity().
                            getApplicationContext().
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView myOption = row.findViewById(R.id.customTextView);

            myOption.setText(options[position]);

            return row;
        }
    }

    /**
     * The Custom ArrayAdapter needs to be accessible to everything for search update.
     */
    CustomAdapter tableNameArrayAdapter;

    public static Fragment newInstance() {
        return new SearchFragment();
    }

    public static Fragment newInstance(ArrayList<String> resultsList, int level,
                                       String _select, String _tableName,
                                       ArrayList<String> whereColumns,
                                       ArrayList<String> whereMatches) {

        // pass in information to the arguments to be accessed later.
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

        // use previous information if coming from the detail fragment.
        if (getArguments() != null) {
            detailBackPressed = true;
            resultsList = getArguments().getStringArrayList("resultsList");
            level = getArguments().getInt("level");
            select = getArguments().getString("_select");
            tableName = getArguments().getString("_tableName");
            whereColumns = getArguments().getStringArrayList("whereColumns");
            whereMatches = getArguments().getStringArrayList("whereMatches");
        } else {
            level = 0;
            select = "";
            tableName = "";
            whereColumns = new ArrayList<>();
            whereMatches = new ArrayList<>();
            resultsList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment. Fill the container.
        View rootView = inflater.inflate(R.layout.activity_search, container,
                false);

        // Assign important variables for searching.
        listView = rootView.findViewById(R.id.listView);
        promptView = rootView.findViewById(R.id.questions);
        directiveView = rootView.findViewById(R.id.prompts);

        // Create restart button and listener.
        restartButton = rootView.findViewById(R.id.restart_button_search);
        restartButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Fragment frag = SearchFragment.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, frag);
                ft.commit();
            }
        });

        // Create back button and listener.
        backButton = rootView.findViewById(R.id.back_button_search);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (level > 0) {
                    level--;
                }

                if (level == 0) {
                    levelZeroBackHelper();
                } else {
                    // remove most recent search and matches from the lists.
                    whereColumns.remove(whereColumns.size() - 1);
                    whereMatches.remove(whereMatches.size() - 1);

                    // call appropriate helper function.
                    switch (level) {
                        case 1:
                            levelOneBackHelper(tableName);
                            break;
                        case 2:
                            levelTwoBackHelper(tableName);
                            break;
                        case 3:
                            levelThreeBackHelper(tableName);
                            break;
                    }

                    // get appropriate column name for select statement.
                    setSelect(level - 1, tableName);

                    // populate cursor with results from DatabaseHelper
                    cur = db.getData(select, tableName,
                            whereColumns, whereMatches);

                    // clear the previous results, in order to add new ones.
                    resultsList.clear();
                    while (cur.moveToNext()) {
                        resultsList.add(cur.getString(0));
                    }

                    // create a new adapter and set listview to use it.
                    CustomAdapter resultArrayAdapter = new CustomAdapter(getActivity(), resultsList);
                    listView.setAdapter(resultArrayAdapter);
                }
            }
        });

        if (!detailBackPressed) {
            // populate the first level of search with a prompt and directive.
            promptView.setText(R.string.initial_search_prompt);
            directiveView.setText(R.string.initial_search_directive);

            // set table names manually into the listview to be displayed.
            resultsList.add(getResources()
                    .getString(R.string.body_part_specific));
            resultsList.add(getResources()
                    .getString(R.string.generalized_symptoms));
            resultsList.add(getResources()
                    .getString(R.string.pregnancy_symptoms));
            resultsList.add(getResources()
                    .getString(R.string.childhood_symptoms));

            // back button should be hidden on first level of search.
            backButton.setVisibility(View.INVISIBLE);
        } else {
            // if coming from the detailFragment, use passed information.
            setPromptText(level - 1, tableName);
            setSelect(level - 1, tableName);

            // remove extra information from the columns.
            if (whereColumns.size() > 0) {
                whereColumns.remove(whereColumns.size() - 1);
                whereMatches.remove(whereMatches.size() - 1);
            }

            // populate cursor with results from DatabaseHelper
            cur = db.getData(select, tableName, whereColumns, whereMatches);

            // clear the previous results, in order to add new ones.
            resultsList.clear();
            while (cur.moveToNext()) {
                resultsList.add(cur.getString(0));
            }
        }

        // create a new adapter and set listview to use it.
        tableNameArrayAdapter = new CustomAdapter(getActivity(), resultsList);
        listView.setAdapter(tableNameArrayAdapter);

        // set listview item click listener to search the tables and repopulate.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if we have reached the end of the search.
                boolean searchDone = false;

                // if it is the entry level of the search.
                if (level == 0) {
                    // first, set back to visible again.
                    backButton.setVisibility(View.VISIBLE);

                    // set table that the user will be traversing through.
                    switch (position) {
                        case 0:
                            tableName = DatabaseHelper.BODY_PART_TABLE;
                            break;
                        case 1:
                            tableName = DatabaseHelper.GENERALIZED_SYMPTOM_TABLE;
                            break;
                        case 2:
                            tableName = DatabaseHelper.PREGNANCY_TABLE;
                            break;
                        case 3:
                            tableName = DatabaseHelper.CHILDHOOD_SYMPTOM_TABLE;
                            break;
                    }

                    // call a helper function, which will update the list.
                    searchHelper();
                } else {
                    // add current column and selected item to the arraylist.
                    whereColumns.add(select);
                    whereMatches.add(listView.getItemAtPosition(position)
                            .toString());

                    // set new prompt and directive information.
                    setPromptText(level, tableName);

                    // get column name to search within.
                    setSelect(level, tableName);

                    // check for transition necessity based on level information.
                    if (level == 2) {
                        if (tableName.equals(DatabaseHelper.GENERALIZED_SYMPTOM_TABLE)
                                || tableName.equals(DatabaseHelper.PREGNANCY_TABLE)
                                || tableName.equals(DatabaseHelper.CHILDHOOD_SYMPTOM_TABLE)) {
                            createDetailFragment(tableName, whereColumns, whereMatches);
                            searchDone = true;
                        }
                    } else if (level == 3) {
                        if (tableName.equals(DatabaseHelper.BODY_PART_TABLE)) {
                            createDetailFragment(tableName, whereColumns, whereMatches);
                            searchDone = true;
                        }
                    }

                    // update search if not completed.
                    setSearchList(searchDone);
                }
                // move up a level in the search.
                level++;
            }
        });

        return rootView;
    }

    //Sets the correct text based on current level of search and tableName.
    private void setPromptText(int level, String _tableName) {
        if (level == 0) {
            switch (_tableName) {
                case DatabaseHelper.BODY_PART_TABLE:
                    select = DatabaseHelper.PRIMARY_AREA;
                    promptView.setText(R.string.primary_symptom_location_prompt);
                    directiveView.setText(R.string.primary_symptom_location_directive);
                    break;
                case DatabaseHelper.GENERALIZED_SYMPTOM_TABLE:
                    select = DatabaseHelper.PRIMARY_SYMPTOM;
                    promptView.setText(R.string.primary_symptom_identification_prompt);
                    directiveView.setText(R.string.primary_symptom_identification_directive);
                    break;
                case DatabaseHelper.PREGNANCY_TABLE:
                    select = DatabaseHelper.PRIMARY_SYMPTOM;
                    promptView.setText(R.string.time_period_prompt);
                    directiveView.setText(R.string.time_period_directive);
                    break;
                case DatabaseHelper.CHILDHOOD_SYMPTOM_TABLE:
                    select = DatabaseHelper.PRIMARY_AREA;
                    promptView.setText(R.string.general_problem_prompt);
                    directiveView.setText(R.string.general_problem_directive);
                    break;
                default:
                    break;
            }
        } else if (level == 1) {
            switch (_tableName) {
                case DatabaseHelper.BODY_PART_TABLE:
                    select = DatabaseHelper.PRIMARY_SYMPTOM;
                    promptView.setText(R.string.general_problem_prompt);
                    directiveView.setText(R.string.general_problem_directive);
                    break;
                case DatabaseHelper.GENERALIZED_SYMPTOM_TABLE:
                case DatabaseHelper.PREGNANCY_TABLE:
                case DatabaseHelper.CHILDHOOD_SYMPTOM_TABLE:
                    select = DatabaseHelper.EXTRA_INFORMATION;
                    promptView.setText(R.string.extra_info_prompt);
                    directiveView.setText(R.string.extra_info_directive);
                    break;
                default:
                    break;
            }
        } else if (level == 2) {
            if (_tableName.equals(DatabaseHelper.BODY_PART_TABLE)) {
                select = DatabaseHelper.EXTRA_INFORMATION;
                promptView.setText(R.string.extra_info_prompt);
                directiveView.setText(R.string.extra_info_directive);
            }
        }
    }

    //Sets the _select variable based on current level of search and tableName.
    private void setSelect(int level, String _tableName) {
        if (level == 0) {
            switch (_tableName) {
                case DatabaseHelper.BODY_PART_TABLE:
                case DatabaseHelper.CHILDHOOD_SYMPTOM_TABLE:
                    select = DatabaseHelper.PRIMARY_AREA;
                    break;
                case DatabaseHelper.GENERALIZED_SYMPTOM_TABLE:
                case DatabaseHelper.PREGNANCY_TABLE:
                    select = DatabaseHelper.PRIMARY_SYMPTOM;
                    break;
                default:
                    break;
            }
        } else if (level == 1) {
            switch (_tableName) {
                case DatabaseHelper.BODY_PART_TABLE:
                    select = DatabaseHelper.PRIMARY_SYMPTOM;
                    break;
                case DatabaseHelper.GENERALIZED_SYMPTOM_TABLE:
                case DatabaseHelper.PREGNANCY_TABLE:
                case DatabaseHelper.CHILDHOOD_SYMPTOM_TABLE:
                    select = DatabaseHelper.EXTRA_INFORMATION;
                    break;
            }
        } else if (level == 2) {
            if (_tableName.equals(DatabaseHelper.BODY_PART_TABLE)) {
                select = DatabaseHelper.EXTRA_INFORMATION;
            }
        }
    }

    // creates a detailFragment based on passed information
    private void createDetailFragment(String _tableName,
                                      ArrayList<String> whereColumns,
                                      ArrayList<String> whereMatches) {
        // create a cursor to get the name of the symptom.
        String name = null;
        Cursor populate = db.getData(DatabaseHelper.SYMPTOM_NAME, _tableName,
                whereColumns, whereMatches);
        while (populate.moveToNext()) {
            name = (populate.getString(0));
        }

        // arraylists hold information about the name and corresponding column.
        ArrayList<String> symptomName = new ArrayList<>();
        ArrayList<String> column = new ArrayList<>();
        symptomName.add(name);
        column.add(DatabaseHelper.SYMPTOM_NAME);

        // get information about the severity of the symptom.
        String emergency = null;
        populate = db.getData(DatabaseHelper.SYMPTOM_SEVERITY,
                DatabaseHelper.DIAGNOSIS_TABLE, column, symptomName);
        while (populate.moveToNext()) {
            emergency = (populate.getString(0));
        }

        // get information about the displayed text.
        String text = null;
        populate = db.getData(DatabaseHelper.SYMPTOM_INFORMATION,
                DatabaseHelper.DIAGNOSIS_TABLE, column, symptomName);
        while (populate.moveToNext()) {
            text = (populate.getString(0));
        }

        // detailfragment needs name, emergency, and text parameters to populate.
        Fragment frag = DetailFragment.newInstance(name, emergency, text,
                resultsList, level , select, _tableName, whereColumns, whereMatches);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, frag);
        ft.commit();
    }

    // create conditions for beginning level of search -- used for back button
    private void levelZeroBackHelper() {
        // set text to beginning information.
        promptView.setText(R.string.initial_search_prompt);
        directiveView.setText(R.string.initial_search_directive);

        // reset the listview.
        final ArrayList<String> tableNames = new ArrayList<>();
        tableNames.add(getResources().getString(R.string.body_part_specific));
        tableNames.add(getResources().getString(R.string.generalized_symptoms));
        tableNames.add(getResources().getString(R.string.pregnancy_symptoms));
        tableNames.add(getResources().getString(R.string.childhood_symptoms));

        // create the new adapter and set listview to it.
        tableNameArrayAdapter = new CustomAdapter(getActivity(), tableNames);
        listView.setAdapter(tableNameArrayAdapter);

        // reset other important variables.
        tableName = "";
        select = "";
        whereMatches.clear();
        whereColumns.clear();

        // hide back button - it should not be visible on level 0.
        backButton.setVisibility(View.INVISIBLE);
    }

    // create conditions for level one of search -- used for back button
    private void levelOneBackHelper(String _tableName) {
        switch (_tableName) {
            case DatabaseHelper.BODY_PART_TABLE:
                select = DatabaseHelper.PRIMARY_AREA;
                promptView.setText(R.string.primary_symptom_location_prompt);
                directiveView.setText(R.string.primary_symptom_location_directive);
                break;
            case DatabaseHelper.GENERALIZED_SYMPTOM_TABLE:
                select = DatabaseHelper.PRIMARY_SYMPTOM;
                promptView.setText(R.string.primary_symptom_identification_prompt);
                directiveView.setText(R.string.primary_symptom_identification_directive);
                break;
            case DatabaseHelper.PREGNANCY_TABLE:
                select = DatabaseHelper.PRIMARY_SYMPTOM;
                promptView.setText(R.string.time_period_prompt);
                directiveView.setText(R.string.time_period_directive);
                break;
            case DatabaseHelper.CHILDHOOD_SYMPTOM_TABLE:
                select = DatabaseHelper.PRIMARY_AREA;
                promptView.setText(R.string.general_problem_prompt);
                directiveView.setText(R.string.general_problem_directive);
                break;
            default:
                break;
        }
    }

    // create conditions for level two of search -- used for back button
    private void levelTwoBackHelper(String _tableName) {
        switch (_tableName) {
            case DatabaseHelper.BODY_PART_TABLE:
                select = DatabaseHelper.PRIMARY_SYMPTOM;
                promptView.setText(R.string.general_problem_prompt);
                directiveView.setText(R.string.general_problem_directive);
                break;
            case DatabaseHelper.GENERALIZED_SYMPTOM_TABLE:
            case DatabaseHelper.PREGNANCY_TABLE:
            case DatabaseHelper.CHILDHOOD_SYMPTOM_TABLE:
                select = DatabaseHelper.EXTRA_INFORMATION;
                promptView.setText(R.string.extra_info_prompt);
                directiveView.setText(R.string.extra_info_directive);
                break;
            default:
                break;
        }
    }

    // create conditions for level three of search -- used for back button
    private void levelThreeBackHelper(String _tableName) {
        if (_tableName.equals(DatabaseHelper.BODY_PART_TABLE)) {
            select = DatabaseHelper.EXTRA_INFORMATION;
            promptView.setText(R.string.extra_info_prompt);
            directiveView.setText(R.string.extra_info_directive);
        }
    }

    private void searchHelper() {
        setPromptText(level, tableName);
        setSelect(level, tableName);

        cur = db.getData(select, tableName, whereColumns, whereMatches);
        resultsList.clear();
        while (cur.moveToNext()) {
            resultsList.add(cur.getString(0));
        }

        CustomAdapter resultArrayAdapter = new CustomAdapter(getActivity(), resultsList);
        listView.setAdapter(resultArrayAdapter);
    }

    private void setSearchList(boolean searchDone) {
        cur = db.getData(select, tableName, whereColumns, whereMatches);
        resultsList.clear();
        while (cur.moveToNext()) {
            resultsList.add(cur.getString(0));
        }

        if (!searchDone) {
            CustomAdapter resultArrayAdapter = new CustomAdapter(getActivity(), resultsList);
            listView.setAdapter(resultArrayAdapter);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}