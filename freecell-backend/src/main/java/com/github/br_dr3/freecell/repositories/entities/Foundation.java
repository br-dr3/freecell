package com.github.br_dr3.freecell.repositories.entities;

import com.github.br_dr3.freecell.repositories.entities.enumeration.CardSuit;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "foundations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "last_card_id", referencedColumnName = "id")
    private Card lastCard;
}
