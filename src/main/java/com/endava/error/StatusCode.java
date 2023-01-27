package com.endava.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatusCode {

    JOB_NOT_FOUND("Job id not found - ", HttpStatus.NOT_FOUND),
    TASK_NOT_FOUND("Task id not found", HttpStatus.NOT_FOUND),
    UNSUPPORTED_STATE("Only update to cancelled state is allowed", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_JOB_REQUEST_BODY("Bad request, Unsupported job request body format. ", HttpStatus.BAD_REQUEST),
    URI_MISSING("BaseURI is missing", HttpStatus.BAD_REQUEST),
    HTTP_METHOD_MISSING("HTTP method is missing", HttpStatus.BAD_REQUEST),

    MAX_RETRY_COUNT("Max retry count has been exceeded", HttpStatus.BAD_REQUEST);


    private String message;
    private HttpStatus httpStatus;

}
