package com.endava.repository;


import com.endava.error.APIException;
import com.endava.error.StatusCode;
import com.endava.model.Job;

import com.endava.model.Status;
import com.endava.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Repository("jobRepository")
public class JobRepositoryImpl implements JobRepository {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    @Transactional
    public Job getJob(int jobId) throws APIException {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("select j from Job j where j.jobId=:id");
        query.setParameter("id", jobId);
        Job job;
        try {
            job = (Job) query.getSingleResult();
        } catch (NoResultException e) {
            throw new APIException(StatusCode.JOB_NOT_FOUND);
        }
        return job;
    }

    public Job addJob(Job job) {
        Session currentSession = sessionFactory.getCurrentSession();

        job.getTasks().stream().forEach(task -> {
            task.setJob(job);
        });

        currentSession.save(job);
        return job;
    }

    @Override
    public void updateJob(Status newStatus, int jobId) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("update Job j set j.status=:state where j.jobId=:id");
        query.setParameter("state", newStatus);
        query.setParameter("id", jobId);
        query.executeUpdate();
    }

    @Override
    public List<String> getFailedTasksId(Job job) {

        List<Task> tasks = job.getTasks();

        return tasks.stream()
                .filter(task -> task.getStatus() == Status.FAILED)
                .map(task -> task.getTaskId())
                .map(taskId -> taskId.toString())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTasksId(Job job) {

        List<Task> tasks = job.getTasks();

        return tasks.stream()
                .filter(task -> task.getTaskId() > 0)
                .map(task -> task.getTaskId())
                .map(taskId -> taskId.toString())
                .collect(Collectors.toList());

    }

    @Override
    public void incrementNumberOfCompletedTasks(Task task) {
        Job job = task.getJob();
        int num = job.getNumberOfCompletedTasks();
        job.setNumberOfCompletedTasks(num + 1);
        updateJobStatus(job);
    }

    @Override
    public void updateJobStatus(Job job) {
        List<Task> tasks = job.getTasks();

        boolean allSuccess = tasks.stream()
                .map(task -> task.getStatus())
                .allMatch(status -> status == Status.SUCCESS);

        boolean allFailed = tasks.stream()
                .map(task -> task.getStatus())
                .allMatch(status -> status == Status.FAILED);

        if (allSuccess == true) {
            job.setStatus(Status.SUCCESS);
        }

        if (allFailed == true) {
            job.setStatus(Status.FAILED);
        }

    }
}
