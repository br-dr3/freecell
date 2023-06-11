package com.github.br_dr3.freecell.service;

import com.github.br_dr3.freecell.distributor.Distributor;
import com.github.br_dr3.freecell.gateway.dto.*;
import com.github.br_dr3.freecell.mapper.CardsMapper;
import com.github.br_dr3.freecell.repositories.GamesRepository;
import com.github.br_dr3.freecell.repositories.InitialGamesRepository;
import com.github.br_dr3.freecell.repositories.entities.Game;
import com.github.br_dr3.freecell.repositories.entities.InitialGame;
import com.github.br_dr3.freecell.shuffler.Shuffler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class GamesService {
    @Autowired CardsService cardsService;
    @Autowired Distributor distributor;
    @Autowired GamesRepository gamesRepository;
    @Autowired InitialGamesRepository initialGamesRepository;
    @Autowired CardsMapper cardsMapper;
    public GameDTO newGame(NewGameRequestDTO newGameRequest) {
        var cards = cardsService.getCardsDTO();
        var gameDTO = GameDTO.builder()
                .seed(newGameRequest.getSeed())
                .cells(CellsDTO.builder().build())
                .foundation(FoundationDTO.builder().build())
                .matrix(getMatrixDTO(newGameRequest, cards))
                .moves(0L)
                .score(0L)
                .build();


        if(exists(gameDTO)) {
            updateGame(gameDTO, 0L);
        } else {
            var gameEntity = saveGame(gameDTO);
            saveInitialGame(gameEntity, gameDTO);
        }

        return gameDTO;
    }

    private Game updateGame(GameDTO gameDTO, long score) {
        var game = gamesRepository.findBySeed(gameDTO.getSeed());
        game.setScore(score);
        return gamesRepository.save(game);
    }

    private Game saveGame(GameDTO gameDTO) {
        return gamesRepository.save(Game.builder()
                .seed(gameDTO.getSeed())
                .score(gameDTO.getScore())
                .build());
    }

    private boolean exists(GameDTO gameDTO) {
        return gamesRepository.existsBySeed(gameDTO.getSeed());
    }

    private List<InitialGame> saveInitialGame(Game gameEntity, GameDTO gameDTO) {
        var columns = gameDTO.getMatrix().getColumns();

        var initialGames = columns.stream()
                .flatMap(column -> column.getCards()
                        .stream()
                        .flatMap(card -> Stream.of(Map.entry(column, card))))
                .map(e -> {
                    try {
                        return InitialGame.builder()
                                .game(gameEntity)
                                .card(cardsService.getCard(e.getValue().getId()))
                                .y(Integer.toUnsignedLong(columns.indexOf(e.getKey())))
                                .x(Integer.toUnsignedLong(e.getKey().getCards().indexOf(e.getValue())))
                                .build();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .toList();

        return StreamSupport.stream(initialGamesRepository.saveAll(initialGames).spliterator(), false)
                .toList();
    }

    private MatrixDTO getMatrixDTO(NewGameRequestDTO newGameRequest, CardsDTO cards) {
        var shuffledCards = Shuffler.shuffle(cards.getCards(), newGameRequest.getSeed());
        return distributor.distribute(shuffledCards);
    }
}
