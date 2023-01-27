package com.endava.rest;

import com.endava.dto.*;
import com.endava.dto.CreateJobRequest;
import com.endava.dto.CreateJobResponse;
import com.endava.dto.GetJobStatusResponse;
import com.endava.dto.UpdateJobStatusRequest;
import com.endava.error.APIException;
import com.endava.error.StatusCode;
import com.endava.model.Job;
import com.endava.model.Status;

import com.endava.service.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.transaction.Transactional;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobRestController {

    @Autowired
    private JobService jobService;

    @GetMapping("/{jobId}/status")
    @Transactional
    public ResponseEntity<GetJobStatusResponse> getJobStatus(@PathVariable int jobId) throws APIException {

        Job job = jobService.getJob(jobId);
        Status status = job.getStatus();

        GetJobStatusResponse getJobStatusResponse = new GetJobStatusResponse();
        getJobStatusResponse.setStatus(status);

        if (status.equals(Status.valueOf("FAILED"))) {
            List<String> failedTasksId = jobService.getFailedTasksId(job);
            getJobStatusResponse.setFailedTasksId(failedTasksId);
            return new ResponseEntity<>(getJobStatusResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(getJobStatusResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/job")
    public ResponseEntity<CreateJobResponse> createJob(@RequestBody CreateJobRequest newJob) throws APIException {
        CreateJobResponse createJobResponse = new CreateJobResponse();
        createJobResponse.setJobId(jobService.createJob(newJob.getJob()));
        return new ResponseEntity<>(createJobResponse, HttpStatus.OK);
    }

    @PutMapping("/{jobId}/status")
    public ResponseEntity<?> updateJobStatus(@PathVariable int jobId, @RequestBody UpdateJobStatusRequest updateJob) throws APIException {

        Job job = jobService.getJob(jobId);

        if (updateJob.getState().equals("CANCELLED") && job != null) {
            Status newStatus = Status.valueOf(updateJob.getState());
            jobService.updateJob(newStatus, jobId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new APIException(StatusCode.UNSUPPORTED_STATE);
        }
    }

    @Transactional
    @GetMapping("/{jobId}/tasks")
    public ResponseEntity<GetListTasksResponse> getListTasksResponse(@PathVariable int jobId) throws APIException {

        Job job = jobService.getJob(jobId);

        if (job != null) {
            GetListTasksResponse getListTasksResponse = new GetListTasksResponse();
            List<String> tasksId = jobService.getTasksId(job);
            getListTasksResponse.setTasksId(tasksId);
            return new ResponseEntity<>(getListTasksResponse, HttpStatus.OK);
        } else {
            throw new APIException(StatusCode.JOB_NOT_FOUND);
        }
    }



}
