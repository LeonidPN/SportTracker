package com.example.sporttracker.Presenters;

import com.example.sporttracker.R;
import com.example.sporttracker.Services.RecommendationManagers.GeneralRecommendationService;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.GeneralRecommendationsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GeneralRecommendationsPresenter {

    private GeneralRecommendationsActivity activity;

    private PreferencesRepository preferences;

    public GeneralRecommendationsPresenter(PreferencesRepository preferencesRepository) {
        this.preferences = preferencesRepository;
    }

    public void attachView(GeneralRecommendationsActivity activity) {
        this.activity = activity;
    }

    public void viewIsReady() {
        Calendar dateOfBirth = Calendar.getInstance();
        try {
            dateOfBirth.setTime(new SimpleDateFormat("dd.MM.yyyy")
                    .parse(preferences.getDateOfBirth()));
        } catch (ParseException e) {
        }
        Calendar currentDate = Calendar.getInstance();

        int age;
        if (currentDate.get(Calendar.DAY_OF_YEAR) >= dateOfBirth.get(Calendar.DAY_OF_YEAR)) {
            age = currentDate.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
        } else {
            age = currentDate.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR) - 1;
        }

        int groupId;
        if (age < 18) {
            groupId = R.integer.age_group_1;
        } else if (age < 65) {
            groupId = R.integer.age_group_2;
        } else {
            groupId = R.integer.age_group_3;
        }

        ArrayList<Integer> recommendationStringsId =
                GeneralRecommendationService.getRecommendationStringsId(groupId);

        String recommendation = "";
        for (int id : recommendationStringsId) {
            recommendation += getResourceString(id);
        }

        activity.setTextView(recommendation);
    }

    private String getResourceString(int id) {
        return activity.getResources().getString(id);
    }
}
