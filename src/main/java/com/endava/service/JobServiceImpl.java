package com.endava.service;

import com.endava.dto.JobDTO;
import com.endava.error.APIException;
import com.endava.error.StatusCode;
import com.endava.model.Job;

import com.endava.model.Status;
import com.endava.model.Task;
import com.endava.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;


import java.util.List;

import static com.endava.model.Job.convertJobDTOToJob;


@Service("jobService")
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository repository;


    @Override
    @Transactional
    public Job getJob(int jobId) throws APIException {
        return repository.getJob(jobId);
    }

    @Override
    @Transactional
    public String createJob(JobDTO jobDTO) throws APIException {
        validateMandatoryFields(jobDTO);
        Job savedJob = repository.addJob(convertJobDTOToJob(jobDTO));
        return Integer.toString(savedJob.getJobId());
    }



    private void validateMandatoryFields(JobDTO jobDTO) throws APIException {
        if (jobDTO.getMetadata().getBaseUri() == null) {
            throw new APIException(StatusCode.URI_MISSING);
        }

        if ((jobDTO.getMetadata().getHttpMethod() == null)) {
            throw new APIException(StatusCode.HTTP_METHOD_MISSING);
        }
    }

    @Override
    @Transactional
    public void updateJob(Status newStatus, int jobId) {
        repository.updateJob(newStatus, jobId);
    }

    @Override
    @Transactional
    public List<String> getFailedTasksId(Job job) {
        return repository.getFailedTasksId(job);
    }

    @Override
    @Transactional
    public List<String> getTasksId(Job job) {
        return repository.getTasksId(job);
    }

    @Override
    @Transactional
    public void incrementNumberOfCompletedTasks(Task task) {
        repository.incrementNumberOfCompletedTasks(task);
    }

    @Override
    @Transactional
    public void updateJobStatus(Job job) {
        repository.updateJobStatus(job);
    }

}
