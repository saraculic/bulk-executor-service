package com.endava.service;

import com.endava.error.APIException;
import com.endava.model.Status;
import com.endava.model.Task;

import java.util.List;
import java.util.Map;

public interface TaskService {

    Task getTask(int taskId) throws APIException;

    void updateTask(int taskId) throws APIException;
    Map<Integer,String> taskExecute();

    void updateTaskStatus(Task task, Status newStatus);
}
