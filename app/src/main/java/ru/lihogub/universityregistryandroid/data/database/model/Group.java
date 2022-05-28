package ru.lihogub.universityregistryandroid.data.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "groups", foreignKeys = {
        @ForeignKey(entity = Faculty.class, parentColumns = "id", childColumns = "facultyId")
})
public class Group {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "name")
    public Long name;

    @ColumnInfo(name = "facultyId")
    public Long facultyId;
}
