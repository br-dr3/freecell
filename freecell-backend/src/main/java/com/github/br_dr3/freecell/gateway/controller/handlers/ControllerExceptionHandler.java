package com.github.br_dr3.freecell.gateway.controller.handlers;

import com.github.br_dr3.freecell.exceptions.AlredyInCellException;
import com.github.br_dr3.freecell.exceptions.CardsNotMovableException;
import com.github.br_dr3.freecell.exceptions.GameNotFoundException;
import com.github.br_dr3.freecell.exceptions.TooManyCardsException;
import com.github.br_dr3.freecell.exceptions.UserNotCreatedException;
import com.github.br_dr3.freecell.exceptions.UserNotFoundException;
import com.github.br_dr3.freecell.gateway.controller.handlers.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = AlredyInCellException.class)
    private ResponseEntity<ErrorDTO> handleAlreadyInCell(AlredyInCellException ex) {
        return new ResponseEntity<>(
                ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("CARD_ALREADY_IN_CELL")
                        .detail("This card is already in cell")
                        .build(),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }
    @ExceptionHandler(value = CardsNotMovableException.class)
    private ResponseEntity<ErrorDTO> handleCardsNotMovable(CardsNotMovableException ex) {
        return new ResponseEntity<>(
                ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("CARDS_NOT_MOVABLE")
                        .detail("This/these cards cannot be moved to the place you asked")
                        .build(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = TooManyCardsException.class)
    private ResponseEntity<ErrorDTO> handleTooManyCards(TooManyCardsException ex) {
        return new ResponseEntity<>(
                ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("TOO_MANY_CARDS_TO_MOVE")
                        .detail("You tried to move more cards than you should")
                        .build(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(value = UserNotCreatedException.class)
    private ResponseEntity<ErrorDTO> handleUserNotCreated(UserNotCreatedException ex) {
        return new ResponseEntity<>(
                ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("USER_ALREADY_CREATED")
                        .detail("This name already have a user associated")
                        .build(),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }
    @ExceptionHandler(value = UserNotFoundException.class)
    private ResponseEntity<ErrorDTO> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(
                ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("USER_NOT_FOUND")
                        .detail("Ensure key to user is correct.")
                        .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = GameNotFoundException.class)
    private ResponseEntity<ErrorDTO> handleGameNotFound(GameNotFoundException ex) {
        return new ResponseEntity<>(
                ErrorDTO.builder()
                        .message(ex.getMessage())
                        .error("GAME_NOT_FOUND")
                        .detail("Ensure key to game is correct.")
                        .build(),
                HttpStatus.NOT_FOUND);
    }
}
