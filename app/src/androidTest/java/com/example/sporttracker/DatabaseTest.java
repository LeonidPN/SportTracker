package com.example.sporttracker;


import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.sporttracker.Models.WeightRecordModel;
import com.example.sporttracker.Services.Repositories.Databases.ActivitiesDatabase.WeightRecordsRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private static final String TAG = "TestInfo";

    private WeightRecordsRepository repository;

    @Before
    public void initRepository() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        repository = new WeightRecordsRepository(appContext);
        repository.open();
        ArrayList<WeightRecordModel> list = (ArrayList<WeightRecordModel>) repository.getList();
        for (WeightRecordModel model : list) {
            repository.delete(model.getId());
        }
    }

    @Test
    public void test1000Entities() {
        Log.i(TAG, "Тест №1");
        Log.i(TAG, "Количество сущностей: 1000");
        WeightRecordModel model = new WeightRecordModel();
        model.setDate(new Date());
        model.setWeight(60);

        for (int i = 0; i < 1000; i++) {
            repository.insert(model);
        }

        long start = System.currentTimeMillis();
        repository.getList();
        long end = System.currentTimeMillis();

        Log.i(TAG, "Время получения списка моделей: " + (end - start));

        ArrayList<WeightRecordModel> list = (ArrayList<WeightRecordModel>) repository.getList();
        for (WeightRecordModel m : list) {
            repository.delete(m.getId());
        }

        assertTrue(true);
    }

    @Test
    public void test10000Entities() {
        Log.i(TAG, "Тест №2");
        Log.i(TAG, "Количество сущностей: 10000");
        WeightRecordModel model = new WeightRecordModel();
        model.setDate(new Date());
        model.setWeight(60);

        for (int i = 0; i < 10000; i++) {
            repository.insert(model);
        }

        long start = System.currentTimeMillis();
        repository.getList();
        long end = System.currentTimeMillis();

        Log.i(TAG, "Время получения списка моделей: " + (end - start));

        ArrayList<WeightRecordModel> list = (ArrayList<WeightRecordModel>) repository.getList();
        for (WeightRecordModel m : list) {
            repository.delete(m.getId());
        }

        assertTrue(true);
    }

    @Test
    public void test100000Entities() {
        Log.i(TAG, "Тест №3");
        Log.i(TAG, "Количество сущностей: 100000");
        WeightRecordModel model = new WeightRecordModel();
        model.setDate(new Date());
        model.setWeight(60);

        for (int i = 0; i < 100000; i++) {
            repository.insert(model);
        }

        long start = System.currentTimeMillis();
        repository.getList();
        long end = System.currentTimeMillis();

        Log.i(TAG, "Время получения списка моделей: " + (end - start));

        ArrayList<WeightRecordModel> list = (ArrayList<WeightRecordModel>) repository.getList();
        for (WeightRecordModel m : list) {
            repository.delete(m.getId());
        }

        assertTrue(true);
    }

    @After
    public void closeRepository() {
        repository.close();
    }
}
