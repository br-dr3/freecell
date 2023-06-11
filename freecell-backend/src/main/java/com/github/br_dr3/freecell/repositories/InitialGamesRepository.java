package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.repositories.entities.InitialGame;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InitialGamesRepository extends CrudRepository<InitialGame, Long> {
}
