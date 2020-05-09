package com.example.sporttracker.Presenters;

import android.os.Bundle;

import com.example.sporttracker.Models.Enumerations.Activities;
import com.example.sporttracker.Models.ExerciseRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.ExerciseRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.ExerciseRecordDetailsActivity;

public class ExerciseRecordDetailsPresenter {

    private ExerciseRecordDetailsActivity activity;
    private ExerciseRecordsRepository repository;
    private PreferencesRepository preferencesRepository;

    private int id;

    public ExerciseRecordDetailsPresenter(ExerciseRecordsRepository repository,
                                          PreferencesRepository preferencesRepository) {
        this.repository = repository;
        this.preferencesRepository = preferencesRepository;
    }

    public void attachView(ExerciseRecordDetailsActivity activity) {
        this.activity = activity;
        Bundle arguments = activity.getIntent().getExtras();
        id = (int) arguments.get(getResourceString(R.string.id));
    }

    public void detachView() {
        activity = null;
    }

    public void viewIsReady() {
        repository.open();
        ExerciseRecordModel recordModel = (ExerciseRecordModel) repository.getElement(id);
        repository.close();

        float calories = Activities.RUN.getCalories(recordModel.getDistance()
                / (float) recordModel.getTime() / 1000 * 3600)
                * recordModel.getTime() * Float.parseFloat(preferencesRepository.getWeight());

        activity.setViews(recordModel, calories);
    }

    public void closeActivity() {
        activity.finish();
    }

    private String getResourceString(int id) {
        return activity.getResources().getString(id);
    }

}
