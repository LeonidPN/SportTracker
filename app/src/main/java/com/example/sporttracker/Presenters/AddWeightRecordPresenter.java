package com.example.sporttracker.Presenters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.sporttracker.Models.WeightRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.WeightRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.AddWeightRecordActivity;

import java.util.Calendar;

public class AddWeightRecordPresenter {

    private AddWeightRecordActivity activity;

    private WeightRecordsRepository repository;
    private PreferencesRepository preferences;

    private Calendar date = Calendar.getInstance();

    public AddWeightRecordPresenter(WeightRecordsRepository repository,
                                    PreferencesRepository preferences) {
        this.repository = repository;
        this.preferences = preferences;
    }

    public void attachView(AddWeightRecordActivity activity) {
        this.activity = activity;
    }

    public void detachView() {
        activity = null;
    }

    public void viewIsReady() {
        activity.updateDate(date);
        activity.updateTime(date);
    }

    public void changeDate() {
        new DatePickerDialog(activity.getContext(), d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            activity.updateDate(date);
        }
    };

    public void changeTime() {
        new TimePickerDialog(activity.getContext(), t,
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE), true)
                .show();
    }

    private TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            date.set(Calendar.HOUR_OF_DAY, hourOfDay);
            date.set(Calendar.MINUTE, minute);
            activity.updateTime(date);
        }
    };

    public void changeWeight() {
        LayoutInflater li = LayoutInflater.from(activity.getContext());
        @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.edit_text_dialog_number, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(activity.getContext());
        mDialogBuilder.setView(promptsView);
        final EditText userInput = promptsView.findViewById(R.id.editText);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton(activity.getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                activity.setTextViewWeight(userInput.getText().toString());
                            }
                        })
                .setNegativeButton(activity.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    public void save() {
        WeightRecordModel model = activity.getModel();
        if (model.getWeight() == -1) {
            activity.showToast(activity.getResources().getString(R.string.choose_weight));
            return;
        }
        repository.open();
        repository.insert(model);
        repository.close();
        preferences.setWeight(model.getWeight() + "");
        activity.finish();
    }

    public void close() {
        activity.finish();
    }
}
