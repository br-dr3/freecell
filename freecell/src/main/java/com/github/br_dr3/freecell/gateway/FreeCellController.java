package com.github.br_dr3.freecell.gateway;

import com.github.br_dr3.freecell.gateway.dto.CardsDTO;
import com.github.br_dr3.freecell.service.CardsService;
import com.github.br_dr3.freecell.util.DataWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/freecell")
public class FreeCellController {
    @Autowired
    private CardsService cardsService;
    @GetMapping("/cards")
    public ResponseEntity<DataWrapper<CardsDTO>> getCards() {
        var cards = cardsService.getCards();
        return ResponseEntity.ok().body(DataWrapper.<CardsDTO> builder().data(cards).build());
    }
}
