package com.example.moodmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Intent intent;

    private ListView moodsListView;
    private ArrayList<Mood> moodsList = new ArrayList<Mood>();
    private CustomListAdapter adapter;
    private ArrayAdapter<String> spinAdapter;
    private String situationArray[];
    private Spinner spinnerSituation;
    private EditText reasonText;
    private CheckBox chkDate;
    public String username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = getIntent();

        reasonText = (EditText) findViewById(R.id.reasonText);
        chkDate = (CheckBox) findViewById(R.id.weekBox);
        spinnerSituation = (Spinner) findViewById(R.id.sitSpinner);
        situationArray = getResources().getStringArray(R.array.situation_array);
        Button editProfileButton = (Button) findViewById(R.id.editButton);
        Button addMoodButton = (Button) findViewById(R.id.addMood);
        Button friendsButton = (Button) findViewById(R.id.friends);
        Button mapButton = (Button) findViewById(R.id.map);
        spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, situationArray);
        spinnerSituation.setAdapter(spinAdapter);
        moodsListView = (ListView) findViewById(R.id.moodList);

        chkDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                filterMoods();
            }

        });

        reasonText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                filterMoods();

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                filterMoods();
            }

            public void afterTextChanged(Editable s) {
                filterMoods();
            }
        });

        spinnerSituation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterMoods();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                filterMoods();
            }

        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent profileIntent = new Intent(v.getContext(), UserProfile.class);

                profileIntent.putExtra("username", username);

                startActivity(profileIntent);
                //finish();
                //TO-DO Start Edit Profile Activity
                /*String text = bodyText.getText().toString();
                NormalTweet newTweet = new NormalTweet(text);
                tweetList.add(newTweet);
                adapter.notifyDataSetChanged();
                ElasticsearchTweetController.AddTweetsTask addTweetsTask = new ElasticsearchTweetController.AddTweetsTask();
                addTweetsTask.execute(newTweet);*/
            }
        });

/*
        friendsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent friendsIntent = new Intent(v.getContext(), FriendsActivity.class);
                startActivity(friendsIntent);
                //TO-DO Start Friends Activity
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent mapIntent = new Intent(v.getContext(), MapActivity.class);
                startActivity(mapIntent);
            }
        });*/

        /* Listener to detect a mood that has been clicked.
                *  This will also launch the ViewEditMood activity**/

        moodsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                setResult(RESULT_OK);

                if (index < moodsList.size()) {
                    Mood moodToEdit = moodsList.get(index);

                    Intent intent = new Intent(MainPageActivity.this, ViewEditMood.class);
                    intent.putExtra("moodID", moodToEdit.getId());

                    startActivityForResult(intent, 1);
                }
            }
        });

        /** Long click listener for the mood list.
                *  Will delete a long-clicked mood.**/


        moodsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                setResult(RESULT_OK);

                ElasticsearchMoodController.DeleteMoodsTask deletemood = new ElasticsearchMoodController.DeleteMoodsTask();

                deletemood.execute(moodsList.get(index));
                moodsList.remove(index);

                adapter.notifyDataSetChanged();
                /** TODO notify elasticsearch**/


                /**
                 Display a user message that the selected person was deleted
                 **/

                Context context = getApplicationContext();
                String text = "Mood Deleted";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        username = intent.getStringExtra("username");

        ElasticsearchMoodController.GetMoodsTask getMoodsTask = new ElasticsearchMoodController.GetMoodsTask();
        getMoodsTask.execute(username);

        try {
            moodsList = getMoodsTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }

        adapter = new CustomListAdapter(this, moodsList);
        moodsListView.setAdapter(adapter);
    }

    protected void onResume() {

        super.onResume();
        filterMoods();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            //TODO popup filter dialog
            return true;
        }

        else if (id == R.id.action_mood){
            setResult(RESULT_OK);
            Intent newMoodIntent = new Intent(this, AddMood.class);
            newMoodIntent.putExtra("username", username);
            startActivity(newMoodIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_moods) {
            if (this == )
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void filterMoods(){
        ArrayList<Mood> filteredMoodsList = new ArrayList<Mood>();
        ElasticsearchMoodController.GetMoodsTask getMoodsTask = new ElasticsearchMoodController.GetMoodsTask();
        getMoodsTask.execute(username);
        String reason;
        String situation;

        try {
            moodsList = getMoodsTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }

        for (Mood mood : moodsList) {
            if (chkDate.isChecked()){
                long DAY_IN_MS = 1000 * 60 * 60 * 24;
                Date week = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
                Date moodDate = mood.getDate();
                if (!(moodDate.compareTo(week) < 0)) {
                    reason = reasonText.getText().toString();
                    if (mood.getMessage().contains(reason) || reason.isEmpty()) {
                        situation = spinnerSituation.getSelectedItem().toString();
                        if (mood.getSituation() == null) {
                            continue;
                        } else if (mood.getSituation().contains(situation)) {
                            filteredMoodsList.add(mood);
                            continue;
                        }
                    }

                    else {
                        continue;
                    }
                }
                else{
                    continue;
                }
            }

            else {
                reason = reasonText.getText().toString();
                if (mood.getMessage().contains(reason)) {
                    situation = spinnerSituation.getSelectedItem().toString();
                    if (mood.getSituation() == null) {
                        continue;
                    } else if (mood.getSituation().contains(situation)) {
                        filteredMoodsList.add(mood);
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        moodsList.clear();
        adapter.clear();
        moodsList.addAll(filteredMoodsList);
        adapter = new CustomListAdapter(this, moodsList);
        moodsListView.setAdapter(adapter);
    }

}
