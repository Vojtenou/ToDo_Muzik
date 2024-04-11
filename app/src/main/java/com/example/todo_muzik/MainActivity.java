package com.example.todo_muzik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> tasks;
    private DatabaseHelper dbHelper;
    private TaskAdapter taskAdapter;
    private Spinner daySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        tasks = new ArrayList<>();

        // Získání aktuálního dne
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] daysArray = getResources().getStringArray(R.array.days_array);
        String currentDay = daysArray[(dayOfWeek + 5) % 7]; // Přizpůsobení indexů pro správné načtení aktuálního dne

        // Nastavení spinneru na aktuální den
        daySpinner = findViewById(R.id.daySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.days_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);
        // Nastavení aktuálního dne jako výchozí hodnoty spinneru
        int currentDayPosition = adapter.getPosition(currentDay);
        daySpinner.setSelection(currentDayPosition);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDay = parent.getItemAtPosition(position).toString();
                updateTaskList(selectedDay);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ListView taskListView = findViewById(R.id.taskListView);
        taskAdapter = new TaskAdapter(this, tasks);
        taskListView.setAdapter(taskAdapter);

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Otevřít okno pro přidání úkolu
                startActivity(new Intent(MainActivity.this, TaskActivity.class));
            }
        });

        // Načtení úkolů pro aktuální den
        updateTaskList(currentDay);
    }

    private void updateTaskList(String selectedDay) {
        tasks.clear();
        tasks.addAll(dbHelper.getTasksByDay(selectedDay));
        taskAdapter.notifyDataSetChanged();
    }
}
