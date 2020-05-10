package com.example.sporttracker.Services.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sporttracker.R;

public class PreferencesRepository {

    private static final String SEX_KEY = "sex";
    private static final String SEX_CHANGED_KEY = "sex_changed";
    private static final String DATE_OF_BIRTH_KEY = "date_of_birth";
    private static final String DATE_OF_BIRTH_CHANGED_KEY = "date_of_birth_changed";
    private static final String HEIGHT_KEY = "height";
    private static final String HEIGHT_CHANGED_KEY = "height_changed";
    private static final String WEIGHT_KEY = "weight";
    private static final String WEIGHT_CHANGED_KEY = "weight_changed";
    private static final String STEP_COUNT_DELTA_KEY = "step_count_delta";
    private static final String WEIGHT_GOAL_KEY = "weight_goal";
    private static final String STEPS_GOAL_KEY = "steps_goal";

    private SharedPreferences settings;

    private Context context;

    public PreferencesRepository(Context context) {
        this.context = context;
        settings = context.getSharedPreferences("Preferences",
                Context.MODE_PRIVATE);
    }

    public String getSex() {
        return settings.getString(SEX_KEY, getResourceString(R.string.sex_default_value));
    }

    public void setSex(String sex) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SEX_KEY, sex);
        editor.putBoolean(SEX_CHANGED_KEY, true);
        editor.apply();
    }

    public boolean getSexChanged() {
        return settings.getBoolean(SEX_CHANGED_KEY, false);
    }

    public String getDateOfBirth() {
        return settings.getString(DATE_OF_BIRTH_KEY, getResourceString(R.string.date_of_birth_default_value));
    }

    public void setDateOfBirth(String date) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DATE_OF_BIRTH_KEY, date);
        editor.putBoolean(DATE_OF_BIRTH_CHANGED_KEY, true);
        editor.apply();
    }

    public boolean getDateOfBirthChanged() {
        return settings.getBoolean(DATE_OF_BIRTH_CHANGED_KEY, false);
    }

    public String getHeight() {
        return settings.getString(HEIGHT_KEY, getResourceString(R.string.height_default_value));
    }

    public void setHeight(String height) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(HEIGHT_KEY, height);
        editor.putBoolean(HEIGHT_CHANGED_KEY, true);
        editor.apply();
    }

    public boolean getHeightChanged() {
        return settings.getBoolean(HEIGHT_CHANGED_KEY, false);
    }

    public String getWeight() {
        return settings.getString(WEIGHT_KEY, getResourceString(R.string.weight_default_value));
    }

    public void setWeight(String weight) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(WEIGHT_KEY, weight);
        editor.putBoolean(WEIGHT_CHANGED_KEY, true);
        editor.apply();
    }

    public boolean getWeightChanged() {
        return settings.getBoolean(WEIGHT_CHANGED_KEY, false);
    }

    public void setStepCountDelta(int delta) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(STEP_COUNT_DELTA_KEY, delta);
        editor.apply();
    }

    public int getStepCountDelta() {
        return settings.getInt(STEP_COUNT_DELTA_KEY, 0);
    }

    public int getWeightGoal() {
        return settings.getInt(WEIGHT_GOAL_KEY, 0);
    }

    public void setWeightGoal(int weight) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(WEIGHT_GOAL_KEY, weight);
        editor.apply();
    }

    public int getStepsGoal() {
        return settings.getInt(STEPS_GOAL_KEY, 0);
    }

    public void setStepsGoal(int steps) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(STEPS_GOAL_KEY, steps);
        editor.apply();
    }

    private String getResourceString(int id) {
        return context.getResources().getString(id);
    }

}
