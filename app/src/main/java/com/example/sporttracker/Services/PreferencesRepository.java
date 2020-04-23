package com.example.sporttracker.Services;

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

    private SharedPreferences settings;

    private Context context;

    public PreferencesRepository(Context context){
        this.context = context;
        settings = context.getSharedPreferences("Preferences",
                Context.MODE_PRIVATE);
    }

    public String getSex(){
        return settings.getString(SEX_KEY, getResourceString(R.string.sex_default_value));
    }

    public void setSex(String sex){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SEX_KEY, sex);
        editor.putBoolean(SEX_CHANGED_KEY, true);
        editor.commit();
    }

    public boolean getSexChanged(){
        return settings.getBoolean(SEX_CHANGED_KEY, false);
    }

    public String getDateOfBirth(){
        return settings.getString(DATE_OF_BIRTH_KEY, getResourceString(R.string.date_of_birth_default_value));
    }

    public void setDateOfBirth(String date){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DATE_OF_BIRTH_KEY, date);
        editor.putBoolean(DATE_OF_BIRTH_CHANGED_KEY, true);
        editor.commit();
    }

    public boolean getDateOfBirthChanged(){
        return settings.getBoolean(DATE_OF_BIRTH_CHANGED_KEY, false);
    }

    public String getHeight(){
        return settings.getString(HEIGHT_KEY, getResourceString(R.string.height_default_value));
    }

    public void setHeight(String height){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(HEIGHT_KEY, height);
        editor.putBoolean(HEIGHT_CHANGED_KEY, true);
        editor.commit();
    }

    public boolean getHeightChanged(){
        return settings.getBoolean(HEIGHT_CHANGED_KEY, false);
    }

    public String getWeight(){
        return settings.getString(WEIGHT_KEY, getResourceString(R.string.weight_default_value));
    }

    public void setWeight(String weight){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(WEIGHT_KEY, weight);
        editor.putBoolean(WEIGHT_CHANGED_KEY, true);
        editor.commit();
    }

    public boolean getWeightChanged(){
        return settings.getBoolean(WEIGHT_CHANGED_KEY, false);
    }

    private String getResourceString(int id){
        return context.getResources().getString(id);
    }

}
