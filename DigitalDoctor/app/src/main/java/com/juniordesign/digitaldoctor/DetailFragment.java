package com.juniordesign.digitaldoctor;

import android.content.Intent;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Fragment class for each nav menu item
 */
public class DetailFragment extends Fragment {

    private View mContent;
    ImageView icon;
    TextView emergency;
    TextView name;
    TextView description;
    ImageButton restart;

    public static Fragment newInstance(String name, String emergency, String text) {
        Fragment frag = new DetailFragment();

        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("emergency", emergency);
        args.putString("text", text);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        String name_ = getArguments().getString("name", getResources().getString(R.string.error_name));
        String emergency_ = getArguments().getString("emergency", getResources().getString(R.string.error_emergency));
        String text_ = getArguments().getString("text", getResources().getString(R.string.error_text));

        //restart button
        restart = (ImageButton) rootView.findViewById(R.id.restart_button);
        restart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Fragment frag = SearchFragment.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, frag);
                ft.commit();
            }
        });

        icon = (ImageView) rootView.findViewById(R.id.detail_icon);
        emergency = (TextView) rootView.findViewById(R.id.detail_emergency);
        name = (TextView) rootView.findViewById(R.id.detail_name);
        description = (TextView) rootView.findViewById(R.id.detail_description);

        name.setText(name_);
        description.setText(text_);
        emergency.setText(emergency_);

        if (emergency_.equalsIgnoreCase(getResources().getString(R.string.emergency))) {
            icon.setImageResource(R.drawable.emergency_icon);
        } else if (emergency_.equalsIgnoreCase(getResources().getString(R.string.caution))) {
            icon.setImageResource(R.drawable.caution_icon);
        } else if (emergency_.equalsIgnoreCase(getResources().getString(R.string.not_an_emergency))) {
            icon.setImageResource(R.drawable.not_an_emergency);
        } else {
            icon.setImageResource(R.drawable.emergency_icon);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text and color from bundle or savedInstanceState
        mContent = view.findViewById(R.id.fragment_content_detail);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}