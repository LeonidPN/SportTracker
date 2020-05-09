package com.example.sporttracker.Services.StepManagers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.example.sporttracker.Models.StepsRecordModel;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Services.Repositories.Databases.StepsDatabase.StepsRecordsRepository;

import java.util.ArrayList;
import java.util.Calendar;

public class StepCounterService extends Service implements SensorEventListener {

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

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        repository.open();

        ArrayList<StepsRecordModel> list = (ArrayList<StepsRecordModel>) repository.getList();

        StepsRecordModel model = new StepsRecordModel();

        int stepCount = 0;

        Calendar today = Calendar.getInstance();

        boolean flagHour = false;

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
        }

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int steps = (int) event.values[0];
            if (flagHour) {
                preferences.setStepCountDelta(delta);
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

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
