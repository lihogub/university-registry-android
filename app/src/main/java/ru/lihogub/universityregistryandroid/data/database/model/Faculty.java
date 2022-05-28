package ru.lihogub.universityregistryandroid.data.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "faculties")
public class Faculty {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "name")
    public String name;
}
