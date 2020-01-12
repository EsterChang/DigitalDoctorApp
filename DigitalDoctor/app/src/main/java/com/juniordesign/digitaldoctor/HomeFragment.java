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


/**
 * Fragment class for each nav menu item
 */
public class HomeFragment extends Fragment {

    private View mContent;
    ImageButton settings;

    public static Fragment newInstance() {
        Fragment frag = new HomeFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        settings = (ImageButton) rootView.findViewById(R.id.settings_button);
        settings.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent settings = new Intent(HomeFragment.this.getActivity(), SettingsActivity.class);
                startActivity(settings);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text and color from bundle or savedInstanceState
        mContent = view.findViewById(R.id.fragment_content_home);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
