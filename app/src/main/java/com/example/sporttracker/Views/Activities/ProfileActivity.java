package com.example.sporttracker.Views.Activities;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sporttracker.Presenters.ProfilePresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    private ProfilePresenter presenter;

    private TextView textViewSex;
    private TextView textViewDateOfBirth;
    private TextView textViewHeight;
    private TextView textViewWeight;

    private ImageButton imageButtonBack;

    private Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        presenter = new ProfilePresenter(new PreferencesRepository(this));
        presenter.attachView(this);

        textViewSex = findViewById(R.id.text_sex);
        textViewDateOfBirth = findViewById(R.id.text_date_of_birth);
        textViewHeight = findViewById(R.id.text_height);
        textViewWeight = findViewById(R.id.text_weight);

        imageButtonBack = findViewById(R.id.imageButton_back);

        presenter.viewIsReady();

        textViewSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeSex();
            }
        });

        textViewDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeDateOfBirth();
            }
        });

        textViewHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeHeight();
            }
        });

        textViewWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeWeight();
            }
        });

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.close();
            }
        });
    }

    public void setFields(){
        textViewSex.setText(presenter.getRepository().getSex());
        textViewDateOfBirth.setText(new SimpleDateFormat("LLLL d, yyyy")
                .format(presenter.getRepository().getDateOfBirth().getTime()));
        textViewHeight.setText(String.format("%.1f", presenter.getRepository().getHeight()) + " " +
                getResources().getString(R.string.santimeters_abbreviation));
        textViewWeight.setText(String.format("%.1f", presenter.getRepository().getWeight()) + " " +
                getResources().getString(R.string.kilogram_abbreviation));
    }

    public void setInitialDate(Calendar dateAndTime) {
        this.date = dateAndTime;
    }

    public void updateDate(Calendar dateAndTime) {
        this.date = dateAndTime;
        textViewDateOfBirth.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    public Context getContext(){
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
