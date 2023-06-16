package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.repositories.entities.Cell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellsRepository extends JpaRepository<Cell, Long> {
    List<Cell> findByGameId(Long gameId);

    @Query("SELECT count(1) FROM Cell c JOIN c.game g WHERE g.id = ?1 AND c.card IS NULL")
    Integer countEmpty(Long gameId);
}
