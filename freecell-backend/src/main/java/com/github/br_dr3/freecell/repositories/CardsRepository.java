package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.entities.Card;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardsRepository extends JpaRepository<Card, UUID>, JpaSpecificationExecutor<Card> {
    List<Card> findAll(Specification<Card> spec);
}
