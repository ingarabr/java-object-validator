package com.github.inarabr.validation;

import java.util.Set;

public interface Validation {

  boolean isValidateObject(Object objectToValidate);

  Set<Violation> validateObject(Object objectToValidate);

}
