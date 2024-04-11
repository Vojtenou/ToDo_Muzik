package com.example.todo_muzik;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class TaskDetailActivity extends AppCompatActivity {
private DatabaseHelper dbHelper;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Načtení ID úkolu z intentu
        int taskId = getIntent().getIntExtra("taskId", -1);

        // Načtení úkolu z databáze podle ID
        dbHelper = new DatabaseHelper(this);
        task = dbHelper.getTaskById(taskId);

        // Zobrazení detailů úkolu
        TextView taskNameTextView = findViewById(R.id.taskNameTextView);
        TextView startTimeTextView = findViewById(R.id.startTimeTextView);
        TextView endTimeTextView = findViewById(R.id.endTimeTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);

        taskNameTextView.setText(task.getTaskName());
        startTimeTextView.setText("Start time: " + task.getStartTime());
        endTimeTextView.setText("End time: " + task.getEndTime());
        descriptionTextView.setText("Description: " + task.getDescription());
    }
}
