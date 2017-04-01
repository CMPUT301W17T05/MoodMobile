package com.example.moodmobile;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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

import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

/** this is the MapView activity. In MoMo, OpenStreetMap is used to perform mapView functionality.
 * there are three radio buttons on the top: "MyMood", "Following", "Nearby" that allows users
 * to switch from different map modes.
 *
 * MyMood will show all the user's mood that attached with a location on mapView
 *
 * Following will show the most recent mood of all following users that attached with a
 * location on mapView.
 *
 * Nearby will show the most recent Mood of all users that within 5 kilometers of the
 * login user.
 */
public class Osm_mapView extends AppCompatActivity implements LocationListener {
    private Intent getUsernameIntent;
    private String username;
    private ArrayList<Account> currentAccount = new ArrayList<>();
    private ArrayList<String> followingUsernameList = new ArrayList<String>();
    private ArrayList<Mood> followingLatestMoods = new ArrayList<Mood>();
    private static final int MY_PERMISSIONS_REQUEST_FOR_EXTERNAL_STORAGE = 3;



    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
   
    private LocationManager locationManager;
    private Location mlocation; // Location
    private static final int MY_PERMISSIONS_REQUEST_FOR_LOCATION = 1;
    private ArrayList<Mood> moodsList = new ArrayList<>();

    private MapView         MapView;
    private MapController   MapController;

    private int i;

    /**
     * The Overlay item array.
     */
    ArrayList<OverlayItem> overlayItemArray;
    /**
     * The Marker color.
     */
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
        rb1 = (RadioButton) findViewById(R.id.myMood);
        rb2 = (RadioButton) findViewById(R.id.following);
        rb3 = (RadioButton) findViewById(R.id.nearby);
        overlayItemArray = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(Osm_mapView.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Osm_mapView.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(Osm_mapView.this, "MoMo need the permission to access your location.!", Toast.LENGTH_SHORT).show();

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Osm_mapView.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FOR_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
        mlocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        GeoPoint center = new GeoPoint(mlocation.getLatitude(), mlocation.getLongitude());
        MapController.animateTo(center);
        addMarker(center, "This is where you are.", "origin");
    }


    /**
     * Add marker.
     *
     * This methods take a GeoPoint, a description and a String that represent color,
     * and it will add a marker on the given point with the given color and description.
     * All the marker icons are stored in R.drawable.
     *
     * @param center the center
     * @param title  the title
     * @param color  the color
     */
    public void addMarker (GeoPoint center, String title, String color){
        Marker marker = new Marker(MapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        switch (color) {
            case "Anger":
                markerColor = getResources().getDrawable(R.drawable.red);
                break;
            case "Confusion":
                markerColor = getResources().getDrawable(R.drawable.blue);
                break;
            case "Disgust":
                markerColor = getResources().getDrawable(R.drawable.pinkheart);
                break;
            case "Fear":
                markerColor = getResources().getDrawable(R.drawable.black);
                break;
            case "Happiness":
                markerColor = getResources().getDrawable(R.drawable.green);
                break;
            case "Sadness":
                markerColor = getResources().getDrawable(R.drawable.grey);
                break;
            case "Shame":
                markerColor = getResources().getDrawable(R.drawable.white);
                break;
            case "Surprise":
                markerColor = getResources().getDrawable(R.drawable.pink);
                break;
            case "origin":
                markerColor = getResources().getDrawable(R.drawable.origin);
                break;
        }
        marker.setIcon(markerColor);
        marker.setTitle(title);
        MapView.getOverlays().add(marker);
        MapView.invalidate();

    }


    /**
     * showMyMood() use ElasticsearchMoodController.GetMoodsTask(username) to take all moods of the user.
     * the a for loop is used to go through all Moods, and put a marker on map if the mood is attached
     * with a location.
     */
    public void showMyMood(){
        MapView.getOverlays().clear();

        GeoPoint center = new GeoPoint(mlocation.getLatitude(), mlocation.getLongitude());
        MapController.animateTo(center);
        addMarker(center, "This is where you are.", "origin");

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
    /**
     * show Following use ElasticsearchAccountController.GetUser to find the List of users that the login
     * user is following. and use ElasticsearchMoodController.GetMoodsTaskByName to get the most recent
     * mood of all the following. If the mood has a location, then put a marker on map.
     */
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

    /**
     * Show nearby.
     */
    public void showNearby(){
        MapView.getOverlays().clear();

        GeoPoint center = new GeoPoint(mlocation.getLatitude(),mlocation.getLongitude());
        MapController.animateTo(center);
        addMarker(center, "This is where you are.","origin");

        //Todo: Make a circle


        ElasticsearchMoodController.GetNearMoodsTask getNearMoodsTask = new ElasticsearchMoodController.GetNearMoodsTask();
        getNearMoodsTask.execute(String.valueOf(mlocation.getLatitude()),String.valueOf(mlocation.getLongitude()));


        try {
            moodsList = getNearMoodsTask.get();
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






        MapView.invalidate();
    }

    /**
     * if location change is detected, change mlocation to the new location, and move mapView
     * to the new center.
     */
    @Override
    public void onLocationChanged(Location location) {
        mlocation.setLatitude(location.getLatitude());
        mlocation.setLongitude(location.getLongitude());
        GeoPoint center = new GeoPoint(location.getLatitude(), location.getLongitude());

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

    /**
     * Gets external permission.
     */
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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