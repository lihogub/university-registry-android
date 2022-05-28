package ru.lihogub.universityregistryandroid.presentation.feature.groupList.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.function.Consumer;

import ru.lihogub.universityregistryandroid.R;
import ru.lihogub.universityregistryandroid.data.database.model.Group;

public class GroupListAdapter extends BaseAdapter {
    private final Context context;
    private final List<Group> groupList;
    private final Consumer<Long> onDeleteAction;
    private final Consumer<Long> onEditAction;
    private final Consumer<Integer> onSortAction;

    public GroupListAdapter(
            Context context,
            List<Group> groupList,
            Consumer<Long> onDeleteAction,
            Consumer<Long> onEditAction,
            Consumer<Integer> onSortAction
    ) {
        this.context = context;
        this.groupList = groupList;
        this.onDeleteAction = onDeleteAction;
        this.onEditAction = onEditAction;
        this.onSortAction = onSortAction;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_list_item, parent, false);
        }
        Group group = (Group) getItem(position);


        return null;
    }
}
