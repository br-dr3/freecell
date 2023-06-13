package com.github.br_dr3.freecell.gateway.controller.handlers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {
    @JsonProperty("detail")
    private String detail;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;
}
