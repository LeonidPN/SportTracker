package com.example.sporttracker.Services.StepManagers;

import android.Manifest;
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
            context.startService(new Intent(context, StepCounterService.class));
        }
    }
}