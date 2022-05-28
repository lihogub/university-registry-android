package ru.lihogub.universityregistryandroid.data.database.dao;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Optional;

import ru.lihogub.universityregistryandroid.data.database.model.Faculty;

@Dao
public interface FacultyDao {
    @Query("SELECT * FROM faculties")
    List<Faculty> findAll();

    @Query("SELECT * FROM faculties")
    LiveData<List<Faculty>> findAllReactive();

    @Query("SELECT * FROM faculties WHERE id = :id")
    Optional<Faculty> findById(Long id);

    @Insert(onConflict = IGNORE)
    void insert(Faculty faculty);

    @Update(onConflict = REPLACE)
    void save(Faculty faculty);

    @Query("DELETE FROM faculties WHERE id = :id")
    void delete(Long id);
}
