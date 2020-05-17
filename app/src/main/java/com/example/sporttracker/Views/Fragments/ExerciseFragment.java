package com.example.sporttracker.Views.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sporttracker.Models.Enumerations.Activities;
import com.example.sporttracker.Presenters.ExercisePresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.ExerciseRecordsRepository;
import com.example.sporttracker.Views.Activities.ExercisesMapActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExerciseFragment extends Fragment implements OnMapReadyCallback {

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private static final int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION = 1;

    private ExercisePresenter presenter;

    private Spinner spinnerExercise;

    private MapView mapView;

    private GoogleMap googleMap;

    private TextView textViewAllExerciseDistance;

    private FloatingActionButton floatingActionButton;

    private Context context;

    private String exercise;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);
        context = root.getContext();

        presenter = new ExercisePresenter(new ExerciseRecordsRepository(context));
        presenter.attachView(this);

        spinnerExercise = root.findViewById(R.id.spinner_exercise);

        mapView = root.findViewById(R.id.mapView);

        textViewAllExerciseDistance = root.findViewById(R.id.textView_allExerciseDistance);

        floatingActionButton = root.findViewById(R.id.floatingActionButton);

        presenter.viewIsReady();

        mapView.onCreate(savedInstanceState);

        if (checkPermissions()) {
            mapView.getMapAsync(this);

            presenter.initLocationManager();

            presenter.setLocationManager();
        }

        mapView.getMapAsync(this);

        presenter.initLocationManager();

        presenter.setLocationManager();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(ExercisesMapActivity.class, exercise);
            }
        });

        presenter.setNavController(NavHostFragment.findNavController(this));

        return root;
    }

    public void updateActivityList() {
        String[] arr = new String[]{Activities.RUN.getName(),
                Activities.WALK.getName(), Activities.CYCLE.getName()};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercise.setAdapter(adapter);
        spinnerExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exercise = parent.getItemAtPosition(position).toString();

                if (exercise.equals(Activities.WALK.getName())) {
                    textViewAllExerciseDistance.setText(presenter.getAllWalkDistance());
                }
                if (exercise.equals(Activities.RUN.getName())) {
                    textViewAllExerciseDistance.setText(presenter.getAllRunDistance());
                }
                if (exercise.equals(Activities.CYCLE.getName())) {
                    textViewAllExerciseDistance.setText(presenter.getAllCyclingDistance());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerExercise.setSelection(0);
        exercise = arr[0];
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

    @Override
    public void onResume() {
        super.onResume();

        checkPermissions();

        if (exercise.equals(Activities.WALK.getName())) {
            textViewAllExerciseDistance.setText(presenter.getAllWalkDistance());
        }
        if (exercise.equals(Activities.RUN.getName())) {
            textViewAllExerciseDistance.setText(presenter.getAllRunDistance());
        }
        if (exercise.equals(Activities.CYCLE.getName())) {
            textViewAllExerciseDistance.setText(presenter.getAllCyclingDistance());
        }

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
        UiSettings uiSettings = this.googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
    }

    public void showLocation(Location location) {
        if (location == null) {
            return;
        }
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, googleMap.getMaxZoomLevel() - 4f));
    }

    public boolean checkPermissions() {
        int permissionStatusAccessFineLocation = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatusAccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
            return false;
        } else {
            return true;
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
                            mapView.getMapAsync(this);

                            presenter.initLocationManager();

                            presenter.setLocationManager();
                        } else {
                            NavHostFragment.findNavController(this).navigate(R.id.navigation_health);
                        }
                    }
                }
                break;
        }
    }

    public Context getContext() {
        return context;
    }
}