package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.repositories.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamesRepository extends JpaRepository<Game, Long> {
    Game findBySeed(Long seed);
    boolean existsBySeed(Long seed);
}
