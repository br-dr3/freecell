package com.github.br_dr3.freecell.repositories.entities.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum CardLabel {
    ACE("A", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 11),
    QUEEN("Q", 12),
    KING("K", 13);

    private String label;
    private Integer order;

    public static List<CardLabel> allLessEqualsThan(CardLabel upperLimit) {
        return Arrays.stream(CardLabel.values())
                .filter(cl -> cl.getOrder() <= upperLimit.getOrder())
                .toList();
    }
}
