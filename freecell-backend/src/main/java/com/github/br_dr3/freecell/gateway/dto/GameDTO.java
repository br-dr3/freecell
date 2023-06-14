package com.github.br_dr3.freecell.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("cardsDistribution")
    private CardsDistributionDTO cardsDistributionDTO;

    @JsonProperty("seed")
    private Long seed;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("moves")
    private Long moves;

    @JsonProperty("score")
    private Long score;
}
