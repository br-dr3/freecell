package com.github.br_dr3.freecell.entities;

import com.github.br_dr3.freecell.entities.enumeration.CardLabel;

import com.github.br_dr3.freecell.entities.enumeration.CardSuit;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    @Enumerated(EnumType.STRING)
    private CardLabel cardLabel;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CardSuit cardSuit;
}
