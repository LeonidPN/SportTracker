package com.example.sporttracker.Presenters;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.sporttracker.Models.WeightRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.WeightRecordsRepository;
import com.example.sporttracker.Views.Activities.WeightHistoryActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class WeightHistoryPresenter {

    private WeightHistoryActivity activity;
    private WeightRecordsRepository repository;

    public WeightHistoryPresenter(WeightRecordsRepository repository) {
        this.repository = repository;
    }

    public void attachView(WeightHistoryActivity activity) {
        this.activity = activity;
    }

    public void detachView() {
        activity = null;
    }

    public void viewIsReady() {

    }

    public void updateRecordsList() {
        ArrayList<ArrayList<WeightRecordModel>> groups = new ArrayList<>();

        repository.open();
        ArrayList<WeightRecordModel> list = (ArrayList) repository.getList();
        repository.close();

        if (list.size() > 0) {

            list.sort(new Comparator<WeightRecordModel>() {
                @Override
                public int compare(WeightRecordModel o1, WeightRecordModel o2) {
                    return -o1.getDate().compareTo(o2.getDate());
                }
            });

            groups.add(new ArrayList<WeightRecordModel>());
            groups.get(0).add(list.get(0));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d LLLL yyyy");

            for (int i = 1; i < list.size(); i++) {
                if (!simpleDateFormat.format(list.get(i).getDate())
                        .equals(simpleDateFormat.format(list.get(i - 1).getDate()))) {
                    groups.add(new ArrayList<WeightRecordModel>());
                }
                groups.get(groups.size() - 1).add(list.get(i));
            }
        }

        activity.updateListAdapter(groups);
    }

    public void deleteRecord(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
        builder.setMessage(R.string.delete_question);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                repository.open();
                repository.delete(id);
                repository.close();

                updateRecordsList();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void closeActivity() {
        activity.finish();
    }

    private String getResourceString(int id) {
        return activity.getResources().getString(id);
    }
}
