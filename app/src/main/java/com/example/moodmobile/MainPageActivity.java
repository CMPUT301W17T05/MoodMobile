/*
package com.example.moodmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private EditText bodyText;
    private ListView oldMoodsList;
    private ArrayList<Mood> moodsList = new ArrayList<Mood>();
    private ArrayAdapter<Mood> adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Button editProfileButton = (Button) findViewById(R.id.editButton);
        Button addMoodButton = (Button) findViewById(R.id.addMood);
        Button friendsButton = (Button) findViewById(R.id.friends);
        Button mapButton = (Button) findViewById(R.id.map);
        oldMoodsList = (ListView) findViewById(R.id.moodList);

        editProfileButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent profileIntent = new Intent(this, EditProfileActivity.class);
                startActivity(profileIntent);
                //TO-DO Start Edit Profile Activity
                */
/*String text = bodyText.getText().toString();
                NormalTweet newTweet = new NormalTweet(text);
                tweetList.add(newTweet);
                adapter.notifyDataSetChanged();
                ElasticsearchTweetController.AddTweetsTask addTweetsTask = new ElasticsearchTweetController.AddTweetsTask();
                addTweetsTask.execute(newTweet);*//*

            }
        });

        */
/** Listener to detect a mood that has been clicked.
         *  This will also launch the ViewEditMood activity*//*

        oldMoodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                setResult(RESULT_OK);

                Mood moodToEdit = moodsList.get(index);

                Intent intent = new Intent(MainPageActivity.this, ViewEditMood.class);
                intent.putExtra("moodToEdit", moodToEdit);

                startActivityForResult(intent, 1);
            }
        });

        */
/** Long clock listener for the mood list.
         *  Will delete a long-clicked mood.
         *//*

        oldMoodsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                setResult(RESULT_OK);
                moodsList.remove(index);

                adapter.notifyDataSetChanged();

                */
/** TODO
                 *  NOTIFY ELASTICSEARCH THAT A MOOD HAS BEEN DELETED
                 *
                 *  Is it actually nessessary?
                 *//*




                */
/**
                 * Display a user message that the selected person was deleted
                 *//*

                Context context = getApplicationContext();
                String text = "Mood Deleted";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return false;
            }
        });

        addMoodButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent newMoodIntent = new Intent(this, NewMoodActivity.class);
                startActivity(newMoodIntent);
                //TO-DO Start New Mood Activity
                */
/*ElasticsearchTweetController.GetTweetsTask getTweetsTask = new ElasticsearchTweetController.GetTweetsTask();
                String message = bodyText.getText().toString();
                getTweetsTask.execute(message);
                try {
                    tweetList.clear();
                    tweetList.addAll(getTweetsTask.get());
                } catch (Exception e) {
                    Log.i("Error", "Failed to get the tweets out of the async object");
                }
                adapter.notifyDataSetChanged();*//*

            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent friendsIntent = new Intent(this, FriendsActivity.class);
                startActivity(friendsIntent);
                //TO-DO Start Friends Activity
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent mapIntent = new Intent(this, MapActivity.class);
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

        adapter = new ArrayAdapter<Mood>(this,
                R.layout.list_item, moodsList);
        oldMoodsList.setAdapter(adapter);
    }
}*/
