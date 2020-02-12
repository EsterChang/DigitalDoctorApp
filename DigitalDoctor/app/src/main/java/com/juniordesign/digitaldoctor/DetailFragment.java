package com.juniordesign.digitaldoctor;

import android.content.Intent;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Fragment class for each nav menu item
 */
public class DetailFragment extends Fragment {

    private View mContent;
    ImageView icon;
    TextView emergency;
    TextView name;
    TextView description;

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
        String name_ = getArguments().getString("name", "");
        String emergency_ = getArguments().getString("emergency", "");
        String text_ = getArguments().getString("text", "");

        // need casting (TextView), etc
        icon = rootView.findViewById(R.id.detail_icon);
        emergency = rootView.findViewById(R.id.detail_emergency);
        name = rootView.findViewById(R.id.detail_name);
        description = rootView.findViewById(R.id.detail_description);

        // need null checks
        name.setText(name_);
        description.setText(text_);
        emergency.setText(emergency_);

        if (emergency_.equalsIgnoreCase("emergency")) {
            icon.setImageResource(R.drawable.emergency);
        } else if (emergency_.equalsIgnoreCase("caution")) {
            icon.setImageResource(R.drawable.caution);
        } else if (emergency_.equalsIgnoreCase("not an emergency")) {
            icon.setImageResource(R.drawable.not_an_emergency);
        } else {

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