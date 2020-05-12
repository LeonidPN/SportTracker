package com.example.sporttracker.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sporttracker.Presenters.WeightPresenter;
import com.example.sporttracker.R;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class WeightStatisticFragment extends Fragment {

    private final int id;

    private WeightPresenter presenter;

    private ImageButton imageButtonPrevious;
    private ImageButton imageButtonNext;

    private TextView textViewDate;
    private TextView textViewCurrentDate;
    private TextView textViewCurrentWeight;
    private TextView textViewWeightChange;

    private ScatterChart chart;

    public WeightStatisticFragment(int id, WeightPresenter presenter) {
        this.id = id;
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_weight_statistic, container, false);

        imageButtonNext = view.findViewById(R.id.imageButton_next);
        imageButtonPrevious = view.findViewById(R.id.imageButton_previous);

        textViewDate = view.findViewById(R.id.textView_date);
        textViewCurrentDate = view.findViewById(R.id.textView_currentDate);
        textViewCurrentWeight = view.findViewById(R.id.textView_currentWeight);
        textViewWeightChange = view.findViewById(R.id.textView_weightChange);

        chart = view.findViewById(R.id.chart);

        chart.getDescription().setEnabled(false);
        chart.setDrawMarkers(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        LimitLine limitLine = new LimitLine(presenter.getWeightGoal());
        limitLine.setLineColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
        limitLine.enableDashedLine(6f, 6f, 1f);
        chart.getAxisLeft().addLimitLine(limitLine);
        chart.getAxisLeft().setAxisMinimum(10);
        chart.setMaxVisibleValueCount(0);
        chart.setScaleEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisRight().setDrawGridLines(false);

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == R.string.week) {
                    if (presenter.getWeekCount() > 0) {
                        presenter.changeWeekCount(-1);
                        presenter.setWeek();

                        chart.setData(presenter.getWeekData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getWeekDate());
                        textViewWeightChange.setText(presenter.getWeekWeightChange());
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
                if (id == R.string.month) {
                    if (presenter.getMonthCount() > 0) {
                        presenter.changeMonthCount(-1);
                        presenter.setMonth();

                        chart.setData(presenter.getMonthData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getMonthDate());
                        textViewWeightChange.setText(presenter.getMonthWeightChange());
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
                if (id == R.string.year) {
                    if (presenter.getYearCount() > 0) {
                        presenter.changeYearCount(-1);
                        presenter.setYear();

                        chart.setData(presenter.getYearData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getYearDate());
                        textViewWeightChange.setText(presenter.getYearWeightChange());
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
            }
        });
        imageButtonNext.setVisibility(View.INVISIBLE);

        imageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == R.string.week) {
                    if (presenter.getWeekCount() < 200) {
                        presenter.changeWeekCount(1);
                        presenter.setWeek();

                        chart.setData(presenter.getWeekData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getWeekDate());
                        textViewWeightChange.setText(presenter.getWeekWeightChange());
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
                if (id == R.string.month) {
                    if (presenter.getMonthCount() < 200) {
                        presenter.changeMonthCount(1);
                        presenter.setMonth();

                        chart.setData(presenter.getMonthData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getMonthDate());
                        textViewWeightChange.setText(presenter.getMonthWeightChange());
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
                if (id == R.string.year) {
                    if (presenter.getYearCount() < 200) {
                        presenter.changeYearCount(1);
                        presenter.setYear();

                        chart.setData(presenter.getYearData());
                        chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));
                        chart.invalidate();

                        chart.highlightValue(0, -1);

                        textViewDate.setText(presenter.getYearDate());
                        textViewWeightChange.setText(presenter.getYearWeightChange());
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
            }
        });

        if (id == R.string.week) {
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
                    textViewCurrentWeight.setText(String.format("%.1f", e.getY()) + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentWeight.setText("--" + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }
            });

            textViewDate.setText(presenter.getWeekDate());
            textViewWeightChange.setText(presenter.getWeekWeightChange());
        }
        if (id == R.string.month) {
            presenter.setMonth();

            chart.setData(presenter.getMonthData());
            chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));

            chart.invalidate();

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    textViewCurrentDate.setText(presenter.getDayInMonth((int) e.getX()));
                    textViewCurrentWeight.setText(String.format("%.1f", e.getY()) + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentWeight.setText("--" + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }
            });

            textViewDate.setText(presenter.getMonthDate());
            textViewWeightChange.setText(presenter.getMonthWeightChange());
        }
        if (id == R.string.year) {
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
                    textViewCurrentWeight.setText(String.format("%.1f", e.getY()) + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentWeight.setText("--" + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }
            });

            textViewDate.setText(presenter.getYearDate());
            textViewWeightChange.setText(presenter.getYearWeightChange());
        }

        chart.highlightValue(0, -1);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (id == R.string.week) {
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
                    textViewCurrentWeight.setText(String.format("%.1f", e.getY()) + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentWeight.setText("--" + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }
            });

            textViewDate.setText(presenter.getWeekDate());
            textViewWeightChange.setText(presenter.getWeekWeightChange());
        }
        if (id == R.string.month) {
            presenter.setMonth();

            chart.setData(presenter.getMonthData());
            chart.getAxisLeft().setAxisMaximum(presenter.getAxisMaximum(chart.getYMax()));

            chart.invalidate();

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    textViewCurrentDate.setText(presenter.getDayInMonth((int) e.getX()));
                    textViewCurrentWeight.setText(String.format("%.1f", e.getY()) + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentWeight.setText("--" + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }
            });

            textViewDate.setText(presenter.getMonthDate());
            textViewWeightChange.setText(presenter.getMonthWeightChange());
        }
        if (id == R.string.year) {
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
                    textViewCurrentWeight.setText(String.format("%.1f", e.getY()) + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }

                @Override
                public void onNothingSelected() {
                    textViewCurrentDate.setText("-");
                    textViewCurrentWeight.setText("--" + " " + getResources()
                            .getString(R.string.kilogram_abbreviation));
                }
            });

            textViewDate.setText(presenter.getYearDate());
            textViewWeightChange.setText(presenter.getYearWeightChange());
        }

        chart.highlightValue(0, -1);
    }
}
