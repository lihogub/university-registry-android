package ru.lihogub.universityregistryandroid.presentation.feature.groupDetails.view;

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
import ru.lihogub.universityregistryandroid.data.database.dao.GroupDao;
import ru.lihogub.universityregistryandroid.data.database.dao.StudentCountDao;
import ru.lihogub.universityregistryandroid.data.database.dao.StudentDao;
import ru.lihogub.universityregistryandroid.data.database.model.Group;
import ru.lihogub.universityregistryandroid.data.database.model.StudentCount;
import ru.lihogub.universityregistryandroid.databinding.FragmentGroupDetailsBinding;
import ru.lihogub.universityregistryandroid.presentation.feature.groupList.view.GroupListFragmentDirections;
import ru.lihogub.universityregistryandroid.presentation.feature.studentDetails.view.StudentDetailsFragmentDirections;

public class GroupDetailsFragment extends Fragment {
    private final GroupDao groupDao = UniversityRegistryApp.appDatabase.getGroupDao();
    private final StudentCountDao studentCountDao = UniversityRegistryApp.appDatabase.getStudentCountDao();
    private final StudentDao studentDao = UniversityRegistryApp.appDatabase.getStudentDao();
    private FragmentGroupDetailsBinding binding;
    private Long currentGroupId;
    private Long currentFacultyId;
    private Group group;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentFacultyId = GroupDetailsFragmentArgs.fromBundle(getArguments()).getFacultyId();
        currentGroupId = GroupDetailsFragmentArgs.fromBundle(getArguments()).getGroupId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        binding.includedToolBar.myToolbar.inflateMenu(R.menu.group_details_menu);
        binding.includedToolBar.myToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.closeMenuOption: {
                    closeGroupDetailsModal();
                    return true;
                }
                case R.id.addStudentMenuOption: {
                    showStudentDetails(0L);
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
        group = groupDao
                .findById(currentGroupId)
                .orElseGet(() -> {
                    Group g = new Group();
                    g.facultyId = currentFacultyId;
                    return g;
                });

        studentCountDao
                .getByGroupId(currentGroupId)
                .observe(this, studentCount1 -> {
                    binding.groupStudentCountBudget.setText("" + studentCount1.budget);
                    binding.groupStudentCountCommerce.setText("" + studentCount1.commerce);
                });

        binding.groupNameEditText.setText(group.name);
        binding.groupDirectionCodeEditText.setText(group.directionCode);
        binding.groupDirectionNameEditText.setText(group.directionName);
        binding.groupDirectionProfileEditText.setText(group.directionProfile);


        binding.includedToolBar.titleTextView.setText("Окно детализации");
        binding.includedToolBar.showDrawerButton.setOnClickListener(v -> {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.open();
        });


        StudentListAdapter studentListAdapter = new StudentListAdapter(
                requireContext(),
                this::showStudentDetails,
                this::showDeleteStudentModal
        );

        binding.studentListView.setAdapter(studentListAdapter);
        studentDao.findAllByGroupIdReactive(currentGroupId).observe(this, studentListAdapter::updateStudentList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void closeGroupDetailsModal() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Сохранить группу?")
                .setPositiveButton("Сохранить и выйти", (dialog, which) -> {
                    group.name = binding.groupNameEditText.getText().toString();
                    group.directionCode = binding.groupDirectionCodeEditText.getText().toString();
                    group.directionName = binding.groupDirectionNameEditText.getText().toString();
                    group.directionProfile = binding.groupDirectionProfileEditText.getText().toString();

                    if (currentGroupId == 0) {
                        groupDao.insert(group);
                    } else {
                        groupDao.save(group);
                    }

                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(GroupListFragmentDirections.actionGlobalGroupListFragment(currentFacultyId));
                })
                .setNegativeButton("Выйти без сохранения", (dialog, which) -> {
                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(GroupListFragmentDirections.actionGlobalGroupListFragment(currentFacultyId));
                    dialog.cancel();
                })
                .show();
    }

    private void showStudentDetails(Long id) {
        GroupDetailsFragmentDirections.ActionGroupDetailsFragmentToStudentDetailsFragment action = GroupDetailsFragmentDirections.actionGroupDetailsFragmentToStudentDetailsFragment();
        action.setGroupId(currentGroupId);
        action.setFacultyId(currentFacultyId);
        action.setStudentId(id);
        Navigation
                .findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(action);
    }

    private void showDeleteStudentModal(long studentId) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Удалить студента?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    studentDao.delete(studentId);
                })
                .setNegativeButton("Отменить", (dialog, which) -> dialog.cancel())
                .show();
    }
}
