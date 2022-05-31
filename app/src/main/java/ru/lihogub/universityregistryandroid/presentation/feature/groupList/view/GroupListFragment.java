package ru.lihogub.universityregistryandroid.presentation.feature.groupList.view;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Random;

import ru.lihogub.universityregistryandroid.R;
import ru.lihogub.universityregistryandroid.UniversityRegistryApp;
import ru.lihogub.universityregistryandroid.data.database.dao.FacultyDao;
import ru.lihogub.universityregistryandroid.data.database.dao.GroupDao;
import ru.lihogub.universityregistryandroid.data.database.model.Faculty;
import ru.lihogub.universityregistryandroid.data.database.model.Group;
import ru.lihogub.universityregistryandroid.databinding.FragmentGroupListBinding;
import ru.lihogub.universityregistryandroid.presentation.feature.groupDetails.view.GroupDetailsFragmentDirections;

public class GroupListFragment extends Fragment {
    private final FacultyDao facultyDao = UniversityRegistryApp.appDatabase.getFacultyDao();
    private final GroupDao groupDao = UniversityRegistryApp.appDatabase.getGroupDao();
    private FragmentGroupListBinding binding;
    private Long currentFacultyId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentFacultyId = GroupListFragmentArgs.fromBundle(getArguments()).getFacultyId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (currentFacultyId == 0) {
            return;
        }
        binding.includedToolBar.myToolbar.inflateMenu(R.menu.group_list_menu);
        binding.includedToolBar.myToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.addGroupMenuOption: {
                    showAddGroupModal();
                    return true;
                }
                case R.id.deleteFacultyMenuOption: {
                    showDeleteFacultyModal();
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
        GroupListFragmentDirections.ActionGroupListFragmentToGroupDetailsFragment action = GroupListFragmentDirections.actionGroupListFragmentToGroupDetailsFragment();
        action.setFacultyId(currentFacultyId);
        if (currentFacultyId != 0) {
            Faculty faculty = facultyDao.findById(currentFacultyId).get();
            binding.includedToolBar.titleTextView.setText("Список групп \n" + faculty.name);
        } else {
            binding.includedToolBar.titleTextView.setText("Выберите факультет");
        }
        GroupListAdapter groupListAdapter = new GroupListAdapter(
                getContext(),
                groupId -> {
                    action.setGroupId(groupId);
                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(action);
                },
                this::showDeleteGroupModal
        );
        binding.groupListView.setAdapter(groupListAdapter);

        groupDao.findAllWithStudentCountByFacultyId(currentFacultyId)
                .observe(this, groupListAdapter::updateGroupList);

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

    private void showAddGroupModal() {
        GroupListFragmentDirections.ActionGroupListFragmentToGroupDetailsFragment action = GroupListFragmentDirections.actionGroupListFragmentToGroupDetailsFragment();
        action.setFacultyId(currentFacultyId);
        Navigation
                .findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(action);
    }

    private void showDeleteFacultyModal() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Удалить факультет?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    facultyDao.delete(currentFacultyId);
                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(GroupListFragmentDirections.actionGlobalGroupListFragment(0));
                })
                .setNegativeButton("Отменить", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void showDeleteGroupModal(Long groupId) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Удалить группу?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    groupDao.delete(groupId);
                })
                .setNegativeButton("Отменить", (dialog, which) -> dialog.cancel())
                .show();
    }
}
