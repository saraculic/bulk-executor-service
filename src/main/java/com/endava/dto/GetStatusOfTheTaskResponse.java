package com.endava.dto;

import com.endava.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStatusOfTheTaskResponse {

    private int taskId;
    private Status status;
    private String taskExecutionResponse;

}
