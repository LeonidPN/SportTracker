package com.example.sporttracker.Presenters;

import android.content.Intent;

import com.example.sporttracker.Models.Activities;
import com.example.sporttracker.Models.ActivityRecordModel;
import com.example.sporttracker.Services.ExerciseRecordsRepository;
import com.example.sporttracker.Views.ExerciseRecordsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

public class ExerciseRecordsPresenter {

    private ExerciseRecordsActivity activity;
    private ExerciseRecordsRepository repository;

    public ExerciseRecordsPresenter(ExerciseRecordsRepository repository) {
        this.repository = repository;
    }

    public void attachView(ExerciseRecordsActivity activity) {
        this.activity = activity;
    }

    public void detachView() {
        activity = null;
    }

    public void viewIsReady() {
        activity.updateActivityList(Activities.values());
    }

    public void updateRecordsList() {
        ArrayList<ArrayList<ActivityRecordModel>> groups = new ArrayList<>();

        final String exercise = activity.getExercise();

        repository.open();
        ArrayList<ActivityRecordModel> list = (ArrayList) repository.getList();
        repository.close();

        if (list != null) {

            list.removeIf(new Predicate<ActivityRecordModel>() {
                @Override
                public boolean test(ActivityRecordModel n) {
                    return (!n.getActivity().equals(exercise));
                }
            });

            if (list.size() > 0) {

                list.sort(new Comparator<ActivityRecordModel>() {
                    @Override
                    public int compare(ActivityRecordModel o1, ActivityRecordModel o2) {
                        return -o1.getDate().compareTo(o2.getDate());
                    }
                });

                groups.add(new ArrayList<ActivityRecordModel>());
                groups.get(0).add(list.get(0));

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLLL yyyy");

                for (int i = 1; i < list.size(); i++) {
                    if (!simpleDateFormat.format(list.get(i).getDate())
                            .equals(simpleDateFormat.format(list.get(i - 1).getDate()))) {
                        groups.add(new ArrayList<ActivityRecordModel>());
                    }
                    groups.get(groups.size() - 1).add(list.get(i));
                }
            }

        }

        activity.updateListAdapter(groups);
    }

    public void startActivity(Class<?> T) {
        Intent intent = new Intent(activity.getContext(), T);
        activity.getContext().startActivity(intent);
    }

    public void closeActivity() {
        activity.finish();
    }

}
