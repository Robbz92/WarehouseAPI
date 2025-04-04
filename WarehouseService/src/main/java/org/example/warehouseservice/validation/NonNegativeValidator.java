package org.example.warehouseservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NonNegativeValidator implements ConstraintValidator<NonNegative, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value >= 0;
    }
}
