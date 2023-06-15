package com.github.br_dr3.freecell.gateway.dto.validation;

import com.github.br_dr3.freecell.config.ApplicationConfiguration;
import com.github.br_dr3.freecell.gateway.dto.MoveCardsRequestDTO;
import com.github.br_dr3.freecell.gateway.dto.validation.ann.PositionConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PositionValidator implements ConstraintValidator<PositionConstraint, MoveCardsRequestDTO> {
    @Autowired ApplicationConfiguration applicationConfiguration;

    @Override
    public boolean isValid(MoveCardsRequestDTO moveCardsRequestDTO, ConstraintValidatorContext constraintValidatorContext) {
        var isOnlyOneCard = moveCardsRequestDTO.getCardsToMove()
                .getCards()
                .size() == 1;

        var isFoundation = moveCardsRequestDTO.isToFoundation() && isOnlyOneCard;
        var isCell = moveCardsRequestDTO.isToCell() && isOnlyOneCard;

        var column = moveCardsRequestDTO.getColumn();
        var isColumn = column != null
                && column >= 0
                && column < applicationConfiguration.getNumberOfColumns();

        return isFoundation ^ isCell ^ isColumn;
    }
}
