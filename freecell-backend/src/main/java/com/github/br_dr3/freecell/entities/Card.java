package com.github.br_dr3.freecell.entities;

import com.github.br_dr3.freecell.entities.enumeration.CardLabel;

import com.github.br_dr3.freecell.entities.enumeration.CardSuit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "cards")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
