package com.example.sporttracker.Presenters;

import android.content.Intent;

import com.example.sporttracker.Views.Fragments.MeFragment;

public class MePresenter {

    private MeFragment fragment;

    public void attachView(MeFragment fragment) {
        this.fragment = fragment;
    }

    public void startActivity(Class<?> T){
        Intent intent = new Intent(fragment.getContext(), T);
        fragment.getContext().startActivity(intent);
    }

}
