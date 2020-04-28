package com.example.sporttracker.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sporttracker.Models.Activities;
import com.example.sporttracker.Models.ActivityRecordModel;
import com.example.sporttracker.Presenters.AddExerciseRecordPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.ExerciseRecordsRepository;

import java.util.Calendar;

public class AddExerciseRecordActivity extends AppCompatActivity {

    AddExerciseRecordPresenter presenter;

    private Spinner spinnerExercise;
    private TextView textViewDate;
    private TextView textViewDistance;
    private TextView textViewTime;
    private TextView textViewComment;

    private ImageButton buttonSave;
    private ImageButton buttonCancel;

    private Calendar date;
    private Calendar time;
    private String exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_record);
        init();
    }

    private void init() {
        presenter = new AddExerciseRecordPresenter(new ExerciseRecordsRepository(this));
        presenter.attachView(this);
        presenter.viewIsReady();

        spinnerExercise = findViewById(R.id.spinner_exercise);
        textViewDistance = findViewById(R.id.text_distance);
        textViewDate = findViewById(R.id.text_date);
        textViewTime = findViewById(R.id.text_time);
        textViewComment = findViewById(R.id.text_comment);

        buttonSave = findViewById(R.id.imageButtonSave);
        buttonCancel = findViewById(R.id.imageButtonCancel);

        textViewDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeDistance();
            }
        });

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setDate();
            }
        });

        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeDuration();
            }
        });

        textViewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeComment();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.save();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void updateActivityList(Activities[] activities) {
        String[] arr = new String[activities.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = activities[i].getName();
        }
        final Spinner spinner = findViewById(R.id.spinner_exercise);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exercise = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(0);
    }

    public Context getContext() {
        return this;
    }

    public void setTextViewDistance(String text) {
        textViewDistance.setText(text + " м");
    }

    public void setTextViewComment(String text) {
        textViewComment.setText(text);
    }

    public void setInitialDate(Calendar dateAndTime) {
        this.date = dateAndTime;
    }

    public void updateDate(Calendar dateAndTime) {
        this.date = dateAndTime;
        textViewDate.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    public void setInitialTime(Calendar dateAndTime) {
        this.time = dateAndTime;
    }

    public void updateTime(Calendar dateAndTime) {
        this.time = dateAndTime;
        textViewTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
    }

    public ActivityRecordModel getModel() {
        ActivityRecordModel model = new ActivityRecordModel();
        model.setActivity(exercise);
        if (textViewDistance.getText().toString().isEmpty() || textViewDistance.getText().toString() == null) {
            model.setDistance(-1);
        } else {
            model.setDistance(Float.parseFloat(textViewDistance.getText().toString()
                    .substring(0, textViewDistance.getText().toString().length() - 2)));
        }
        if(date != null) {
            model.setDate(date.getTime());
        }
        if(time != null) {
            model.setTime(time.getTimeInMillis());
        }
        model.setComment(textViewComment.getText().toString());
        return model;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
