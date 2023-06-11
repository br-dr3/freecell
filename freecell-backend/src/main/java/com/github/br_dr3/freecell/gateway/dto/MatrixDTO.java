package com.github.br_dr3.freecell.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatrixDTO {
    @JsonProperty("columns")
    private List<CardsDTO> columns;
}
