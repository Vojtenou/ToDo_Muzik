package com.example.todo_muzik;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    private ArrayList<Task> mTasks;

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
        mContext = context;
        mTasks = tasks;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        TextView taskNameTextView = convertView.findViewById(R.id.taskNameTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        Button deleteTaskButton = convertView.findViewById(R.id.deleteTaskButton);
        Button editTaskButton = convertView.findViewById(R.id.editTaskButton); // Přidání tlačítka pro úpravu úkolu

        taskNameTextView.setText(task.getTaskName());
        timeTextView.setText(task.getStartTime() + " - " + task.getEndTime());


        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task taskToDelete = getItem(position); // Získání úkolu, který má být smazán
                removeTask(taskToDelete); // Volání metody pro odstranění úkolu
            }
        });


        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task taskToEdit = getItem(position); // Získání úkolu, který má být upraven
                editTask(taskToEdit); // Volání metody pro úpravu úkolu
            }
        });

        return convertView;
    }

    // Metoda pro odstranění úkolu z adapteru a databáze
    private void removeTask(Task task) {
        // Odstranění úkolu z databáze
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        dbHelper.deleteTask(task.getId());

        // Odstranění úkolu z adapteru
        remove(task);
        notifyDataSetChanged(); // Aktualizace zobrazení seznamu úkolů
    }


    private void editTask(Task task) {
        Intent intent = new Intent(mContext, TaskActivity.class);
        intent.putExtra("selectedTaskId", task.getId()); // Předání ID úkolu, který má být upraven
        mContext.startActivity(intent); // Spuštění aktivity pro úpravu úkolu
    }
}
