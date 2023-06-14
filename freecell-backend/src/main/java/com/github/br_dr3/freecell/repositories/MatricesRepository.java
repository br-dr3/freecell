package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.repositories.entities.Matrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatricesRepository extends JpaRepository<Matrix, Long> {
    List<Matrix> findByGameId(Long gameId);
}
