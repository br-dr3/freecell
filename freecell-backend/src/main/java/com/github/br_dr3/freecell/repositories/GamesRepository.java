package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.repositories.entities.Game;
import org.springframework.data.repository.CrudRepository;

public interface GamesRepository extends CrudRepository<Game, Long> {
    Game findBySeed(Long seed);
    boolean existsBySeed(Long seed);
}
