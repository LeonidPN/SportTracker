package com.example.sporttracker.Views.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sporttracker.Presenters.WeightPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.WeightRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Adapters.SimpleFragmentPagerAdapter;
import com.example.sporttracker.Views.Fragments.WeightInDayFragment;
import com.example.sporttracker.Views.Fragments.WeightStatisticFragment;
import com.google.android.material.tabs.TabLayout;

public class WeightActivity extends AppCompatActivity {

    private WeightPresenter presenter;

    private ImageButton buttonBack;

    private Button buttonAddRecord;
    private Button buttonHistory;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        presenter = new WeightPresenter(new WeightRecordsRepository(this),
                new PreferencesRepository(this));
        presenter.attachView(this);

        buttonBack = findViewById(R.id.imageButton_back);

        buttonAddRecord = findViewById(R.id.button_addRecord);
        buttonHistory = findViewById(R.id.button_history);

        tabLayout = findViewById(R.id.tabLayout);

        viewPager = findViewById(R.id.viewPager);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.close();
            }
        });

        buttonAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(AddWeightRecordActivity.class);
            }
        });

        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(WeightHistoryActivity.class);
            }
        });

        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        presenter.updateRecordsList();
        adapter.addFragment(new WeightInDayFragment(presenter), getString(R.string.day));
        adapter.addFragment(new WeightStatisticFragment(R.string.week, presenter), getString(R.string.week));
        adapter.addFragment(new WeightStatisticFragment(R.string.month, presenter), getString(R.string.month));
        adapter.addFragment(new WeightStatisticFragment(R.string.year, presenter), getString(R.string.year));
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        presenter.updateRecordsList();
        adapter.addFragment(new WeightInDayFragment(presenter), getString(R.string.day));
        adapter.addFragment(new WeightStatisticFragment(R.string.week, presenter), getString(R.string.week));
        adapter.addFragment(new WeightStatisticFragment(R.string.month, presenter), getString(R.string.month));
        adapter.addFragment(new WeightStatisticFragment(R.string.year, presenter), getString(R.string.year));
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
