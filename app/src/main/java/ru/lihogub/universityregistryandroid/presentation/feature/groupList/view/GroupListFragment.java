package ru.lihogub.universityregistryandroid.presentation.feature.groupList.view;

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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import ru.lihogub.universityregistryandroid.R;
import ru.lihogub.universityregistryandroid.UniversityRegistryApp;
import ru.lihogub.universityregistryandroid.data.database.dao.FacultyDao;
import ru.lihogub.universityregistryandroid.data.database.dao.GroupDao;
import ru.lihogub.universityregistryandroid.data.database.model.Group;
import ru.lihogub.universityregistryandroid.databinding.FragmentGroupListBinding;

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
        binding.myToolbar.inflateMenu(R.menu.group_list_menu);
        binding.myToolbar.setOnMenuItemClickListener(item -> {
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

    @Override
    public void onStart() {
        super.onStart();
        ArrayAdapter<Group> arrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );
        binding.groupListView.setAdapter(arrayAdapter);

        groupDao.findAllByFacultyIdReactive(currentFacultyId)
                .observe(this, groups -> {
                    arrayAdapter.clear();
                    arrayAdapter.addAll(groups);
                    arrayAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showAddGroupModal() {
        Group group = new Group();
        group.facultyId = currentFacultyId;
        EditText editText = new EditText(getContext());
        new AlertDialog.Builder(requireContext())
                .setTitle("Add group")
                .setView(editText)
                .setPositiveButton("Add", (dialog, which) -> {
                    group.name = editText.getText().toString();
                    groupDao.insert(group);
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void showDeleteFacultyModal() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete faculty?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    facultyDao.delete(currentFacultyId);
                    Navigation
                            .findNavController(requireActivity(), R.id.navHostFragment)
                            .navigate(GroupListFragmentDirections.actionGlobalGroupListFragment(0));
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }
}
