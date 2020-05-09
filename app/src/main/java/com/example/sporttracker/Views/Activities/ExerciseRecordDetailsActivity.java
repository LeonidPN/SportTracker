package com.example.sporttracker.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sporttracker.Models.ExerciseRecordModel;
import com.example.sporttracker.Presenters.ExerciseRecordDetailsPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.ExerciseRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;

import java.text.SimpleDateFormat;

public class ExerciseRecordDetailsActivity extends AppCompatActivity {

    private ExerciseRecordDetailsPresenter presenter;

    private TextView textViewExercise;
    private TextView textViewDate;
    private TextView textViewDistance;
    private TextView textViewDuration;
    private TextView textViewCalories;

    private ImageButton imageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_record_details);

        presenter = new ExerciseRecordDetailsPresenter(new ExerciseRecordsRepository(this),
                new PreferencesRepository(this));
        presenter.attachView(this);

        textViewExercise = findViewById(R.id.textView_title);
        textViewDate = findViewById(R.id.textView_date);
        textViewDistance = findViewById(R.id.textView_distance);
        textViewDuration = findViewById(R.id.textView_time);
        textViewCalories = findViewById(R.id.textView_calories);

        imageButtonBack = findViewById(R.id.imageButton_back);

        presenter.viewIsReady();

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.closeActivity();
            }
        });
    }

    public void setViews(ExerciseRecordModel recordModel, float calories) {
        textViewExercise.setText(recordModel.getActivity());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLLL d, yyyy");
        textViewDate.setText(simpleDateFormat.format(recordModel.getDate()));

        textViewDistance.setText(recordModel.getDistance() + " м");

        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");
        textViewDuration.setText(simpleTimeFormat.format(recordModel.getTime()));

        textViewCalories.setText(calories + " ккал");
    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
