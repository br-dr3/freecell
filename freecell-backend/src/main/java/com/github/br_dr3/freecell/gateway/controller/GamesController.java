package com.github.br_dr3.freecell.gateway.controller;

import com.github.br_dr3.freecell.gateway.dto.GameDTO;
import com.github.br_dr3.freecell.gateway.dto.NewGameRequestDTO;
import com.github.br_dr3.freecell.service.GamesService;
import com.github.br_dr3.freecell.util.DataWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
