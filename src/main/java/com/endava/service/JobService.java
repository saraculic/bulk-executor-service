package com.endava.service;

import com.endava.dto.JobDTO;
import com.endava.error.APIException;
import com.endava.model.Job;
import com.endava.model.Status;
import com.endava.model.Task;


import java.util.List;

public interface JobService {

    Job getJob(int jobId) throws APIException;
    String createJob(JobDTO jobDTO) throws APIException;

    void updateJob(Status newStatus, int jobId);

    List<String> getFailedTasksId(Job job);

    List<String> getTasksId(Job job);

    void incrementNumberOfCompletedTasks(Task task);

    void updateJobStatus(Job job);


}
