package com.github.br_dr3.freecell.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;
}
