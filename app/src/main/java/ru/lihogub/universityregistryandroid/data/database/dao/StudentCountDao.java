package ru.lihogub.universityregistryandroid.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import ru.lihogub.universityregistryandroid.data.database.model.StudentCount;

@Dao
public interface StudentCountDao {
    @Query("WITH selected_students AS (SELECT * FROM students WHERE group_id = :groupId) " +
            "SELECT " +
            "(SELECT COUNT(*) FROM selected_students WHERE is_budget = 1) count_budget, " +
            "(SELECT COUNT(*) FROM selected_students WHERE is_budget = 0) count_commerce")
    LiveData<StudentCount> getByGroupId(Long groupId);
}
