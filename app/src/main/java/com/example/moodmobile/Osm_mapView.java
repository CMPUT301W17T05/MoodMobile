package com.example.moodmobile;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class Osm_mapView extends AppCompatActivity {
    private MapView         MapView;
    private MapController   MapController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        MapView = (MapView) findViewById(R.id.map);
        MapView.setTileSource(TileSourceFactory.MAPNIK);
        MapView.setBuiltInZoomControls(true);
        MapView.setMultiTouchControls(true);
        MapController = (MapController) MapView.getController();
        MapController.setZoom(9);
        GeoPoint gPt = new GeoPoint(51500000, -150000);
        MapController.setCenter(gPt);

        ArrayList<OverlayItem> overlayItemArray;
        overlayItemArray = new ArrayList<>();

        overlayItemArray.add(new OverlayItem("Starting Point", "This is the starting point", gPt));
    }

    protected void onStart() {
        super.onStart();
    }


}