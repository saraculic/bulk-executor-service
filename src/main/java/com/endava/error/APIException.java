package com.endava.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class APIException extends Exception {

    private StatusCode statusCode;
}
