package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.repositories.entities.Card;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {}
