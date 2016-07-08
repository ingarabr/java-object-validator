package com.github.inarabr.validation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AndValidation implements Validation, SubValidation {

  private final Set<Validation> validations;

  private AndValidation(Set<Validation> validations) {
    this.validations = validations;
  }

  public static AndValidation and(Validation... validations) {
    return and(Arrays.asList(validations));
  }

  public static AndValidation and(Collection<Validation> validations) {
    return new AndValidation(new HashSet<>(validations));
  }

  @Override
  public Collection<Validation> getSubValidation() {
    return Collections.unmodifiableCollection(validations);
  }

  @Override
  public boolean isValidateObject(Object objectToValidate) {
    for (Validation validation : validations) {
      if (!validation.isValidateObject(objectToValidate)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Set<Violation> validateObject(Object objectToValidate) {
    Set<Violation> violations = new HashSet<>();
    for (Validation validation : validations) {
      violations.addAll(validation.validateObject(objectToValidate));
    }
    return violations;
  }

}
