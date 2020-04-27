package com.example.sporttracker.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.sporttracker.Models.Activities;
import com.example.sporttracker.Models.ActivityRecordModel;
import com.example.sporttracker.Presenters.ExerciseRecordsPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.ExerciseRecordsRepository;
import com.example.sporttracker.Views.Adapters.ExersiseRecordsExpendableListAdapter;

import java.util.ArrayList;

public class ExerciseRecordsActivity extends AppCompatActivity {

    private ExerciseRecordsPresenter presenter;

    private ImageButton imageButtonBack;
    private Button buttonAddRecord;
    private Spinner spinnerExercise;
    private ExpandableListView expandableListView;

    private String exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_records);

        presenter = new ExerciseRecordsPresenter(new ExerciseRecordsRepository(this));
        presenter.attachView(this);
        presenter.viewIsReady();

        imageButtonBack = findViewById(R.id.imageButton_back);
        buttonAddRecord = findViewById(R.id.button_add_record);
        spinnerExercise = findViewById(R.id.spinner_exercise);
        expandableListView = findViewById(R.id.expandableListView);

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.closeActivity();
            }
        });

        buttonAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(AddExerciseRecordActivity.class);
            }
        });
    }

    public void updateActivityList(Activities[] activities) {
        String[] arr = new String[activities.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = activities[i].getName();
        }
        final Spinner spinner = findViewById(R.id.spinner_exercise);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exercise = parent.getItemAtPosition(position).toString();
                presenter.updateRecordsList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(0);
    }

    public void updateListAdapter(ArrayList<ArrayList<ActivityRecordModel>> groups){
        ExersiseRecordsExpendableListAdapter adapter =
                new ExersiseRecordsExpendableListAdapter(this, groups, exercise);
        expandableListView.setAdapter(adapter);
    }

    public String getExercise(){
        return exercise;
    }

    public Context getContext(){
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
