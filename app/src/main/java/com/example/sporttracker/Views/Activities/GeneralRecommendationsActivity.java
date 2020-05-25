package com.example.sporttracker.Views.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sporttracker.Presenters.GeneralRecommendationsPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;

public class GeneralRecommendationsActivity extends AppCompatActivity {

    private GeneralRecommendationsPresenter presenter;

    private TextView textViewGeneralRecommendations;

    private ImageButton imageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_recommendations);

        presenter = new GeneralRecommendationsPresenter(
                new PreferencesRepository(this)
        );
        presenter.attachView(this);

        textViewGeneralRecommendations = findViewById(R.id.textView_generalRecommendations);

        imageButtonBack = findViewById(R.id.imageButton_back);

        presenter.viewIsReady();

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setTextView(String recommendations) {
        textViewGeneralRecommendations.setText(recommendations);
    }
}
