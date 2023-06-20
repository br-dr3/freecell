package com.github.br_dr3.freecell.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("label")
    private String label;

    @JsonProperty("suit")
    private String suit;

    @JsonProperty("color")
    private String color;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("order")
    private Integer order;
}
