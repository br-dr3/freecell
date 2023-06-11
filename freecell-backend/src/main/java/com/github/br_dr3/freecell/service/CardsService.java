package com.github.br_dr3.freecell.service;

import com.github.br_dr3.freecell.gateway.dto.CardsDTO;
import com.github.br_dr3.freecell.mapper.CardsMapper;
import com.github.br_dr3.freecell.repositories.CardsRepository;
import com.github.br_dr3.freecell.repositories.entities.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CardsService {
    @Autowired CardsRepository cardsRepository;
    @Autowired CardsMapper cardsMapper;
    public CardsDTO getCardsDTO() {
        var cards = cardsRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return cardsMapper.toCardsDTO(cards);
    }

    public Card getCard(Long id) {
        return cardsRepository.getReferenceById(id);
    }
}
