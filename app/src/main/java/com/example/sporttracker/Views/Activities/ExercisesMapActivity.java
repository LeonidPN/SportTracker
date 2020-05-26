package com.example.sporttracker.Views.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.util.Timer;
import java.util.TimerTask;

public class ExercisesMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private static final int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION = 1;

    private ExerciseMapPresenter presenter;

    private MapView mapView;

    private ConstraintLayout constraintLayout;
    private ConstraintLayout constraintLayoutCountDown;

    private TextView textViewDistance;
    private TextView textViewTime;
    private TextView textViewCalories;
    private TextView textViewCountDown;

    private FloatingActionButton floatingActionButtonPause;
    private FloatingActionButton floatingActionButtonStart;
    private FloatingActionButton floatingActionButtonStop;

    private GoogleMap googleMap;

    private UiSettings uiSettings;

    private String exercise;

    private int countDown;

    private Timer countDownTimer;

    private ExercisesMapActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_map);

        activity = this;

        countDown = 3;

        exercise = getIntent().getExtras().getString(getResources().getString(R.string.exercises));

        presenter = new ExerciseMapPresenter(new ExerciseRecordsRepository(this),
                new PreferencesRepository(this));
        presenter.attachView(this);

        presenter.initLocationManager();

        mapView = findViewById(R.id.mapView);

        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayoutCountDown = findViewById(R.id.constraintLayout_countDown);

        textViewDistance = findViewById(R.id.textView_distance);
        textViewTime = findViewById(R.id.textView_time);
        textViewCalories = findViewById(R.id.textView_calories);
        textViewCountDown = findViewById(R.id.textView_countDown);

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

        startCountDown();
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
    }

    public void setLocation(Location location) {
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, googleMap.getMaxZoomLevel() - 4f));
    }

    public Polyline setPolyline(PolylineOptions polylineOptions) {
        return googleMap.addPolyline(polylineOptions);
    }

    public void checkPermissions() {
        int permissionStatusAccessFineLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatusAccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            finish();
                        }
                    }
                }
                break;
        }
    }

    public Context getContext() {
        return this;
    }

    public String getExercise() {
        return exercise;
    }

    private void startCountDown() {
        mapView.setVisibility(View.INVISIBLE);
        mapView.setEnabled(false);

        constraintLayout.setVisibility(View.INVISIBLE);
        constraintLayout.setEnabled(false);

        constraintLayoutCountDown.setVisibility(View.VISIBLE);
        constraintLayoutCountDown.setEnabled(true);

        countDownTimer = new Timer();

        CountDownTimerTask countDownTimerTask = new CountDownTimerTask();

        countDownTimer.schedule(countDownTimerTask, 0, 1000);
    }

    private void setTextViewCountDown() {
        switch (countDown) {
            case 3:
                textViewCountDown.setText("3");
                break;
            case 2:
                textViewCountDown.setText("2");
                break;
            case 1:
                textViewCountDown.setText("1");
                break;
            case 0:
                textViewCountDown.setText("Go!");
                break;
        }
    }

    private void startExercise() {
        countDownTimer.cancel();
        countDownTimer = null;

        mapView.setVisibility(View.VISIBLE);
        mapView.setEnabled(true);

        constraintLayout.setVisibility(View.VISIBLE);
        constraintLayout.setEnabled(true);

        constraintLayoutCountDown.setVisibility(View.INVISIBLE);
        constraintLayoutCountDown.setEnabled(false);

        presenter.resume();
    }

    private class CountDownTimerTask extends TimerTask {
        @Override
        public void run() {
            if (countDown >= 0) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTextViewCountDown();
                        countDown--;
                    }
                });
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startExercise();
                    }
                });
            }
        }
    }
}
