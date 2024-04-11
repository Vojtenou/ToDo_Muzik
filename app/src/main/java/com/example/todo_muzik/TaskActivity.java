package com.example.todo_muzik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TaskActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Spinner daySpinner;
    private int taskIdToUpdate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        dbHelper = new DatabaseHelper(this);

        final TimePicker startTimePicker = findViewById(R.id.startTimePicker);
        final TimePicker endTimePicker = findViewById(R.id.endTimePicker);
        final EditText taskNameEditText = findViewById(R.id.taskNameEditText);
        final EditText descriptionEditText = findViewById(R.id.descriptionEditText);

        // Spinner pro výběr dne
        daySpinner = findViewById(R.id.daySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.days_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);


        Intent intent = getIntent();
        taskIdToUpdate = intent.getIntExtra("selectedTaskId", -1);

        // Pokud máme ID úkolu k úpravě, načteme jeho údaje
        if (taskIdToUpdate != -1) {
            Task taskToUpdate = dbHelper.getTaskById(taskIdToUpdate);
            taskNameEditText.setText(taskToUpdate.getTaskName());
            descriptionEditText.setText(taskToUpdate.getDescription());

        }

        Button saveTaskButton = findViewById(R.id.saveTaskButton);
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = taskNameEditText.getText().toString();
                String startTime = startTimePicker.getCurrentHour() + ":" + startTimePicker.getCurrentMinute();
                String endTime = endTimePicker.getCurrentHour() + ":" + endTimePicker.getCurrentMinute();
                String description = descriptionEditText.getText().toString();
                String selectedDay = daySpinner.getSelectedItem().toString(); // Získání vybraného dne

                Task task = new Task(taskName, startTime, endTime, description);


                if (taskIdToUpdate != -1) {
                    task.setId(taskIdToUpdate);
                    long result = dbHelper.updateTask(task, selectedDay); // metoda updateTask je potřeba implementovat v DatabaseHelper
                    if (result > 0) {
                        Toast.makeText(TaskActivity.this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TaskActivity.this, "Error updating task", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    long result = dbHelper.addTask(task, selectedDay);
                    if (result > 0) {
                        Toast.makeText(TaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TaskActivity.this, "Error adding task", Toast.LENGTH_SHORT).show();
                    }
                }

                // Po uložení úkolu se vrátíme zpět do MainActivity
                Intent intent = new Intent(TaskActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Ukončení aktivity TaskActivity
            }
        });
    }
}
