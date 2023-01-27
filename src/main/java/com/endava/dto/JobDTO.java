package com.endava.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobDTO {

    private MetadataDTO metadata;
    private List<TaskDTO> tasks;
}
