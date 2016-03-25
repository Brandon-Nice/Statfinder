package com.statfinder.statfinder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText searchText = (EditText) findViewById(R.id.searchText);
        final ListView searchList = (ListView) findViewById(R.id.searchList);
        final ArrayList<HashMap<String, Object>> searchQuestions = new ArrayList();
        final SearchAdapter searchAdapter = new SearchAdapter(searchQuestions, this);
        searchList.setAdapter(searchAdapter);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent init = new Intent(SearchActivity.this, QuestionActivityFromSearch.class);
                init.putExtra("List", searchQuestions);
                init.putExtra("CurrentQuestion", position);
                startActivity(init);
            }
        });


        final User currentUser = ((MyApplication) getApplication()).getUser();

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            hideKeyboard(v);
                            searchText.setFocusable(false);
                            searchText.setFocusableInTouchMode(false);
                            searchList.setFocusable(true);
                            searchList.setFocusableInTouchMode(true);

                            searchQuestions.clear();

                            final String search = v.getText().toString();

                            final Firebase questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + currentUser.getCountry() + "/" + currentUser.getState() + "/" + currentUser.getCity());
                            questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<String> userCategories = currentUser.getSelCat();
                                    for (DataSnapshot categories : dataSnapshot.getChildren())
                                    {
                                        if (userCategories.contains(categories.getKey()))
                                        {
                                            for (DataSnapshot idNumbers : categories.getChildren())
                                            {
                                                HashMap<String, Object> question = new HashMap();
                                                HashMap<String, String> questionInfo = new HashMap();
                                                String name = (String) ((HashMap) idNumbers.getValue()).get("Name");
                                                boolean modStatus = (boolean) ((HashMap) idNumbers.getValue()).get("Moderated");
                                                boolean modPreference = currentUser.getModPreference();
                                                int i = 0;
                                                while(i<2) { //Bug 16: displays bug twice
                                                    if (name.contains(search.replace(' ', '_'))) {
                                                        questionInfo.put("Name", name);
                                                        questionInfo.put("Category", categories.getKey());
                                                        question.put(idNumbers.getKey(), questionInfo);
                                                        if (modPreference) {
                                                            if (modStatus) {
                                                                searchQuestions.add(question);
                                                                searchAdapter.notifyDataSetChanged();
                                                            }
                                                        } else {
                                                            searchQuestions.add(question);
                                                            searchAdapter.notifyDataSetChanged();
                                                        }

                                                    }
                                                    i++;
                                                }

                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }
                        return false;
            }
        });

        searchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchList.setFocusable(false);
                searchList.setFocusableInTouchMode(false);
                searchText.setFocusable(true);
                searchText.setFocusableInTouchMode(true);
                return false;
            }
        });


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
