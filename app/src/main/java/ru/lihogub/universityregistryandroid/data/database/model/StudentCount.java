package ru.lihogub.universityregistryandroid.data.database.model;

import androidx.room.ColumnInfo;

public class StudentCount {
    @ColumnInfo(name = "count_budget")
    public Long budget;

    @ColumnInfo(name = "count_commerce")
    public Long commerce;
}
