package com.github.br_dr3.freecell.gateway.controller;

import com.github.br_dr3.freecell.gateway.dto.CardDTO;
import com.github.br_dr3.freecell.gateway.dto.GameDTO;
import com.github.br_dr3.freecell.gateway.dto.MoveCardsRequestDTO;
import com.github.br_dr3.freecell.gateway.dto.NewGameRequestDTO;
import com.github.br_dr3.freecell.repositories.entities.Matrix;
import com.github.br_dr3.freecell.repositories.entities.enumeration.CardSuit;
import com.github.br_dr3.freecell.service.GamesService;
import com.github.br_dr3.freecell.util.DataWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/games")
public class GamesController {

    @Autowired
    private GamesService gamesService;

    @RequestMapping(value = { "/newGame", "" }, method = RequestMethod.POST)
    public ResponseEntity<DataWrapper<GameDTO>> newGame(@RequestBody NewGameRequestDTO newGameRequest) {
        var game = gamesService.createGame(newGameRequest);

        return ResponseEntity.ok().body(DataWrapper.<GameDTO>builder().data(game).build());
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<DataWrapper<GameDTO>> getGame(@PathVariable("gameId") Long gameId) {
        var game = gamesService.getGame(gameId);

        return ResponseEntity.ok().body(DataWrapper.<GameDTO>builder().data(game).build());
    }

    @GetMapping("/{gameId}/vision")
    public ResponseEntity<String> getGameVision(@PathVariable("gameId") Long gameId) {
        var game = gamesService.getGame(gameId);
        return ResponseEntity.ok(getVision(game));
    }

    private static String getVision(GameDTO game) {
        var cardsDistribution = game.getCardsDistributionDTO();
        var foundation = cardsDistribution.getFoundation();
        var cells = cardsDistribution.getCells();
        var matrix = cardsDistribution.getMatrix();

        var foundationResponse = Stream.of(foundation.getClubs(), foundation.getHearts(), foundation.getSpades(), foundation.getDiamonds())
                .map(l -> !l.getCards().isEmpty()? l.getCards().get(l.getCards().size()-1):null)
                .map(c -> c == null? "__ __" : StringUtils.leftPad(c.getLabel(), 2) + " " + c.getSymbol())
                .map(c -> StringUtils.rightPad(c, 4))
                .reduce((ns, acc) -> ns + "   " + acc)
                .orElse("");

        var cellResponse = Stream.of(cells.getCards(),
                IntStream.range(0, 4-cells.getCards().size()).mapToObj(i -> CardDTO.builder().build()).toList())
                .flatMap(Collection::stream)
                .map(c -> c.getLabel() == null? "__ __": StringUtils.leftPad(c.getLabel(), 2) + " " + c.getSymbol())
                .map(c -> StringUtils.rightPad(c, 5))
                .reduce((ns, acc) -> ns + "   " + acc)
                .orElse("");

        Comparator<Map.Entry<List<Integer>, CardDTO>> first = Comparator.comparing(e -> e.getKey().get(0));
        Comparator<Map.Entry<List<Integer>, CardDTO>> second = Comparator.comparing(e -> e.getKey().get(1));
        var biggestColumn = matrix.getColumns().stream().map(c -> c.getCards().size())
                .max(Integer::compare).orElse(0);

        var matrixResponse = IntStream.range(0, matrix.getColumns().size())
                .boxed()
                .flatMap(i -> IntStream.range(0, biggestColumn)
                        .boxed()
                        .flatMap(j -> Stream.of(Map.entry(i, j))))
                .map(e -> {
                    var i = e.getKey();
                    var j = e.getValue();
                    var card = matrix.getColumns().get(i).getCards().size() <= j?
                            CardDTO.builder().build()
                            : matrix.getColumns().get(i).getCards().get(j);
                    return Map.entry(List.<Integer>of(i, j), card);
                })
                .sorted(second.thenComparing(first))
                .map(e -> Map.entry(e.getKey(),
                        e.getValue().getLabel() == null?
                                "     ":
                                StringUtils.leftPad(e.getValue().getLabel(), 2)
                                        + " " + e.getValue().getSymbol()))
                .map(e -> e.getKey().get(0) == 0 ? "\n" + e.getValue() : e.getValue())
                .reduce((ns, acc) -> ns + "   " + acc)
                .orElse("");

        return foundationResponse + "          " + cellResponse
                + "\n\n" + matrixResponse;
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<DataWrapper<GameDTO>> moveCard(
            @PathVariable("gameId") Long gameId,
            @RequestBody MoveCardsRequestDTO moveCardsRequest) {
        var game = gamesService.moveCards(gameId, moveCardsRequest);

        return ResponseEntity.ok().body(DataWrapper.<GameDTO>builder().data(game).build());
    }

    @PostMapping("/{gameId}/move/vision")
    public ResponseEntity<String> moveCardVision(
            @PathVariable("gameId") Long gameId,
            @RequestBody MoveCardsRequestDTO moveCardsRequest) {
        var game = gamesService.moveCards(gameId, moveCardsRequest);

        return ResponseEntity.ok(getVision(game));
    }
}
