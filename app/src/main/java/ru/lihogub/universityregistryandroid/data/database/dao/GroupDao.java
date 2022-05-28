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

import ru.lihogub.universityregistryandroid.data.database.model.Faculty;
import ru.lihogub.universityregistryandroid.data.database.model.Group;

@Dao
public interface GroupDao {
    @Query("SELECT * FROM groups")
    List<Group> findAll();

    @Query("SELECT * FROM groups")
    LiveData<List<Group>> findAllReactive();

    @Query("SELECT * FROM groups WHERE id = :id")
    Optional<Group> findById(Long id);

    @Insert(onConflict = IGNORE)
    void insert(Group group);

    @Update(onConflict = REPLACE)
    void save(Group group);
}
