package com.endava.repository;

import com.endava.error.APIException;
import com.endava.error.StatusCode;
import com.endava.model.Status;
import com.endava.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

@Repository("taskRepository")
@PropertySource({"classpath:application.properties"})
public class TaskRepositoryImpl implements TaskRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private Environment env;

    @Override
    public Task getTask(int taskId) throws APIException {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("select t from Task t where t.taskId=:id");
        query.setParameter("id", taskId);
        Task task;
        try {
            task = (Task) query.getSingleResult();
        } catch (NoResultException e) {
            throw new APIException(StatusCode.TASK_NOT_FOUND);
        }
        return task;
    }

    @Override
    public void updateTask(int taskId) throws APIException {
        Session currentSession = sessionFactory.getCurrentSession();

        int maxCount = Integer.parseInt(env.getProperty("com.endava.bulk_executor_service.task.maxRetry"));

        Task task = getTask(taskId);
        int retryCount = task.getRetryCount();

        if(retryCount < maxCount){
            Query query = currentSession.createQuery("update Task t set t.status=:status, t.retryCount=t.retryCount+1 where t.taskId=:id");
            query.setParameter("status", Status.SUBMITTED);
            query.setParameter("id", taskId);
            query.executeUpdate();
        } else {
            throw new APIException(StatusCode.MAX_RETRY_COUNT);
        }
    }

    @Override
    public List<Task> getTasksToExecute() {
        Session currentSession = sessionFactory.getCurrentSession();
        int number = Integer.parseInt(env.getProperty("com.endava.bulk_executor_service.task.number_of_tasks_to_retrieve"));
        Query query = currentSession.createQuery("select t from Task t where t.status='SUBMITTED' order by t.lastUpdatedTime");
        query.setMaxResults(number);
        List<Task> tasks = query.getResultList();
        return tasks;
    }

    @Override
    public void updateTaskStatus(Task task, Status newStatus) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("update Task t set t.status=:status, t.lastUpdatedTime=:time");
        query.setParameter("status", newStatus);
        query.setParameter("time", new Date());
        query.executeUpdate();
    }
}
