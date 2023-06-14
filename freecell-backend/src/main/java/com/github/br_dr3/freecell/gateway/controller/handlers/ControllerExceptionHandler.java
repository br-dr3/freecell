package com.github.br_dr3.freecell.gateway.controller.handlers;

import com.github.br_dr3.freecell.exceptions.GameNotFoundException;
import com.github.br_dr3.freecell.exceptions.UserNotFoundException;
import com.github.br_dr3.freecell.gateway.controller.handlers.dto.ErrorDTO;
import com.github.br_dr3.freecell.service.UserNotCreatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

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
