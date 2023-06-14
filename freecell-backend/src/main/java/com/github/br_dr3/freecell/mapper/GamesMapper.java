package com.github.br_dr3.freecell.mapper;

import com.github.br_dr3.freecell.config.ApplicationConfiguration;
import com.github.br_dr3.freecell.distributor.Distributor;
import com.github.br_dr3.freecell.gateway.dto.*;
import com.github.br_dr3.freecell.repositories.entities.Cell;
import com.github.br_dr3.freecell.repositories.entities.Foundation;
import com.github.br_dr3.freecell.repositories.entities.Matrix;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardLabel;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardSuit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class GamesMapper {
    @Autowired CardsMapper cardsMapper;
    @Autowired ApplicationConfiguration applicationConfiguration;

    public CellsDTO toCellsDTO(List<Cell> cells) {
        var cards = cells.stream()
                .map(Cell::getCard)
                .filter(Objects::nonNull)
                .toList();

        return CellsDTO.builder()
                .cards(cardsMapper.toCardsDTO(cards))
                .build();
    }

    public FoundationDTO toFoundationDTO(List<Foundation> foundations) {
        var cardsSuits = Arrays.stream(CardSuit.values())
                .map(cs -> Map.entry(cs, CardsDTO.builder()
                        .cards(getFoundationOfSuit(foundations, cs))
                        .build()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return FoundationDTO.builder()
                .clubs(cardsSuits.get(CardSuit.CLUBS))
                .diamonds(cardsSuits.get(CardSuit.DIAMONDS))
                .hearts(cardsSuits.get(CardSuit.HEARTS))
                .spades(cardsSuits.get(CardSuit.SPADES))
                .build();
    }

    private static List<CardDTO> getFoundationOfSuit(List<Foundation> foundations, CardSuit cs) {
        var biggestSuit = foundations.stream()
                .filter(f -> f.getCardSuit().equals(cs))
                .map(Foundation::getLastCard)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if(Objects.isNull(biggestSuit)) {
            return List.of();
        }

        return CardLabel.allLessThan(biggestSuit.getCardLabel())
                .stream()
                .map(cl -> CardDTO.builder()
                        .label(cl.getLabel())
                        .type(cs.name())
                        .color(cs.getColor().name())
                        .build())
                .toList();
    }

    public MatrixDTO toMatrixDTO(List<Matrix> matrices) {
        var columns = IntStream.range(0, applicationConfiguration.getNumberOfColumns())
                .mapToObj(Long::valueOf)
                .flatMap(i -> Stream.of(matrices.stream()
                        .filter(m -> m.getColumn().equals(i))
                        .toList()))
                .map(c -> c.stream().map(Matrix::getCard).toList())
                .map(c -> cardsMapper.toCardsDTO(c))
                .toList();

        return MatrixDTO.builder()
                .columns(columns)
                .build();
    }
}
