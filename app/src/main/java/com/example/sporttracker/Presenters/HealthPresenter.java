package com.example.sporttracker.Presenters;

import android.content.Intent;

import com.example.sporttracker.Models.StepsRecordModel;
import com.example.sporttracker.Services.Repositories.Databases.StepsDatabase.StepsRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Fragments.HealthFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.Predicate;

public class HealthPresenter {

    private HealthFragment fragment;

    private StepsRecordsRepository repository;
    private PreferencesRepository preferences;

    public HealthPresenter(StepsRecordsRepository repository,
                           PreferencesRepository preferences) {
        this.repository = repository;
        this.preferences = preferences;
    }

    public void attachView(HealthFragment fragment) {
        this.fragment = fragment;
    }

    public void detachView() {
        fragment = null;
    }

    public void viewIsReady() {
        repository.open();
        ArrayList<StepsRecordModel> list = (ArrayList<StepsRecordModel>) repository.getList();
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
            for (StepsRecordModel model : list) {
                count += model.getCount();
            }
        }

        float distance = (Float.parseFloat(preferences.getHeight()) / 4 + 25) / 100 * count;

        float calories = 1f * Float.parseFloat(preferences.getWeight()) * count *
                (Float.parseFloat(preferences.getHeight()) / 4 + 25) / 100000 / 2;

        fragment.updateViews(count, distance, calories);
    }

    public void startActivity(Class<?> T) {
        Intent intent = new Intent(fragment.getContext(), T);
        fragment.getContext().startActivity(intent);
    }

    private String getResourceString(int id) {
        return fragment.getResources().getString(id);
    }
}
