package com.endava.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetadataDTO {

    private String httpMethod;
    private String baseUri;
    private String path;
    private String params;

}
