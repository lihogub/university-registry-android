package ru.lihogub.universityregistryandroid.presentation.feature.studentDetails.view;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ru.lihogub.universityregistryandroid.R;
import ru.lihogub.universityregistryandroid.UniversityRegistryApp;
import ru.lihogub.universityregistryandroid.data.database.dao.StudentDao;
import ru.lihogub.universityregistryandroid.data.database.model.Student;
import ru.lihogub.universityregistryandroid.databinding.FragmentStudentDetailsBinding;
import ru.lihogub.universityregistryandroid.presentation.feature.groupList.view.GroupListFragmentDirections;

public class StudentDetailsFragment extends Fragment {
    private final StudentDao studentDao = UniversityRegistryApp.appDatabase.getStudentDao();
    private FragmentStudentDetailsBinding binding;
    private Long currentFacultyId;
    private Long currentGroupId;
    private Long currentStudentId;
    private Student student;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentFacultyId = StudentDetailsFragmentArgs.fromBundle(getArguments()).getFacultyId();
        currentGroupId = StudentDetailsFragmentArgs.fromBundle(getArguments()).getGroupId();
        currentStudentId = StudentDetailsFragmentArgs.fromBundle(getArguments()).getStudentId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        binding.includedToolBar.myToolbar.inflateMenu(R.menu.student_details_menu);
        binding.includedToolBar.myToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.closeMenuOption: {
                    closeGroupDetailsModal();
                    return true;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();
        student = studentDao
                .findById(currentStudentId)
                .orElseGet(() -> {
                    Student s = new Student();
                    s.groupId = currentGroupId;
                    return s;
                });

        binding.studentFirstnameEditText.setText(student.firstname);
        binding.studentMiddlenameEditText.setText(student.middlename);
        binding.studentLastnameEditText.setText(student.lastname);
        binding.studentPaymentToggleButton.setChecked(student.isBudget);

        binding.includedToolBar.titleTextView.setText("Детали студента");
        binding.includedToolBar.showDrawerButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.open();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void closeGroupDetailsModal() {
        StudentDetailsFragmentDirections.ActionStudentDetailsFragmentToGroupDetailsFragment2 action = StudentDetailsFragmentDirections.actionStudentDetailsFragmentToGroupDetailsFragment2();
        action.setGroupId(currentGroupId);
        new AlertDialog.Builder(requireContext())
                .setTitle("Сохранить студента?")
                .setPositiveButton("Сохранить и выйти", (dialog, which) -> {
                    student.firstname = binding.studentFirstnameEditText.getText().toString();
                    student.middlename = binding.studentMiddlenameEditText.getText().toString();
                    student.lastname = binding.studentLastnameEditText.getText().toString();
                    student.isBudget = binding.studentPaymentToggleButton.isChecked();

                    if (currentStudentId == 0) {
                        studentDao.insert(student);
                    } else {
                        studentDao.save(student);
                    }

                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(action);
                })
                .setNegativeButton("Выйти без сохранения", (dialog, which) -> {
                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(action);
                    dialog.cancel();
                })
                .show();
    }
}
