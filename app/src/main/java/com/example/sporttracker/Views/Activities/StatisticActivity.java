package com.example.sporttracker.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.sporttracker.Models.Enumerations.Activities;
import com.example.sporttracker.Presenters.StatisticPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.ExerciseRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Adapters.StatisticFragmentPagerAdapter;
import com.example.sporttracker.Views.Fragments.StatisticFragment;
import com.google.android.material.tabs.TabLayout;

public class StatisticActivity extends AppCompatActivity {

    private StatisticPresenter presenter;

    private ImageButton imageButtonBack;

    private Spinner spinnerExercise;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private String exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        presenter = new StatisticPresenter(new ExerciseRecordsRepository(this),
                new PreferencesRepository(this));
        presenter.attachView(this);

        imageButtonBack = findViewById(R.id.imageButton_back);
        spinnerExercise = findViewById(R.id.spinner_exercise);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        presenter.viewIsReady();

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.close();
            }
        });

        StatisticFragmentPagerAdapter adapter = new StatisticFragmentPagerAdapter(getSupportFragmentManager());
        presenter.updateRecordsList();
        adapter.addFragment(new StatisticFragment(R.string.week, presenter), getString(R.string.week));
        adapter.addFragment(new StatisticFragment(R.string.month, presenter), getString(R.string.month));
        adapter.addFragment(new StatisticFragment(R.string.year, presenter), getString(R.string.year));
        adapter.addFragment(new StatisticFragment(R.string.all, presenter), getString(R.string.all));
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    public void updateActivityList(Activities[] activities) {
        String[] arr = new String[activities.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = activities[i].getName();
        }
        final Spinner spinner = findViewById(R.id.spinner_exercise);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exercise = parent.getItemAtPosition(position).toString();
                presenter.updateRecordsList();
                StatisticFragmentPagerAdapter adapter = new StatisticFragmentPagerAdapter(getSupportFragmentManager());
                presenter.updateRecordsList();
                adapter.addFragment(new StatisticFragment(R.string.week, presenter), getString(R.string.week));
                adapter.addFragment(new StatisticFragment(R.string.month, presenter), getString(R.string.month));
                adapter.addFragment(new StatisticFragment(R.string.year, presenter), getString(R.string.year));
                adapter.addFragment(new StatisticFragment(R.string.all, presenter), getString(R.string.all));
                viewPager.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(0);
        exercise = spinnerExercise.getItemAtPosition(0).toString();
    }

    public String getExercise() {
        return exercise;
    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
