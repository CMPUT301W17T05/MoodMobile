package com.example.moodmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private EditText bodyText;
    private ListView oldMoodsList;
    private ArrayList<Mood> moodList = new ArrayList<Mood>();
    private ArrayAdapter<Mood> adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Button editProfileButton = (Button) findViewById(R.id.editButton);
        Button addMoodButton = (Button) findViewById(R.id.addMood);
        Button friendsButton = (Button) findViewById(R.id.friends);
        Button mapButton = (Button) findViewById(R.id.map);
        oldMoodsList = (ListView) findViewById(R.id.oldMoodsList);

        editProfileButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
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
                //TO-DO Start Friends Activity
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                //TO-DO Start Map Activity
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        ElasticsearchMoodController.GetMoodsTask getMoodsTask = new ElasticsearchMoodController.GetMoodsTask();
        getMoodsTask.execute("");

        try {
            moodList = getMoodsTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }

        adapter = new ArrayAdapter<Mood>(this,
                R.layout.list_item, moodList);
        oldMoodsList.setAdapter(adapter);
    }
}