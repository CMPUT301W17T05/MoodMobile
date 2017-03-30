package com.example.moodmobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class MainPageActivity extends AppCompatActivity {
    private Intent intent;
    private static final String SYNC_FILE = "syncmood.sav";

    private ListView moodsListView;
    private ArrayList<Mood> moodsList = new ArrayList<>();
    private CustomListAdapter adapter;
    private Spinner spinnerSituation;
    private EditText reasonText;
    private CheckBox chkDate;
    public String username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        intent = getIntent();

        reasonText = (EditText) findViewById(R.id.reasonText);
        chkDate = (CheckBox) findViewById(R.id.weekBox);
        spinnerSituation = (Spinner) findViewById(R.id.sitSpinner);
        String[] situationArray = getResources().getStringArray(R.array.situation_array);
        Button editProfileButton = (Button) findViewById(R.id.editButton);
        Button addMoodButton = (Button) findViewById(R.id.addMood);
        Button friendsButton = (Button) findViewById(R.id.friends);
        Button mapButton = (Button) findViewById(R.id.map);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, situationArray);
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
                //filterMoods();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //filterMoods();
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

                if (IsConnected()) {
                    deletemood.execute(moodsList.get(index));
                } else {
                    SaveToFile(moodsList.get(index), 3);
                }
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
        //zfilterMoods();
    }
    private void filterMoods(){
        ArrayList<Mood> filteredMoodsList = new ArrayList<>();
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
                            //TODO add something?
                            continue;
                        } else if (mood.getSituation().contains(situation)) {
                            filteredMoodsList.add(mood);
                            //TODO ADD SOMETHING?
                            continue;
                        }
                    }

                    else {
                        //TODO ADD SOMETHING?
                        continue;
                    }
                }
                else{
                    //TODO ADD SOMETHING?
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

    private boolean IsConnected(){
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void SaveToFile(Mood mood, int task){
        SyncMood syncMood = new SyncMood(mood, task);
        ArrayList<SyncMood> syncList;

        try {
            FileInputStream fis = openFileInput(SYNC_FILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<SyncMood>>(){}.getType();
            syncList = gson.fromJson(in, listType);

            syncList.add(syncMood);

            FileOutputStream fos = openFileOutput(SYNC_FILE, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            gson = new Gson();
            gson.toJson(syncList, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
