package com.github.br_dr3.freecell.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("number")
    private String number;

    @JsonProperty("type")
    private String type;

    @JsonProperty("color")
    private String color;
}
