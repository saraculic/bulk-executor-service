package com.endava.dto;

import com.endava.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetJobStatusResponse {

    private Status status;
    private List<String> failedTasksId;

}
