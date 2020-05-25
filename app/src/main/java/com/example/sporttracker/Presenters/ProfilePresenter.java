package com.example.sporttracker.Presenters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.ProfileActivity;

import java.util.Calendar;

public class ProfilePresenter {

    private ProfileActivity activity;
    private PreferencesRepository preferencesRepository;

    private Calendar date = Calendar.getInstance();

    public ProfilePresenter(PreferencesRepository preferencesRepository) {
        this.preferencesRepository = preferencesRepository;
    }

    public void attachView(ProfileActivity activity) {
        this.activity = activity;
    }

    public void detachView() {
        activity = null;
    }

    public PreferencesRepository getRepository() {
        return preferencesRepository;
    }

    public void viewIsReady() {
        activity.setFields();
        date.setTime(preferencesRepository.getDateOfBirth().getTime());
    }

    public void changeSex() {
        final String[] strings = {getResourceString(R.string.male),
                getResourceString(R.string.female)};

        final SelectedItem selectedItem = new SelectedItem();
        selectedItem.setId(-1);

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equals(preferencesRepository.getSex())) {
                selectedItem.setId(i);
                break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(getResourceString(R.string.sex))
                .setSingleChoiceItems(strings, selectedItem.getId(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                selectedItem.setId(item);
                            }
                        })
                .setPositiveButton(getResourceString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (selectedItem.getId() > -1) {
                            preferencesRepository.setSex(strings[selectedItem.getId()]);
                        }
                        activity.setFields();
                    }
                })
                .setNegativeButton(getResourceString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        activity.setFields();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private class SelectedItem {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public void changeDateOfBirth() {
        activity.setInitialDate(date);
        new DatePickerDialog(activity.getContext(), this.d,
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

            preferencesRepository.setDateOfBirth(date);

            activity.setFields();
        }
    };

    public void changeHeight() {
        LayoutInflater li = LayoutInflater.from(activity.getContext());
        View promptsView = li.inflate(R.layout.edit_text_dialog_number, null);

        final EditText userInput = promptsView.findViewById(R.id.editText);
        userInput.setText(String.format("%.1f", preferencesRepository.getHeight()));
        userInput.requestFocus();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(promptsView);
        builder.setTitle(getResourceString(R.string.height));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!userInput.getText().toString().isEmpty()) {
                    preferencesRepository.setHeight(Float.parseFloat(userInput.getText().toString()));
                    activity.setFields();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.setFields();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        activity.setFields();
    }

    public void changeWeight() {
        LayoutInflater li = LayoutInflater.from(activity.getContext());
        View promptsView = li.inflate(R.layout.edit_text_dialog_number, null);

        final EditText userInput = promptsView.findViewById(R.id.editText);
        userInput.setText(String.format("%.1f", preferencesRepository.getWeight()));
        userInput.requestFocus();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(promptsView);
        builder.setTitle(getResourceString(R.string.weight));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!userInput.getText().toString().isEmpty()) {
                    preferencesRepository.setWeight(Float.parseFloat(userInput.getText().toString()));
                }
                activity.setFields();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.setFields();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void close() {
        activity.finish();
    }

    private String getResourceString(int id) {
        return activity.getResources().getString(id);
    }

}
