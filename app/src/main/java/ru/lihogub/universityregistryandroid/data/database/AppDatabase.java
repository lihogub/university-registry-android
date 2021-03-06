package ru.lihogub.universityregistryandroid.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.lihogub.universityregistryandroid.data.database.dao.FacultyDao;
import ru.lihogub.universityregistryandroid.data.database.dao.GroupDao;
import ru.lihogub.universityregistryandroid.data.database.dao.StudentCountDao;
import ru.lihogub.universityregistryandroid.data.database.dao.StudentDao;
import ru.lihogub.universityregistryandroid.data.database.model.Faculty;
import ru.lihogub.universityregistryandroid.data.database.model.Group;
import ru.lihogub.universityregistryandroid.data.database.model.Student;

@Database(
        entities = {Group.class, Faculty.class, Student.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GroupDao getGroupDao();
    public abstract FacultyDao getFacultyDao();
    public abstract StudentDao getStudentDao();
    public abstract StudentCountDao getStudentCountDao();
}
