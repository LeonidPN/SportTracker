package com.example.sporttracker.Views.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sporttracker.Presenters.StepsStatisticPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.StepsDatabase.StepsRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Adapters.SimpleFragmentPagerAdapter;
import com.example.sporttracker.Views.Fragments.StepsStatisticFragment;
import com.google.android.material.tabs.TabLayout;

public class StepsStatisticActivity extends AppCompatActivity {

    private StepsStatisticPresenter presenter;

    private ImageButton imageButtonBack;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_statistic);

        presenter = new StepsStatisticPresenter(new StepsRecordsRepository(this),
                new PreferencesRepository(this));
        presenter.attachView(this);

        imageButtonBack = findViewById(R.id.imageButton_back);

        tabLayout = findViewById(R.id.tabLayout);

        viewPager = findViewById(R.id.viewPager);

        presenter.viewIsReady();

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.close();
            }
        });

        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StepsStatisticFragment(R.string.day, presenter),
                getString(R.string.day));
        adapter.addFragment(new StepsStatisticFragment(R.string.week, presenter),
                getString(R.string.week));
        adapter.addFragment(new StepsStatisticFragment(R.string.month, presenter),
                getString(R.string.month));
        adapter.addFragment(new StepsStatisticFragment(R.string.year, presenter),
                getString(R.string.year));
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
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
