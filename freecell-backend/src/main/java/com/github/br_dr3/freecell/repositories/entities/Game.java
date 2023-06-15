package com.github.br_dr3.freecell.repositories.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "games")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Game {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seed")
    private Long seed;

    @Column(name = "score")
    private Long score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            mappedBy = "game")
    private List<Foundation> foundations;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            mappedBy = "game")
    private List<Cell> cells;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            mappedBy = "game",
            orphanRemoval = true)
    private List<Matrix> matrices;
}
