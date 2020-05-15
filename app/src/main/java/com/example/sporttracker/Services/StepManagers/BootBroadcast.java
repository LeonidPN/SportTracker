package com.example.sporttracker.Services.StepManagers;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class BootBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            if (!isServiceRunning(StepCounterService.class, context)) {
                context.startService(new Intent(context, StepCounterService.class));
            }
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
