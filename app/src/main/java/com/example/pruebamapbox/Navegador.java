package com.example.pruebamapbox;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.navigation.base.trip.model.RouteLegProgress;
import com.mapbox.navigation.core.MapboxNavigation;
import com.mapbox.navigation.core.arrival.ArrivalController;
import com.mapbox.navigation.core.arrival.ArrivalOptions;
import com.mapbox.navigation.ui.NavigationView;
import com.mapbox.navigation.ui.NavigationViewOptions;
import com.mapbox.navigation.ui.OnNavigationReadyCallback;
import com.mapbox.navigation.ui.listeners.NavigationListener;
import com.mapbox.navigation.ui.map.NavigationMapboxMap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Navegador extends AppCompatActivity
        implements OnNavigationReadyCallback, NavigationListener, SensorEventListener {

    private NavigationMapboxMap navigationMapBoxMap = null;
    private MapboxNavigation mapboxNavigation=null;
    private DirectionsRoute currentRoute;
    private NavigationView navigationView;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastZ;

    private boolean showingDialog;
    private AlertDialog currentDialog;

    private ArrivalController arrivalController = new ArrivalController() {
        @NotNull
        @Override
        public ArrivalOptions arrivalOptions() {
            // Cuando queden menos de 5 segundos para llegar se llamará a navigatenext route leg
            return new ArrivalOptions.Builder().arrivalInSeconds(5.0).build();
        }

        @Override
        public boolean navigateNextRouteLeg(@NotNull RouteLegProgress routeLegProgress) {
            if(!Navegador.this.showingDialog) {
                Log.d("Franprueba", "Franprueba1");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Navegador.this);
                builder1.setMessage("Has llegado a X, ¿ qué te gustaría hacer?");
                builder1.setCancelable(true);
                /*
                builder1.setPositiveButton(
                        "Enséñame más",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Quiero continuar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mapboxNavigation.navigateNextRouteLeg();
                                dialog.cancel();
                            }
                        });

                 */

                currentDialog = builder1.create();
                currentDialog.show();
                showingDialog = true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_navegador);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this);

        currentRoute=Routes.getCurrentDirectionsRoute();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e("f","f");
            // fai! we dont have an accelerometer!
        }

        lastZ=0;
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
                NavigationViewOptions opts = NavigationViewOptions.builder(this)
                        .navigationListener(this)
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(true)
                        .build();
                navigationView.startNavigation(opts);
                if(navigationView.retrieveMapboxNavigation()!=null) {
                    mapboxNavigation = navigationView.retrieveMapboxNavigation();
                    mapboxNavigation.setArrivalController(arrivalController);
                }
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        float deltaZ=lastZ-event.values[2];
        if(showingDialog){
            if(deltaZ<-12.0f){
                //mapboxNavigation.navigateNextRouteLeg();
                currentDialog.cancel();
                showingDialog=false;
                Toast.makeText(this, "Hacia arriba", Toast.LENGTH_LONG).show();
            }
            else if(deltaZ>12.0f){
                currentDialog.cancel();
                showingDialog=false;
                Toast.makeText(this, "Hacia abajo", Toast.LENGTH_LONG).show();
            }
        }
        lastZ=event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}