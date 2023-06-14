package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.repositories.entities.Cell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellsRepository extends JpaRepository<Cell, Long> {
    List<Cell> findByGameId(Long gameId);
}
