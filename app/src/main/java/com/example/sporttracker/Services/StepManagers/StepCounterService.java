package com.example.sporttracker.Services.StepManagers;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.sporttracker.Models.StepsRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.StepsDatabase.StepsRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.StepsStatisticActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.Predicate;

public class StepCounterService extends Service implements SensorEventListener {

    private static final int NOTIFY_ID = 101;

    private static String CHANNEL_ID = "step_counter_channel";

    private StepsRecordsRepository repository;

    private PreferencesRepository preferences;

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor sensor;

    public StepCounterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        repository = new StepsRecordsRepository(this);

        preferences = new PreferencesRepository(this);

        simpleStepDetector = new StepDetector();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            //Нет необходимых сенсоров
        }

        updateNotification(0);

        return Service.START_STICKY;
    }

    private void updateNotification(int stepCount) {
        Intent notificationIntent = new Intent(this, StepsStatisticActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String steps = "";

        if (stepCount == 0) {
            steps = getResources().getString(R.string.steps_notify_abbreviation_3);
        } else if (stepCount % 10 == 1) {
            steps = getResources().getString(R.string.steps_notify_abbreviation_1);
        } else if (stepCount % 10 == 2 || stepCount % 10 == 3 || stepCount % 10 == 4) {
            steps = getResources().getString(R.string.steps_notify_abbreviation_2);
        } else if (stepCount % 100 < 21) {
            steps = getResources().getString(R.string.steps_notify_abbreviation_3);
        } else {
            steps = getResources().getString(R.string.steps_notify_abbreviation_3);
        }

        steps = stepCount + " " + steps;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(steps)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(contentIntent)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent));

        Notification notification = builder.build();

        startForeground(NOTIFY_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        repository.open();

        ArrayList<StepsRecordModel> list = (ArrayList<StepsRecordModel>) repository.getList();

        StepsRecordModel model = new StepsRecordModel();

        int stepCount = 0;

        Calendar today = Calendar.getInstance();

        boolean flagHour = false;
        boolean flagListEmpty = false;

        int delta = 0;

        if (list.size() > 0) {
            model = list.get(list.size() - 1);
            Calendar modelTime = Calendar.getInstance();
            modelTime.setTime(model.getDate());
            if (modelTime.get(Calendar.HOUR_OF_DAY) < today.get(Calendar.HOUR_OF_DAY) ||
                    modelTime.get(Calendar.HOUR_OF_DAY) - today.get(Calendar.HOUR_OF_DAY) > 1) {
                delta = model.getCount();
                model = new StepsRecordModel();
                model.setCount(0);
                flagHour = true;
            }
            stepCount = model.getCount();
        } else {
            flagListEmpty = true;
        }

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int steps = (int) event.values[0];
            if (flagListEmpty) {
                preferences.setStepCountDelta(steps);
            }
            if (flagHour) {
                preferences.setStepCountDelta(preferences.getStepCountDelta() + delta);
            }
            if (steps < 25) {
                preferences.setStepCountDelta(0);
                stepCount = steps + stepCount;
            } else {
                stepCount = steps - preferences.getStepCountDelta();
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            stepCount += simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }

        model.setCount(stepCount);

        if (list.size() > 0) {
            if (!flagHour) {
                repository.update(model);
            } else {
                Calendar calendar = Calendar.getInstance();
                model.setHourInDay(calendar.get(Calendar.HOUR_OF_DAY));
                model.setDate(calendar.getTime());
                repository.insert(model);
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            model.setHourInDay(calendar.get(Calendar.HOUR_OF_DAY));
            model.setDate(calendar.getTime());
            repository.insert(model);
        }

        repository.close();

        list.removeIf(new Predicate<StepsRecordModel>() {
            @Override
            public boolean test(StepsRecordModel n) {
                Calendar today = Calendar.getInstance();

                Calendar recordDate = Calendar.getInstance();
                recordDate.setTime(n.getDate());

                return (today.get(Calendar.YEAR) != recordDate.get(Calendar.YEAR) ||
                        today.get(Calendar.DAY_OF_YEAR) != recordDate.get(Calendar.DAY_OF_YEAR));
            }
        });

        int count = 0;

        if (list.size() > 0) {
            for (StepsRecordModel m : list) {
                count += m.getCount();
            }
        }

        updateNotification(count);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
