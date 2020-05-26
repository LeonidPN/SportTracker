package com.example.sporttracker.Views.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sporttracker.Presenters.GoalsPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;

public class GoalsActivity extends AppCompatActivity {

    private GoalsPresenter presenter;

    private TextView textViewSteps;
    private TextView textViewWeight;

    private SeekBar seekBarSteps;
    private SeekBar seekBarWeight;

    private Button buttonSave;

    private ImageButton imageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        presenter = new GoalsPresenter(new PreferencesRepository(this));
        presenter.attachView(this);

        textViewSteps = findViewById(R.id.textView_steps);
        textViewWeight = findViewById(R.id.textView_weight);

        seekBarSteps = findViewById(R.id.seekBar_steps);
        seekBarWeight = findViewById(R.id.seekBar_weight);

        buttonSave = findViewById(R.id.button_save);

        imageButtonBack = findViewById(R.id.imageButton_back);

        seekBarSteps.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSteps.setText(presenter.getStepsGoal(progress) + " " +
                        getResources().getString(R.string.steps_notify_abbreviation_3));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewWeight.setText(presenter.getWeightGoal(progress) + " " +
                        getResources().getString(R.string.kilogram_abbreviation));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.save(Integer.parseInt(textViewSteps.getText().toString().split(" ")[0]),
                        Integer.parseInt(textViewWeight.getText().toString().split(" ")[0]));
                presenter.close();
            }
        });

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.close();
            }
        });

        presenter.viewIsReady();
    }

    public void updateViews(int steps, int weight) {
        seekBarSteps.setMax(presenter.getStepsSeekBarMax());
        seekBarSteps.setProgress(steps);

        seekBarWeight.setMax(presenter.getWeightSeekBarMax());
        seekBarWeight.setProgress(0);
        seekBarWeight.setProgress(weight);
    }

}
