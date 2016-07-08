package com.github.inarabr.conditions;

import com.github.inarabr.validation.Validation;
import com.github.inarabr.validation.Violation;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class MustFulfilCondition<T> implements ValidationCondition<T>, Validation {

  private Validation validation;

  private MustFulfilCondition(Validation validation) {
    this.validation = validation;
  }

  public static MustFulfilCondition mustFulfil(Validation validation) {
    return new MustFulfilCondition<>(validation);
  }

  @Override
  public boolean isValid(T objectToValidate) {
    return validation.isValidateObject(objectToValidate);
  }

  @Override
  public Collection<?> validValues() {
    return Collections.emptyList();
  }

  @Override
  public boolean isValidateObject(Object objectToValidate) {
    return validation.isValidateObject(objectToValidate);
  }

  @Override
  public Set<Violation> validateObject(Object objectToValidate) {
    return validation.validateObject(objectToValidate);
  }

}
