package com.github.br_dr3.freecell.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AllCellsBusyException extends RuntimeException {
    String message;
}
