package com.github.br_dr3.freecell.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ApplicationConfiguration {
    @Value("${freecell.number-of-cards}")
    private Integer numberOfCards;

    @Value("${freecell.number-of-columns}")
    private Integer numberOfColumns;

    @Value("${freecell.number-of-cells}")
    private Integer numberOfCells;
}
