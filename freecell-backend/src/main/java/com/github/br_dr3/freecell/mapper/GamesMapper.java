package com.github.br_dr3.freecell.mapper;

import com.github.br_dr3.freecell.config.ApplicationConfiguration;
import com.github.br_dr3.freecell.gateway.dto.CardDTO;
import com.github.br_dr3.freecell.gateway.dto.CardsDTO;
import com.github.br_dr3.freecell.gateway.dto.CardsDistributionDTO;
import com.github.br_dr3.freecell.gateway.dto.FoundationDTO;
import com.github.br_dr3.freecell.gateway.dto.GameDTO;
import com.github.br_dr3.freecell.gateway.dto.MatrixDTO;
import com.github.br_dr3.freecell.repositories.entities.Cell;
import com.github.br_dr3.freecell.repositories.entities.Foundation;
import com.github.br_dr3.freecell.repositories.entities.Game;
import com.github.br_dr3.freecell.repositories.entities.Matrix;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardLabel;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardSuit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class GamesMapper {
    @Autowired CardsMapper cardsMapper;
    @Autowired ApplicationConfiguration applicationConfiguration;

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

        return CardLabel.allLessEqualsThan(biggestSuit.getCardLabel())
                .stream()
                .map(cl -> CardDTO.builder()
                        .label(cl.getLabel())
                        .symbol(cs.getSymbol())
                        .suit(cs.name())
                        .color(cs.getColor().name())
                        .order(cl.getOrder())
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

    public GameDTO toGameDTO(Game game) {
        return GameDTO.builder()
                .id(game.getId())
                .seed(game.getSeed())
                .cardsDistributionDTO(
                        CardsDistributionDTO.builder()
                                .foundation(toFoundationDTO(game.getFoundations()))
                                .cells(toCardsDTO(game.getCells()))
                                .matrix(toMatrixDTO(game.getMatrices()))
                                .build()
                )
                .userId(game.getUser().getId())
                .moves(0L)
                .score(game.getScore())
                .build();
    }

    private CardsDTO toCardsDTO(List<Cell> cells) {
        var cards = cells.stream()
                .map(Cell::getCard)
                .filter(Objects::nonNull)
                .map(c -> cardsMapper.toCardDTO(c))
                .toList();

        return CardsDTO.builder()
                .cards(cards)
                .build();
    }
}
