package com.juniordesign.digitaldoctor;

import android.content.Intent;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Fragment class for each nav menu item
 */
public class BookFragment extends Fragment {

    private View mContent;
    private TextView book_description;

    public static Fragment newInstance() {
        Fragment frag = new BookFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book, container, false);
        TextView link = (TextView)rootView.findViewById(R.id.link);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        link.setText(Html.fromHtml("<a href='https://www.amazon.com/Bodys-Light-Warning-" +
                "Signals-revised/dp/038534161X/ref=sr_1_2?keywords=your+body%27s+red+light+warni" +
                "ng+signals&qid=1583037974&sr=8-2'> Buy NOW on Amazon </a>"));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text and color from bundle or savedInstanceState
        mContent = view.findViewById(R.id.fragment_content_book);
        book_description = view.findViewById(R.id.description);
        book_description.setText("This potentially lifesaving guide, newly revised and updated, " +
                "gives you instant access to the information you need to spot a serious medical condition before it's too late." +
                "When is a headache just an annoyance...and when is it the symptom of a life-threatening condition? When is it crucial " +
                "to get to a doctor or an emergency room within the next few days, hours, or even minutes?\n" +
                "\n" +
                "This potentially lifesaving guide, newly revised and updated, gives you instant access to the " +
                "information you need to spot a serious medical condition before it’s too late. No matter what the symptom—wavy or " +
                "distorted vision, a child’s wheezing, a severe sore throat, or an irregular pulse—it offers up-to-date information on " +
                "possible diagnoses and invaluable advice on when you should seek medical help.\n" +
                "\n" +
                "With an essential new section on how to protect yourself from hospital errors, a new appendix listing screening tests " +
                "that may help you detect health problems even before red-light warning signals show up, special sections on pregnancy and " +
                "pediatrics, and tips on care for the elderly scattered throughout, this book is a useful resource for all. Written with " +
                "expertise by three physicians and the input of hundreds of other medical specialists, it is the next best thing to having a doctor in the house.");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}