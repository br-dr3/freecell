package com.github.br_dr3.freecell.repositories;

import com.github.br_dr3.freecell.repositories.entities.Card;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardLabel;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardSuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    @Query("SELECT c FROM Card c WHERE c.cardLabel = ?1 AND c.cardSuit = ?2")
    Card getByCardLabelAndCardSuit(CardLabel cl, CardSuit cs);
}
