package com.endava.repository;

import com.endava.error.APIException;
import com.endava.model.Job;
import com.endava.model.Status;
import com.endava.model.Task;


import java.util.List;

public interface JobRepository {


    Job getJob(int jobId) throws APIException;

    Job addJob(Job job);

    void updateJob(Status newStatus, int jobId);

    List<String> getFailedTasksId(Job job);

    List<String> getTasksId(Job job);

    void incrementNumberOfCompletedTasks(Task task);

    void updateJobStatus(Job job);
}
