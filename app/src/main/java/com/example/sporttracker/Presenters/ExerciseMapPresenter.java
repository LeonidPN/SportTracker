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

import com.example.sporttracker.Models.Enumerations.Activities;
import com.example.sporttracker.Models.ExerciseRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.ExerciseRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.ExercisesMapActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ExerciseMapPresenter {

    private ExerciseRecordsRepository repository;

    private PreferencesRepository preferences;

    private ExercisesMapActivity activity;

    private LocationManager locationManager;

    private Location lastLocation;

    private boolean enabledGPS = false;
    private boolean enabledNet = false;

    private float distance;

    private Calendar time;

    private Timer timer;

    private PolylineOptions polylineOptions;

    private Polyline polyline;

    private ArrayList<LatLng> listLatLng;

    public ExerciseMapPresenter(ExerciseRecordsRepository repository,
                                PreferencesRepository preferences) {
        this.repository = repository;
        this.preferences = preferences;
        distance = 0;
        time = Calendar.getInstance();
        time.set(1970, 0, 0, 0, 0, 0);
    }

    public void attachView(ExercisesMapActivity activity) {
        this.activity = activity;
    }

    public void viewIsReady() {
        setActivityViews();
    }

    public void initLocationManager() {
        locationManager = (LocationManager) activity.getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public void setLocationManager() {
        checkEnabled();
        if (enabledGPS) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 1, locationListener);
        } else if (enabledNet) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000, 1, locationListener);
        }
    }

    public void clearLocationManager() {
        locationManager.removeUpdates(locationListener);
    }

    public void save() {
        if (distance < 10) {
            showEndExerciseLittleDistanceAlertDialog();
        } else {
            showEndExerciseAlertDialog();
        }
    }

    private void saveData() {
        ExerciseRecordModel model = new ExerciseRecordModel();
        model.setActivity(activity.getExercise());
        model.setComment("");
        model.setDate(new Date());
        model.setDistance(distance);
        model.setTime(time.getTimeInMillis());

        repository.open();
        repository.insert(model);
        repository.close();
    }

    public void pause() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        clearLocationManager();
    }

    public void resume() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        EverySecondTimerTask everySecondTimerTask = new EverySecondTimerTask();

        timer.schedule(everySecondTimerTask, 1000, 1000);

        setLocationManager();

        polylineOptions = new PolylineOptions();
        listLatLng = new ArrayList<>();
        polyline = activity.setPolyline(polylineOptions);
    }

    public void close() {
        activity.finish();
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            checkEnabled();
            if (lastLocation != null) {
                distance += lastLocation.distanceTo(location);
            } else {
                lastLocation = location;
            }
            activity.setLocation(location);
            listLatLng.add(new LatLng(location.getLatitude(), location.getLongitude()));
            polyline.setPoints(listLatLng);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            Location location = locationManager.getLastKnownLocation(provider);
            if (lastLocation != null) {
                distance += lastLocation.distanceTo(location);
            } else {
                lastLocation = location;
            }
            activity.setLocation(location);
            listLatLng.add(new LatLng(location.getLatitude(), location.getLongitude()));
            polyline.setPoints(listLatLng);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void showEndExerciseAlertDialog() {
        pause();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity.getContext());

        alertDialog.setMessage(R.string.end_exercise_question);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                saveData();
                close();
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                resume();
            }
        });

        alertDialog.create().show();
    }

    private void showEndExerciseLittleDistanceAlertDialog() {
        pause();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity.getContext());

        alertDialog.setMessage(R.string.end_exercise_little_distance_question);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                close();
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                resume();
            }
        });

        alertDialog.create().show();
    }

    private void showLocationAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity.getContext());

        alertDialog.setMessage(R.string.navigation_settings_question);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                activity.startActivity(new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                activity.finish();
            }
        });

        alertDialog.create().show();
    }

    private void checkEnabled() {
        enabledGPS = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        enabledNet = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!enabledGPS && !enabledNet) {
            showLocationAlertDialog();
        }
    }

    private class EverySecondTimerTask extends TimerTask {
        @Override
        public void run() {
            time.add(Calendar.SECOND, 1);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setActivityViews();
                }
            });
        }
    }

    private void setActivityViews() {
        String distance = String.format("%.2f", this.distance / 1000) +
                " " + getResourceString(R.string.kilometers_abbreviation);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(this.time.getTime());

        String cal;
        long duration = 0;
        duration += this.time.get(Calendar.HOUR_OF_DAY) * 60 * 60;
        duration += this.time.get(Calendar.MINUTE) * 60;
        duration += this.time.get(Calendar.SECOND);
        duration *= 1000;
        float calories = 0;
        if (duration == 0) {
            cal = String.format("%.1f", 0.0f) + " " + getResourceString(R.string.calories_abbreviation);
        } else {
            if (activity.getExercise().equals(Activities.RUN.getName())) {
                calories = Activities.RUN.getCalories(this.distance / (float) duration / 1000 * 3600)
                        * duration * Float.parseFloat(preferences.getWeight());
            }
            if (activity.getExercise().equals(Activities.CYCLE.getName())) {
                calories = Activities.CYCLE.getCalories(this.distance / (float) duration / 1000 * 3600)
                        * duration * Float.parseFloat(preferences.getWeight());
            }
            if (activity.getExercise().equals(Activities.SWIM.getName())) {
                calories = Activities.SWIM.getCalories(this.distance / (float) duration / 1000 * 3600)
                        * duration * Float.parseFloat(preferences.getWeight());
            }
            if (activity.getExercise().equals(Activities.WALK.getName())) {
                calories = Activities.WALK.getCalories(this.distance / (float) duration / 1000 * 3600)
                        * duration * Float.parseFloat(preferences.getWeight());
            }
            cal = String.format("%.1f", calories) + " " + getResourceString(R.string.calories_abbreviation);
        }

        activity.updateViews(distance, time, cal);
    }

    private String getResourceString(int id) {
        return activity.getContext().getResources().getString(id);
    }
}
