package com.github.br_dr3.freecell.repositories.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name = "games")
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class Game {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seed", unique = true)
    private Long seed;

    @Column(name = "score")
    private Long score;
}
