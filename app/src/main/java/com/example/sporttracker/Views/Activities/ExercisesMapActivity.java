package com.example.sporttracker.Views.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sporttracker.Presenters.ExerciseMapPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.ExerciseRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExercisesMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private ExerciseMapPresenter presenter;

    private MapView mapView;

    private TextView textViewDistance;
    private TextView textViewTime;
    private TextView textViewCalories;

    private FloatingActionButton floatingActionButtonPause;
    private FloatingActionButton floatingActionButtonStart;
    private FloatingActionButton floatingActionButtonStop;

    private GoogleMap googleMap;

    private UiSettings uiSettings;

    private String exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_map);

        exercise = getIntent().getExtras().getString(getResources().getString(R.string.exercises));

        presenter = new ExerciseMapPresenter(new ExerciseRecordsRepository(this),
                new PreferencesRepository(this));
        presenter.attachView(this);

        presenter.initLocationManager();

        mapView = findViewById(R.id.mapView);

        textViewDistance = findViewById(R.id.textView_distance);
        textViewTime = findViewById(R.id.textView_time);
        textViewCalories = findViewById(R.id.textView_calories);

        floatingActionButtonPause = findViewById(R.id.floatingActionButton_pause);
        floatingActionButtonStart = findViewById(R.id.floatingActionButton_start);
        floatingActionButtonStop = findViewById(R.id.floatingActionButton_stop);

        floatingActionButtonStart.setVisibility(View.INVISIBLE);
        floatingActionButtonStart.setEnabled(false);

        floatingActionButtonStop.setVisibility(View.INVISIBLE);
        floatingActionButtonStop.setEnabled(false);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        presenter.viewIsReady();

        floatingActionButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButtonPause.setVisibility(View.INVISIBLE);
                floatingActionButtonPause.setEnabled(false);

                floatingActionButtonStart.setVisibility(View.VISIBLE);
                floatingActionButtonStart.setEnabled(true);

                floatingActionButtonStop.setVisibility(View.VISIBLE);
                floatingActionButtonStop.setEnabled(true);

                presenter.pause();
            }
        });

        floatingActionButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButtonStart.setVisibility(View.INVISIBLE);
                floatingActionButtonStart.setEnabled(false);

                floatingActionButtonStop.setVisibility(View.INVISIBLE);
                floatingActionButtonStop.setEnabled(false);

                floatingActionButtonPause.setVisibility(View.VISIBLE);
                floatingActionButtonPause.setEnabled(true);

                presenter.resume();
            }
        });

        floatingActionButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.save();
            }
        });
    }

    public void updateViews(String distance, String time, String calories) {
        textViewDistance.setText(distance);
        textViewTime.setText(time);
        textViewCalories.setText(calories);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        presenter.clearLocationManager();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(true);
        uiSettings = this.googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(true);

        presenter.resume();
    }

    public void setLocation(Location location) {
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, googleMap.getMaxZoomLevel() - 4f));
    }

    public Polyline setPolyline(PolylineOptions polylineOptions) {
        return googleMap.addPolyline(polylineOptions);
    }

    public Context getContext() {
        return this;
    }

    public String getExercise() {
        return exercise;
    }
}
