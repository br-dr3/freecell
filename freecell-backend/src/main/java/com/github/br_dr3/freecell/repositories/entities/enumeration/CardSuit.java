package com.github.br_dr3.freecell.repositories.entities.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CardSuit {
    CLUBS(Color.BLACK),
    HEARTS(Color.RED),
    SPADES(Color.BLACK),
    DIAMONDS(Color.RED);

    private final Color color;
}
