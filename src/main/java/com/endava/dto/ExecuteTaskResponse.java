package com.endava.dto;

import com.endava.model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteTaskResponse {

    private List<TaskExecute> tasks;

    public void add(TaskExecute tempTask){
        if (tasks == null){
            tasks = new ArrayList<>();
        }
        tasks.add(tempTask);
    }
}
