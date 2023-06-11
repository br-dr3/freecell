package com.github.br_dr3.freecell.repositories.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "initial_games")
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class InitialGame {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;

    @Column(name = "x")
    private Long x;

    @Column(name = "y")
    private Long y;
}
