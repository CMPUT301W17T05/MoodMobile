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

import com.google.android.gms.maps.model.Circle;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

public class Osm_mapView extends AppCompatActivity implements LocationListener {
    private Intent getUsernameIntent;
    private String username;
    private ArrayList<Mood> moodsList = new ArrayList<Mood>();
    private ArrayList<Account> currentAccount = new ArrayList<>();
    private ArrayList<String> followingUsernameList = new ArrayList<String>();
    private ArrayList<Mood> followingLatestMoods = new ArrayList<Mood>();

    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    private MapView MapView;
    private MapController MapController;
    private LocationManager locationManager;
    private Location mlocation; // Location
    private double latitude; // Latitude
    private double longitude; // Longitude
    private static final int MY_PERMISSIONS_REQUEST_FOR_LOCATION = 1;
    private int i;
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
        overlayItemArray = new ArrayList<OverlayItem>();
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


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
        mlocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        latitude = mlocation.getLatitude();
        longitude = mlocation.getLongitude();

        GeoPoint center = new GeoPoint(latitude, longitude);
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

        GeoPoint center = new GeoPoint(latitude, longitude);
        MapController.animateTo(center);
        addMarker(center, "This is where you are.","origin");

        // Make a circle


        ElasticsearchMoodController.GetNearMoodsTask getNearMoodsTask = new ElasticsearchMoodController.GetNearMoodsTask();
        getNearMoodsTask.execute(String.valueOf(latitude),String.valueOf(longitude));


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
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        GeoPoint center = new GeoPoint(latitude, longitude);
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

