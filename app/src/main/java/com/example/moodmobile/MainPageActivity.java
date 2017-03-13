package com.example.moodmobile;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Date;

public class MainPageActivity extends AppCompatActivity {

    private ListView oldMoodsList;
    private ArrayList<Mood> moodsList = new ArrayList<Mood>();
    private ArrayAdapter<Mood> adapter;
    private Spinner spinnerSituation = (Spinner) findViewById(R.id.sitSpinner);
    private EditText reasonText = (EditText) findViewById(R.id.reasonText);
    private CheckBox chkDate = (CheckBox) findViewById(R.id.weekBox);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Button editProfileButton = (Button) findViewById(R.id.editButton);
        Button addMoodButton = (Button) findViewById(R.id.addMood);
        Button friendsButton = (Button) findViewById(R.id.friends);
        Button mapButton = (Button) findViewById(R.id.map);
        final ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this, R.array.situation_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSituation.setAdapter(spinAdapter);
        oldMoodsList = (ListView) findViewById(R.id.moodList);

        chkDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                filterMoods();
                adapter.notifyDataSetChanged();
            }

        });

        reasonText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                filterMoods();

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        spinnerSituation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterMoods();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent profileIntent = new Intent(v.getContext(), EditProfileActivity.class);
                startActivity(profileIntent);
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
                Intent newMoodIntent = new Intent(v.getContext(), NewMoodActivity.class);
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
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        ElasticsearchMoodController.GetMoodsTask getMoodsTask = new ElasticsearchMoodController.GetMoodsTask();
        getMoodsTask.execute("");

        try {
            moodsList = getMoodsTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }

        adapter = new ArrayAdapter<Mood>(this, R.layout.list_item, moodsList);
        oldMoodsList.setAdapter(adapter);
    }

    protected void filterMoods(){
        ElasticsearchMoodController.GetMoodsTask getMoodsTask = new ElasticsearchMoodController.GetMoodsTask();
        getMoodsTask.execute("");

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
                if (moodDate.compareTo(week) < 0){
                    moodsList.remove(mood);
                    continue;
                }
            }
            String reason = reasonText.getText().toString();
            if (!mood.getMessage().contains(reason)){
                moodsList.remove(mood);
                continue;
            }

            String situation = spinnerSituation.getSelectedItem().toString();
            if (mood.getSituation().toString() != situation){
                moodsList.remove(mood);
                continue;
            }
        }
    }
}