package com.example.sporttracker.Presenters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.navigation.NavController;

import com.example.sporttracker.Models.Enumerations.Activities;
import com.example.sporttracker.Models.ExerciseRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.ExerciseRecordsRepository;
import com.example.sporttracker.Views.Fragments.ExerciseFragment;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ExercisePresenter {

    private ExerciseRecordsRepository repository;

    private ExerciseFragment fragment;

    private NavController navController;

    private LocationManager locationManager;

    private boolean enabledGPS = false;
    private boolean enabledNet = false;

    public ExercisePresenter(ExerciseRecordsRepository repository) {
        this.repository = repository;
    }

    public void attachView(ExerciseFragment fragment) {
        this.fragment = fragment;
    }

    public void viewIsReady() {
        fragment.updateActivityList();
    }

    public String getAllWalkDistance() {
        repository.open();
        ArrayList<ExerciseRecordModel> list = (ArrayList) repository.getList();
        repository.close();

        list.removeIf(new Predicate<ExerciseRecordModel>() {
            @Override
            public boolean test(ExerciseRecordModel n) {
                return (!n.getActivity().equals(Activities.WALK.getName()));
            }
        });

        float distance = 0;

        for (ExerciseRecordModel model : list) {
            distance += model.getDistance();
        }

        distance /= 1000;

        return String.format("%.2f", distance) + " " + getResourceString(R.string.kilometers_abbreviation);
    }

    public String getAllRunDistance() {
        repository.open();
        ArrayList<ExerciseRecordModel> list = (ArrayList) repository.getList();
        repository.close();

        list.removeIf(new Predicate<ExerciseRecordModel>() {
            @Override
            public boolean test(ExerciseRecordModel n) {
                return (!n.getActivity().equals(Activities.RUN.getName()));
            }
        });

        float distance = 0;

        for (ExerciseRecordModel model : list) {
            distance += model.getDistance();
        }

        distance /= 1000;

        return String.format("%.2f", distance) + " " + getResourceString(R.string.kilometers_abbreviation);
    }

    public String getAllCyclingDistance() {
        repository.open();
        ArrayList<ExerciseRecordModel> list = (ArrayList) repository.getList();
        repository.close();

        list.removeIf(new Predicate<ExerciseRecordModel>() {
            @Override
            public boolean test(ExerciseRecordModel n) {
                return (!n.getActivity().equals(Activities.CYCLE.getName()));
            }
        });

        float distance = 0;

        for (ExerciseRecordModel model : list) {
            distance += model.getDistance();
        }

        distance /= 1000;

        return String.format("%.2f", distance) + " " + getResourceString(R.string.kilometers_abbreviation);
    }

    public void startActivity(Class<?> T, String exercise) {
        Intent intent = new Intent(fragment.getContext(), T);
        intent.putExtra(getResourceString(R.string.exercises), exercise);
        fragment.getContext().startActivity(intent);
    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }

    public void initLocationManager() {
        locationManager = (LocationManager) fragment.getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public void setLocationManager() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 20, 2, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 20, 2,
                locationListener);
    }

    public void clearLocationManager() {
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            checkEnabled();
            fragment.showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            fragment.showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public void showLocationAllertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(fragment.getContext());

        alertDialog.setMessage(R.string.navigation_settings_question);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                fragment.startActivity(new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                navController.navigate(R.id.navigation_health);
            }
        });

        alertDialog.create().show();
    }

    public void checkEnabled() {
        enabledGPS = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        enabledNet = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!enabledGPS && !enabledNet) {
            showLocationAllertDialog();
        }
    }

    private String getResourceString(int id) {
        return fragment.getContext().getResources().getString(id);
    }

}
