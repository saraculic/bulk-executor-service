package com.endava.client;

import com.endava.dto.ExecuteTaskResponse;
import com.endava.dto.TaskExecute;
import com.endava.error.APIException;
import com.endava.model.Status;
import com.endava.model.Task;
import com.endava.service.JobService;
import com.endava.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class WebClientService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JobService jobService;

    private WebClient webClient;

    public WebClientService() {
        this.webClient = WebClient.create();
    }

    public ResponseEntity<ExecuteTaskResponse> execute() throws APIException {

        Map<Integer, String> map = taskService.taskExecute();
        int i = 1;
        ExecuteTaskResponse executeTaskResponse = new ExecuteTaskResponse();

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println("Start: Task " + i );
            String request = entry.getValue();
            Task task = taskService.getTask(entry.getKey());

            ResponseEntity<String> responseEntity = webClient.get()
                    .uri(request)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 401,
                            clientResponse -> Mono.empty()
                    )
                    .toEntity(String.class)
                    .block();

            if (responseEntity.getStatusCode().is4xxClientError()) {
                taskService.updateTaskStatus(task, Status.FAILED);
            } else {
                taskService.updateTaskStatus(task, Status.SUCCESS);
                jobService.incrementNumberOfCompletedTasks(task);
            }

            TaskExecute taskExecute = new TaskExecute();
            taskExecute.setTaskId(entry.getKey());
            taskExecute.setHttpStatus(responseEntity.getStatusCode());
            executeTaskResponse.add(taskExecute);
            System.out.println("Finish: Task " + i);
            i++;
        }
        return new ResponseEntity<>(executeTaskResponse, HttpStatus.OK);
    }
}
