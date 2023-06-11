package com.github.br_dr3.freecell.service;

import com.github.br_dr3.freecell.distributor.Distributor;
import com.github.br_dr3.freecell.gateway.dto.CellsDTO;
import com.github.br_dr3.freecell.gateway.dto.FoundationDTO;
import com.github.br_dr3.freecell.gateway.dto.GameDTO;
import com.github.br_dr3.freecell.gateway.dto.NewGameRequestDTO;
import com.github.br_dr3.freecell.shuffler.Shuffler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GamesService {
    @Autowired CardsService cardsService;
    @Autowired Distributor distributor;
    public GameDTO newGame(NewGameRequestDTO newGameRequest) {
        var cards = cardsService.getCards();
        var shuffledCards = Shuffler.shuffle(cards.getCards(), newGameRequest.getSeed());
        var distributedCards = distributor.distribute(shuffledCards);

        return GameDTO.builder()
                .cells(CellsDTO.builder().build())
                .foundation(FoundationDTO.builder().build())
                .matrix(distributedCards)
                .moves(0)
                .score(0)
                .build();
    }
}
