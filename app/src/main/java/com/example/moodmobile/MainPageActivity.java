package com.example.moodmobile;


import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

/**
 * This is the main page activity that the user sees when the log into
 * the application. From here, the user can access any of the other activities,
 * as well as filter their moods.
 */

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ImageView userImage;
    private TextView userText;
    private TextView welcomeText;
    private ListView moodsListView;
    private ArrayList<Mood> moodsList = new ArrayList<>();
    private CustomListAdapter adapter;
    private ArrayAdapter<String> spinAdapter;
    private String situationArray[];
    private Spinner spinnerEmotion;
    private EditText reasonText;
    private CheckBox chkDate;
    private String username;
    private Context context = this;


    /**
     * When the activity is first created, the various views are gathered and the user's profile is
     * pulled from the ElasticSearch server. Using this, the user's moods are displayed and their
     * profile picture is set up in the drawer along with their username.
     * @param savedInstanceState
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar for the activity.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the side menu drawer.
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Setup the navigation view.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        username = getIntent().getStringExtra("username");
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + username + "!");
        userImage = (ImageView) hView.findViewById(R.id.navImage);
        userText = (TextView) hView.findViewById(R.id.navText);

        moodsListView = (ListView) findViewById(R.id.moodList);

        // Get the user's profile picture and put it and their username into the drawer
        final ArrayList<Account> accountList = new ArrayList<>();
        ElasticsearchAccountController.GetUser getUser = new ElasticsearchAccountController.GetUser();
        getUser.execute(username);

        try {
            accountList.clear();
            accountList.addAll(getUser.get());
        } catch (Exception e) {
            Log.i("Error", "Failed to get the tweets out of asyc object");
        }

        if (accountList.get(0).getProfileImage() != null) {
            byte[] decodedString = Base64.decode(accountList.get(0).getProfileImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            userImage.setImageBitmap(decodedByte);
        }
        userText.setText(username);


        /**
         * When the user clicks either their username or their profile picture on the side menu,
         * they are taken to edit their profile.
         */
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                drawer.closeDrawer(GravityCompat.START);
                Intent profileIntent = new Intent(MainPageActivity.this, UserProfile.class);
                profileIntent.putExtra("username", username);
                startActivity(profileIntent);
            }
        });

        userText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                drawer.closeDrawer(GravityCompat.START);
                Intent profileIntent = new Intent(MainPageActivity.this, UserProfile.class);
                profileIntent.putExtra("username", username);
                startActivity(profileIntent);
            }

        });

        /**
         * When the user clicks on a mood, they are taken to edit that mood event.
         */
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
         *  Will delete a long-clicked mood.
         */


        moodsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                setResult(RESULT_OK);

                Gson gson = new Gson();
                String json = gson.toJson(moodsList.get(index));
                PersistableBundle bundle = new PersistableBundle();
                bundle.putString("mood", json);
                int jobid = (int) System.currentTimeMillis();
                JobInfo job = new JobInfo.Builder(jobid, new ComponentName(context, DeleteJobService.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPersisted(true)
                        .setExtras(bundle)
                        .build();
                JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                scheduler.schedule(job);


                /**
                 Display a user message that the selected person was deleted
                 */

                Context context = getApplicationContext();
                String text = "Mood Deleted";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                moodsList.remove(index);
                adapter = new CustomListAdapter(MainPageActivity.this , moodsList);
                moodsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                return false;
            }
        });

    }

    /**
     * When the activity is resumed, the moods are retrieved from the server, to show any changes or additions made.
     */
    protected void onResume() {
        super.onResume();
        updateList();

    }


    /**
     * When the back button is pushed, a dialog is presented to confirm the
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("Are you sure you want to close this activity?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
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
                    updateList();
                }
            });


            //TODO popup filter dialog
            dialog.show();
        }

        // Launch the add mood activity
        else if (id == R.id.action_mood){
            setResult(RESULT_OK);
            Intent newMoodIntent = new Intent(this, AddMood.class);
            newMoodIntent.putExtra("username", username);
            startActivity(newMoodIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Switch used to determine which activity to go to.
     * @param item The activity selected.
     * @return the result of the item being selected
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case(R.id.nav_moods):
                setResult(RESULT_OK);
                Intent mainIntent = new Intent(this, MainPageActivity.class);
                mainIntent.putExtra("username", username);
                startActivity(mainIntent);
                finish();
                break;

            case(R.id.nav_following):
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, 1);
                break;

            case(R.id.nav_requests):
                setResult(RESULT_OK);
                Intent friendsIntent = new Intent(this, AddNewFriendActivity.class);
                friendsIntent.putExtra("username", username);
                startActivityForResult(friendsIntent, 1);
                break;

            case(R.id.nav_map):
                setResult(RESULT_OK);
                Intent mapIntent = new Intent(this, Osm_mapView.class);
                mapIntent.putExtra("username", username);
                startActivity(mapIntent);
                break;

            case(R.id.nav_logout):
                setResult(RESULT_OK);
                Intent loginIntent = new Intent(this, LoginPage.class);
                startActivity(loginIntent);
                finish();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        ArrayList<Mood> filteredMoodsList = new ArrayList<>();
        if (IsConnected()) {
            ElasticsearchMoodController.GetMoodsTaskByName getMoodsTask = new ElasticsearchMoodController.GetMoodsTaskByName();
            getMoodsTask.execute(username);
            try {
                filteredMoodsList = getMoodsTask.get();
            } catch (Exception e) {
                Log.i("Error", "Failed to get the moods out of the async object");
            }
        }

        else{filteredMoodsList = moodsList;}

        // Go through each mood to see if it passes the criteria.
        for (Mood mood : new ArrayList<Mood>(filteredMoodsList)) {
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
        adapter = new CustomListAdapter(this, filteredMoodsList);
        moodsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
     * Method that tries to get the most recent moods form the ElasticSearch server and display them.
     */
    private void updateList(){
        ElasticsearchMoodController.GetMoodsTaskByName getMoodsTask = new ElasticsearchMoodController.GetMoodsTaskByName();
        getMoodsTask.execute(username);

        try {
            moodsList = getMoodsTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }
        adapter = new CustomListAdapter(this, moodsList);
        moodsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
