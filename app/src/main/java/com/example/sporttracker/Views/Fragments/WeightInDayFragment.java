package com.example.sporttracker.Views.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporttracker.Models.WeightRecordModel;
import com.example.sporttracker.Presenters.WeightPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Views.Activities.GoalsActivity;
import com.example.sporttracker.Views.Adapters.WeightRecordsItemRecyclerViewAdapter;

import java.util.ArrayList;

public class WeightInDayFragment extends Fragment {

    private WeightPresenter presenter;

    private TextView textViewDate;
    private TextView textViewWeight;
    private TextView textViewStart;
    private TextView textViewGoal;
    private TextView textViewSetGoal;

    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private Context context;

    public WeightInDayFragment(WeightPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_weight_in_day, container, false);

        presenter.attachView(this);

        context = view.getContext();

        textViewDate = view.findViewById(R.id.textView_date);
        textViewWeight = view.findViewById(R.id.textView_weight);
        textViewStart = view.findViewById(R.id.textView_start);
        textViewGoal = view.findViewById(R.id.textView_goal);
        textViewSetGoal = view.findViewById(R.id.textView_setGoal);

        progressBar = view.findViewById(R.id.progressBar);

        recyclerView = view.findViewById(R.id.recyclerView);

        presenter.fragmentIsReady();

        textViewSetGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(GoalsActivity.class);
            }
        });

        return view;
    }

    public void updateViews(String date, float weight, float start, float goal,
                            ArrayList<WeightRecordModel> list) {
        if (!date.equals("-1")) {
            textViewStart.setVisibility(View.VISIBLE);
            textViewStart.setEnabled(true);
            textViewGoal.setVisibility(View.VISIBLE);
            textViewGoal.setEnabled(true);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setEnabled(true);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setEnabled(true);

            textViewDate.setText(date);
            textViewWeight.setText(String.format("%.1f", weight) + " " +
                    getResources().getString(R.string.kilogram_abbreviation));
            textViewStart.setText(getResources().getString(R.string.start) + " " + String.format("%.1f", start) + " " +
                    getResources().getString(R.string.kilogram_abbreviation));
            textViewGoal.setText(getResources().getString(R.string.goal) + " " + String.format("%.1f", goal) + " " +
                    getResources().getString(R.string.kilogram_abbreviation));

            if (start > goal) {
                progressBar.setProgress(0);
                progressBar.setMax((int) (start * 10) - (int) (goal * 10));
                progressBar.setProgress(Math.max((int) ((start - weight) * 10), 0), true);
            } else if (start < goal) {
                progressBar.setProgress(0);
                progressBar.setMax((int) (goal * 10) - (int) (start * 10));
                progressBar.setProgress(Math.max((int) ((weight - start) * 10), 0), true);
            } else {
                progressBar.setProgress(0);
                progressBar.setMax(1);
                progressBar.setProgress(1, true);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new WeightRecordsItemRecyclerViewAdapter(list, presenter));
        } else {
            textViewDate.setText("");
            textViewWeight.setText(String.format("%.1f", weight) + " " +
                    getResources().getString(R.string.kilogram_abbreviation));
            textViewStart.setVisibility(View.INVISIBLE);
            textViewStart.setEnabled(false);
            textViewGoal.setVisibility(View.INVISIBLE);
            textViewGoal.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.setEnabled(false);
            recyclerView.setVisibility(View.INVISIBLE);
            recyclerView.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.fragmentIsReady();
    }
}
