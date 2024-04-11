package com.example.todo_muzik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK_NAME = "task_name";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_END_TIME = "end_time";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DAY_OF_WEEK = "day_of_week";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_START_TIME + " TEXT, " +
                COLUMN_END_TIME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DAY_OF_WEEK + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addTask(Task task, String dayOfWeek) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_START_TIME, task.getStartTime());
        values.put(COLUMN_END_TIME, task.getEndTime());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DAY_OF_WEEK, dayOfWeek);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4));
                task.setId(cursor.getInt(0)); // Přidání ID úkolu
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tasks;
    }

    public List<Task> getTasksByDay(String dayOfWeek) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DAY_OF_WEEK + " = ?",
                new String[]{dayOfWeek});

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4));
                task.setId(cursor.getInt(0)); // Přidání ID úkolu
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tasks;
    }

    // Metoda pro získání úkolu podle ID
    public Task getTaskById(int taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Task task = null;

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_TASK_NAME, COLUMN_START_TIME, COLUMN_END_TIME, COLUMN_DESCRIPTION},
                COLUMN_ID + "=?", new String[]{String.valueOf(taskId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            task = new Task(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4));
            task.setId(cursor.getInt(0)); // Přidání ID úkolu
            cursor.close();
        }

        db.close();
        return task;
    }

    // Metoda pro aktualizaci úkolu v databázi
    public long updateTask(Task task, String dayOfWeek) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_START_TIME, task.getStartTime());
        values.put(COLUMN_END_TIME, task.getEndTime());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DAY_OF_WEEK, dayOfWeek);

        // Aktualizujeme řádek v databázi, kde id odpovídá id úkolu
        long result = db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(task.getId())});

        db.close();
        return result;
    }

    // Metoda pro odstranění úkolu z databáze podle ID
    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
    }
}
