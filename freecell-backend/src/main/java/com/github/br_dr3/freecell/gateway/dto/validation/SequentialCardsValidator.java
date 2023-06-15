package com.github.br_dr3.freecell.gateway.dto.validation;

import com.github.br_dr3.freecell.gateway.dto.CardDTO;
import com.github.br_dr3.freecell.gateway.dto.CardsDTO;
import com.github.br_dr3.freecell.gateway.dto.validation.ann.SequentialCardsConstraint;
import com.github.br_dr3.freecell.repositories.entities.Card;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardLabel;
import com.github.br_dr3.freecell.repositories.entities.enumeration.Color;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SequentialCardsValidator implements ConstraintValidator<SequentialCardsConstraint, CardsDTO> {
    @Override
    public boolean isValid(CardsDTO cardsDTO, ConstraintValidatorContext constraintValidatorContext) {
        var cards = cardsDTO.getCards();

        if(cards.size() == 0) {
            return false;
        }

        var isNumberSequential = isValidNumberSequential(cards);
        var isColorSequential = isValidColorSequential(cards);

        return isNumberSequential && isColorSequential;
    }

    private boolean isValidColorSequential(List<CardDTO> cards) {
        var sequentialColor = cards.get(0).getColor().equals(Color.BLACK.name()) ?
                List.of(Color.BLACK.name(), Color.RED.name()) :
                List.of(Color.RED.name(), Color.BLACK.name());

        return cards.stream()
                .allMatch(c -> {
                    var cardIndex = cards.indexOf(c);
                    var colorsSize = Color.values().length;
                    var module = cardIndex % colorsSize;
                    var color = c.getColor();

                    return sequentialColor.get(module).equals(color);
                });

    }

    private boolean isValidNumberSequential(List<CardDTO> cards) {
        if(cards.size() == 1) {
            return true;
        }

        return IntStream.range(1, cards.size())
                .mapToObj(i -> Map.entry(cards.get(i-1), cards.get(i)))
                .map(e -> Map.entry(CardLabel.valueOf(e.getKey().getLabel()),
                        CardLabel.valueOf(e.getValue().getLabel())))
                .allMatch(e -> e.getKey()
                        .getOrder()
                        .equals(Integer.sum(e.getValue().getOrder(), 1)));
    }
}
