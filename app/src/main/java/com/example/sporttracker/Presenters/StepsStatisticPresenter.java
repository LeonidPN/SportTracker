package com.example.sporttracker.Presenters;

import androidx.core.content.ContextCompat;

import com.example.sporttracker.Models.StepsRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.StepsDatabase.StepsRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.StepsStatisticActivity;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StepsStatisticPresenter {

    private StepsStatisticActivity activity;

    private StepsRecordsRepository repository;

    private PreferencesRepository preferences;

    private ArrayList<StepsRecordModel> records;

    private Calendar[] day;
    private Calendar[] week;
    private Calendar[] month;
    private Calendar[] year;

    private int dayCount = 0;
    private int dayStepsCount = 0;

    private int weekCount = 0;
    private int weekStepsCount = 0;

    private int monthCount = 0;
    private int monthStepsCount = 0;

    private int yearCount = 0;
    private int yearStepsCount = 0;

    public StepsStatisticPresenter(StepsRecordsRepository repository,
                                   PreferencesRepository preferences) {
        this.repository = repository;
        this.preferences = preferences;
    }

    public void attachView(StepsStatisticActivity activity) {
        this.activity = activity;
    }

    public void detachView() {
        activity = null;
    }

    public void viewIsReady() {
        repository.open();
        records = (ArrayList) repository.getList();
        repository.close();
        records.sort(new Comparator<StepsRecordModel>() {
            @Override
            public int compare(StepsRecordModel o1, StepsRecordModel o2) {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    public void close() {
        activity.finish();
    }

    public void setDay() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, -dayCount);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        day = new Calendar[24];
        for (int i = 0; i < 24; i++) {
            day[i] = (Calendar) today.clone();
            today.add(Calendar.HOUR_OF_DAY, 1);
        }
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

    public void changeDayCount(int delta) {
        dayCount += delta;
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

    public int getAllCount() {
        return dayCount;
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

    public BarData getDayData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        dayStepsCount = 0;

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < day.length; i++) {
            int steps = 0;
            for (StepsRecordModel record : records) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < day[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == day[i].get(Calendar.YEAR)) {
                    if (calendar.get(Calendar.DAY_OF_YEAR) < day[i].get(Calendar.DAY_OF_YEAR)) {
                        break;
                    }
                    if (calendar.get(Calendar.DAY_OF_YEAR) == day[i].get(Calendar.DAY_OF_YEAR)) {
                        if (record.getHourInDay() < day[i].get(Calendar.HOUR_OF_DAY)) {
                            break;
                        }
                        if (record.getHourInDay() == day[i].get(Calendar.HOUR_OF_DAY)) {
                            steps += record.getCount();
                        }
                    }
                }
            }
            dayStepsCount += steps;
            entries.add(new BarEntry(i, steps));
        }

        BarDataSet dataset = new BarDataSet(entries, getResourceString(R.string.distance));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new BarData(dataset);
    }

    public BarData getWeekData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        weekStepsCount = 0;

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < week.length; i++) {
            int steps = 0;
            for (StepsRecordModel record : records) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < week[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == week[i].get(Calendar.YEAR)) {
                    if (calendar.get(Calendar.DAY_OF_YEAR) < week[i].get(Calendar.DAY_OF_YEAR)) {
                        break;
                    }
                    if (calendar.get(Calendar.DAY_OF_YEAR) == week[i].get(Calendar.DAY_OF_YEAR)) {
                        steps += record.getCount();
                    }
                }
            }
            weekStepsCount += steps;
            entries.add(new BarEntry(i, steps));
        }

        BarDataSet dataset = new BarDataSet(entries, getResourceString(R.string.distance));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new BarData(dataset);
    }

    public BarData getMonthData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        monthStepsCount = 0;

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < month.length; i++) {
            int steps = 0;
            for (StepsRecordModel record : records) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < month[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == month[i].get(Calendar.YEAR)) {
                    if (calendar.get(Calendar.DAY_OF_YEAR) < month[i].get(Calendar.DAY_OF_YEAR)) {
                        break;
                    }
                    if (calendar.get(Calendar.DAY_OF_YEAR) == month[i].get(Calendar.DAY_OF_YEAR)) {
                        steps += record.getCount();
                    }
                }
            }
            monthStepsCount += steps;
            entries.add(new BarEntry(i, steps));
        }

        BarDataSet dataset = new BarDataSet(entries, getResourceString(R.string.distance));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new BarData(dataset);
    }

    public BarData getYearData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        yearStepsCount = 0;

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < year.length; i++) {
            int steps = 0;
            for (StepsRecordModel record : records) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < year[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == year[i].get(Calendar.YEAR)) {
                    if (calendar.get(Calendar.MONTH) < year[i].get(Calendar.MONTH)) {
                        break;
                    }
                    if (calendar.get(Calendar.MONTH) == year[i].get(Calendar.MONTH)) {
                        steps += record.getCount();
                    }
                }
            }
            yearStepsCount += steps;
            entries.add(new BarEntry(i, steps / year[i].getActualMaximum(Calendar.DAY_OF_MONTH)));
        }

        BarDataSet dataset = new BarDataSet(entries, getResourceString(R.string.distance));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new BarData(dataset);
    }

    public String[] getDayLabels() {
        String[] labels = new String[day.length];

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        for (int i = 0; i < day.length; i++) {
            labels[i] = simpleDateFormat.format(day[i].getTime());
        }

        return labels;
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

    public String getDayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy ");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(day[0].getTime())
                + simpleTimeFormat.format(day[0].getTime()) + " - "
                + simpleTimeFormat.format(day[day.length - 1].getTime());
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

    public String getHourInDay(int number) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(day[number].getTime());
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

    public String getDayStepsCount() {
        return dayStepsCount + " " + getStepAbbreviation(dayStepsCount);
    }

    public String getWeekStepsCount() {
        return weekStepsCount + " " + getStepAbbreviation(weekStepsCount);
    }

    public String getMonthStepsCount() {
        return monthStepsCount + " " + getStepAbbreviation(monthStepsCount);
    }

    public String getYearStepsCount() {
        return yearStepsCount + " " + getStepAbbreviation(yearStepsCount);
    }

    public String getDayCalories() {
        float calories = 1f * Float.parseFloat(preferences.getWeight()) * dayStepsCount *
                (Float.parseFloat(preferences.getHeight()) / 4 + 25) / 100000 / 2;
        return String.format("%.1f", calories) + " " + getResourceString(R.string.calories_abbreviation);
    }

    public String getWeekCalories() {
        float calories = 1f * Float.parseFloat(preferences.getWeight()) * weekStepsCount *
                (Float.parseFloat(preferences.getHeight()) / 4 + 25) / 100000 / 2;
        return String.format("%.1f", calories) + " " + getResourceString(R.string.calories_abbreviation);
    }

    public String getMonthCalories() {
        float calories = 1f * Float.parseFloat(preferences.getWeight()) * monthStepsCount *
                (Float.parseFloat(preferences.getHeight()) / 4 + 25) / 100000 / 2;
        return String.format("%.1f", calories) + " " + getResourceString(R.string.calories_abbreviation);
    }

    public String getYearCalories() {
        float calories = 1f * Float.parseFloat(preferences.getWeight()) * yearStepsCount *
                (Float.parseFloat(preferences.getHeight()) / 4 + 25) / 100000 / 2;
        return String.format("%.1f", calories) + " " + getResourceString(R.string.calories_abbreviation);
    }

    public int getAxisMaximum(float max) {
        if (max == 0) {
            return 20;
        } else {
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

    private String getStepAbbreviation(int stepCount) {
        if (stepCount == 0) {
            return getResourceString(R.string.steps_notify_abbreviation_3);
        } else if (stepCount % 10 == 1) {
            return getResourceString(R.string.steps_notify_abbreviation_1);
        } else if (stepCount % 10 == 2 || stepCount % 10 == 3 || stepCount % 10 == 4) {
            return getResourceString(R.string.steps_notify_abbreviation_2);
        } else if (stepCount % 100 < 21) {
            return getResourceString(R.string.steps_notify_abbreviation_3);
        } else {
            return getResourceString(R.string.steps_notify_abbreviation_3);
        }
    }

    private String getResourceString(int id) {
        return activity.getResources().getString(id);
    }

}
