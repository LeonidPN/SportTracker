package com.example.sporttracker.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sporttracker.Models.Activities;
import com.example.sporttracker.Models.ActivityRecordModel;
import com.example.sporttracker.Presenters.AddExerciseRecordPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.ActivityRecordRepository;

import java.util.ArrayList;
import java.util.Calendar;

public class AddExerciseRecordActivity extends AppCompatActivity {

    AddExerciseRecordPresenter presenter;

    private Spinner spinner;
    private TextView textViewCount;
    private TextView textViewDate;
    private TextView textViewComment;

    private Button buttonSave;
    private Button buttonCancel;

    private Calendar dateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_record);
        init();
    }

    private void init() {
        presenter = new AddExerciseRecordPresenter(new ActivityRecordRepository(this));
        presenter.attachView(this);
        presenter.viewIsReady();

        spinner = findViewById(R.id.spinner);
        textViewCount = findViewById(R.id.textViewCount);
        textViewDate = findViewById(R.id.textViewDate);
        textViewComment = findViewById(R.id.textViewComment);

        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        textViewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeCount();
            }
        });

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setDate();
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
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void updateActivityList(Activities[] activities) {
        String[] arr = new String[activities.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = activities[i].getName();
        }
        final Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public Context getContext() {
        return this;
    }

    public void setTextViewCount(String text) {
        textViewCount.setText(text + " м");
    }

    public void setTextViewComment(String text) {
        textViewComment.setText(text);
    }

    public void setInitialDateTime(Calendar dateAndTime) {
        this.dateAndTime = dateAndTime;
        textViewDate.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    public ActivityRecordModel getModel() {
        ActivityRecordModel model = new ActivityRecordModel();
        //model.setCount(Float.parseFloat(textViewCount.getText().toString().substring(0, textViewCount.getText().toString().length() - 2)));
        model.setDate(dateAndTime.getTime());
        model.setComment(textViewComment.getText().toString());
        return model;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
