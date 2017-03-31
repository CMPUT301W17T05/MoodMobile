package com.example.moodmobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class MainPageActivity extends AppCompatActivity {
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

    private static final int MY_PERMISSIONS_REQUEST_FOR_FINE_LOCATION = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        getLocationPermission();

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

        addMoodButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent newMoodIntent = new Intent(v.getContext(), AddMood.class);
                newMoodIntent.putExtra("username", username);
                startActivity(newMoodIntent);
                //TO-DO Start New Mood Activity
                /*ElasticsearchTweetController.GetTweetsTask getTweetsTask = new ElasticsearchTweetController.GetTweetsTask();
                String message = bodyText.getText().toString();
                getTweetsTask.execute(message);
                try {
                    tweetList.clear();
                    tweetList.addAll(getTweetsTask.get());
                } catch (Exception e) {
                    Log.i("Error", "Failed to get the tweets out of the async object");
                }
                adapter.notifyDataSetChanged();*/
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
        });*/

        mapButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent mapIntent = new Intent(v.getContext(), Osm_mapView.class);

                mapIntent.putExtra("username", username);

                startActivity(mapIntent);
            }
        });

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


    public void getLocationPermission() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FOR_FINE_LOCATION);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == MY_PERMISSIONS_REQUEST_FOR_FINE_LOCATION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
