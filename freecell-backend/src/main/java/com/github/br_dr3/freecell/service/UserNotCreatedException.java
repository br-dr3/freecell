package com.github.br_dr3.freecell.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserNotCreatedException extends RuntimeException {
    String message;
}
