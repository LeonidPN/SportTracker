package com.example.sporttracker.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporttracker.Models.WeightRecordModel;
import com.example.sporttracker.Presenters.WeightHistoryPresenter;
import com.example.sporttracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeightRecordsExpendableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<WeightRecordModel>> groups;
    private Context context;

    private WeightHistoryPresenter presenter;

    public WeightRecordsExpendableListAdapter(Context context,
                                              ArrayList<ArrayList<WeightRecordModel>> groups,
                                              WeightHistoryPresenter presenter) {
        this.context = context;
        this.groups = groups;
        this.presenter = presenter;
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
            convertView = inflater.inflate(R.layout.group_view_weight_records_list, null);
        }

        if (isExpanded) {
            //Изменяем что-нибудь, если текущая Group раскрыта
        } else {
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        TextView textViewDate = convertView.findViewById(R.id.textView_date);

        Date date = groups.get(groupPosition).get(0).getDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        textViewDate.setText(simpleDateFormat.format(date));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_view_weight_records_list, null);
        }

        ArrayList<WeightRecordModel> list = groups.get(groupPosition);

        RecyclerView recyclerView = convertView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new WeightRecordsItemRecyclerViewAdapter(list, presenter));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
