package com.example.pruebamapbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.navigation.core.MapboxNavigation;
import com.mapbox.navigation.ui.NavigationView;
import com.mapbox.navigation.ui.NavigationViewOptions;
import com.mapbox.navigation.ui.OnNavigationReadyCallback;
import com.mapbox.navigation.ui.listeners.NavigationListener;
import com.mapbox.navigation.ui.map.NavigationMapboxMap;

import java.util.ArrayList;
import java.util.List;

public class Navegador extends AppCompatActivity implements OnNavigationReadyCallback, NavigationListener {

    private NavigationMapboxMap navigationMapBoxMap = null;
    private MapboxNavigation mapboxNavigation=null;
    private DirectionsRoute currentRoute;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_navegador);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this);

        currentRoute=Routes.getCurrentDirectionsRoute();

    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onStart(){
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        navigationView.onResume();
    }

    @Override
    public void onBackPressed(){
        if (!navigationView.onBackPressed())
            super.onBackPressed();
    }

    @Override
    public void onStop(){
        super.onStop();
        navigationView.onStop();
    }

    @Override
    public void onPause(){
        super.onPause();
        navigationView.onPause();
    }

    @Override
    public void onDestroy(){
        navigationView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState){
        super.onRestoreInstanceState(outState);
        navigationView.onRestoreInstanceState(outState);
    }

    @Override
    public void onNavigationReady(boolean isRunning) {
        if(!isRunning && navigationMapBoxMap==null){
            if(navigationView.retrieveNavigationMapboxMap()!=null){
                navigationMapBoxMap=navigationView.retrieveNavigationMapboxMap();
                if(navigationView.retrieveMapboxNavigation()!=null)
                    mapboxNavigation=navigationView.retrieveMapboxNavigation();
                NavigationViewOptions opts = NavigationViewOptions.builder(this)
                        .navigationListener(this)
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(true)
                        .build();
                navigationView.startNavigation(opts);
            }
        }
    }

    @Override
    public void onCancelNavigation() {
        navigationView.stopNavigation();
        finish();
    }

    @Override
    public void onNavigationFinished() {
        finish();
    }

    @Override
    public void onNavigationRunning() {

    }
}