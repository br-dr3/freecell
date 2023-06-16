package com.github.br_dr3.freecell.gateway.dto.validation.ann;

import com.github.br_dr3.freecell.gateway.dto.validation.PositionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PositionValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PositionConstraint {
    String message() default "Invalid position chosen to card";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
