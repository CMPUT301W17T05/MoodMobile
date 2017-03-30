package com.example.moodmobile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import java.util.ArrayList;
import java.util.Date;

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Intent intent;
    private Spinner spinnerSituation;
    private EditText reasonText;
    private CheckBox chkDate;
    private ImageView userImage;
    private TextView userText;
    private TextView welcomeText;
    private ListView moodsListView;
    private ArrayList<Mood> moodsList = new ArrayList<Mood>();
    private CustomListAdapter adapter;
    private ArrayAdapter<String> spinAdapter;
    private String situationArray[];
    private String username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        welcomeText.setText("Welcome " + username);
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
        ElasticsearchMoodController.GetMoodsTask getMoodsTask = new ElasticsearchMoodController.GetMoodsTask();
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
            /*
                setResult(RESULT_OK);
                Intent friendsIntent = new Intent(v.getContext(), FriendsActivity.class);
                startActivity(friendsIntent);*/

        } else if (id == R.id.nav_requests) {

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

    public void filterMoods(){
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
