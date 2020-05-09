package com.example.sporttracker.Presenters;

import androidx.core.content.ContextCompat;

import com.example.sporttracker.Models.Enumerations.Activities;
import com.example.sporttracker.Models.ExerciseRecordModel;
import com.example.sporttracker.R;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.ExerciseRecordsRepository;
import com.example.sporttracker.Services.Repositories.PreferencesRepository;
import com.example.sporttracker.Views.Activities.StatisticActivity;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class StatisticPresenter {

    private StatisticActivity activity;

    private ExerciseRecordsRepository repository;

    private PreferencesRepository preferences;

    private ArrayList<ExerciseRecordModel> exerciseRecords;

    private Calendar[] week;
    private Calendar[] month;
    private Calendar[] year;
    private Calendar[] all;

    private int weekCount = 0;
    private long weekDuration = 0;
    private long weekDurationInMilis = 0;
    private float weekDistance = 0;

    private int monthCount = 0;
    private long monthDuration = 0;
    private long monthDurationInMilis = 0;
    private float monthDistance = 0;

    private int yearCount = 0;
    private long yearDuration = 0;
    private long yearDurationInMilis = 0;
    private float yearDistance = 0;

    private int allCount = 0;
    private long allDuration = 0;
    private long allDurationInMilis = 0;
    private float allDistance = 0;

    public StatisticPresenter(ExerciseRecordsRepository repository,
                              PreferencesRepository preferences) {
        this.repository = repository;
        this.preferences = preferences;
    }

    public void attachView(StatisticActivity activity) {
        this.activity = activity;
    }

    public void detachView() {
        activity = null;
    }

    public void viewIsReady() {
        activity.updateActivityList(Activities.values());
    }

    public void updateRecordsList() {
        final String exercise = activity.getExercise();

        repository.open();
        ArrayList<ExerciseRecordModel> list = (ArrayList) repository.getList();
        repository.close();

        if (list != null) {
            list.removeIf(new Predicate<ExerciseRecordModel>() {
                @Override
                public boolean test(ExerciseRecordModel n) {
                    return (!n.getActivity().equals(exercise));
                }
            });
            list.sort(new Comparator<ExerciseRecordModel>() {
                @Override
                public int compare(ExerciseRecordModel o1, ExerciseRecordModel o2) {
                    return -o1.getDate().compareTo(o2.getDate());
                }
            });
        }

        exerciseRecords = list;
    }

    public void close() {
        activity.finish();
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

    public void setAll() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.YEAR, -allCount * 5);
        today.set(Calendar.MONTH, 0);
        all = new Calendar[5];
        for (int i = 0; i < 5; i++) {
            all[i] = (Calendar) today.clone();
            today.add(Calendar.YEAR, -1);
        }
        List<Calendar> list = Arrays.asList(all);
        Collections.reverse(list);
        all = (Calendar[]) list.toArray();
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

    public void changeAllCount(int delta) {
        allCount += delta;
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

    public int getAllCount() {
        return allCount;
    }

    public BarData getWeekData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        weekDuration = 0;
        weekDurationInMilis = 0;
        weekDistance = 0;
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat();

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < week.length; i++) {
            float distance = 0;
            long duration = 0;
            long durationInMilis = 0;
            for (ExerciseRecordModel record : exerciseRecords) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < week[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == week[i].get(Calendar.YEAR)) {
                    if (calendar.get(Calendar.DAY_OF_YEAR) < week[i].get(Calendar.DAY_OF_YEAR)) {
                        break;
                    }
                    if (calendar.get(Calendar.DAY_OF_YEAR) == week[i].get(Calendar.DAY_OF_YEAR)) {
                        distance += record.getDistance();
                        simpleTimeFormat.applyPattern("H");
                        duration += (Long.parseLong(simpleTimeFormat.format(new Date(record.getTime()))) * 60);
                        simpleTimeFormat.applyPattern("m");
                        duration += Long.parseLong(simpleTimeFormat.format(new Date(record.getTime())));
                        durationInMilis += record.getTime();
                    }
                }

            }
            weekDistance += distance;
            weekDuration += duration;
            weekDurationInMilis += durationInMilis;
            entries.add(new BarEntry(i, distance));
        }

        BarDataSet dataset = new BarDataSet(entries, getResourceString(R.string.distance));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new BarData(dataset);
    }

    public BarData getMonthData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        monthDuration = 0;
        monthDurationInMilis = 0;
        monthDistance = 0;
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat();

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < month.length; i++) {
            float distance = 0;
            long duration = 0;
            long durationInMilis = 0;
            for (ExerciseRecordModel record : exerciseRecords) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < month[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == month[i].get(Calendar.YEAR)) {
                    if (calendar.get(Calendar.DAY_OF_YEAR) < month[i].get(Calendar.DAY_OF_YEAR)) {
                        break;
                    }
                    if (calendar.get(Calendar.DAY_OF_YEAR) == month[i].get(Calendar.DAY_OF_YEAR)) {
                        distance += record.getDistance();
                        simpleTimeFormat.applyPattern("H");
                        duration += (Long.parseLong(simpleTimeFormat.format(new Date(record.getTime()))) * 60);
                        simpleTimeFormat.applyPattern("m");
                        duration += Long.parseLong(simpleTimeFormat.format(new Date(record.getTime())));
                        durationInMilis += record.getTime();
                    }
                }

            }
            monthDistance += distance;
            monthDuration += duration;
            monthDurationInMilis += durationInMilis;
            entries.add(new BarEntry(i, distance));
        }

        BarDataSet dataset = new BarDataSet(entries, getResourceString(R.string.distance));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new BarData(dataset);
    }

    public BarData getYearData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        yearDuration = 0;
        yearDurationInMilis = 0;
        yearDistance = 0;
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat();

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < year.length; i++) {
            float distance = 0;
            long duration = 0;
            long durationInMilis = 0;
            for (ExerciseRecordModel record : exerciseRecords) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < year[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == year[i].get(Calendar.YEAR)) {
                    if (calendar.get(Calendar.MONTH) < year[i].get(Calendar.MONTH)) {
                        break;
                    }
                    if (calendar.get(Calendar.MONTH) == year[i].get(Calendar.MONTH)) {
                        distance += record.getDistance();
                        simpleTimeFormat.applyPattern("H");
                        duration += (Long.parseLong(simpleTimeFormat.format(new Date(record.getTime()))) * 60);
                        simpleTimeFormat.applyPattern("m");
                        duration += Long.parseLong(simpleTimeFormat.format(new Date(record.getTime())));
                        durationInMilis += record.getTime();
                    }
                }

            }
            yearDistance += distance;
            yearDuration += duration;
            yearDurationInMilis += durationInMilis;
            entries.add(new BarEntry(i, distance));
        }

        BarDataSet dataset = new BarDataSet(entries, getResourceString(R.string.distance));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new BarData(dataset);
    }

    public BarData getAllData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        allDuration = 0;
        allDurationInMilis = 0;
        allDistance = 0;
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat();

        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < all.length; i++) {
            float distance = 0;
            long duration = 0;
            long durationInMilis = 0;
            for (ExerciseRecordModel record : exerciseRecords) {
                calendar.setTime(record.getDate());
                if (calendar.get(Calendar.YEAR) < all[i].get(Calendar.YEAR)) {
                    break;
                }
                if (calendar.get(Calendar.YEAR) == all[i].get(Calendar.YEAR)) {
                    distance += record.getDistance();
                    simpleTimeFormat.applyPattern("H");
                    duration += (Long.parseLong(simpleTimeFormat.format(new Date(record.getTime()))) * 60);
                    simpleTimeFormat.applyPattern("m");
                    duration += Long.parseLong(simpleTimeFormat.format(new Date(record.getTime())));
                    durationInMilis += record.getTime();
                }

            }
            allDistance += distance;
            allDuration += duration;
            allDurationInMilis += durationInMilis;
            entries.add(new BarEntry(i, distance));
        }

        BarDataSet dataset = new BarDataSet(entries, getResourceString(R.string.distance));
        dataset.setColor(ContextCompat.getColor(activity.getContext(), R.color.colorAccent));

        return new BarData(dataset);
    }

    public String[] getWeekLabels() {
        String[] labels = new String[week.length];

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");

        for (int i = 0; i < week.length; i++) {
            labels[i] = simpleDateFormat.format(week[i].getTime());
        }

        return labels;
    }

    public String[] getMonthLabels() {
        String[] labels = new String[month.length];

        for (int i = 0; i < month.length; i++) {
            labels[i] = (i + 1) + "";
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

    public String[] getAllLabels() {
        String[] labels = new String[all.length];

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

        for (int i = 0; i < all.length; i++) {
            labels[i] = simpleDateFormat.format(all[i].getTime());
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

    public String getAllDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(all[0].getTime()) + " - "
                + simpleDateFormat.format(all[all.length - 1].getTime());
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

    public String getYearInAll(int number) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(all[number].getTime());
    }

    public String getWeekDistance() {
        return weekDistance + " м";
    }

    public String getMonthDistance() {
        return monthDistance + " м";
    }

    public String getYearDistance() {
        return yearDistance + " м";
    }

    public String getAllDistance() {
        return allDistance + " м";
    }

    public String getWeekDuration() {
        return weekDuration + " мин";
    }

    public String getMonthDuration() {
        return monthDuration + " мин";
    }

    public String getYearDuration() {
        return yearDuration + " мин";
    }

    public String getAllDuration() {
        return allDuration + " мин";
    }

    public String getWeekCalories() {
        float calories = 0;
        float distance = weekDistance;
        long time = weekDurationInMilis;
        if (time == 0) {
            return String.format("%.1f", 0.0f) + " ккал";
        }
        if (activity.getExercise().equals(Activities.RUN.getName())) {
            calories = Activities.RUN.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.CYCLE.getName())) {
            calories = Activities.CYCLE.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.SWIM.getName())) {
            calories = Activities.SWIM.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.WALK.getName())) {
            calories = Activities.WALK.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        return String.format("%.1f", calories) + " ккал";
    }

    public String getMonthCalories() {
        float calories = 0;
        float distance = monthDistance;
        long time = monthDurationInMilis;
        if (time == 0) {
            return String.format("%.1f", 0.0f) + " ккал";
        }
        if (activity.getExercise().equals(Activities.RUN.getName())) {
            calories = Activities.RUN.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.CYCLE.getName())) {
            calories = Activities.CYCLE.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.SWIM.getName())) {
            calories = Activities.SWIM.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.WALK.getName())) {
            calories = Activities.WALK.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        return String.format("%.1f", calories) + " ккал";
    }

    public String getYearCalories() {
        float calories = 0;
        float distance = yearDistance;
        long time = yearDurationInMilis;
        if (time == 0) {
            return String.format("%.1f", 0.0f) + " ккал";
        }
        if (activity.getExercise().equals(Activities.RUN.getName())) {
            calories = Activities.RUN.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.CYCLE.getName())) {
            calories = Activities.CYCLE.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.SWIM.getName())) {
            calories = Activities.SWIM.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.WALK.getName())) {
            calories = Activities.WALK.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        return String.format("%.1f", calories) + " ккал";
    }

    public String getAllCalories() {
        float calories = 0;
        float distance = allDistance;
        long time = allDurationInMilis;
        if (time == 0) {
            return String.format("%.1f", 0.0f) + " ккал";
        }
        if (activity.getExercise().equals(Activities.RUN.getName())) {
            calories = Activities.RUN.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.CYCLE.getName())) {
            calories = Activities.CYCLE.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.SWIM.getName())) {
            calories = Activities.SWIM.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        if (activity.getExercise().equals(Activities.WALK.getName())) {
            calories = Activities.WALK.getCalories(distance / (float) time / 1000 * 3600)
                    * time * Float.parseFloat(preferences.getWeight());
        }
        return String.format("%.1f", calories) + " ккал";
    }

    private String getResourceString(int id) {
        return activity.getResources().getString(id);
    }

}
