package com.github.br_dr3.freecell.gateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.br_dr3.freecell.gateway.dto.validation.ann.PositionConstraint;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@PositionConstraint
public class MoveCardsRequestDTO {
    @JsonProperty("head")
    private CardDTO head;

    @JsonProperty("foundation")
    private boolean toFoundation;

    @JsonProperty("cell")
    private boolean toCell;

    @JsonProperty("column")
    private Long column;
}
