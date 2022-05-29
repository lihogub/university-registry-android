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
import ru.lihogub.universityregistryandroid.data.database.model.Group;
import ru.lihogub.universityregistryandroid.databinding.FragmentGroupDetailsBinding;
import ru.lihogub.universityregistryandroid.presentation.feature.groupList.view.GroupListFragmentDirections;

public class GroupDetailsFragment extends Fragment {
    private final GroupDao groupDao = UniversityRegistryApp.appDatabase.getGroupDao();
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

        binding.groupNameEditText.setText(group.name);
        binding.groupDirectionCodeEditText.setText(group.directionCode);
        binding.groupDirectionNameEditText.setText(group.directionName);
        binding.groupDirectionProfileEditText.setText(group.directionProfile);
        binding.groupStudentCountBudgetEditText.setText("" + group.countBudget);
        binding.groupStudentCountCommerceEditText.setText("" + group.countCommerce);

        binding.includedToolBar.titleTextView.setText("Окно детализации");
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
        new AlertDialog.Builder(requireContext())
                .setTitle("Сохранить группу?")
                .setPositiveButton("Сохранить и выйти", (dialog, which) -> {
                    group.name = binding.groupNameEditText.getText().toString();
                    group.directionCode = binding.groupDirectionCodeEditText.getText().toString();
                    group.directionName = binding.groupDirectionNameEditText.getText().toString();
                    group.directionProfile = binding.groupDirectionProfileEditText.getText().toString();
                    group.countBudget = Long.parseLong(binding.groupStudentCountBudgetEditText.getText().toString());
                    group.countCommerce = Long.parseLong(binding.groupStudentCountCommerceEditText.getText().toString());

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
}
