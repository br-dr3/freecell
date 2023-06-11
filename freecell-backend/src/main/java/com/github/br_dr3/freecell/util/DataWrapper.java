package com.github.br_dr3.freecell.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataWrapper<T extends Object> {
    @JsonProperty("data")
    T data;
}
