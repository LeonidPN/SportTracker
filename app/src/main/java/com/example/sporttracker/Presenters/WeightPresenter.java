package com.example.sporttracker.Presenters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.example.sporttracker.Models.WeightRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.WeightRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.WeightActivity;
import com.example.sporttracker.Views.Fragments.WeightInDayFragment;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class WeightPresenter {

    private WeightActivity activity;

    private WeightInDayFragment fragment;

    private WeightRecordsRepository repository;

    private PreferencesRepository preferences;

    private ArrayList<WeightRecordModel> records;

    private Calendar[] week;
    private Calendar[] month;
    private Calendar[] year;

    private int weekCount = 0;
    private float weekWeightChange = 0;

    private int monthCount = 0;
    private float monthWeightChange = 0;

    private int yearCount = 0;
    private float yearWeightChange = 0;

    public WeightPresenter(WeightRecordsRepository repository,
                           PreferencesRepository preferences) {
        this.repository = repository;
        this.preferences = preferences;
    }

    public void attachView(WeightActivity activity) {
        this.activity = activity;
    }

    public void attachView(WeightInDayFragment fragment) {
        this.fragment = fragment;
    }

    public void detachView() {
        activity = null;
    }

    public void fragmentIsReady() {
        updateRecordsList();
        ArrayList<WeightRecordModel> list = (ArrayList<WeightRecordModel>) records.clone();
        if (list.size() > 0) {
            final Date date = list.get(0).getDate();
            list.removeIf(new Predicate<WeightRecordModel>() {
                @Override
                public boolean test(WeightRecordModel n) {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(date);
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(n.getDate());
                    return (!(calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)));
                }
            });
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            float weight = list.get(0).getWeight();
            float start = preferences.getStartWeight();
            float goal = preferences.getWeightGoal();
            fragment.updateViews(simpleDateFormat.format(date), weight, start, goal, list);
        } else {
            fragment.updateViews("-1", preferences.getWeight(), -1, -1, null);
        }
    }

    public void updateRecordsList() {
        repository.open();
        ArrayList<WeightRecordModel> list = (ArrayList) repository.getList();
        repository.close();

        if (list != null) {
            list.sort(new Comparator<WeightRecordModel>() {
                @Override
                public int compare(WeightRecordModel o1, WeightRecordModel o2) {
                    return -o1.getDate().compareTo(o2.getDate());
                }
            });
        }

        records = list;
    }

    public void setWeek() {
        week = new Calendar[7];
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, (-7 * weekCount));
        for (int i = 0; i < 7; i++) {
            week[i] = (Calendar) today.clone();
            today.add(Calendar.DAY_OF_YEAR, (-1));
        }
        List<Calendar> list = Arrays.asList(week);
        Collections.reverse(list);
        week = (Calendar[]) list.toArray();
    }

    public void setMonth() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.MONTH, -monthCount);
        int daysCount = today.getActualMaximum(Calendar.DAY_OF_MONTH);
        today.set(Calendar.DAY_OF_MONTH, 1);
        month = new Calendar[daysCount];
        for (int i = 0; i < daysCount; i++) {
            month[i] = (Calendar) today.clone();
            today.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public void setYear() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.YEAR, -yearCount);
        today.set(Calendar.MONTH, 0);
        year = new Calendar[12];
        for (int i = 0; i < 12; i++) {
            year[i] = (Calendar) today.clone();
            today.add(Calendar.MONTH, 1);
        }
    }

    public void changeWeekCount(int delta) {
        weekCount += delta;
    }

    public void changeMonthCount(int delta) {
        monthCount += delta;
    }

    public void changeYearCount(int delta) {
        yearCount += delta;
    }

    public int getWeekCount() {
        return weekCount;
    }

    public int getMonthCount() {
        return monthCount;
    }

    public int getYearCount() {
        return yearCount;
    }

    public ScatterData getWeekData() {
        ArrayList<Entry> entries = new ArrayList<>();

        weekWeightChange = 0;

        Calendar calendar = Calendar.getInstance();
        ArrayList<Float> weights = new ArrayList<>();
        for (int i = 0; i < week.length; i++) {
            float weight = 0;
            int count = 0;
            for (WeightRecordModel record : records) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < week[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == week[i].get(Calendar.YEAR)) {
                    if (calendar.get(Calendar.DAY_OF_YEAR) < week[i].get(Calendar.DAY_OF_YEAR)) {
                        break;
                    }
                    if (calendar.get(Calendar.DAY_OF_YEAR) == week[i].get(Calendar.DAY_OF_YEAR)) {
                        weight += record.getWeight();
                        count++;
                    }
                }

            }
            float averageWeight = 0;
            if (count > 0) {
                averageWeight = weight / count;
                weights.add(averageWeight);
            }
            entries.add(new Entry(i, averageWeight));
        }
        if (weights.size() == 0) {
            weekWeightChange = 0;
        } else {
            weekWeightChange = weights.get(weights.size() - 1) - weights.get(0);
        }

        ScatterDataSet dataset = new ScatterDataSet(entries, getResourceString(R.string.distance));
        dataset.setHighLightColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new ScatterData(dataset);
    }

    public ScatterData getMonthData() {
        ArrayList<Entry> entries = new ArrayList<>();

        monthWeightChange = 0;

        Calendar calendar = Calendar.getInstance();
        ArrayList<Float> weights = new ArrayList<>();
        for (int i = 0; i < month.length; i++) {
            float weight = 0;
            int count = 0;
            for (WeightRecordModel record : records) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < month[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == month[i].get(Calendar.YEAR)) {
                    if (calendar.get(Calendar.DAY_OF_YEAR) < month[i].get(Calendar.DAY_OF_YEAR)) {
                        break;
                    }
                    if (calendar.get(Calendar.DAY_OF_YEAR) == month[i].get(Calendar.DAY_OF_YEAR)) {
                        weight += record.getWeight();
                        count++;
                    }
                }

            }
            float averageWeight = 0;
            if (count > 0) {
                averageWeight = weight / count;
                weights.add(averageWeight);
            }
            entries.add(new Entry(i, averageWeight));
        }
        if (weights.size() == 0) {
            monthWeightChange = 0;
        } else {
            monthWeightChange = weights.get(weights.size() - 1) - weights.get(0);
        }

        ScatterDataSet dataset = new ScatterDataSet(entries, getResourceString(R.string.distance));
        dataset.setHighLightColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new ScatterData(dataset);
    }

    public ScatterData getYearData() {
        ArrayList<Entry> entries = new ArrayList<>();

        yearWeightChange = 0;

        Calendar calendar = Calendar.getInstance();
        ArrayList<Float> weights = new ArrayList<>();
        for (int i = 0; i < year.length; i++) {
            float weight = 0;
            int count = 0;
            for (WeightRecordModel record : records) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < year[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == year[i].get(Calendar.YEAR)) {
                    if (calendar.get(Calendar.MONTH) < year[i].get(Calendar.MONTH)) {
                        break;
                    }
                    if (calendar.get(Calendar.MONTH) == year[i].get(Calendar.MONTH)) {
                        weight += record.getWeight();
                        count++;
                    }
                }

            }
            float averageWeight = 0;
            if (count > 0) {
                averageWeight = weight / count;
                weights.add(averageWeight);
            }
            entries.add(new Entry(i, averageWeight));
        }
        if (weights.size() == 0) {
            yearWeightChange = 0;
        } else {
            yearWeightChange = weights.get(weights.size() - 1) - weights.get(0);
        }

        ScatterDataSet dataset = new ScatterDataSet(entries, getResourceString(R.string.distance));
        dataset.setHighLightColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new ScatterData(dataset);
    }

    public String[] getWeekLabels() {
        String[] labels = new String[week.length];

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");

        for (int i = 0; i < week.length; i++) {
            labels[i] = simpleDateFormat.format(week[i].getTime());
        }

        return labels;
    }

    public String[] getYearLabels() {
        String[] labels = new String[year.length];

        for (int i = 0; i < year.length; i++) {
            labels[i] = (i + 1) + "";
        }

        return labels;
    }

    public String getWeekDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(week[0].getTime()) + " - "
                + simpleDateFormat.format(week[week.length - 1].getTime());
    }

    public String getMonthDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(month[0].getTime()) + " - "
                + simpleDateFormat.format(month[month.length - 1].getTime());
    }

    public String getYearDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.yyyy");
        return simpleDateFormat.format(year[0].getTime()) + " - "
                + simpleDateFormat.format(year[year.length - 1].getTime());
    }

    public String getDayInWeek(int number) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM");
        return simpleDateFormat.format(week[number].getTime());
    }

    public String getDayInMonth(int number) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM");
        return simpleDateFormat.format(month[number].getTime());
    }

    public String getMonthInYear(int number) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLLL");
        return simpleDateFormat.format(year[number].getTime());
    }

    public String getWeekWeightChange() {
        if (weekWeightChange > 0) {
            return "+" + String.format("%.1f", weekWeightChange) + " " +
                    getResourceString(R.string.kilogram_abbreviation);
        } else if (weekWeightChange < 0) {
            return "-" + String.format("%.1f", -weekWeightChange) + " " +
                    getResourceString(R.string.kilogram_abbreviation);
        } else {
            return String.format("%.1f", weekWeightChange) + " " +
                    getResourceString(R.string.kilogram_abbreviation);
        }
    }

    public String getMonthWeightChange() {
        if (monthWeightChange > 0) {
            return "+" + String.format("%.1f", monthWeightChange) + " " +
                    getResourceString(R.string.kilogram_abbreviation);
        } else if (monthWeightChange < 0) {
            return "-" + String.format("%.1f", -monthWeightChange) + " " +
                    getResourceString(R.string.kilogram_abbreviation);
        } else {
            return String.format("%.1f", monthWeightChange) + " " +
                    getResourceString(R.string.kilogram_abbreviation);
        }
    }

    public String getYearWeightChange() {
        if (yearWeightChange > 0) {
            return "+" + String.format("%.1f", yearWeightChange) + " " +
                    getResourceString(R.string.kilogram_abbreviation);
        } else if (yearWeightChange < 0) {
            return "-" + String.format("%.1f", -yearWeightChange) + " " +
                    getResourceString(R.string.kilogram_abbreviation);
        } else {
            return String.format("%.1f", yearWeightChange) + " " +
                    getResourceString(R.string.kilogram_abbreviation);
        }
    }

    public int getAxisMaximum(float max) {
        if (max == 0) {
            return 70;
        } else {
            max = Math.max(max, preferences.getWeightGoal());
            int maxValue = (int) max;
            int digits = 0;
            int lastDigit = 0;
            while (maxValue > 0) {
                lastDigit = maxValue % 10;
                digits++;
                maxValue /= 10;
            }
            if (digits > 0) {
                return (lastDigit + 1) * (int) Math.pow(10, digits - 1);
            } else {
                return 1;
            }
        }
    }

    public int getAxisMinimum(float min) {
        if (min == 0) {
            return 50;
        } else {
            int minValue = (int) min;
            int digits = 0;
            int lastDigit = 0;
            while (minValue > 0) {
                lastDigit = minValue % 10;
                digits++;
                minValue /= 10;
            }
            if (digits > 0) {
                return Math.max((lastDigit - 1) * (int) Math.pow(10, digits - 1), 0);
            } else {
                return 1;
            }
        }
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
                fragmentIsReady();
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

    public int getWeightGoal() {
        return preferences.getWeightGoal();
    }

    public void close() {
        activity.finish();
    }

    public void startActivity(Class<?> T) {
        Intent intent = new Intent(activity.getContext(), T);
        activity.getContext().startActivity(intent);
    }

    private String getResourceString(int id) {
        return activity.getResources().getString(id);
    }

}
