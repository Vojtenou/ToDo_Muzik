package com.example.todo_muzik;

public class Task {

    private int id;
    private String taskName;
    private String startTime;
    private String endTime;
    private String description;

    public Task(String taskName, String startTime, String endTime, String description) {
        this.taskName = taskName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public Task(int id, String taskName, String startTime, String endTime, String description) {
        this.id = id;
        this.taskName = taskName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
