package com.statfinder.statfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

public class DynamicLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_layout);

        Intent init = getIntent();
        String mapsize = init.getStringExtra("size");
        String question = init.getStringExtra("question");
        String index = "";
        int size = Integer.valueOf(mapsize);

        //Set the question text
        TextView q = (TextView) findViewById(R.id.qText);
        q.setText("Question: " + question);

        //For each answer add a button
        for(int i = 0; i < size; i++) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.buttonlayout);
            ll.setOrientation(LinearLayout.VERTICAL);
            Button btn = new Button(this);
            index = "answer_" + String.valueOf(i);
            btn.setText(init.getStringExtra(index)); //set each button with the corresponding text
            //btn.setId(i);
            LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ll.addView(btn, lp);
        }





    }
}


