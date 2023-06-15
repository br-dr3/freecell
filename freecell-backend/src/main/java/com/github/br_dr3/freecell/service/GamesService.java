package com.github.br_dr3.freecell.service;

import com.github.br_dr3.freecell.config.ApplicationConfiguration;
import com.github.br_dr3.freecell.distributor.Distributor;
import com.github.br_dr3.freecell.exceptions.*;
import com.github.br_dr3.freecell.gateway.dto.*;
import com.github.br_dr3.freecell.mapper.GamesMapper;
import com.github.br_dr3.freecell.repositories.*;
import com.github.br_dr3.freecell.repositories.entities.*;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardLabel;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardSuit;
import com.github.br_dr3.freecell.shuffler.Shuffler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
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

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    public GameDTO createGame(NewGameRequestDTO newGameRequest) {
        var user = getUserEntity(newGameRequest.getUserId());
        var cells = createCells();
        var foundations = createFoundations();
        var matrices = createMatrices(newGameRequest.getSeed());
        var seed = newGameRequest.getSeed();

        var game = saveGame(user, cells, foundations, matrices, seed, 0L);
        return gamesMapper.toGameDTO(game);
    }

    private User getUserEntity(Long userId) {
        var user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User with id '"+ userId +"' not found.");
        }
        return user.get();
    }

    private List<Matrix> createMatrices(Long seed) {
        var cards = cardsService.getCardsDTO();
        var shuffleCards = Shuffler.shuffle(cards.getCards(), seed);
        var distributedCards = distributor.distribute(shuffleCards);

        var columns = distributedCards.getColumns();

        return columns.stream()
                .flatMap(c -> c.getCards()
                        .stream()
                        .flatMap(l -> Stream.of(Map.entry(columns.indexOf(c), c.getCards().indexOf(l)))))
                .map(e -> Matrix.builder()
                        .card(cardsService.getCard(getCardId(columns, e)))
                        .column(Long.valueOf(e.getKey()))
                        .line(Long.valueOf(e.getValue()))
                        .build())
                .toList();
    }

    private static Long getCardId(List<CardsDTO> columns, Map.Entry<Integer, Integer> e) {
        return columns.get(e.getKey()).getCards().get(e.getValue()).getId();
    }

    private List<Foundation> createFoundations() {
        return Arrays.stream(CardSuit.values())
                .map(cs -> Foundation.builder()
                        .cardSuit(cs)
                        .build())
                .toList();
    }

    private List<Cell> createCells() {
        return IntStream.range(0, applicationConfiguration.getNumberOfCells())
                .mapToObj(i -> Cell.builder()
                        .build())
                .toList();
    }

    private Game saveGame(User user, List<Cell> cells, List<Foundation> foundations, List<Matrix> matrices, Long seed, Long score) {
        return saveGame(Game.builder()
                .user(user)
                .cells(cells)
                .foundations(foundations)
                .matrices(matrices)
                .seed(seed)
                .score(score)
                .build());
    }

    private Game saveGame(Game game) {
        return gamesRepository.save(game);
    }

    public GameDTO getGame(Long gameId) {
        var game = getGameEntity(gameId);
        return gamesMapper.toGameDTO(game);
    }

    private Game getGameEntity(Long gameId) {
        var potentialGame = gamesRepository.findById(gameId);

        if(potentialGame.isEmpty()) {
            throw new GameNotFoundException("Game with id '" + gameId + "' not found");
        }

        return potentialGame.get();
    }

    public GameDTO moveCards(Long gameId, MoveCardsRequestDTO moveCardsRequest) {
        var cardsToMove = moveCardsRequest.getCardsToMove();

        if(!isMovable(cardsToMove)) {
            throw new CardsNotMovableException("These cards are not movable");
        }

        Game updatedGame;
        var actualGame = getGameEntity(gameId);
        if(moveCardsRequest.isToFoundation()) {
            updatedGame = moveToFoundation(actualGame, cardsToMove);
        } else if(moveCardsRequest.isToCell()) {
            updatedGame = moveToCell(actualGame, cardsToMove);
        } else {
            updatedGame = moveToColumn(actualGame, cardsToMove, moveCardsRequest.getColumn());
        }

        return gamesMapper.toGameDTO(updatedGame);
    }

    private Game moveToColumn(Game actualGame, CardsDTO cardsToMove, Long column) {
        return actualGame;
    }

    private Game moveToCell(Game actualGame, CardsDTO cardsToMove) {
        return actualGame;
    }

    private Game moveToFoundation(Game gameToUpdate, CardsDTO cardsToMove) {
        if(cardsToMove.getCards().size() != 1) {
            throw new TooManyCardsException("You can only send one card per time to Foundation");
        }

        var cardToMove = cardsToMove.getCards().get(0);
        var labelCardToMove = CardLabel.valueOf(cardToMove.getLabel());
        var suitCardToMove = CardSuit.valueOf(cardToMove.getType());

        var foundation = gameToUpdate.getFoundations()
                .stream()
                .filter(f -> f.getCardSuit().equals(suitCardToMove))
                .findFirst()
                .orElseThrow(() -> new FoundationNotFoundException("Foundation '"+ suitCardToMove +"' not found."));

        if((Objects.isNull(foundation.getLastCard())
                && !labelCardToMove.equals(CardLabel.ACE))
                || (Objects.nonNull(foundation.getLastCard())
                        && foundation.getLastCard().getCardLabel().getOrder() + 1 != labelCardToMove.getOrder())) {
            throw new CardsNotMovableException("The card '" + cardToMove + "' cannot go to Foundation");
        }

        var cardToMoveEntity = cardsService.getCard(labelCardToMove, suitCardToMove);
        foundation.setLastCard(cardToMoveEntity);

        gameToUpdate.getFoundations()
                .stream()
                .map(f -> Map.entry(gameToUpdate.getFoundations().indexOf(f), f))
                .filter(e -> e.getValue().getCardSuit().equals(foundation.getCardSuit()))
                .forEach(e -> gameToUpdate.getFoundations().set(e.getKey(), foundation));

        gameToUpdate.getCells()
                .stream()
                .map(c -> Map.entry(gameToUpdate.getCells().indexOf(c), c))
                .filter(e -> Objects.nonNull(e.getValue().getCard())
                        && e.getValue().getCard().equals(cardToMoveEntity))
                .forEach(e -> gameToUpdate.getCells()
                        .set(e.getKey(),
                                Cell.builder()
                                        .id(e.getValue().getId())
                                        .game(gameToUpdate)
                                        .card(null)
                                        .build()));

        var matricesToDelete = gameToUpdate.getMatrices()
                .stream()
                .filter(m -> m.getCard().equals(cardToMoveEntity))
                .toList();
        matricesToDelete.forEach(m -> gameToUpdate.getMatrices().remove(m));

        gameToUpdate.setScore(gameToUpdate.getScore() + 100);

        return saveGame(gameToUpdate);
    }

    private boolean isMovable(CardsDTO cardsToMove) {
        return true;
    }
}
