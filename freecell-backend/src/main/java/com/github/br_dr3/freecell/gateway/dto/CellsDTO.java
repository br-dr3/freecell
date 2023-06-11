package com.github.br_dr3.freecell.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class CellsDTO {
    @JsonProperty("cards")
    @Size(min = 0, max = 4)
    private CardsDTO cards;
}
