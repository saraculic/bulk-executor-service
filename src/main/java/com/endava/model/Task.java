package com.endava.model;

import com.endava.dto.TaskDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int taskId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "path_params")
    private String pathParams;

    @Column(name = "query_params")
    private String queryParams;

    @Column(name = "body")
    private String body;

    @Column(name = "last_updated_time")
    private Date lastUpdatedTime;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "task_execution_response")
    private String taskExecutionResponse;


    public static Task convertTaskDTOtoTask(TaskDTO taskDTO, String params){
        Task task = new Task();

        Map<String, String> pathParameters = taskDTO.getPathParameters();
        Map<String, String> queryParameters = taskDTO.getQueryParameters();

        String newParams = null;

        for (String key : pathParameters.keySet()) {
            if(params.contains(key)){
                newParams = params.replace("{{" + key + "}}", pathParameters.get(key));
            }
        }

        System.out.println(newParams);
        task.setPathParams(newParams);

        StringBuilder queryParams = new StringBuilder();

        for(String key : queryParameters.keySet()){
            queryParams.append(key + "=" + queryParameters.get(key) + "&");
        }

        queryParams.deleteCharAt(queryParams.length()-1);
        task.setQueryParams(queryParams.toString());
        task.setBody(taskDTO.getBody());
        task.setLastUpdatedTime(new Date());
        task.setRetryCount(0);
        task.setStatus(Status.SUBMITTED);
        task.setTaskExecutionResponse("");

        return task;
    }

}
