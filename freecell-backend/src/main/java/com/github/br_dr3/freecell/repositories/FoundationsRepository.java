package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.repositories.entities.Foundation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoundationsRepository extends JpaRepository<Foundation, Long> {
    List<Foundation> findByGameId(Long gameId);
}
