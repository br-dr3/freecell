package com.github.br_dr3.freecell.gateway.dto.validation.ann;

import com.github.br_dr3.freecell.gateway.dto.validation.PositionValidator;
import com.github.br_dr3.freecell.gateway.dto.validation.SequentialCardsValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SequentialCardsValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SequentialCardsConstraint {
}
