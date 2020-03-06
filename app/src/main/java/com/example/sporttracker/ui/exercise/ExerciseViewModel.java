package com.example.sporttracker.ui.exercise;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExerciseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExerciseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Упражнения");
    }

    public LiveData<String> getText() {
        return mText;
    }
}