package ru.lihogub.universityregistryandroid.data.database.dao;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.lihogub.universityregistryandroid.data.database.model.Group;
import ru.lihogub.universityregistryandroid.data.database.model.StudentCount;

@Dao
public interface GroupDao {
    @Query("SELECT * FROM groups")
    List<Group> findAll();

    @Query("SELECT * FROM groups")
    LiveData<List<Group>> findAllReactive();

    @Query("SELECT * FROM groups WHERE id = :id")
    Optional<Group> findById(Long id);

    @Query("SELECT * FROM groups WHERE faculty_id = :facultyId")
    LiveData<List<Group>> findAllByFacultyIdReactive(Long facultyId);

    @Query("WITH groups_selected AS (SELECT * FROM groups g WHERE g.faculty_id = :facultyId), " +
            "students_selected AS (SELECT * FROM groups_selected gs JOIN students s ON gs.id = s.group_id)" +
            "SELECT gs.id, gs.faculty_id, gs.name, gs.direction_code, gs.direction_name, gs.direction_profile, " +
            "(SELECT COUNT(*) FROM students_selected ss WHERE ss.group_id = gs.id and is_budget = 1) count_budget, " +
            "(SELECT COUNT(*) FROM students_selected ss WHERE ss.group_id = gs.id and is_budget = 0) count_commerce " +
            "FROM groups_selected gs " +
            "GROUP BY gs.id")
    LiveData<Map<Group, StudentCount>> findAllWithStudentCountByFacultyId(Long facultyId);

    @Insert(onConflict = IGNORE)
    void insert(Group group);

    @Update(onConflict = REPLACE)
    void save(Group group);

    @Query("DELETE FROM groups WHERE id = :id")
    void delete(Long id);
}
