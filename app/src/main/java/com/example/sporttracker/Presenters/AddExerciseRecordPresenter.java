package com.example.sporttracker.Presenters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.sporttracker.Models.Activities;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.ActivityRecordRepository;
import com.example.sporttracker.Views.AddExerciseRecordActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class AddExerciseRecordPresenter {

    private AddExerciseRecordActivity view;
    private ActivityRecordRepository repository;

    private Calendar dateAndTime = Calendar.getInstance();

    public AddExerciseRecordPresenter(ActivityRecordRepository repository) {
        this.repository = repository;
    }

    public void attachView(AddExerciseRecordActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        loadActivityList();
    }

    private void loadActivityList() {
        view.updateActivityList(Activities.values());
    }

    public void changeCount() {
        LayoutInflater li = LayoutInflater.from(view.getContext());
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(view.getContext());
        mDialogBuilder.setView(promptsView);
        final EditText userInput = promptsView.findViewById(R.id.editText);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                view.setTextViewCount(userInput.getText().toString());
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    public void setDate() {
        new DatePickerDialog(view.getContext(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
        this.view.setInitialDateTime(dateAndTime);
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };

    public void changeComment() {
        LayoutInflater li = LayoutInflater.from(view.getContext());
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(view.getContext());
        mDialogBuilder.setView(promptsView);
        final EditText userInput = promptsView.findViewById(R.id.editText);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                view.setTextViewComment(userInput.getText().toString());
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    public void save() {
        repository.open();
        repository.insert(view.getModel());
        repository.close();
    }

}
