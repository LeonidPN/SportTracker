package com.example.sporttracker.Presenters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.sporttracker.Models.Enumerations.Activities;
import com.example.sporttracker.Models.ExerciseRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.ExerciseRecordsRepository;
import com.example.sporttracker.Views.Activities.AddExerciseRecordActivity;

import java.util.Calendar;

public class AddExerciseRecordPresenter {

    private AddExerciseRecordActivity activity;
    private ExerciseRecordsRepository repository;

    private Calendar date = Calendar.getInstance();
    private Calendar time = Calendar.getInstance();

    public AddExerciseRecordPresenter(ExerciseRecordsRepository repository) {
        this.repository = repository;
        time.set(1970, 0, 0, 0, 0, 0);
    }

    public void attachView(AddExerciseRecordActivity activity) {
        this.activity = activity;
    }

    public void detachView() {
        activity = null;
    }

    public void viewIsReady() {
        loadActivityList();
        date = Calendar.getInstance();
        activity.updateDate(date);
        activity.setTextViewDistance("0");
        activity.updateTime(time);
    }

    private void loadActivityList() {
        activity.updateActivityList(Activities.values());
    }

    public void changeDistance() {
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
                                activity.setTextViewDistance(userInput.getText().toString());
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

    public void changeDuration() {
        LayoutInflater li = LayoutInflater.from(activity.getContext());
        View promptsView = li.inflate(R.layout.number_picker_time_dialog, null);

        final NumberPicker numberPickerHour = promptsView.findViewById(R.id.numberPicker_hour);
        final NumberPicker numberPickerMinute = promptsView.findViewById(R.id.numberPicker_minute);
        final NumberPicker numberPickerSecond = promptsView.findViewById(R.id.numberPicker_second);

        int minValueHour = 0;
        int maxValueHour = 23;

        int minValueMinute = 0;
        int maxValueMinute = 59;

        int minValueSecond = 0;
        int maxValueSecond = 59;

        final String[] valuesHour = new String[maxValueHour - minValueHour + 1];

        for (int i = 0; i < valuesHour.length; i++) {
            if (minValueHour + i < 10) {
                valuesHour[i] = "0" + (minValueHour + i);
            } else {
                valuesHour[i] = (minValueHour + i) + "";
            }
        }

        final String[] valuesMinute = new String[maxValueMinute - minValueMinute + 1];

        for (int i = 0; i < valuesMinute.length; i++) {
            if (minValueMinute + i < 10) {
                valuesMinute[i] = "0" + (minValueMinute + i);
            } else {
                valuesMinute[i] = (minValueMinute + i) + "";
            }
        }

        final String[] valuesSecond = new String[maxValueSecond - minValueSecond + 1];

        for (int i = 0; i < valuesSecond.length; i++) {
            if (minValueSecond + i < 10) {
                valuesSecond[i] = "0" + (minValueSecond + i);
            } else {
                valuesSecond[i] = (minValueSecond + i) + "";
            }
        }

        numberPickerHour.setMinValue(minValueHour);
        numberPickerHour.setMaxValue(maxValueHour);
        numberPickerHour.setDisplayedValues(valuesHour);
        numberPickerHour.setValue(time.get(Calendar.HOUR_OF_DAY));
        numberPickerHour.setWrapSelectorWheel(true);

        numberPickerMinute.setMinValue(minValueMinute);
        numberPickerMinute.setMaxValue(maxValueMinute);
        numberPickerMinute.setDisplayedValues(valuesMinute);
        numberPickerMinute.setValue(time.get(Calendar.MINUTE));
        numberPickerMinute.setWrapSelectorWheel(true);

        numberPickerSecond.setMinValue(minValueSecond);
        numberPickerSecond.setMaxValue(maxValueSecond);
        numberPickerSecond.setDisplayedValues(valuesSecond);
        numberPickerSecond.setValue(time.get(Calendar.SECOND));
        numberPickerSecond.setWrapSelectorWheel(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(promptsView);
        builder.setTitle(getResourceString(R.string.duration));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                time.set(Calendar.HOUR_OF_DAY, numberPickerHour.getValue());
                time.set(Calendar.MINUTE, numberPickerMinute.getValue());
                time.set(Calendar.SECOND, numberPickerSecond.getValue());
                activity.updateTime(time);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.updateTime(time);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        this.activity.setInitialTime(time);
    }

    public void setDate() {
        this.activity.setInitialDate(date);
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

    public void changeComment() {
        LayoutInflater li = LayoutInflater.from(activity.getContext());
        @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.edit_text_dialog_text, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(activity.getContext());
        mDialogBuilder.setView(promptsView);
        final EditText userInput = promptsView.findViewById(R.id.editText);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton(activity.getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                activity.setTextViewComment(userInput.getText().toString());
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
        ExerciseRecordModel model = activity.getModel();
        if (model.getActivity().isEmpty() || model.getActivity() == null) {
            activity.showToast(activity.getResources().getString(R.string.choose_exercise));
            return;
        }
        if (model.getDate() == null) {
            activity.showToast(activity.getResources().getString(R.string.choose_date));
            return;
        }
        if (model.getDistance() == -1) {
            activity.showToast(activity.getResources().getString(R.string.choose_distance));
            return;
        }
        if (model.getTime() == 0) {
            activity.showToast(activity.getResources().getString(R.string.choose_duration));
            return;
        }
        if (model.getComment().isEmpty() || model.getComment() == null) {
            model.setComment("-");
        }
        repository.open();
        repository.insert(model);
        repository.close();
        activity.finish();
    }

    private String getResourceString(int id) {
        return activity.getResources().getString(id);
    }

}
