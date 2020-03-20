package com.juniordesign.digitaldoctor;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static android.content.ContentValues.TAG;


/**
 * Fragment class for each nav menu item
 */
public class BookFragment extends Fragment {

    View mContent;
    private TextView book_description;

    public static Fragment newInstance() {
        return new BookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book, container, false);
        TextView link = rootView.findViewById(R.id.link);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text and color from bundle or savedInstanceState
        mContent = view.findViewById(R.id.fragment_content_book);
        book_description = view.findViewById(R.id.description);

        // add the book description to the page
        String message;

        try {
            // reads the raw resource file and converts it to a string
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.bookdescription);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int i;
            i = in_s.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = in_s.read();
            }

            in_s.close();

            message = byteArrayOutputStream.toString();
        } catch (Exception e) {
            message = this.getResources().getString(R.string.book_info);;
        }

        book_description.setText(message);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}