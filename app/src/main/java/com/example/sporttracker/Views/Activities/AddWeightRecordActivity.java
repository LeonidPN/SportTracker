package com.example.sporttracker.Views.Activities;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sporttracker.Models.WeightRecordModel;
import com.example.sporttracker.Presenters.AddWeightRecordPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.WeightRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;

import java.util.Calendar;

public class AddWeightRecordActivity extends AppCompatActivity {

    private AddWeightRecordPresenter presenter;

    private TextView textViewDate;
    private TextView textViewTime;
    private TextView textViewWeight;

    private ImageButton buttonSave;
    private ImageButton buttonCancel;

    private Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight_record);

        presenter = new AddWeightRecordPresenter(new WeightRecordsRepository(this),
                new PreferencesRepository(this));
        presenter.attachView(this);

        textViewDate = findViewById(R.id.textView_date);
        textViewTime = findViewById(R.id.textView_time);
        textViewWeight = findViewById(R.id.textView_weight);

        buttonSave = findViewById(R.id.imageButton_save);
        buttonCancel = findViewById(R.id.imageButton_cancel);

        presenter.viewIsReady();

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeDate();
            }
        });

        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeTime();
            }
        });

        textViewWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeWeight();
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
                presenter.close();
            }
        });
    }

    public void updateDate(Calendar dateAndTime) {
        this.date = dateAndTime;
        textViewDate.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    public void updateTime(Calendar dateAndTime) {
        this.date = dateAndTime;
        textViewTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
    }

    public WeightRecordModel getModel() {
        WeightRecordModel model = new WeightRecordModel();
        model.setDate(date.getTime());
        if (textViewWeight.getText().toString().isEmpty() || textViewWeight.getText().toString() == null) {
            model.setWeight(-1);
        } else {
            model.setWeight(Float.parseFloat(textViewWeight.getText().toString()
                    .substring(0, textViewWeight.getText().toString().length() - 3)));
        }
        return model;
    }

    public void setTextViewWeight(String weight) {
        textViewWeight.setText(weight + " " + getResources().getString(R.string.kilogram_abbreviation));
    }

    public Context getContext() {
        return this;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
