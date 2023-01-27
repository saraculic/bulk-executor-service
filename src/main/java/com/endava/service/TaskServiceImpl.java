package com.endava.service;

import com.endava.client.WebClientService;
import com.endava.error.APIException;
import com.endava.model.Job;
import com.endava.model.Status;
import com.endava.model.Task;
import com.endava.repository.JobRepository;
import com.endava.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private WebClientService webClientService;

    @Override
    @Transactional
    public Task getTask(int taskId) throws APIException {
        return taskRepository.getTask(taskId);
    }

    @Override
    @Transactional
    public void updateTask(int taskId) throws APIException {
        taskRepository.updateTask(taskId);
    }

    @Override
    @Transactional
    public Map<Integer,String> taskExecute()  {

        String pathparameters, queryParameters, body;
        String httpMethod, baseUri, path;

        Map<Integer, String> map = new HashMap<>();
        String request = null;

        List<Task> tasks = taskRepository.getTasksToExecute();
        for (Task task : tasks) {
            pathparameters = task.getPathParams();
            queryParameters = task.getQueryParams();
            body = task.getBody();

            Job job = task.getJob();
            httpMethod = job.getHttpMethod();
            baseUri = job.getBaseUri();
            path = job.getPath();
            request = baseUri + path + "?" + queryParameters;
            map.put(task.getTaskId(), request);
        }
        return map;
    }

    @Override
    @Transactional
    public void updateTaskStatus(Task task, Status newStatus) {
        taskRepository.updateTaskStatus(task, newStatus);
    }

}
