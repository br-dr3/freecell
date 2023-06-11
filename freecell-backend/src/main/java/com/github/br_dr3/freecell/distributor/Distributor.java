package com.github.br_dr3.freecell.distributor;

import com.github.br_dr3.freecell.config.ApplicationConfiguration;
import com.github.br_dr3.freecell.gateway.dto.CardDTO;
import com.github.br_dr3.freecell.gateway.dto.CardsDTO;
import com.github.br_dr3.freecell.gateway.dto.MatrixDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class Distributor {
    @Autowired
    ApplicationConfiguration applicationConfiguration;

    public MatrixDTO distribute(List<CardDTO> cards) {
        var numberOfColumns = applicationConfiguration.getNumberOfColumns();

        var distributedCards = IntStream.range(0, numberOfColumns)
                .mapToObj(columnIndex -> IntStream.range(0, cards.size())
                        .filter(cardIndex -> cardIndex % numberOfColumns == columnIndex)
                        .mapToObj(cards::get)
                        .toList())
                .map(cardColumn -> CardsDTO.builder()
                        .cards(cardColumn)
                        .build())
                .toList();

        return MatrixDTO.builder()
                .columns(distributedCards)
                .build();
    }
}
