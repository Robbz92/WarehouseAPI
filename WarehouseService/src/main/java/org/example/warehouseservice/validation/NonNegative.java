package org.example.warehouseservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NonNegativeValidator.class)
public @interface NonNegative {
    String message() default "ID must not be negative";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
