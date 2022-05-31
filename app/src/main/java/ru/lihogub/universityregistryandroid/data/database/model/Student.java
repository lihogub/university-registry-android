package ru.lihogub.universityregistryandroid.data.database.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "students", foreignKeys = {
        @ForeignKey(entity = Group.class, parentColumns = "id", childColumns = "group_id", onDelete = CASCADE)
})
public class Student {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "group_id")
    public Long groupId;

    @ColumnInfo(name = "firstname")
    public String firstname;

    @ColumnInfo(name = "middlename")
    public String middlename;

    @ColumnInfo(name = "lastname")
    public String lastname;

    @ColumnInfo(name = "is_budget")
    public boolean isBudget;
}
