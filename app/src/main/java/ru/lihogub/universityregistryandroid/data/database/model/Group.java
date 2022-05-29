package ru.lihogub.universityregistryandroid.data.database.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "groups", foreignKeys = {
        @ForeignKey(entity = Faculty.class, parentColumns = "id", childColumns = "facultyId", onDelete = CASCADE)
})
public class Group {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "facultyId")
    public Long facultyId;

    @ColumnInfo(name = "direction_code")
    public String directionCode;

    @ColumnInfo(name = "direction_name")
    public String directionName;

    @ColumnInfo(name = "direction_profile")
    public String directionProfile;

    @ColumnInfo(name = "fulltime_tuition")
    public Boolean fulltimeTuition;

    @ColumnInfo(name = "count_budget")
    public long countBudget;

    @ColumnInfo(name = "count_commerce")
    public long countCommerce;
}
