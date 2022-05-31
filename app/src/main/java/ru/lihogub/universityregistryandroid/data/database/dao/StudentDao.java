package ru.lihogub.universityregistryandroid.data.database.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Optional;

import ru.lihogub.universityregistryandroid.data.database.model.Student;

@Dao
public interface StudentDao {
    @Query("SELECT * FROM students WHERE id = :id")
    Optional<Student> findById(Long id);

    @Query("SELECT * FROM students WHERE group_id = :groupId")
    LiveData<List<Student>> findAllByGroupIdReactive(Long groupId);

    @Insert(onConflict = REPLACE)
    void insert(Student student);

    @Update(onConflict = REPLACE)
    void save(Student student);

    @Query("DELETE FROM students WHERE id = :id")
    void delete(Long id);
}
