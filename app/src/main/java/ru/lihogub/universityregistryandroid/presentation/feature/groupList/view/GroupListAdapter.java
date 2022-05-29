package ru.lihogub.universityregistryandroid.presentation.feature.groupList.view;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import ru.lihogub.universityregistryandroid.R;
import ru.lihogub.universityregistryandroid.data.database.model.Group;
import ru.lihogub.universityregistryandroid.databinding.GroupListItemBinding;

@RequiresApi(api = Build.VERSION_CODES.N)
public class GroupListAdapter extends BaseAdapter {
    private final Context context;
    private final Consumer<Long> onEditAction;
    private final Consumer<Long> onDeleteAction;
    private final List<Group> groupList = new ArrayList<>();
    private SortField sortField = SortField.GROUP_NAME;
    private final Comparator<String> nullsLastStringComparator = Comparator.nullsLast((Comparator<String>) String::compareTo);


    public GroupListAdapter(
            Context context,
            Consumer<Long> onEditAction,
            Consumer<Long> onDeleteAction
    ) {
        this.context = context;
        this.onEditAction = onEditAction;
        this.onDeleteAction = onDeleteAction;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupListItemBinding groupListItemBinding;
        if (convertView == null) {
            groupListItemBinding = GroupListItemBinding.inflate(LayoutInflater.from(context), parent, false);
        } else {
            groupListItemBinding = GroupListItemBinding.bind(convertView);
        }
        Group group = (Group) getItem(position);

        groupListItemBinding.groupName.setText(group.name);
        groupListItemBinding.groupDirectionCode.setText(group.directionCode);
        groupListItemBinding.groupDirectionName.setText(group.directionName);
        groupListItemBinding.groupDirectionProfile.setText(group.directionProfile);

        groupListItemBinding.groupEditButton
                .setOnClickListener(v -> onEditAction.accept(group.id));
        groupListItemBinding.groupDeleteButton.
                setOnClickListener(v -> onDeleteAction.accept(group.id));

        groupListItemBinding.groupNameLayout
                .setOnLongClickListener(view -> {
                    handleSortAction(view);
                    return true;
                });
        groupListItemBinding.groupDirectionCodeLayout
                .setOnLongClickListener(view -> {
                    handleSortAction(view);
                    return true;
                });
        groupListItemBinding.groupDirectionNameLayout
                .setOnLongClickListener(view -> {
                    handleSortAction(view);
                    return true;
                });

        groupListItemBinding.groupDirectionProfileLayout
                .setOnLongClickListener(view -> {
                    handleSortAction(view);
                    return true;
                });

        return groupListItemBinding.getRoot();
    }

    public void updateGroupList(List<Group> groupList) {
        this.groupList.clear();
        this.groupList.addAll(groupList);
        this.sortGroups();
        notifyDataSetChanged();
    }

    private void handleSortAction(View view) {
        switch (view.getId()) {
            case R.id.groupNameLayout: {
                sortField = SortField.GROUP_NAME;
                break;
            }
            case R.id.groupDirectionCodeLayout: {
                sortField = SortField.DIRECTION_CODE;
                break;
            }
            case R.id.groupDirectionNameLayout: {
                sortField = SortField.DIRECTION_NAME;
                break;
            }
            case R.id.groupDirectionProfileLayout: {
                sortField = SortField.DIRECTION_PROFILE;
                break;
            }
        }
        sortGroups();
        notifyDataSetChanged();
    }

    private void sortGroups() {
        Collections.sort(this.groupList, (o1, o2) -> {
            switch (sortField) {
                case GROUP_NAME: {
                    return nullsLastStringComparator.compare(o1.name, o2.name);
                }
                case DIRECTION_CODE: {
                    return nullsLastStringComparator.compare(o1.directionCode, o2.directionCode);
                }
                case DIRECTION_NAME: {
                    return nullsLastStringComparator.compare(o1.directionName, o2.directionName);
                }
                case DIRECTION_PROFILE: {
                    return nullsLastStringComparator.compare(o1.directionProfile, o2.directionProfile);
                }
            }
            return 0;
        });
    }

    private enum SortField {
        GROUP_NAME, DIRECTION_CODE, DIRECTION_NAME, DIRECTION_PROFILE;
    }
}
