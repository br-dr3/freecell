package com.github.br_dr3.freecell.entities;

import java.util.UUID;

import com.github.br_dr3.freecell.entities.enumeration.CardNumber;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private CardNumber cardNumber;
    private CardType cardType;
}
