package com.github.br_dr3.freecell.shuffler;

import com.github.br_dr3.freecell.gateway.dto.CardDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Shuffler {

    public static List<CardDTO> shuffle(List<CardDTO> cards, Integer seed) {
        var copy = new ArrayList<>(cards.stream().toList());

        var random = new Random(seed);
        Collections.shuffle(copy, random);

        return copy;
    }
}
