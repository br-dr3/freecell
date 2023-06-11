package com.github.br_dr3.freecell.config;

import com.github.br_dr3.freecell.entities.Card;
import com.github.br_dr3.freecell.entities.enumeration.CardLabel;
import com.github.br_dr3.freecell.entities.enumeration.CardSuit;
import com.github.br_dr3.freecell.repositories.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Configuration
public class LoadDatabaseConfiguration {
    @Autowired
    ApplicationConfiguration applicationConfiguration;
    @Bean
    CommandLineRunner initDatabase(CardsRepository cardsRepository) {
        var cards = cardsRepository.findAll();

        if(cards.size() < applicationConfiguration.getNumberOfCards()) {
            return args -> insertDatabase(cardsRepository);
        }

        return args -> {};
    }
    private void insertDatabase(CardsRepository cardsRepository) {
        List<CardLabel> cardLabels = List.of(CardLabel.values());
        List<CardSuit> cardSuits = List.of(CardSuit.values());

        var cards = cardLabels.stream()
                .flatMap(cardLabel -> cardSuits.stream()
                        .flatMap(cardSuit -> Stream.of(Map.entry(cardSuit, cardLabel))))
                .map(c -> Card.builder()
                        .cardSuit(c.getKey())
                        .cardLabel(c.getValue())
                        .build())
                .toList();

        System.out.println("Cards -> " + cards);

        cardsRepository.saveAll(cards);
    }
}
