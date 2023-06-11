package com.github.br_dr3.freecell.entities;

import java.util.UUID;

import com.github.br_dr3.freecell.entities.enumeration.CardNumber;

import com.github.br_dr3.freecell.entities.enumeration.CardType;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "number")
    @Enumerated(EnumType.STRING)
    private CardNumber cardNumber;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;
}
