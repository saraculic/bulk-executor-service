package com.endava.repository;

import com.endava.error.APIException;
import com.endava.model.Status;
import com.endava.model.Task;

import java.util.List;

public interface TaskRepository {

    Task getTask(int taskId) throws APIException;

    void updateTask(int taskId) throws APIException;

    List<Task> getTasksToExecute();


    void updateTaskStatus(Task task, Status newStatus);
}
