package com.github.br_dr3.freecell.mapper;

import com.github.br_dr3.freecell.gateway.dto.CardDTO;
import com.github.br_dr3.freecell.gateway.dto.CardsDTO;
import com.github.br_dr3.freecell.repositories.entities.Card;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardsMapper {
    public CardsDTO toCardsDTO(List<Card> cards) {
        var convertedCards = cards.stream()
                .map(this::toCardDTO)
                .toList();

        return CardsDTO.builder()
                .cards(convertedCards)
                .build();
    }
    public CardDTO toCardDTO(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .label(card.getCardLabel().getLabel())
                .type(card.getCardSuit().name())
                .color(card.getCardSuit().getColor().name())
                .symbol(card.getCardSuit().getSymbol())
                .build();
    }
}
