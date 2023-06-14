package com.github.br_dr3.freecell.repositories.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "cells")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Cell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false)
    private Game game;
}
