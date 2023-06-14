package com.github.br_dr3.freecell.repositories.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "matrices")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Matrix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    @Column(name = "column_id", nullable = false)
    private Long column;

    @Column(name = "line_id", nullable = false)
    private Long line;
}
