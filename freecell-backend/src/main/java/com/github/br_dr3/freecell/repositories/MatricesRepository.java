package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.repositories.entities.Matrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatricesRepository extends JpaRepository<Matrix, Long> {
    List<Matrix> findByGameId(Long gameId);

    @Query("SELECT count(distinct m.column) FROM Matrix m JOIN m.game g WHERE g.id = ?1")
    Integer countUsedColumns(Long gameId);
}
