package com.endava.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TaskDTO {

    private Map<String, String> pathParameters;
    private Map<String, String> queryParameters;
    private String body;

}
