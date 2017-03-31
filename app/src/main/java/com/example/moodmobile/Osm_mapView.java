package com.example.moodmobile;

import org.apache.commons.lang3.ObjectUtils;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationManager;
import android.system.Os;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

public class Osm_mapView extends AppCompatActivity implements LocationListener {
    private Intent getUsernameIntent;
    private String username;
    private ArrayList<Mood> moodsList = new ArrayList<Mood>();
    private ArrayList<Account> currentAccount = new ArrayList<>();
    private ArrayList<String> followingUsernameList = new ArrayList<String>();
    private ArrayList<Mood> followingLatestMoods = new ArrayList<Mood>();
    private static final int MY_PERMISSIONS_REQUEST_FOR_EXTERNAL_STORAGE = 3;


    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    private MapView MapView;
    private MapController MapController;
    private LocationManager locationManager;
    private Location mlocation; // Location


    private int i;
    ArrayList<OverlayItem> overlayItemArray;
    Drawable markerColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        getExternalPermission();


        getUsernameIntent = getIntent();
        MapView = (MapView) findViewById(R.id.map);

        rb1 = (RadioButton) findViewById(R.id.myMood);
        rb2 = (RadioButton) findViewById(R.id.following);
        rb3 = (RadioButton) findViewById(R.id.nearby);
        rb1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showMyMood();
            }

        });
        rb2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showFollowing();
            }

        });
        rb3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showNearby();
            }

        });




    }

    protected void onStart() {
        super.onStart();
        username = getUsernameIntent.getStringExtra("username");
        Log.d("username:::", String.valueOf(username));

        MapView.setTileSource(TileSourceFactory.MAPNIK);
        MapView.setBuiltInZoomControls(true);
        MapView.setMultiTouchControls(true);
        MapController = (MapController) MapView.getController();
        MapController.setZoom(13);
        overlayItemArray = new ArrayList<OverlayItem>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
        mlocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        GeoPoint center = new GeoPoint(mlocation.getLatitude(),mlocation.getLongitude());
        MapController.animateTo(center);
        addMarker(center, "This is where you are.","origin");


    }


    public void addMarker (GeoPoint center, String title, String color){
        Marker marker = new Marker(MapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        if (color.equals("Anger")){
            markerColor = getResources().getDrawable(R.drawable.red);
        }
        else if (color.equals("Confusion")){
            markerColor = getResources().getDrawable(R.drawable.blue);
        }
        else if (color.equals("Disgust")){
            markerColor = getResources().getDrawable(R.drawable.pinkheart);
        }
        else if (color.equals("Fear")){
            markerColor = getResources().getDrawable(R.drawable.black);
        }
        else if (color.equals("Happiness")){
            markerColor = getResources().getDrawable(R.drawable.green);
        }
        else if (color.equals("Sadness")){
            markerColor = getResources().getDrawable(R.drawable.grey);
        }
        else if (color.equals("Shame")){
            markerColor = getResources().getDrawable(R.drawable.white);
        }
        else if (color.equals("Surprise")){
            markerColor = getResources().getDrawable(R.drawable.pink);
        }
        else if (color.equals("origin")){
            markerColor = getResources().getDrawable(R.drawable.origin);
        }
        marker.setIcon(markerColor);
        marker.setTitle(title);
        MapView.getOverlays().add(marker);
        MapView.invalidate();

    }


    public void showMyMood(){
        MapView.getOverlays().clear();

        GeoPoint center = new GeoPoint(mlocation.getLatitude(),mlocation.getLongitude());
        MapController.animateTo(center);
        addMarker(center, "This is where you are.","origin");


        ElasticsearchMoodController.GetMoodsTask getMoodsTask = new ElasticsearchMoodController.GetMoodsTask();
        getMoodsTask.execute(username);

        try {
            moodsList = getMoodsTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the moods out of the async object");
        }

        Log.i("AAAAA", String.valueOf(moodsList.size()));
        for(int i =0;i<moodsList.size();i++){

            Mood mood=moodsList.get(i);

            if (mood.getLocation() != null ){
                //GeoPoint marker = new GeoPoint(mood.getLatitude(), mood.getLongitude());
                //Log.i("Latitude is: ",String.valueOf(mood.getLatitude()));

                String titleTxt = mood.getUsername() + " feels " + mood.getFeeling() + " here.";
                addMarker(new GeoPoint(mood.getLatitude(), mood.getLongitude()), titleTxt, mood.getFeeling());

            }

        }
        Toast.makeText(Osm_mapView.this, "Mood size: "+String.valueOf(moodsList.size()), Toast.LENGTH_SHORT).show();

    }

    public void showFollowing(){
        MapView.getOverlays().clear();

        GeoPoint center = new GeoPoint(mlocation.getLatitude(),mlocation.getLongitude());
        MapController.animateTo(center);
        addMarker(center, "This is where you are.","origin");

        ElasticsearchAccountController.GetUser getCurrentUser = new ElasticsearchAccountController.GetUser();

        getCurrentUser.execute(username);

        try {
            currentAccount.clear();
            currentAccount.addAll(getCurrentUser.get());
            followingUsernameList = currentAccount.get(0).getFollowing();
            Toast.makeText(Osm_mapView.this, "Size of following user: "+String.valueOf(currentAccount.get(0).getFollowing().size()), Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Log.i("Error", "Failed to get the Accounts out of asyc object");
        }

        if (followingUsernameList != null && followingUsernameList.size() != 0) {
            for (i = 0; i < followingUsernameList.size(); i++) {
                ElasticsearchMoodController.GetMoodsTaskByName getLatestMood = new ElasticsearchMoodController.GetMoodsTaskByName();
                try {
                    Mood LatestMood = getLatestMood.execute(followingUsernameList.get(i)).get().get(0);//Should get latest mood
                    if (LatestMood.getLatitude() != null && LatestMood.getLongitude() != null) {
                        String titleTxt = LatestMood.getUsername() + " feels " + LatestMood.getFeeling() + " here.";
                        addMarker(new GeoPoint(LatestMood.getLatitude(), LatestMood.getLongitude()), titleTxt, LatestMood.getFeeling());

                    }


                } catch (Exception e) {

                }

            }
        }


        MapView.invalidate();

    }

    public void showNearby(){
        MapView.getOverlays().clear();
        MapView.invalidate();
        }
    @Override
    public void onLocationChanged(Location location) {
        GeoPoint center = new GeoPoint(location.getLatitude(), location.getLongitude());
        mlocation.setLatitude(location.getLatitude());
        mlocation.setLongitude(location.getLongitude());
        MapController.animateTo(center);


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void getExternalPermission() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_FOR_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode ==  MY_PERMISSIONS_REQUEST_FOR_EXTERNAL_STORAGE ) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());

            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}


