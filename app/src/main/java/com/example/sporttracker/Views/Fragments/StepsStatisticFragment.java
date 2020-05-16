package com.example.sporttracker.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sporttracker.Presenters.StepsStatisticPresenter;
import com.example.sporttracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class StepsStatisticFragment extends Fragment {

    private int stringId;

    private StepsStatisticPresenter presenter;

    private ImageButton imageButtonPrevious;
    private ImageButton imageButtonNext;

    private TextView textViewDate;
    private TextView textViewCurrentDate;
    private TextView textViewCurrentStepCount;
    private TextView textViewAverageInMonth;
    private TextView textViewSteps;
    private TextView textViewCalories;

    private BarChart chart;

    public StepsStatisticFragment(int stringId, StepsStatisticPresenter presenter) {
        this.stringId = stringId;
        this.presenter = presenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_statistic, container, false);

        imageButtonNext = view.findViewById(R.id.imageButton_next);
        imageButtonPrevious = view.findViewById(R.id.imageButton_previous);

        textViewDate = view.findViewById(R.id.textView_date);
        textViewCurrentDate = view.findViewById(R.id.textView_currentDate);
        textViewCurrentStepCount = view.findViewById(R.id.textView_currentStepCount);
        textViewAverageInMonth = view.findViewById(R.id.textView_averageInMonth);
        textViewSteps = view.findViewById(R.id.textView_steps);
        textViewCalories = view.findViewById(R.id.textView_calories);

        chart = view.findViewById(R.id.chart);

        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setDrawLabels(false);
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
                if (stringId == R.string.day) {
                    if (presenter.getAllCount() > 0) {
                        presenter.changeDayCount(-1);
                        presenter.setDay();

                        chart.setData(presenter.getDayData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getDayDate());
                        textViewSteps.setText(presenter.getDayStepsCount());
                        textViewCalories.setText(presenter.getDayCalories());
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
                if (stringId == R.string.week) {
                    if (presenter.getWeekCount() > 0) {
                        presenter.changeWeekCount(-1);
                        presenter.setWeek();

                        chart.setData(presenter.getWeekData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getWeekDate());
                        textViewSteps.setText(presenter.getWeekStepsCount());
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
                        textViewSteps.setText(presenter.getMonthStepsCount());
                        textViewCalories.setText(presenter.getMonthCalories());
                    }
                    if (presenter.getMonthCount() == 0) {
                        imageButtonNext.setEnabled(false);
                        imageButtonNext.setVisibility(View.INVISIBLE);
                    }
                    if (presenter.getMonthCount() < 120) {
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
                        textViewSteps.setText(presenter.getYearStepsCount());
                        textViewCalories.setText(presenter.getYearCalories());
                    }
                    if (presenter.getYearCount() == 0) {
                        imageButtonNext.setEnabled(false);
                        imageButtonNext.setVisibility(View.INVISIBLE);
                    }
                    if (presenter.getYearCount() < 10) {
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
                if (stringId == R.string.day) {
                    if (presenter.getAllCount() < 200) {
                        presenter.changeDayCount(1);
                        presenter.setDay();

                        chart.setData(presenter.getDayData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getDayDate());
                        textViewSteps.setText(presenter.getDayStepsCount());
                        textViewCalories.setText(presenter.getDayCalories());
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
                if (stringId == R.string.week) {
                    if (presenter.getWeekCount() < 200) {
                        presenter.changeWeekCount(1);
                        presenter.setWeek();

                        chart.setData(presenter.getWeekData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getWeekDate());
                        textViewSteps.setText(presenter.getWeekStepsCount());
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
                    if (presenter.getMonthCount() < 120) {
                        presenter.changeMonthCount(1);
                        presenter.setMonth();

                        chart.setData(presenter.getMonthData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getMonthDate());
                        textViewSteps.setText(presenter.getMonthStepsCount());
                        textViewCalories.setText(presenter.getMonthCalories());
                    }
                    if (presenter.getMonthCount() > 0) {
                        imageButtonNext.setEnabled(true);
                        imageButtonNext.setVisibility(View.VISIBLE);
                    }
                    if (presenter.getMonthCount() == 120) {
                        imageButtonPrevious.setEnabled(false);
                        imageButtonPrevious.setVisibility(View.INVISIBLE);
                    }
                }
                if (stringId == R.string.year) {
                    if (presenter.getYearCount() < 10) {
                        presenter.changeYearCount(1);
                        presenter.setYear();

                        chart.setData(presenter.getYearData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getYearDate());
                        textViewSteps.setText(presenter.getYearStepsCount());
                        textViewCalories.setText(presenter.getYearCalories());
                    }
                    if (presenter.getYearCount() > 0) {
                        imageButtonNext.setEnabled(true);
                        imageButtonNext.setVisibility(View.VISIBLE);
                    }
                    if (presenter.getYearCount() == 10) {
                        imageButtonPrevious.setEnabled(false);
                        imageButtonPrevious.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        if (stringId == R.string.day) {
            presenter.setDay();

            chart.setData(presenter.getDayData());
            chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));

            chart.invalidate();

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return presenter.getDayLabels()[(int) value];
                }
            };
            chart.getXAxis().setValueFormatter(formatter);

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    textViewCurrentDate.setText(presenter.getHourInDay((int) e.getX()));
                    textViewCurrentStepCount.setText((int) e.getY() + " " + getStepAbbreviation((int) e.getY()));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentStepCount.setText("--" + " " + getStepAbbreviation(0));
                }
            });

            textViewDate.setText(presenter.getDayDate());
            textViewSteps.setText(presenter.getDayStepsCount());
            textViewCalories.setText(presenter.getDayCalories());
        }
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
                    textViewCurrentStepCount.setText((int) e.getY() + " " + getStepAbbreviation((int) e.getY()));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentStepCount.setText("--" + " " + getStepAbbreviation(0));
                }
            });

            textViewDate.setText(presenter.getWeekDate());
            textViewSteps.setText(presenter.getWeekStepsCount());
            textViewCalories.setText(presenter.getWeekCalories());
        }
        if (stringId == R.string.month) {
            presenter.setMonth();

            chart.setData(presenter.getMonthData());
            chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));

            chart.invalidate();

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    textViewCurrentDate.setText(presenter.getDayInMonth((int) e.getX()));
                    textViewCurrentStepCount.setText((int) e.getY() + " " + getStepAbbreviation((int) e.getY()));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentStepCount.setText("--" + " " + getStepAbbreviation(0));
                }
            });

            textViewDate.setText(presenter.getMonthDate());
            textViewSteps.setText(presenter.getMonthStepsCount());
            textViewCalories.setText(presenter.getMonthCalories());
        }
        if (stringId == R.string.year) {
            textViewAverageInMonth.setVisibility(View.VISIBLE);

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
                    textViewCurrentStepCount.setText((int) e.getY() + " " + getStepAbbreviation((int) e.getY()));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentStepCount.setText("--" + " " + getStepAbbreviation(0));
                }
            });

            textViewDate.setText(presenter.getYearDate());
            textViewSteps.setText(presenter.getYearStepsCount());
            textViewCalories.setText(presenter.getYearCalories());
        }

        chart.highlightValue(0, -1);

        return view;
    }

    private String getStepAbbreviation(int stepCount) {
        if (stepCount == 0) {
            return getResources()
                    .getString(R.string.steps_notify_abbreviation_3);
        } else if (stepCount % 10 == 1) {
            return getResources()
                    .getString(R.string.steps_notify_abbreviation_1);
        } else if (stepCount % 10 == 2 || stepCount % 10 == 3 || stepCount % 10 == 4) {
            return getResources()
                    .getString(R.string.steps_notify_abbreviation_2);
        } else if (stepCount % 100 < 21) {
            return getResources()
                    .getString(R.string.steps_notify_abbreviation_3);
        } else {
            return getResources()
                    .getString(R.string.steps_notify_abbreviation_3);
        }
    }
}
