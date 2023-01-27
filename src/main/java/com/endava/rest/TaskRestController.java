package com.endava.rest;

import com.endava.client.WebClientService;
import com.endava.dto.GetStatusOfTheTaskResponse;
import com.endava.error.APIException;
import com.endava.error.StatusCode;
import com.endava.model.Task;
import com.endava.service.JobService;
import com.endava.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.JobImpressionsSupported;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskRestController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private WebClientService webClientService;


    @GetMapping("/{taskId}")
    public ResponseEntity<GetStatusOfTheTaskResponse> getStatusOfTheTask(@PathVariable int taskId) throws APIException {

        Task task = taskService.getTask(taskId);

        if (task != null){
            GetStatusOfTheTaskResponse getStatusOfTheTaskResponse = new GetStatusOfTheTaskResponse();
            getStatusOfTheTaskResponse.setTaskId(task.getTaskId());
            getStatusOfTheTaskResponse.setStatus(task.getStatus());
            getStatusOfTheTaskResponse.setTaskExecutionResponse(task.getTaskExecutionResponse());
            return new ResponseEntity<>(getStatusOfTheTaskResponse, HttpStatus.OK);
        } else {
            throw new APIException(StatusCode.TASK_NOT_FOUND);
        }
    }

    @PutMapping("/{taskId}/retry")
    public ResponseEntity<?> updateTask(@PathVariable int taskId) throws APIException {
        taskService.updateTask(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/execute")
    public void execute() throws APIException {
        webClientService.execute();
    }

}
