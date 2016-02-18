package com.statfinder.statfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Settings");

        //Gets all the checkboxes
        final CheckBox general = (CheckBox) findViewById(R.id.generalCheckBox);
        final CheckBox sports = (CheckBox) findViewById(R.id.sportsCheckBox);
        final CheckBox entertainment = (CheckBox) findViewById(R.id.entertainmentCheckBox);
        final CheckBox games = (CheckBox) findViewById(R.id.gamesCheckBox);
        final CheckBox art = (CheckBox) findViewById(R.id.artCheckBox);
        final CheckBox history= (CheckBox) findViewById(R.id.historyCheckBox);
        final CheckBox scitech = (CheckBox) findViewById(R.id.scitechCheckBox);

        User currentUser = ((MyApplication) getApplication()).getUser();

        ArrayList<String> selCat = currentUser.getSelCat();
        for(String str: selCat) {
            if(str.equals("General")) {
                general.setChecked(true);
            }
            else if(str.equals("Sports")) {
                sports.setChecked(true);
            }
            else if(str.equals("Entertainment")) {
                entertainment.setChecked(true);
            }
            else if(str.equals("Games")) {
                games.setChecked(true);
            }
            else if(str.equals("Art")) {
                art.setChecked(true);
            }
            else if(str.equals("History")) {
                history.setChecked(true);
            }
            else if(str.equals("SciTech")) {
                scitech.setChecked(true);
            }
        }

        Button applyButton = (Button) findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean atLeastOne = false;
                ArrayList<String> selCat = new ArrayList<String>();
                if(general.isChecked()) {
                    selCat.add("General");
                    atLeastOne = true;
                }
                if(sports.isChecked()) {
                    selCat.add("Sports");
                    atLeastOne = true;
                }
                if(entertainment.isChecked()) {
                    selCat.add("Entertainment");
                    atLeastOne = true;
                }
                if(games.isChecked()) {
                    selCat.add("Games");
                    atLeastOne = true;
                }
                if(art.isChecked()) {
                    selCat.add("Art");
                    atLeastOne = true;
                }
                if(history.isChecked()) {
                    selCat.add("History");
                    atLeastOne = true;
                }
                if(scitech.isChecked()) {
                    selCat.add("SciTech");
                    atLeastOne = true;
                }

                if(!atLeastOne) {
                    Toast.makeText(getApplicationContext(), "Please select at least one category.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    ((MyApplication) getApplication()).getUser().setSelCat(selCat);
                    Intent init = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(init);
                }

            }
        });
    }

}
