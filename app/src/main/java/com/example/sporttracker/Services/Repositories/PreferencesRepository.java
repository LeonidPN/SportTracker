package com.example.sporttracker.Services.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sporttracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PreferencesRepository {

    private static final String SEX_KEY = "sex";
    private static final String DATE_OF_BIRTH_KEY = "date_of_birth";
    private static final String HEIGHT_KEY = "height";
    private static final String WEIGHT_KEY = "weight";
    private static final String STEP_COUNT_DELTA_KEY = "step_count_delta";
    private static final String WEIGHT_GOAL_KEY = "weight_goal";
    private static final String STEPS_GOAL_KEY = "steps_goal";
    private static final String WEIGHT_START_KEY = "weight_start";

    private final SimpleDateFormat dateOfBirthFormat = new SimpleDateFormat("dd.MM.yyyy");

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
        editor.apply();
    }

    public Calendar getDateOfBirth() {
        String date = settings.getString(DATE_OF_BIRTH_KEY,
                getResourceString(R.string.date_of_birth_default_value));
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(dateOfBirthFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public void setDateOfBirth(Calendar date) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DATE_OF_BIRTH_KEY, dateOfBirthFormat.format(date.getTime()));
        editor.apply();
    }

    public float getHeight() {
        return Float.parseFloat(settings.getString(HEIGHT_KEY, getResourceString(R.string.height_default_value)));
    }

    public void setHeight(float height) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(HEIGHT_KEY, height + "");
        editor.apply();
    }

    public float getWeight() {
        return Float.parseFloat(settings.getString(WEIGHT_KEY, getResourceString(R.string.weight_default_value)));
    }

    public void setWeight(float weight) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(WEIGHT_KEY, weight + "");
        editor.apply();
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
        editor.putString(WEIGHT_START_KEY,
                settings.getString(WEIGHT_KEY, getResourceString(R.string.weight_default_value)));
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

    public float getStartWeight() {
        return Float.parseFloat(settings.getString(WEIGHT_START_KEY, getResourceString(R.string.weight_default_value)));
    }

    private String getResourceString(int id) {
        return context.getResources().getString(id);
    }

}
