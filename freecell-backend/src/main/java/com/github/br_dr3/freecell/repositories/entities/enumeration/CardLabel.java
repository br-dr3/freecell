package com.github.br_dr3.freecell.repositories.entities.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CardLabel {
    ACE("A"),
    TWO("2"), 
    THREE("3"), 
    FOUR("4"), 
    FIVE("5"), 
    SIX("6"), 
    SEVEN("7"), 
    EIGHT("8"), 
    NINE("9"), 
    TEN("10"), 
    JACK("J"), 
    QUEEN("Q"), 
    KING("K");

    private String label;

    public static CardLabel fromLabel(String candidate) throws Exception {
        return Arrays.stream(CardLabel.values())
                .filter(cl -> cl.getLabel().equals(candidate))
                .findAny()
                .orElseThrow(() -> new Exception("Card Label not Found"));
    }
}
