package com.example.moodmobile;

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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

public class Osm_mapView extends AppCompatActivity implements LocationListener {
    private MapView         MapView;
    private MapController   MapController;
    private  LocationManager locationManager;
    private Location location; // Location
    private double latitude; // Latitude
    private double longitude; // Longitude
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    ArrayList<OverlayItem> overlayItemArray;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        MapView = (MapView) findViewById(R.id.map);
        MapView.setTileSource(TileSourceFactory.MAPNIK);
        MapView.setBuiltInZoomControls(true);
        MapView.setMultiTouchControls(true);
        MapController = (MapController) MapView.getController();
        MapController.setZoom(13);
        overlayItemArray = new ArrayList<OverlayItem>();

        GeoPoint center = new GeoPoint(53.34, -113.9);
        MapController.animateTo(center);
        addMarker(center);



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);


    }

    protected void onStart() {
        super.onStart();
    }


    public void addMarker (GeoPoint center){
        Marker marker = new Marker(MapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_dark));

        MapView.getOverlays().clear();
        MapView.getOverlays().add(marker);
        MapView.invalidate();

    }

    @Override
    public void onLocationChanged(Location location) {
        GeoPoint center = new GeoPoint(location.getLatitude(), location.getLongitude());

        MapController.animateTo(center);
        addMarker(center);


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