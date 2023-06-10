package com.github.br_dr3.freecell.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/freecell")
public class FreeCellController {
    @GetMapping("/")
    public ResponseEntity<String> getCards() {
        return ResponseEntity.ok().body("Hello World!");
    }
}
