package com.endava.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(value = APIException.class)
    public ResponseEntity<?> handleException(APIException exc){

        ErrorResponse error = new ErrorResponse();
        error.setCode(exc.getStatusCode().getHttpStatus().toString());
        error.setMessage(exc.getStatusCode().getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

}
