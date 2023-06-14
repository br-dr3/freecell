package com.github.br_dr3.freecell.service;

import com.github.br_dr3.freecell.distributor.Distributor;
import com.github.br_dr3.freecell.exceptions.GameNotFoundException;
import com.github.br_dr3.freecell.exceptions.UserNotFoundException;
import com.github.br_dr3.freecell.gateway.dto.*;
import com.github.br_dr3.freecell.mapper.GamesMapper;
import com.github.br_dr3.freecell.repositories.*;
import com.github.br_dr3.freecell.repositories.entities.Cell;
import com.github.br_dr3.freecell.repositories.entities.Foundation;
import com.github.br_dr3.freecell.repositories.entities.Game;
import com.github.br_dr3.freecell.repositories.entities.Matrix;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardSuit;
import com.github.br_dr3.freecell.shuffler.Shuffler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class GamesService {
    @Autowired CardsService cardsService;
    @Autowired Distributor distributor;
    @Autowired GamesRepository gamesRepository;
    @Autowired UserRepository userRepository;
    @Autowired CellsRepository cellsRepository;
    @Autowired MatricesRepository matricesRepository;
    @Autowired FoundationsRepository foundationsRepository;
    @Autowired GamesMapper gamesMapper;

    public GameDTO createGame(NewGameRequestDTO newGameRequest) {
        var game = saveGame(newGameRequest);
        var cells = createCells(game);
        var foundations = createFoundations(game);
        var matrices = createMatrices(game, newGameRequest.getSeed());

        return GameDTO.builder()
                .id(game.getId())
                .seed(game.getSeed())
                .userId(game.getUser().getId())
                .cardsDistributionDTO(
                        CardsDistributionDTO.builder()
                        .cells(gamesMapper.toCellsDTO(cells))
                        .foundation(gamesMapper.toFoundationDTO(foundations))
                        .matrix(gamesMapper.toMatrixDTO(matrices))
                        .build())
                .moves(0L)
                .score(game.getScore())
                .build();
    }

    private List<Matrix> createMatrices(Game game, Long seed) {
        var cards = cardsService.getCardsDTO();
        var shuffleCards = Shuffler.shuffle(cards.getCards(), seed);
        var distributedCards = distributor.distribute(shuffleCards);

        var columns = distributedCards.getColumns();

        var matrices = columns.stream()
                .flatMap(c -> c.getCards()
                        .stream()
                        .flatMap(l -> Stream.of(Map.entry(columns.indexOf(c), c.getCards().indexOf(l)))))
                .map(e -> Matrix.builder()
                        .game(game)
                        .card(cardsService.getCard(getCardId(columns, e)))
                        .column(Long.valueOf(e.getKey()))
                        .line(Long.valueOf(e.getValue()))
                        .build())
                .toList();

        return matricesRepository.saveAll(matrices);
    }

    private static Long getCardId(List<CardsDTO> columns, Map.Entry<Integer, Integer> e) {
        return columns.get(e.getKey()).getCards().get(e.getValue()).getId();
    }

    private List<Foundation> createFoundations(Game game) {
        var foundations = Arrays.stream(CardSuit.values())
                .map(cs -> Foundation.builder()
                        .game(game)
                        .cardSuit(cs)
                        .build())
                .toList();

        return foundationsRepository.saveAll(foundations);
    }

    private List<Cell> createCells(Game game) {
        var cells = IntStream.range(0, 4)
                .mapToObj(i -> Cell.builder()
                        .game(game)
                        .build())
                .toList();

        return cellsRepository.saveAll(cells);
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

    public GameDTO getGame(Long gameId) {
        var potentialGame = gamesRepository.findById(gameId);

        if(potentialGame.isEmpty()) {
            throw new GameNotFoundException("Game with id '" + gameId + "' not found");
        }

        var game = potentialGame.get();

        return GameDTO.builder()
                .id(game.getId())
                .seed(game.getSeed())
                .userId(game.getUser().getId())
                .score(game.getScore())
                .build();
    }
}
