package ru.lihogub.universityregistryandroid;

import android.app.Application;

import androidx.room.Room;

import ru.lihogub.universityregistryandroid.data.database.AppDatabase;

public class UniversityRegistryApp extends Application {
    public static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
    }
}
