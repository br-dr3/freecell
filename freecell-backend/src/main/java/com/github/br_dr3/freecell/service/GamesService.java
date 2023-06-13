package com.github.br_dr3.freecell.service;

import com.github.br_dr3.freecell.distributor.Distributor;
import com.github.br_dr3.freecell.exceptions.UserNotFoundException;
import com.github.br_dr3.freecell.gateway.dto.*;
import com.github.br_dr3.freecell.mapper.CardsMapper;
import com.github.br_dr3.freecell.repositories.GamesRepository;
import com.github.br_dr3.freecell.repositories.UserRepository;
import com.github.br_dr3.freecell.repositories.entities.Game;
import com.github.br_dr3.freecell.shuffler.Shuffler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GamesService {
    @Autowired CardsService cardsService;
    @Autowired Distributor distributor;
    @Autowired GamesRepository gamesRepository;
    @Autowired CardsMapper cardsMapper;
    @Autowired UserRepository userRepository;

    public GameDTO newGame(NewGameRequestDTO newGameRequest) {
        var cards = cardsService.getCardsDTO();
        var game = saveGame(newGameRequest);
        return GameDTO.builder()
                .id(game.getId())
                .seed(game.getSeed())
                .userId(game.getUser().getId())
                .cells(CellsDTO.builder().build())
                .foundation(FoundationDTO.builder().build())
                .matrix(getMatrixDTO(newGameRequest, cards))
                .moves(0L)
                .score(game.getScore())
                .build();
    }

    private Game saveGame(NewGameRequestDTO gameRequest) {
        var userId = gameRequest.getUserId();
        var user = userRepository.findById(userId);

        if(user.isEmpty()) {
            throw new UserNotFoundException("User with id '"+ userId +"' not found.");
        }

        return gamesRepository.save(Game.builder()
                .user(user.get())
                .seed(gameRequest.getSeed())
                .score(0L)
                .build());
    }

    private MatrixDTO getMatrixDTO(NewGameRequestDTO newGameRequest, CardsDTO cards) {
        var shuffledCards = Shuffler.shuffle(cards.getCards(), newGameRequest.getSeed());
        return distributor.distribute(shuffledCards);
    }
}
