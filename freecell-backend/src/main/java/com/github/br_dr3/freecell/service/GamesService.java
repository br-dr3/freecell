package com.github.br_dr3.freecell.service;

import com.github.br_dr3.freecell.config.ApplicationConfiguration;
import com.github.br_dr3.freecell.distributor.Distributor;
import com.github.br_dr3.freecell.exceptions.AllCellsBusyException;
import com.github.br_dr3.freecell.exceptions.AlredyInCellException;
import com.github.br_dr3.freecell.exceptions.CardInFoundationException;
import com.github.br_dr3.freecell.exceptions.CardNotFoundException;
import com.github.br_dr3.freecell.exceptions.CardsNotMovableException;
import com.github.br_dr3.freecell.exceptions.FoundationNotFoundException;
import com.github.br_dr3.freecell.exceptions.GameNotFoundException;
import com.github.br_dr3.freecell.exceptions.TooManyCardsException;
import com.github.br_dr3.freecell.exceptions.UserNotFoundException;
import com.github.br_dr3.freecell.gateway.dto.CardDTO;
import com.github.br_dr3.freecell.gateway.dto.CardsDTO;
import com.github.br_dr3.freecell.gateway.dto.GameDTO;
import com.github.br_dr3.freecell.gateway.dto.MoveCardsRequestDTO;
import com.github.br_dr3.freecell.gateway.dto.NewGameRequestDTO;
import com.github.br_dr3.freecell.mapper.GamesMapper;
import com.github.br_dr3.freecell.repositories.CardsRepository;
import com.github.br_dr3.freecell.repositories.GamesRepository;
import com.github.br_dr3.freecell.repositories.UserRepository;
import com.github.br_dr3.freecell.repositories.entities.Card;
import com.github.br_dr3.freecell.repositories.entities.Cell;
import com.github.br_dr3.freecell.repositories.entities.Foundation;
import com.github.br_dr3.freecell.repositories.entities.Game;
import com.github.br_dr3.freecell.repositories.entities.Matrix;
import com.github.br_dr3.freecell.repositories.entities.User;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardLabel;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardSuit;
import com.github.br_dr3.freecell.shuffler.Shuffler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class GamesService {
    @Autowired Distributor distributor;
    @Autowired GamesRepository gamesRepository;
    @Autowired UserRepository userRepository;
    @Autowired CardsRepository cardsRepository;
    @Autowired GamesMapper gamesMapper;
    @Autowired CardsService cardsService;
    @Autowired ApplicationConfiguration applicationConfiguration;

    public GameDTO createGame(NewGameRequestDTO newGameRequest) {
        var user = getUserEntity(newGameRequest.getUserId());
        var cells = createCells();
        var foundations = createFoundations();
        var matrices = createMatrices(newGameRequest.getSeed());
        var seed = newGameRequest.getSeed();

        var game = saveGame(user, cells, foundations, matrices, seed, 0L);
        return gamesMapper.toGameDTO(game);
    }

    public GameDTO getGame(Long gameId) {
        var game = getGameEntity(gameId);
        return gamesMapper.toGameDTO(game);
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
                        .card(getCardEntity(getCardId(columns, e)))
                        .column(Long.valueOf(e.getKey()))
                        .line(Long.valueOf(e.getValue()))
                        .build())
                .toList();
    }

    private Card getCardEntity(Long id) {
        var potentialCard = cardsRepository.findById(id);

        if(potentialCard.isEmpty()) {
            throw new CardNotFoundException("Card with id '"+ id +"' not found");
        }

        return potentialCard.get();
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
        var game = Game.builder()
                .user(user)
                .cells(cells)
                .foundations(foundations)
                .matrices(matrices)
                .seed(seed)
                .score(score)
                .build();

        game.getCells().forEach(c -> c.setGame(game));
        game.getFoundations().forEach(f -> f.setGame(game));
        game.getMatrices().forEach(m -> m.setGame(game));

        return saveGame(game);
    }

    private Game saveGame(Game game) {
        var updatedGame = gamesRepository.save(game);
        return updatedGame;
    }

    private Game getGameEntity(Long gameId) {
        var potentialGame = gamesRepository.findById(gameId);

        if(potentialGame.isEmpty()) {
            throw new GameNotFoundException("Game with id '" + gameId + "' not found");
        }

        return potentialGame.get();
    }

    private Game moveToColumn(Game actualGame, CardsDTO cardsToMove, Long column) {
        return actualGame;
    }

    private Game moveToCell(Game gameToUpdate, CardsDTO cardsToMove) {
        var cardToMove = getCardToMove(cardsToMove);
        var labelCardToMove = CardLabel.valueOf(cardToMove.getLabel());
        var suitCardToMove = CardSuit.valueOf(cardToMove.getType());

        var cardToMoveEntity = cardsRepository.getByCardLabelAndCardSuit(labelCardToMove, suitCardToMove);

        var alreadyInCell = gameToUpdate.getCells()
                .stream()
                .anyMatch(c -> Objects.nonNull(c.getCard())
                        && c.getCard().equals(cardToMoveEntity));

        if(alreadyInCell) {
            throw new AlredyInCellException("Card '"+cardToMove+"' already in cell");
        }

        var isInFoundation = gameToUpdate.getFoundations()
                .stream()
                .filter(f -> f.getCardSuit().equals(suitCardToMove)
                        && Objects.nonNull(f.getLastCard()))
                .map(f -> f.getLastCard().getCardLabel())
                .anyMatch(cl -> cl.getOrder() >= labelCardToMove.getOrder());

        if(isInFoundation) {
            throw new CardInFoundationException("Card '" + cardToMove + "' in Foundation, cannot be moved to cell");
        }

        var availableCell = gameToUpdate.getCells()
                .stream()
                .filter(c -> Objects.isNull(c.getCard()))
                .findFirst()
                .orElseThrow(() -> new AllCellsBusyException("All cells are busy."));

        gameToUpdate.getMatrices()
                .stream()
                .filter(m -> m.getCard().equals(cardToMoveEntity))
                .toList()
                .forEach(m -> gameToUpdate.getMatrices().remove(m));

        availableCell.setCard(cardToMoveEntity);
        gameToUpdate.getCells()
                .stream()
                .filter(c -> c.getId().equals(availableCell.getId()))
                .map(c -> gameToUpdate.getCells().indexOf(c))
                .forEach(i -> gameToUpdate.getCells().set(i, availableCell));

        return saveGame(gameToUpdate);
    }

    private static CardDTO getCardToMove(CardsDTO cardsToMove) {
        if(cardsToMove.getCards().size() != 1) {
            throw new TooManyCardsException("You can only send one card per time to Foundation or Cell");
        }

        return cardsToMove.getCards().get(0);
    }

    private Game moveToFoundation(Game gameToUpdate, CardsDTO cardsToMove) {
        var cardToMove = getCardToMove(cardsToMove);
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

        var cardToMoveEntity = cardsRepository.getByCardLabelAndCardSuit(labelCardToMove, suitCardToMove);

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

        gameToUpdate.getMatrices()
                .removeIf(m -> m.getCard().equals(cardToMoveEntity));

        foundation.setLastCard(cardToMoveEntity);
        gameToUpdate.getFoundations()
                .stream()
                .map(f -> Map.entry(gameToUpdate.getFoundations().indexOf(f), f))
                .filter(e -> e.getValue().getCardSuit().equals(foundation.getCardSuit()))
                .forEach(e -> gameToUpdate.getFoundations().set(e.getKey(), foundation));
        gameToUpdate.setScore(gameToUpdate.getScore() + 100);

        return saveGame(gameToUpdate);
    }

    private boolean isMovable(CardsDTO cardsToMove) {
        return true;
    }
}
