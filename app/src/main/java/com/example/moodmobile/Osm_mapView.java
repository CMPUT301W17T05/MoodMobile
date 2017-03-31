package com.example.moodmobile;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class Osm_mapView extends AppCompatActivity implements LocationListener {
    private Intent getUsernameIntent;
    private String username;
    private ArrayList<Account> currentAccount = new ArrayList<>();
    private ArrayList<String> followingUsernameList = new ArrayList<>();
    private ArrayList<Mood> followingLatestMoods = new ArrayList<>();

    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    private LocationManager locationManager;
    private Location mlocation; // Location
    private static final int MY_PERMISSIONS_REQUEST_FOR_LOCATION = 1;
    private ArrayList<Mood> moodsList = new ArrayList<>();

    private MapView         MapView;
    private MapController   MapController;
    private Location location; // Location
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;
    ArrayList<OverlayItem> overlayItemArray;
    Drawable markerColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        getUsernameIntent = getIntent();
        MapView = (MapView) findViewById(R.id.map);
        MapView.setTileSource(TileSourceFactory.MAPNIK);
        MapView.setBuiltInZoomControls(true);
        MapView.setMultiTouchControls(true);
        MapController = (MapController) MapView.getController();
        MapController.setZoom(13);
        rb1 = (RadioButton) findViewById(R.id.myMood);
        rb2 = (RadioButton) findViewById(R.id.following);
        rb3 = (RadioButton) findViewById(R.id.nearby);
        overlayItemArray = new ArrayList<>();

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

        GeoPoint center = new GeoPoint(mlocation.getLatitude(),mlocation.getLongitude());
        MapController.animateTo(center);
        addMarker(center, "This is where you are.","origin");


    }

    protected void onStart() {
        super.onStart();
        username = getUsernameIntent.getStringExtra("username");
        Log.d("username:::", String.valueOf(username));


    }


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


    public void showMyMood(){
        MapView.getOverlays().clear();

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

                switch (mood.getFeeling()) {
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
                }

            }

        }
        Toast.makeText(Osm_mapView.this, "Mood size: "+String.valueOf(moodsList.size()), Toast.LENGTH_SHORT).show();

    }

    public void showFollowing(){

        ElasticsearchAccountController.GetUser getUser = new ElasticsearchAccountController.GetUser();

        getUser.execute(username);

        try {
            currentAccount.clear();
            currentAccount.addAll(getUser.get());
            //followingUsernameList = currentAccount.get(0).getFollowing();
            Toast.makeText(Osm_mapView.this, "Size of following user: "+String.valueOf(currentAccount.get(0).getUsername()), Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Log.i("Error", "Failed to get the Accounts out of asyc object");
        }

        /*for (i = 0; i < followingUsernameList.size(); i++){
            ElasticsearchMoodController.GetMoodsTaskByName getLatestMood = new ElasticsearchMoodController.GetMoodsTaskByName();
            try {
                Mood LatestMood = getLatestMood.execute(followingUsernameList.get(i)).get().get(0);//Should get latest mood
                if (LatestMood.getLatitude() != null && LatestMood.getLongitude() != null){
                    String titleTxt = LatestMood.getUsername() + " feels " + LatestMood.getFeeling() + " here.";
                    addMarker(new GeoPoint(LatestMood.getLatitude(), LatestMood.getLongitude()), titleTxt, LatestMood.getFeeling());
                }
            } catch (Exception e) {
            }
        }
        */


        MapView.invalidate();

    }

    public void showNearby(){
        MapView.getOverlays().clear();
        MapView.invalidate();
    }
    @Override
    public void onLocationChanged(Location location) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FOR_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Osm_mapView.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Osm_mapView.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

}