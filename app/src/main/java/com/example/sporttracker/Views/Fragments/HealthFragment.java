package com.example.sporttracker.Views.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sporttracker.Presenters.HealthPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.StepsDatabase.StepsRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.AddExerciseRecordActivity;
import com.example.sporttracker.Views.Activities.ExerciseRecordsActivity;
import com.example.sporttracker.Views.Activities.StatisticActivity;
import com.example.sporttracker.Views.Activities.StepsStatisticActivity;
import com.example.sporttracker.Views.Activities.WeightActivity;

public class HealthFragment extends Fragment {

    private HealthPresenter presenter;

    private Context context;

    private TextView textViewSteps;
    private TextView textViewDistance;
    private TextView textViewCalories;

    private View viewSteps;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_health, container, false);

        context = root.getContext();

        presenter = new HealthPresenter(new StepsRecordsRepository(context),
                new PreferencesRepository(context));
        presenter.attachView(this);

        textViewSteps = root.findViewById(R.id.textView_steps);
        textViewDistance = root.findViewById(R.id.textView_distance);
        textViewCalories = root.findViewById(R.id.textView_calories);

        viewSteps = root.findViewById(R.id.view_steps);

        presenter.viewIsReady();

        viewSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(StepsStatisticActivity.class);
            }
        });

        TextView textView4 = root.findViewById(R.id.textView4);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExerciseRecordsActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        TextView textView5 = root.findViewById(R.id.textView5);
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddExerciseRecordActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        TextView textView6 = root.findViewById(R.id.textView6);
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WeightActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        TextView textView7 = root.findViewById(R.id.textView7);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StatisticActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        return root;
    }

    public void updateViews(int steps, int stepsGoal, float distance, float calories) {
        textViewSteps.setText(steps + "/" + stepsGoal + " " + getResources()
                .getString(R.string.steps_notify_abbreviation_3));
        textViewDistance.setText(String.format("%.1f", distance) + " " + getResources()
                .getString(R.string.kilometers_abbreviation));
        textViewCalories.setText(String.format("%.1f", calories) + " " + getResources()
                .getString(R.string.calories_abbreviation));
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.viewIsReady();
    }
}