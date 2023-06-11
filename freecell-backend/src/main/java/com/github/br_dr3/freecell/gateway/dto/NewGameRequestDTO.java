package com.github.br_dr3.freecell.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Valid
public class NewGameRequestDTO {
    @JsonProperty("id")
    @Min(1)
    @Max(30000)
    private Integer seed;
}
