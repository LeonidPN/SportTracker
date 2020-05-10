package com.example.sporttracker.Presenters;

import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.GoalsActivity;

public class GoalsPresenter {

    private GoalsActivity activity;
    private PreferencesRepository preferences;

    private final static int MIN_STEPS_GOAL = 2000;
    private final static int MAX_STEPS_GOAL = 20000;
    private final static int STEPS_GOAL_STEP = 1000;

    private final static int MIN_WEIGHT_GOAL = 10;
    private final static int MAX_WEIGHT_GOAL = 250;
    private final static int WEIGHT_GOAL_STEP = 1;

    public GoalsPresenter(PreferencesRepository preferencesRepository) {
        this.preferences = preferencesRepository;
    }

    public void attachView(GoalsActivity activity) {
        this.activity = activity;
    }

    public void detachView() {
        activity = null;
    }

    public void viewIsReady() {
        int steps = preferences.getStepsGoal();
        if (steps == 0) {
            steps = (10000 - MIN_STEPS_GOAL) / STEPS_GOAL_STEP;
        } else {
            steps = (steps - MIN_STEPS_GOAL) / STEPS_GOAL_STEP;
        }
        int weight = preferences.getWeightGoal();
        if (weight == 0) {
            weight = (60 - MIN_WEIGHT_GOAL) / WEIGHT_GOAL_STEP;
        } else {
            weight = (weight - MIN_WEIGHT_GOAL) / WEIGHT_GOAL_STEP;
        }
        activity.updateViews(steps, weight);
    }

    public int getStepsSeekBarMax() {
        return (MAX_STEPS_GOAL - MIN_STEPS_GOAL) / STEPS_GOAL_STEP;
    }

    public int getWeightSeekBarMax() {
        return (MAX_WEIGHT_GOAL - MIN_WEIGHT_GOAL) / WEIGHT_GOAL_STEP;
    }

    public String getStepsGoal(int progress) {
        return (MIN_STEPS_GOAL + STEPS_GOAL_STEP * progress) + "";
    }

    public String getWeightGoal(int progress) {
        return (MIN_WEIGHT_GOAL + WEIGHT_GOAL_STEP * progress) + "";
    }

    public void close() {
        activity.finish();
    }

    public void save(int steps, int weight) {
        preferences.setStepsGoal(steps);
        preferences.setWeightGoal(weight);
    }

    private String getResourceString(int id) {
        return activity.getResources().getString(id);
    }
}
