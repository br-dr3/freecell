package com.github.br_dr3.freecell.service;

import com.github.br_dr3.freecell.gateway.dto.CardsDTO;
import com.github.br_dr3.freecell.mapper.CardsMapper;
import com.github.br_dr3.freecell.repositories.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardsService {
    @Autowired CardsRepository cardsRepository;
    @Autowired CardsMapper cardsMapper;
    public CardsDTO getCards() {
        var cards = cardsRepository.findAll();
        return cardsMapper.toCardsDTO(cards);
    }
}
