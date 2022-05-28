package ru.lihogub.universityregistryandroid.data.database.dao;

import static androidx.room.OnConflictStrategy.IGNORE;
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
    @Query("SELECT * FROM students")
    List<Student> findAll();

    @Query("SELECT * FROM students")
    LiveData<List<Student>> findAllReactive();

    @Query("SELECT * FROM students WHERE groupId = :groupId")
    List<Student> findAllByGroup(Long groupId);

    @Query("SELECT * FROM students WHERE id = :id")
    Optional<Student> findById(Long id);

    @Insert(onConflict = IGNORE)
    void insert(Student student);

    @Update(onConflict = REPLACE)
    void save(Student student);
}
