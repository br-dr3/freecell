package com.github.br_dr3.freecell.repositories.entities;

import com.github.br_dr3.freecell.repositories.entities.enumeration.CardLabel;

import com.github.br_dr3.freecell.repositories.entities.enumeration.CardSuit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "cards", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"label", "suit"})
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Card {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardLabel cardLabel;

    @Column(name = "suit", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSuit cardSuit;
}
