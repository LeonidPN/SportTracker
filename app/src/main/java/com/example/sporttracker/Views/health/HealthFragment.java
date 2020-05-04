package com.example.sporttracker.Views.health;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sporttracker.Views.StatisticActivity;
import com.example.sporttracker.Views.AddExerciseRecordActivity;
import com.example.sporttracker.R;
import com.example.sporttracker.Views.ExerciseRecordsActivity;

public class HealthFragment extends Fragment {

    private HealthViewModel healthViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        healthViewModel =
                ViewModelProviders.of(this).get(HealthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_health, container, false);
        final TextView textView = root.findViewById(R.id.text_health);
        healthViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
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
                Intent intent = new Intent(v.getContext(), AddExerciseRecordActivity.class);
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
}