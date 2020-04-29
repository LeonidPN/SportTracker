package com.example.sporttracker.Views.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporttracker.Models.ActivityRecordModel;
import com.example.sporttracker.Presenters.ExerciseRecordsPresenter;
import com.example.sporttracker.R;
import com.example.sporttracker.Views.ExerciseRecordDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExerciseRecordsItemRecyclerViewAdapter
        extends RecyclerView.Adapter<ExerciseRecordsItemRecyclerViewAdapter.ExerciseRecordsItemRecyclerViewHolder> {

    private ArrayList<ActivityRecordModel> list;

    private ExerciseRecordsPresenter presenter;

    public static class ExerciseRecordsItemRecyclerViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ExerciseRecordsItemRecyclerViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public ExerciseRecordsItemRecyclerViewAdapter(ArrayList<ActivityRecordModel> list,
                                                  ExerciseRecordsPresenter presenter) {
        this.list = list;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ExerciseRecordsItemRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_view_exercise_records_list_item, parent, false);
        ExerciseRecordsItemRecyclerViewHolder holder = new ExerciseRecordsItemRecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseRecordsItemRecyclerViewHolder holder, int position) {
        final ActivityRecordModel recordModel = list.get(position);

        ((TextView) holder.view.findViewById(R.id.textView_distance))
                .setText(recordModel.getDistance() + " м");

        Date date = new Date();
        date.setTime(recordModel.getTime());
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");

        ((TextView) holder.view.findViewById(R.id.textView_time))
                .setText(simpleTimeFormat.format(date));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/d");

        ((TextView) holder.view.findViewById(R.id.textView_date))
                .setText(simpleDateFormat.format(recordModel.getDate()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startActivity(ExerciseRecordDetailsActivity.class, recordModel.getId());
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                presenter.deleteRecord(recordModel.getId());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
