package com.example.sporttracker.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporttracker.Models.WeightRecordModel;
import com.example.sporttracker.Presenters.WeightHistoryPresenter;
import com.example.sporttracker.Presenters.WeightPresenter;
import com.example.sporttracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WeightRecordsItemRecyclerViewAdapter
        extends RecyclerView.Adapter<WeightRecordsItemRecyclerViewAdapter.WeightRecordsItemRecyclerViewHolder> {

    private ArrayList<WeightRecordModel> list;

    private WeightPresenter presenter;

    private WeightHistoryPresenter historyPresenter;

    public static class WeightRecordsItemRecyclerViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public WeightRecordsItemRecyclerViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public WeightRecordsItemRecyclerViewAdapter(ArrayList<WeightRecordModel> list,
                                                WeightPresenter presenter) {
        this.list = list;
        this.presenter = presenter;
        this.historyPresenter = null;
    }

    public WeightRecordsItemRecyclerViewAdapter(ArrayList<WeightRecordModel> list,
                                                WeightHistoryPresenter presenter) {
        this.list = list;
        this.presenter = null;
        this.historyPresenter = presenter;
    }

    @NonNull
    @Override
    public WeightRecordsItemRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_view_weight_records_list_item, parent, false);
        WeightRecordsItemRecyclerViewHolder holder = new WeightRecordsItemRecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WeightRecordsItemRecyclerViewHolder holder, int position) {
        final WeightRecordModel recordModel = list.get(position);

        ((TextView) holder.view.findViewById(R.id.textView_weight))
                .setText(recordModel.getWeight() + " " + holder.view.getContext().
                        getResources().getString(R.string.kilogram_abbreviation));

        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");

        ((TextView) holder.view.findViewById(R.id.textView_date))
                .setText(simpleTimeFormat.format(recordModel.getDate()));

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (presenter != null) {
                    presenter.deleteRecord(recordModel.getId());
                }
                if (historyPresenter != null) {
                    historyPresenter.deleteRecord(recordModel.getId());
                }
                return true;
            }
        });

        if (position == list.size() - 1) {
            (holder.view.findViewById(R.id.divider)).setEnabled(false);
            (holder.view.findViewById(R.id.divider)).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
