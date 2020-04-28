package com.example.sporttracker.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporttracker.Models.Activities;
import com.example.sporttracker.Models.ActivityRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.PreferencesRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExersiseRecordsExpendableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<ActivityRecordModel>> groups;
    private Context context;
    private String exercise;

    public ExersiseRecordsExpendableListAdapter(Context context,
                                                ArrayList<ArrayList<ActivityRecordModel>> groups,
                                                String exercise) {
        this.context = context;
        this.groups = groups;
        this.exercise = exercise;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_view_exercise_records_list, null);
        }

        if (isExpanded) {
            //Изменяем что-нибудь, если текущая Group раскрыта
        } else {
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        TextView textViewDate = convertView.findViewById(R.id.textView_date);

        Date date = groups.get(groupPosition).get(0).getDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLLL yyyy");

        textViewDate.setText(simpleDateFormat.format(date));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_view_execise_records_list, null);
        }

        ArrayList<ActivityRecordModel> list = groups.get(groupPosition);

        float distance = 0;
        long time = 0;

        for (ActivityRecordModel record : list) {
            distance += record.getDistance();
            time += record.getTime();
        }

        TextView textViewDistance = convertView.findViewById(R.id.textView_distance);
        textViewDistance.setText(distance + " м");

        PreferencesRepository preferencesRepository = new PreferencesRepository(context);

        float calories = 0;

        if(exercise.equals(Activities.RUN.getName())){
            calories = Activities.RUN.getCalories(distance/(float)time / 1000 * 3600)
                    * time * Float.parseFloat(preferencesRepository.getWeight());
        }
        if(exercise.equals(Activities.CYCLE.getName())){
            calories = Activities.CYCLE.getCalories(distance/(float)time / 1000 * 3600)
                    * time * Float.parseFloat(preferencesRepository.getWeight());
        }
        if(exercise.equals(Activities.SWIM.getName())){
            calories = Activities.SWIM.getCalories(distance/(float)time / 1000 * 3600)
                    * time * Float.parseFloat(preferencesRepository.getWeight());
        }
        if(exercise.equals(Activities.WALK.getName())){
            calories = Activities.WALK.getCalories(distance/(float)time / 1000 * 3600)
                    * time * Float.parseFloat(preferencesRepository.getWeight());
        }

        TextView textViewCalories = convertView.findViewById(R.id.textView_calories);
        textViewCalories.setText(calories + " ккал");

        RecyclerView recyclerView = convertView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new ExerciseRecordsItemRecyclerViewAdapter(list));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}