package ru.lihogub.universityregistryandroid.presentation.feature.groupDetails.view;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.lihogub.universityregistryandroid.data.database.model.Student;
import ru.lihogub.universityregistryandroid.databinding.StudentListItemBinding;

@RequiresApi(api = Build.VERSION_CODES.N)
public class StudentListAdapter extends BaseAdapter {
    private final Context context;
    private final Consumer<Long> onEditAction;
    private final Consumer<Long> onDeleteAction;
    private final List<Student> studentList = new ArrayList<>();


    public StudentListAdapter(
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
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudentListItemBinding studentListItemBinding;
        if (convertView == null) {
            studentListItemBinding = StudentListItemBinding.inflate(LayoutInflater.from(context), parent, false);
        } else {
            studentListItemBinding = StudentListItemBinding.bind(convertView);
        }
        Student student = (Student) getItem(position);

        studentListItemBinding.studentFirstname.setText(student.firstname);
        studentListItemBinding.studentMiddlename.setText(student.middlename);
        studentListItemBinding.studentLastname.setText(student.lastname);
        studentListItemBinding.studentPayment.setText(student.isBudget ? "бюджет" : "коммерция");

        studentListItemBinding.getRoot().setOnLongClickListener(v -> {
            onDeleteAction.accept(student.id);
            return true;
        });
        studentListItemBinding.getRoot().setOnClickListener(v -> {
            onEditAction.accept(student.id);
        });

        return studentListItemBinding.getRoot();
    }

    public void updateStudentList(List<Student> studentList) {
        this.studentList.clear();
        this.studentList.addAll(studentList);
        notifyDataSetChanged();
    }
}