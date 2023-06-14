package com.github.br_dr3.freecell.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardsDistributionDTO {
    @JsonProperty("matrix")
    private MatrixDTO matrix;

    @JsonProperty("foundation")
    private FoundationDTO foundation;

    @JsonProperty("cells")
    private CellsDTO cells;
}
