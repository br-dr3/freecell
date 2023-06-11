package com.github.br_dr3.freecell.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FoundationDTO {
    @JsonProperty("clubs")
    private CardsDTO clubs;

    @JsonProperty("hearts")
    private CardsDTO hearts;

    @JsonProperty("spades")
    private CardsDTO spades;

    @JsonProperty("diamonds")
    private CardsDTO diamonds;
}
