package com.example.moodmobile;


import android.Manifest;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

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
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;

/**
 * This is the main page activity that the user sees when the log into
 * the application.
 */

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Intent intent;
    private static final String SYNC_FILE = "syncmood.sav";
    private ImageView userImage;
    private TextView userText;
    private TextView welcomeText;
    private ListView moodsListView;
    private ArrayList<Mood> moodsList = new ArrayList<>();
    private CustomListAdapter adapter;
    private ArrayAdapter<String> spinAdapter;
    private String situationArray[];
    private Spinner spinnerSituation;
    private EditText reasonText;
    private CheckBox chkDate;
    private String username;
    private Context context = this;

    private static final int MY_PERMISSIONS_REQUEST_FOR_FINE_LOCATION = 1;


    /**
     * When the
     * @param savedInstanceState
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocationPermission();

        intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        username = getIntent().getStringExtra("username");
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + username + "!");
        userImage = (ImageView) hView.findViewById(R.id.navImage);
        userText = (TextView) hView.findViewById(R.id.navText);

        moodsListView = (ListView) findViewById(R.id.moodList);


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

                moodsList.remove(index);

                adapter.notifyDataSetChanged();
                /** TODO notify elasticsearch
                 */


                /**
                 Display a user message that the selected person was deleted
                 */

                Context context = getApplicationContext();
                String text = "Mood Deleted";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                adapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    protected void onResume() {
        super.onResume();
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

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.filter_dialog);
            dialog.setTitle("Filter Moods");

            Button filterButton = (Button) dialog.findViewById(R.id.filterButton);
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
            reasonText = (EditText) dialog.findViewById(R.id.reasonText);
            chkDate = (CheckBox) dialog.findViewById(R.id.weekBox);
            spinnerSituation = (Spinner) dialog.findViewById(R.id.sitSpinner);
            situationArray = getResources().getStringArray(R.array.situation_array);
            spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, situationArray);
            spinnerSituation.setAdapter(spinAdapter);

            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    filterMoods();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            //TODO popup filter dialog
            dialog.show();
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
            if (this.getClass() != MainPageActivity.class){
                setResult(RESULT_OK);
                Intent mainIntent = new Intent(this, MainPageActivity.class);
                mainIntent.putExtra("username", username);
                startActivity(mainIntent);
            }
        } else if (id == R.id.nav_following) {
            Intent intent = new Intent(this, FriendsActivity.class);
            intent.putExtra("username", username);

            startActivityForResult(intent, 1);



        } else if (id == R.id.nav_requests) {
            setResult(RESULT_OK);
            Intent friendsIntent = new Intent(this, AddNewFriendActivity.class);
            friendsIntent.putExtra("username", username);
            startActivityForResult(friendsIntent, 1);

        } else if (id == R.id.nav_map) {
            setResult(RESULT_OK);
            Intent mapIntent = new Intent(this, Osm_mapView.class);

            mapIntent.putExtra("username", username);

            startActivity(mapIntent);

        } else if (id == R.id.nav_logout) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void filterMoods(){
        ArrayList<Mood> filteredMoodsList = new ArrayList<>();
        ElasticsearchMoodController.GetMoodsTaskByName getMoodsTask = new ElasticsearchMoodController.GetMoodsTaskByName();
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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

    private boolean IsConnected(){
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    private void SaveToFile(Mood mood){
        SyncMood syncMood = new SyncMood(mood, 3);
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
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
