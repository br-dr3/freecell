package com.github.br_dr3.freecell.gateway.controller;

import com.github.br_dr3.freecell.gateway.dto.GameDTO;
import com.github.br_dr3.freecell.gateway.dto.MoveCardsRequestDTO;
import com.github.br_dr3.freecell.gateway.dto.NewGameRequestDTO;
import com.github.br_dr3.freecell.service.GamesService;
import com.github.br_dr3.freecell.util.DataWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/{gameId}/move")
    public ResponseEntity<DataWrapper<GameDTO>> moveCard(
            @PathVariable("gameId") Long gameId,
            @RequestBody MoveCardsRequestDTO moveCardsRequest) {
        var game = gamesService.moveCards(gameId, moveCardsRequest);

        return ResponseEntity.ok().body(DataWrapper.<GameDTO>builder().data(game).build());
    }
}
