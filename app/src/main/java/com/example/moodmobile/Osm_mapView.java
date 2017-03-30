package com.example.moodmobile;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class Osm_mapView extends AppCompatActivity implements LocationListener {
    private Intent getUsernameIntent;
    public String username;
    private ArrayList<Mood> moodsList = new ArrayList<>();

    private MapView         MapView;
    private MapController   MapController;
    private Location location; // Location
    private double latitude; // Latitude
    private double longitude; // Longitude
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
        overlayItemArray = new ArrayList<>();

        GeoPoint center = new GeoPoint(53.34, -113.9);
        MapController.animateTo(center);
        addMarker(center, "This is where you are.", getResources().getDrawable(R.drawable.origin));


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);




    }

    protected void onStart() {
        super.onStart();
        username = getUsernameIntent.getStringExtra("username");
        Log.d("username:::", String.valueOf(username));


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
            Log.i("Latitude is: ",String.valueOf(mood.getLatitude()));

            if (mood.getLatitude() != null && mood.getLongitude() != null ){
                //GeoPoint marker = new GeoPoint(mood.getLatitude(), mood.getLongitude());
                Log.i("Latitude is: ",String.valueOf(mood.getLatitude()));

                String titleTxt = mood.getUsername() + " feels " + mood.getFeeling() + " here.";

                if (mood.getFeeling().equals("Anger")){
                    markerColor = getResources().getDrawable(R.drawable.red);
                }
                else if (mood.getFeeling().equals("Confusion")){
                    markerColor = getResources().getDrawable(R.drawable.blue);
                }
                else if (mood.getFeeling().equals("Disgust")){
                    markerColor = getResources().getDrawable(R.drawable.pinkheart);
                }
                else if (mood.getFeeling().equals("Fear")){
                    markerColor = getResources().getDrawable(R.drawable.black);
                }
                else if (mood.getFeeling().equals("Happiness")){
                    markerColor = getResources().getDrawable(R.drawable.green);
                }
                else if (mood.getFeeling().equals("Sadness")){
                    markerColor = getResources().getDrawable(R.drawable.grey);
                }
                else if (mood.getFeeling().equals("Shame")){
                    markerColor = getResources().getDrawable(R.drawable.white);
                }
                else if (mood.getFeeling().equals("Surprise")){
                    markerColor = getResources().getDrawable(R.drawable.pink);
                }
                addMarker(new GeoPoint(mood.getLatitude(), mood.getLongitude()), titleTxt, markerColor);

            }

        }

    }


    public void addMarker (GeoPoint center, String title, Drawable icon){
        Marker marker = new Marker(MapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        marker.setIcon(icon);
        marker.setTitle(title);
        MapView.getOverlays().add(marker);
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


}