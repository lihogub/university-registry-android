package ru.lihogub.universityregistryandroid.data.database.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;


@Entity(tableName = "groups", foreignKeys = {
        @ForeignKey(entity = Faculty.class, parentColumns = "id", childColumns = "faculty_id", onDelete = CASCADE)
})
public class Group {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "faculty_id")
    public Long facultyId;

    @ColumnInfo(name = "direction_code")
    public String directionCode;

    @ColumnInfo(name = "direction_name")
    public String directionName;

    @ColumnInfo(name = "direction_profile")
    public String directionProfile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(name, group.name) && Objects.equals(facultyId, group.facultyId) && Objects.equals(directionCode, group.directionCode) && Objects.equals(directionName, group.directionName) && Objects.equals(directionProfile, group.directionProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, facultyId, directionCode, directionName, directionProfile);
    }
}
