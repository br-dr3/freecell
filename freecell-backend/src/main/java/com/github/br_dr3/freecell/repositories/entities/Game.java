package com.github.br_dr3.freecell.repositories.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "games")
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Game {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seed")
    private Long seed;

    @Column(name = "score")
    private Long score;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private List<Foundation> foundations;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private List<Cell> cells;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game", orphanRemoval = true)
    private List<Matrix> matrices;
}
