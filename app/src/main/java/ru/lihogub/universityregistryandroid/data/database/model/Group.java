package ru.lihogub.universityregistryandroid.data.database.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
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

    @Embedded(prefix = "direction_")
    public StudyDirection direction;

    @ColumnInfo(name = "fulltime_tuition")
    public Boolean fulltimeTuition;

    @Embedded(prefix = "count_")
    public StudentCount studentCount;
}
