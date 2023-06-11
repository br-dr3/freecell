package com.github.br_dr3.freecell.distributor;

import com.github.br_dr3.freecell.gateway.dto.CardDTO;
import com.github.br_dr3.freecell.gateway.dto.CardsDTO;
import com.github.br_dr3.freecell.gateway.dto.MatrixDTO;

import java.util.List;
import java.util.stream.IntStream;

public class Distributor {
    private static final Integer NUMBER_OF_COLUMNS = 8;
    public static MatrixDTO distribute(List<CardDTO> cards) {
        var distributedCards = IntStream.range(0, NUMBER_OF_COLUMNS)
                .mapToObj(columnIndex -> IntStream.range(0, cards.size())
                        .filter(cardIndex -> cardIndex % NUMBER_OF_COLUMNS == columnIndex)
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
