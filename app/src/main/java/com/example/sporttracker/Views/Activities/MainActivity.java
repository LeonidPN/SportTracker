package com.example.sporttracker.Views.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.sporttracker.R;
import com.example.sporttracker.Services.StepManagers.StepCounterService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_ACTIVITY_RECOGNITION = 1;
    private static final int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION = 2;
    private static final int REQUEST_CODE_PERMISSION_ACCESS_COARSE_LOCATION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        int permissionStatusActivityRecognition = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION);
        if (permissionStatusActivityRecognition != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    REQUEST_CODE_PERMISSION_ACTIVITY_RECOGNITION);
        }

        int permissionStatusAccessFineLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionStatusAccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
        }

        int permissionStatusAccessCoarseLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionStatusAccessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_PERMISSION_ACCESS_COARSE_LOCATION);
        }

        startService(new Intent(this, StepCounterService.class));

        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_ACTIVITY_RECOGNITION:
                if (permissions.length == 1 &&
                        permissions[0] == Manifest.permission.ACTIVITY_RECOGNITION &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //finish();
                }
                return;
            case REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION:
                if (permissions.length == 1 &&
                        permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //finish();
                }
                return;
            case REQUEST_CODE_PERMISSION_ACCESS_COARSE_LOCATION:
                if (permissions.length == 1 &&
                        permissions[0] == Manifest.permission.ACCESS_COARSE_LOCATION &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //finish();
                }
                return;
        }
    }
}
