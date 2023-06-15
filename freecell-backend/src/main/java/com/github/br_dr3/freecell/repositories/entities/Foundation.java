package com.github.br_dr3.freecell.repositories.entities;

import com.github.br_dr3.freecell.repositories.entities.Card;
import com.github.br_dr3.freecell.repositories.entities.Game;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardSuit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "foundations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Foundation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "suit", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSuit cardSuit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false)
    private Game game;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "last_card_id", referencedColumnName = "id")
    private Card lastCard;
}
