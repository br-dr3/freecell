package com.github.br_dr3.freecell.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seed", unique = true)
    private Long seed;

    @Column(name = "score")
    private Long score;
}
