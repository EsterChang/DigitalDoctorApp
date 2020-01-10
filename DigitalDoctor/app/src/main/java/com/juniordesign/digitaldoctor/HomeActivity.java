package com.juniordesign.digitaldoctor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;


public class HomeActivity extends AppCompatActivity {
    BottomNavigationView mBottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBottomNav = findViewById(R.id.navigationView);
//        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment = null;
//                switch (item.getItemId()) {
//                    case R.id.navigation_home:
//                        // Switch to page one
//                        break;
//                    case R.id.navigation_search:
//                        // Switch to page two
//                        break;
//                    case R.id.navigation_book:
//                        // Switch to page three
//                        break;
//                }
//                return true;
//            }
//        });
        mBottomNav.getMenu().findItem(R.id.navigation_home).setChecked(true);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

      //uncomment to make easy edits to first load
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", true);
        editor.apply();

        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showTermsDialog();
        }
    }

    private void showTermsDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Terms and Conditions")
            .setMessage("These terms and conditions (\"Terms\", \"Agreement\") are an " +
                    "agreement between Mobile Application Developer (\"Mobile Application " +
                    "Developer\", \"us\", \"we\" or \"our\") and you (\"User\", \"you\" or " +
                    "\"your\"). This Agreement sets forth the general terms and conditions of " +
                    "your use of the Digital Doctor mobile application and any of its products " +
                    "or services (collectively, \"Mobile Application\" or \"Services\")." + "\n" +
                    "\n" +  "Age requirement\n" + "\n" + "You must be at least 18 years of age " +
                    "to use this Mobile Application. By using this Mobile Application and by " +
                    "agreeing to this Agreement you warrant and represent that you are at least " +
                    "18 years of age.\n" + "\n" + "Adult content\n" + "\n" + "Please be aware " +
                    "that there may be certain adult or mature content available in the Mobile " +
                    "Application. Where there is mature or adult content, individuals who are " +
                    "less than 18 years of age or are not permitted to access such content " +
                    "under the laws of any applicable jurisdiction may not access such content. " +
                    "If we learn that anyone under the age of 18 seeks to conduct a transaction " +
                    "through the Services, we will require verified parental consent, in " +
                    "accordance with the Children's Online Privacy Protection Act of 1998 " +
                    "(\"COPPA\"). Certain areas of the Mobile Application may not be available " +
                    "to children under 18 under any circumstances.\n" + "\n" + "Backups\n" +
                    "\n" + "We are not responsible for Content residing in the Mobile Application. " +
                    "In no event shall we be held liable for any loss of any Content. It is your " +
                    "sole responsibility to maintain appropriate backup of your Content. " +
                    "Notwithstanding the foregoing, on some occasions and in certain " +
                    "circumstances, with absolutely no obligation, we may be able to restore " +
                    "some or all of your data that has been deleted as of a certain date and " +
                    "time when we may have backed up data for our own purposes. We make no " +
                    "guarantee that the data you need will be available.\n" + "\n" + "Prohibited" +
                    " uses\n" + "\n" + "In addition to other terms as set forth in the " +
                    "Agreement, you are prohibited from using the Mobile Application or " +
                    "its Content: (a) for any unlawful purpose; (b) to solicit others to perform " +
                    "or participate in any unlawful acts; (c) to violate any international, " +
                    "federal, provincial or state regulations, rules, laws, or local ordinances; " +
                    "(d) to infringe upon or violate our intellectual property rights or the " +
                    "intellectual property rights of others; (e) to harass, abuse, insult, harm, " +
                    "defame, slander, disparage, intimidate, or discriminate based on gender, " +
                    "sexual orientation, religion, ethnicity, race, age, national origin, or " +
                    "disability; (f) to submit false or misleading information; (g) to upload or " +
                    "transmit viruses or any other type of malicious code that will or may be " +
                    "used in any way that will affect the functionality or operation of the " +
                    "Service or of any related mobile application, other mobile applications, or " +
                    "the Internet; (h) to collect or track the personal information of others; " +
                    "(i) to spam, phish, pharm, pretext, spider, crawl, or scrape; (j) for any " +
                    "obscene or immoral purpose; or (k) to interfere with or circumvent the " +
                    "security features of the Service or any related mobile application, other " +
                    "mobile applications, or the Internet. We reserve the right to terminate your " +
                    "use of the Service or any related mobile application for violating any of " +
                    "the prohibited uses.\n" + "\n" + "Intellectual property rights\n" + "\n" +
                    "This Agreement does not transfer to you any intellectual property owned by " +
                    "Mobile Application Developer or third-parties, and all rights, titles, and " +
                    "interests in and to such property will remain (as between the parties) " +
                    "solely with Mobile Application Developer. All trademarks, service marks, " +
                    "graphics and logos used in connection with our Mobile Application or " +
                    "Services, are trademarks or registered trademarks of Mobile Application " +
                    "Developer or Mobile Application Developer licensors. Other trademarks, " +
                    "service marks, graphics and logos used in connection with our Mobile " +
                    "Application or Services may be the trademarks of other third-parties. Your " +
                    "use of our Mobile Application and Services grants you no right or license to " +
                    "reproduce or otherwise use any Mobile Application Developer or third-party " +
                    "trademarks.\n" + "\n" + "Limitation of liability\n" + "\n" + "To the " +
                    "fullest extent permitted by applicable law, in no event will Mobile " +
                    "Application Developer, its affiliates, officers, directors, employees, " +
                    "agents, suppliers or licensors be liable to any person for (a): any " +
                    "indirect, incidental, special, punitive, cover or consequential damages " +
                    "(including, without limitation, damages for lost profits, revenue, sales, " +
                    "goodwill, use of content, impact on business, business interruption, loss " +
                    "of anticipated savings, loss of business opportunity) however caused, under " +
                    "any theory of liability, including, without limitation, contract, tort, " +
                    "warranty, breach of statutory duty, negligence or otherwise, even if Mobile " +
                    "Application Developer has been advised as to the possibility of such damages " +
                    "or could have foreseen such damages. To the maximum extent permitted by " +
                    "applicable law, the aggregate liability of Mobile Application Developer " +
                    "and its affiliates, officers, employees, agents, suppliers and licensors, " +
                    "relating to the services will be limited to an amount greater of one dollar " +
                    "or any amounts actually paid in cash by you to Mobile Application Developer " +
                    "for the prior one month period prior to the first event or occurrence giving " +
                    "rise to such liability. The limitations and exclusions also apply if this " +
                    "remedy does not fully compensate you for any losses or fails of its essential " +
                    "purpose.\n" + "\n" + "Indemnification\n" + "\n" + "You agree to indemnify " +
                    "and hold Mobile Application Developer and its affiliates, directors, " +
                    "officers, employees, and agents harmless from and against any liabilities, " +
                    "losses, damages or costs, including reasonable attorneys' fees, incurred " +
                    "in connection with or arising from any third-party allegations, claims, " +
                    "actions, disputes, or demands asserted against any of them as a result of " +
                    "or relating to your Content, your use of the Mobile Application or Services " +
                    "or any willful misconduct on your part.\n" + "\n" + "Severability\n" + "\n" +
                    "All rights and restrictions contained in this Agreement may be exercised " +
                    "and shall be applicable and binding only to the extent that they do not " +
                    "violate any applicable laws and are intended to be limited to the extent " +
                    "necessary so that they will not render this Agreement illegal, invalid or " +
                    "unenforceable. If any provision or portion of any provision of this Agreement" +
                    " shall be held to be illegal, invalid or unenforceable by a court of competent" +
                    " jurisdiction, it is the intention of the parties that the remaining provisions" +
                    " or portions thereof shall constitute their agreement with respect to the" +
                    " subject matter hereof, and all such remaining provisions or portions thereof" +
                    " shall remain in full force and effect.\n" + "\n" + "Dispute resolution\n" +
                    "\n" + "The formation, interpretation, and performance of this Agreement " +
                    "and any disputes arising out of it shall be governed by the substantive " +
                    "and procedural laws of Georgia, United States without regard to its rules " +
                    "on conflicts or choice of law and, to the extent applicable, the laws of " +
                    "United States. The exclusive jurisdiction and venue for actions related to " +
                    "the subject matter hereof shall be the state and federal courts located in " +
                    "Georgia, United States, and you hereby submit to the personal jurisdiction " +
                    "of such courts. You hereby waive any right to a jury trial in any " +
                    "proceeding arising out of or related to this Agreement. The United Nations " +
                    "Convention on Contracts for the International Sale of Goods does not apply " +
                    "to this Agreement.\n" + "\n" + "Changes and amendments\n" + "\n" + "We reserve" +
                    " the right to modify this Agreement or its policies relating to the Mobile" +
                    " Application or Services at any time, effective upon posting of an updated" +
                    " version of this Agreement in the Mobile Application. When we do, we will" +
                    " revise the updated date at the bottom of this page. Continued use of the" +
                    " Mobile Application after any such changes shall constitute your consent to" +
                    " such changes. Policy was created with https://www.WebsitePolicies.com\n" +
                    "\n" + "Acceptance of these terms\n" + "\n" + "You acknowledge that you have " +
                    "read this Agreement and agree to all its terms and conditions. By using " +
                    "the Mobile Application or its Services you agree to be bound by this " +
                    "Agreement. If you do not agree to abide by the terms of this Agreement, " +
                    "you are not authorized to use or access the Mobile Application and its " +
                    "Services.\n" + "\n" + "Contacting us\n" + "\n" + "If you would like to " +
                    "contact us to understand more about this Agreement or wish to contact us " +
                    "concerning any matter relating to it, you may send an email to " +
                    "gtteam9303@gmail.com\n" + "\n" + "This document was last updated on " +
                    "November 29, 2019")
            .setPositiveButton("I agree", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    showLiabilityDialog();
                }
            })
            .setCancelable(false)
            .create()
            .show();
    }

    private void showLiabilityDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Liability Warning")
                .setMessage("All content found on Digital Doctor is created for informational " +
                        "purposes only and is not intended to be a substitute for a medical " +
                        "professional. Always seek advice from a qualified health provider with any " +
                        "questions you may have regarding a medical condition. Never disregard " +
                        "professional medical advice or delay in seeking it because of something " +
                        "you have read on this application.\n" + "\n" +
                        "If you think you may have a medical emergency, call your doctor, go to " +
                        "the emergency department, or call 911 immediately. Digital Doctor does " +
                        "not recommend or endorse any specific tests, physicians, products, " +
                        "procedures, opinions, or other information that may be mentioned on " +
                        "this application. Reliance on any information provided by Digital Doctor" +
                        " is solely at your own risk.\n" + "\n" +
                        "The app may contain health- or medical-related materials or " +
                        "discussions regarding sexually explicit disease states. If you find" +
                        " these materials offensive, you may not want to use our app. The app" +
                        " and its content is provided on an \"as is\" basis.")
                .setPositiveButton("I agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    public void onSettingsButtonPressed(View view) {
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
    }

}
