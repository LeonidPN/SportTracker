package com.example.sporttracker.Presenters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.sporttracker.Models.Activities;
import com.example.sporttracker.Models.ActivityRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.ActivityRecordRepository;
import com.example.sporttracker.Views.AddExerciseRecordActivity;

import java.util.Calendar;

public class AddExerciseRecordPresenter {

    private AddExerciseRecordActivity activity;
    private ActivityRecordRepository repository;

    private Calendar date = Calendar.getInstance();
    private Calendar time = Calendar.getInstance();

    public AddExerciseRecordPresenter(ActivityRecordRepository repository) {
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
    }

    private void loadActivityList() {
        activity.updateActivityList(Activities.values());
    }

    public void changeDistance() {
        LayoutInflater li = LayoutInflater.from(activity.getContext());
        View promptsView = li.inflate(R.layout.edit_text_dialog_number, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(activity.getContext());
        mDialogBuilder.setView(promptsView);
        final EditText userInput = promptsView.findViewById(R.id.editText);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                activity.setTextViewDistance(userInput.getText().toString());
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

    public void changeDuration() {
        this.activity.setInitialTime(time);
        new TimePickerDialog(activity.getContext(), t,
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE), true)
                .show();
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time.set(Calendar.HOUR_OF_DAY, hourOfDay);
            time.set(Calendar.MINUTE, minute);
            activity.updateTime(time);
        }
    };

    public void setDate() {
        this.activity.setInitialDate(date);
        new DatePickerDialog(activity.getContext(), d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            activity.updateDate(date);
        }
    };

    public void changeComment() {
        LayoutInflater li = LayoutInflater.from(activity.getContext());
        View promptsView = li.inflate(R.layout.edit_text_dialog_text, null);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(activity.getContext());
        mDialogBuilder.setView(promptsView);
        final EditText userInput = promptsView.findViewById(R.id.editText);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                activity.setTextViewComment(userInput.getText().toString());
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
        ActivityRecordModel model = activity.getModel();
        if (model.getActivity().isEmpty() || model.getActivity() == null) {
            activity.showToast("Выберите упражнение");
            return;
        }
        if (model.getDate() == null) {
            activity.showToast("Укажите дату");
            return;
        }
        if (model.getDistance() == -1) {
            activity.showToast("Укажите дистанцию");
            return;
        }
        if (model.getTime() == 0) {
            activity.showToast("Укажите продолжительность");
            return;
        }
        if (model.getComment().isEmpty() || model.getComment() == null) {
            model.setComment("-");
        }
        repository.open();
        repository.insert(model);
        repository.close();
        activity.showToast("Ok");
        activity.finish();
    }

}
