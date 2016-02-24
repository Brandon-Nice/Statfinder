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
        String answer = init.getStringExtra("answer");
        int size = Integer.valueOf(mapsize);

        for(int i = 0; i < size; i++) { //for each answer add a button
            LinearLayout ll = (LinearLayout) findViewById(R.id.buttonlayout);
            ll.setOrientation(LinearLayout.VERTICAL);
            Button btn = new Button(this);

//            for(int j = i; j < size; j++){ //iterate through the answer text to set each button with the corresponding text
//                String answer = init.getStringExtra("answer");
//                btn.setText(answer);
//            }
            btn.setText(answer);
            btn.setId(i);
            LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ll.addView(btn, lp);
        }



    }
}


