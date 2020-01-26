package com.juniordesign.digitaldoctor;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Fragment class for each nav menu item
 */
public class SearchFragment extends Fragment {

    private View mContent;
    ListView listView;

    public static Fragment newInstance() {
        Fragment frag = new SearchFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_search, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text and color from bundle or savedInstanceState
        mContent = view.findViewById(R.id.fragment_content_search);


        // example listView
        listView = (ListView)view.findViewById(R.id.listView);
        ArrayList<String> arrayList = new ArrayList<String>();

        arrayList.add("this");
        arrayList.add("holds");
        arrayList.add("the");
        arrayList.add("place");
        arrayList.add("for");
        arrayList.add("symptoms");
        arrayList.add("body-part");
        arrayList.add("generalized symptoms");
        arrayList.add("pregnancy symptoms");
        arrayList.add("common childhood symptoms");
        arrayList.add("head");
        arrayList.add("eyes");
        arrayList.add("nose, ear, mouth");
        arrayList.add("arms and hand");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}