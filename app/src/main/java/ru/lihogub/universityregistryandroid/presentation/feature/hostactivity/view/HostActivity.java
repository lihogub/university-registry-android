package ru.lihogub.universityregistryandroid.presentation.feature.hostactivity.view;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import ru.lihogub.universityregistryandroid.R;
import ru.lihogub.universityregistryandroid.UniversityRegistryApp;
import ru.lihogub.universityregistryandroid.data.database.dao.FacultyDao;
import ru.lihogub.universityregistryandroid.data.database.model.Faculty;
import ru.lihogub.universityregistryandroid.presentation.feature.groupList.view.GroupListFragmentDirections;

public class HostActivity extends AppCompatActivity {
    private final FacultyDao facultyDao = UniversityRegistryApp.appDatabase.getFacultyDao();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setupNavigationDrawer();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupNavigationDrawer() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Menu menu = navigationView.getMenu();
        menu.add(0, 0, Menu.NONE, "Add faculty");
        facultyDao
                .findAllReactive()
                .observe(this, faculties -> {
                    menu.removeGroup(1);
                    faculties.forEach(faculty -> {
                        menu.add(1, faculty.id.intValue(), Menu.NONE, faculty.name);
                    });
                });
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getGroupId() == 0) {
                showAddFacultyModal();
            } else {
                Navigation
                        .findNavController(this, R.id.navHostFragment)
                        .navigate(
                                GroupListFragmentDirections
                                        .actionGlobalGroupListFragment(item.getItemId())
                        );
                drawerLayout.close();
            }
            return false;
        });
    }

    private void showAddFacultyModal() {
        Faculty faculty = new Faculty();
        EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Add faculty")
                .setView(editText)
                .setPositiveButton("Add", (dialog, which) -> {
                    faculty.name = editText.getText().toString();
                    facultyDao.insert(faculty);
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }
}