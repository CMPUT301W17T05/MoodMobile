package com.example.moodmobile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class controlls the functionality of displaying your following list.
 * It takes in your username as an argument via an intent.
 *
 * It displays your following list, the number of people you
 * are following, and their latest mood.
 *
 */

public class FriendsActivity extends AppCompatActivity {

    private String username;

    private Account userAccount;


    private TextView NumberFriendsTextView;

    private ListView FriendsListView;
    private ArrayList<String> FriendsNames;
    private ArrayList<Mood> FriendsList;
    private CustomListAdapter FriendsListViewAdapter;

    private ArrayAdapter<String> spinAdapter;
    private String situationArray[];
    private Spinner spinnerEmotion;
    private EditText reasonText;
    private CheckBox chkDate;


    /**
     * OnCreate method called upon creation of the class
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        username = getIntent().getStringExtra("username");

        FriendsList = new ArrayList<>();
        FriendsNames = new ArrayList<>();

        NumberFriendsTextView = (TextView) findViewById(R.id.NumberFriendsTextView);

        FriendsListView = (ListView) findViewById(R.id.FriendsListView);

    }

    /**
     * When the activity is resumed, the moods are retrieved from the server, to show any changes or additions made.
     */
    protected void onResume() {
        super.onResume();
        getFriends();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Launch a dialog that allows the user to filter their moods.
        if (id == R.id.action_filter) {

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.filter_dialog);
            dialog.setTitle("Filter Moods");


            Button filterButton = (Button) dialog.findViewById(R.id.filterButton);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
            reasonText = (EditText) dialog.findViewById(R.id.reasonText);
            chkDate = (CheckBox) dialog.findViewById(R.id.weekBox);
            spinnerEmotion = (Spinner) dialog.findViewById(R.id.emotionSpinner);
            situationArray = getResources().getStringArray(R.array.filter_mood_array);
            spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, situationArray);
            spinnerEmotion.setAdapter(spinAdapter);

            // Filter the moods
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    filterMoods(chkDate.isChecked(), spinnerEmotion.getSelectedItem().toString(), reasonText.getText().toString());
                }
            });

            // Undo any filters that have been done.
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    getFriends();
                }
            });


            //TODO popup filter dialog
            dialog.show();
        }

        // Launch the add friend activity
        else if (id == R.id.action_friend){
            setResult(RESULT_OK);
            Intent newFriendIntent = new Intent(this, AddNewFriendActivity.class);
            newFriendIntent.putExtra("username", username);
            startActivity(newFriendIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method that filters the moods depending on the inputs passed to it.
     * @param dateChecked If the user wants to sort by most recent week.
     * @param emotion The emotion the user wants to sort by.
     * @param reason The reason that the moods must contain.
     */
    private void filterMoods(Boolean dateChecked, String emotion, String reason){
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date week = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        ArrayList<Mood> filteredMoodsList;
        if (IsConnected()) {
            ElasticsearchAccountController.GetUser getAccountTask = new ElasticsearchAccountController.GetUser();
            try {
                userAccount = getAccountTask.execute(username).get().get(0);
            } catch (Exception e) {
                Log.i("Error", "Could not download account details for logged in user.");

            }

            FriendsNames = userAccount.getFollowing();
            filteredMoodsList = new ArrayList<>();

            for (String FriendName : FriendsNames){
                ElasticsearchMoodController.GetMoodsTaskByName getMoodsTask = new ElasticsearchMoodController.GetMoodsTaskByName();
                getMoodsTask.execute(FriendName);
                try {
                    filteredMoodsList.add(getMoodsTask.get().get(0));
                } catch (Exception e) {

                }
            }
        }

        else{filteredMoodsList = FriendsList;}

        // Go through each mood to see if it passes the criteria.
        for (Mood mood : new ArrayList<>(filteredMoodsList)) {
            Date moodDate = mood.getDate();
            if (dateChecked && (moodDate.compareTo(week) < 0)) {
                filteredMoodsList.remove(mood);
                continue;
            }

            if (!emotion.isEmpty() && !mood.getFeeling().equals(emotion)) {
                filteredMoodsList.remove(mood);
                continue;
            }

            if (!reason.isEmpty() && !mood.getMessage().contains(reason)) {
                filteredMoodsList.remove(mood);
                continue;
            }
        }

        // Update the view.
        FriendsListViewAdapter = new CustomListAdapter(this, filteredMoodsList);
        FriendsListView.setAdapter(FriendsListViewAdapter);
        FriendsListViewAdapter.notifyDataSetChanged();
    }

    /**
     * This function checks the online connectivity on the application.
     * @return true if connected to internet and false if not.
     */
    private boolean IsConnected(){
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }


    /**
     *
     * function which downloads the users following list, and then
     * populates the listview with the followers.
     */
    private void getFriends() {
        ElasticsearchAccountController.GetUser getAccountTask = new ElasticsearchAccountController.GetUser();

        try {
            userAccount = getAccountTask.execute(username).get().get(0);
        } catch (Exception e) {
            Log.i("Error", "Could not download account details for logged in user.");

        }

        FriendsNames = userAccount.getFollowing();
        FriendsList = new ArrayList<>();

        for (String FriendName : FriendsNames){
            ElasticsearchMoodController.GetMoodsTaskByName getMoodsTask = new ElasticsearchMoodController.GetMoodsTaskByName();
            getMoodsTask.execute(FriendName);
            try {
                FriendsList.add(getMoodsTask.get().get(0));
            } catch (Exception e) {

            }
        }

        NumberFriendsTextView.setText("You are following " + FriendsNames.size() + " people");

        FriendsListViewAdapter = new CustomListAdapter(this, FriendsList);
        FriendsListView.setAdapter(FriendsListViewAdapter);
        FriendsListViewAdapter.notifyDataSetChanged();
    }
}
