package com.endava.model;

import com.endava.dto.JobDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private int jobId;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy="job",
            cascade = {CascadeType.ALL})
    private List<Task> tasks;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "number_of_tasks")
    private int numberOfTasks;

    @Column(name = "number_of_completed_tasks")
    private int numberOfCompletedTasks;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "base_uri")
    private String baseUri;

    @Column(name = "path")
    private String path;


    //add convenience method for bi-directional relationship
    public void add(Task tempTask){
        if (tasks == null){
            tasks = new ArrayList<>();
        }
        tasks.add(tempTask);
        tempTask.setJob(this);
    }


    public static Job convertJobDTOToJob(JobDTO jobDTO) {
            Job job = new Job();
            job.setBaseUri(jobDTO.getMetadata().getBaseUri());
            job.setHttpMethod(jobDTO.getMetadata().getHttpMethod());
            job.setPath(jobDTO.getMetadata().getPath());
            job.setNumberOfCompletedTasks(0);
            job.setNumberOfTasks(jobDTO.getTasks().size());
            job.setStatus(Status.SUBMITTED);

            job.setTasks(jobDTO.getTasks()
                    .stream()
                    .map(taskDTO -> Task.convertTaskDTOtoTask(taskDTO, jobDTO.getMetadata().getParams()))
                    .collect(Collectors.toList()));

            return job;
    }

}
