package com.example.sporttracker.Views.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sporttracker.Models.WeightRecordModel;
import com.example.sporttracker.Presenters.WeightHistoryPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.WeightRecordsRepository;
import com.example.sporttracker.Views.Adapters.WeightRecordsExpendableListAdapter;

import java.util.ArrayList;

public class WeightHistoryActivity extends AppCompatActivity {

    private WeightHistoryPresenter presenter;

    private ImageButton imageButtonBack;

    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_history);

        presenter = new WeightHistoryPresenter(new WeightRecordsRepository(this));
        presenter.attachView(this);

        imageButtonBack = findViewById(R.id.imageButton_back);
        expandableListView = findViewById(R.id.expandableListView);

        presenter.viewIsReady();

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.closeActivity();
            }
        });

        presenter.updateRecordsList();
    }

    public void updateListAdapter(ArrayList<ArrayList<WeightRecordModel>> groups) {
        WeightRecordsExpendableListAdapter adapter =
                new WeightRecordsExpendableListAdapter(this, groups, presenter);
        expandableListView.setAdapter(adapter);
    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.updateRecordsList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
