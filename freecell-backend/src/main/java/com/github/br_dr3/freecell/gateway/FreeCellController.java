package com.github.br_dr3.freecell.gateway;

import com.github.br_dr3.freecell.gateway.dto.CardsDTO;
import com.github.br_dr3.freecell.gateway.dto.GameDTO;
import com.github.br_dr3.freecell.gateway.dto.NewGameRequestDTO;
import com.github.br_dr3.freecell.service.CardsService;
import com.github.br_dr3.freecell.service.GamesService;
import com.github.br_dr3.freecell.util.DataWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/freecell")
public class FreeCellController {
    @Autowired
    private CardsService cardsService;
    @Autowired
    private GamesService gamesService;

    @GetMapping("/cards")
    public ResponseEntity<DataWrapper<CardsDTO>> getCards() {
        var cards = cardsService.getCardsDTO();
        return ResponseEntity.ok().body(DataWrapper.<CardsDTO> builder().data(cards).build());
    }

    @PostMapping("/newGame")
    public ResponseEntity<DataWrapper<GameDTO>> newGame(@RequestBody NewGameRequestDTO newGameRequest) {
        var game = gamesService.newGame(newGameRequest);

        return ResponseEntity.ok().body(DataWrapper.<GameDTO>builder().data(game).build());
    }
}
