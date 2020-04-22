package com.example.sporttracker.Views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.sporttracker.Presenters.MePresenter;
import com.example.sporttracker.R;

public class MeFragment extends Fragment {

    private MePresenter presenter;

    private ConstraintLayout constraintLayoutProfile;
    private ConstraintLayout constraintLayoutGoals;
    private ConstraintLayout constraintLayoutPreferences;
    private ConstraintLayout constraintLayoutAbout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);

        presenter = new MePresenter();

        presenter.attachView(this);

        constraintLayoutProfile = root.findViewById(R.id.constraintLayoutProfile);
        constraintLayoutGoals = root.findViewById(R.id.constraintLayoutGoals);
        constraintLayoutPreferences = root.findViewById(R.id.constraintLayoutPreferences);
        constraintLayoutAbout = root.findViewById(R.id.constraintLayoutAbout);

        constraintLayoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(ProfileActivity.class);
            }
        });

        constraintLayoutGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(GoalsActivity.class);
            }
        });

        constraintLayoutPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(PreferencesActivity.class);
            }
        });

        constraintLayoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(AboutActivity.class);
            }
        });

        return root;
    }

    public Context getContext(){
        return getContext();
    }
}