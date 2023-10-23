package com.douzone.prosync.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HtmlTagExcludedValidator implements ConstraintValidator<HtmlTagExcluded, String> {

    private int maxLength;

    @Override
    public void initialize(HtmlTagExcluded constraintAnnotation) {
        maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        String sanitizedValue = value.replaceAll("<[^>]*>", "");

        return sanitizedValue.length() > 0 && sanitizedValue.length() <= maxLength;
    }
}
