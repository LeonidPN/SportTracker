package com.example.sporttracker.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sporttracker.Presenters.StatisticPresenter;
import com.example.sporttracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class StatisticFragment extends Fragment {

    private StatisticPresenter presenter;

    private final int stringId;

    private ImageButton imageButtonPrevious;
    private ImageButton imageButtonNext;

    private TextView textViewDate;
    private TextView textViewCurrentDate;
    private TextView textViewCurrentDistance;
    private TextView textViewDistance;
    private TextView textViewTime;
    private TextView textViewCalories;

    private BarChart chart;

    public StatisticFragment(int stringId, StatisticPresenter presenter) {
        this.stringId = stringId;
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        imageButtonNext = view.findViewById(R.id.imageButton_next);
        imageButtonPrevious = view.findViewById(R.id.imageButton_previous);

        textViewDate = view.findViewById(R.id.textView_date);
        textViewCurrentDate = view.findViewById(R.id.textView_currentDate);
        textViewCurrentDistance = view.findViewById(R.id.textView_currentDistance);
        textViewDistance = view.findViewById(R.id.textView_distance);
        textViewTime = view.findViewById(R.id.textView_time);
        textViewCalories = view.findViewById(R.id.textView_calories);

        chart = view.findViewById(R.id.chart);

        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setMaxVisibleValueCount(0);
        chart.setDrawBarShadow(false);
        chart.setScaleEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisRight().setDrawGridLines(false);
        chart.setFitBars(true);

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringId == R.string.week) {
                    if (presenter.getWeekCount() > 0) {
                        presenter.changeWeekCount(-1);
                        presenter.setWeek();

                        chart.setData(presenter.getWeekData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getWeekDate());
                        textViewDistance.setText(presenter.getWeekDistance());
                        textViewTime.setText(presenter.getWeekDuration());
                        textViewCalories.setText(presenter.getWeekCalories());
                    }
                    if (presenter.getWeekCount() == 0) {
                        imageButtonNext.setEnabled(false);
                        imageButtonNext.setVisibility(View.INVISIBLE);
                    }
                    if (presenter.getWeekCount() < 200) {
                        imageButtonPrevious.setEnabled(true);
                        imageButtonPrevious.setVisibility(View.VISIBLE);
                    }
                }
                if (stringId == R.string.month) {
                    if (presenter.getMonthCount() > 0) {
                        presenter.changeMonthCount(-1);
                        presenter.setMonth();

                        chart.setData(presenter.getMonthData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getMonthDate());
                        textViewDistance.setText(presenter.getMonthDistance());
                        textViewTime.setText(presenter.getMonthDuration());
                        textViewCalories.setText(presenter.getMonthCalories());
                    }
                    if (presenter.getMonthCount() == 0) {
                        imageButtonNext.setEnabled(false);
                        imageButtonNext.setVisibility(View.INVISIBLE);
                    }
                    if (presenter.getMonthCount() < 200) {
                        imageButtonPrevious.setEnabled(true);
                        imageButtonPrevious.setVisibility(View.VISIBLE);
                    }
                }
                if (stringId == R.string.year) {
                    if (presenter.getYearCount() > 0) {
                        presenter.changeYearCount(-1);
                        presenter.setYear();

                        chart.setData(presenter.getYearData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getYearDate());
                        textViewDistance.setText(presenter.getYearDistance());
                        textViewTime.setText(presenter.getYearDuration());
                        textViewCalories.setText(presenter.getYearCalories());
                    }
                    if (presenter.getYearCount() == 0) {
                        imageButtonNext.setEnabled(false);
                        imageButtonNext.setVisibility(View.INVISIBLE);
                    }
                    if (presenter.getYearCount() < 200) {
                        imageButtonPrevious.setEnabled(true);
                        imageButtonPrevious.setVisibility(View.VISIBLE);
                    }
                }
                if (stringId == R.string.all) {
                    if (presenter.getAllCount() > 0) {
                        presenter.changeAllCount(-1);
                        presenter.setAll();

                        chart.setData(presenter.getAllData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getAllDate());
                        textViewDistance.setText(presenter.getAllDistance());
                        textViewTime.setText(presenter.getAllDuration());
                        textViewCalories.setText(presenter.getAllCalories());
                    }
                    if (presenter.getAllCount() == 0) {
                        imageButtonNext.setEnabled(false);
                        imageButtonNext.setVisibility(View.INVISIBLE);
                    }
                    if (presenter.getAllCount() < 200) {
                        imageButtonPrevious.setEnabled(true);
                        imageButtonPrevious.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        imageButtonNext.setVisibility(View.INVISIBLE);

        imageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringId == R.string.week) {
                    if (presenter.getWeekCount() < 200) {
                        presenter.changeWeekCount(1);
                        presenter.setWeek();

                        chart.setData(presenter.getWeekData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getWeekDate());
                        textViewDistance.setText(presenter.getWeekDistance());
                        textViewTime.setText(presenter.getWeekDuration());
                        textViewCalories.setText(presenter.getWeekCalories());
                    }
                    if (presenter.getWeekCount() > 0) {
                        imageButtonNext.setEnabled(true);
                        imageButtonNext.setVisibility(View.VISIBLE);
                    }
                    if (presenter.getWeekCount() == 200) {
                        imageButtonPrevious.setEnabled(false);
                        imageButtonPrevious.setVisibility(View.INVISIBLE);
                    }
                }
                if (stringId == R.string.month) {
                    if (presenter.getMonthCount() < 200) {
                        presenter.changeMonthCount(1);
                        presenter.setMonth();

                        chart.setData(presenter.getMonthData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getMonthDate());
                        textViewDistance.setText(presenter.getMonthDistance());
                        textViewTime.setText(presenter.getMonthDuration());
                        textViewCalories.setText(presenter.getMonthCalories());
                    }
                    if (presenter.getMonthCount() > 0) {
                        imageButtonNext.setEnabled(true);
                        imageButtonNext.setVisibility(View.VISIBLE);
                    }
                    if (presenter.getMonthCount() == 200) {
                        imageButtonPrevious.setEnabled(false);
                        imageButtonPrevious.setVisibility(View.INVISIBLE);
                    }
                }
                if (stringId == R.string.year) {
                    if (presenter.getYearCount() < 200) {
                        presenter.changeYearCount(1);
                        presenter.setYear();

                        chart.setData(presenter.getYearData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getYearDate());
                        textViewDistance.setText(presenter.getYearDistance());
                        textViewTime.setText(presenter.getYearDuration());
                        textViewCalories.setText(presenter.getYearCalories());
                    }
                    if (presenter.getYearCount() > 0) {
                        imageButtonNext.setEnabled(true);
                        imageButtonNext.setVisibility(View.VISIBLE);
                    }
                    if (presenter.getYearCount() == 200) {
                        imageButtonPrevious.setEnabled(false);
                        imageButtonPrevious.setVisibility(View.INVISIBLE);
                    }
                }
                if (stringId == R.string.all) {
                    if (presenter.getAllCount() < 200) {
                        presenter.changeAllCount(1);
                        presenter.setAll();

                        chart.setData(presenter.getAllData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getAllDate());
                        textViewDistance.setText(presenter.getAllDistance());
                        textViewTime.setText(presenter.getAllDuration());
                        textViewCalories.setText(presenter.getAllCalories());
                    }
                    if (presenter.getAllCount() > 0) {
                        imageButtonNext.setEnabled(true);
                        imageButtonNext.setVisibility(View.VISIBLE);
                    }
                    if (presenter.getAllCount() == 200) {
                        imageButtonPrevious.setEnabled(false);
                        imageButtonPrevious.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        if (stringId == R.string.week) {
            presenter.setWeek();

            chart.setData(presenter.getWeekData());
            chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
            chart.invalidate();

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return presenter.getWeekLabels()[(int) value];
                }
            };

            chart.getXAxis().setValueFormatter(formatter);

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    textViewCurrentDate.setText(presenter.getDayInWeek((int) e.getX()));
                    textViewCurrentDistance.setText(e.getY() + " " + getResources()
                            .getString(R.string.kilometers_abbreviation));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentDistance.setText("--" + " " + getResources()
                            .getString(R.string.kilometers_abbreviation));
                }
            });

            textViewDate.setText(presenter.getWeekDate());
            textViewDistance.setText(presenter.getWeekDistance());
            textViewTime.setText(presenter.getWeekDuration());
            textViewCalories.setText(presenter.getWeekCalories());
        }
        if (stringId == R.string.month) {
            presenter.setMonth();

            chart.setData(presenter.getMonthData());
            chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));

            chart.invalidate();

            /*ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return presenter.getMonthLabels()[(int) value];
                }
            };
            chart.getXAxis().setValueFormatter(formatter);*/

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    textViewCurrentDate.setText(presenter.getDayInMonth((int) e.getX()));
                    textViewCurrentDistance.setText(e.getY() + " " + getResources()
                            .getString(R.string.kilometers_abbreviation));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentDistance.setText("--" + " " + getResources()
                            .getString(R.string.kilometers_abbreviation));
                }
            });

            textViewDate.setText(presenter.getMonthDate());
            textViewDistance.setText(presenter.getMonthDistance());
            textViewTime.setText(presenter.getMonthDuration());
            textViewCalories.setText(presenter.getMonthCalories());
        }
        if (stringId == R.string.year) {
            presenter.setYear();

            chart.setData(presenter.getYearData());
            chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
            chart.invalidate();

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return presenter.getYearLabels()[(int) value];
                }
            };

            chart.getXAxis().setValueFormatter(formatter);

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    textViewCurrentDate.setText(presenter.getMonthInYear((int) e.getX()));
                    textViewCurrentDistance.setText(e.getY() + " " + getResources()
                            .getString(R.string.kilometers_abbreviation));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentDistance.setText("--" + " " + getResources()
                            .getString(R.string.kilometers_abbreviation));
                }
            });

            textViewDate.setText(presenter.getYearDate());
            textViewDistance.setText(presenter.getYearDistance());
            textViewTime.setText(presenter.getYearDuration());
            textViewCalories.setText(presenter.getYearCalories());
        }
        if (stringId == R.string.all) {
            presenter.setAll();

            chart.setData(presenter.getAllData());
            chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));

            chart.invalidate();

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return presenter.getAllLabels()[(int) value];
                }
            };
            chart.getXAxis().setValueFormatter(formatter);

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    textViewCurrentDate.setText(presenter.getYearInAll((int) e.getX()));
                    textViewCurrentDistance.setText(e.getY() + " " + getResources()
                            .getString(R.string.kilometers_abbreviation));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentDistance.setText("--" + " " + getResources()
                            .getString(R.string.kilometers_abbreviation));
                }
            });

            textViewDate.setText(presenter.getAllDate());
            textViewDistance.setText(presenter.getAllDistance());
            textViewTime.setText(presenter.getAllDuration());
            textViewCalories.setText(presenter.getAllCalories());
        }

        chart.highlightValue(0, -1);

        return view;
    }

}
