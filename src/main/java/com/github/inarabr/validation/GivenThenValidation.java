package com.github.inarabr.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.github.inarabr.validation.AndValidation.and;
import static java.util.Collections.emptySet;

public class GivenThenValidation implements Validation {

  private final Validation givenValidation;
  private final Validation thenValidation;

  private GivenThenValidation(Validation given, Validation then) {
    this.givenValidation = given;
    this.thenValidation = then;
  }

  public static Given given(Validation validation) {
    return new Given(validation);
  }

  public static Given given(FieldCondition fieldCondition, FieldCondition... fieldConditions) {
    return new Given(createGivenThenValidation(fieldCondition, fieldConditions));
  }

  private static Validation createGivenThenValidation(FieldCondition fieldCondition, FieldCondition[] fieldConditions) {
    List<Validation> validations = new ArrayList<>();
    validations.add(fieldCondition);
    Collections.addAll(validations, fieldConditions);
    return and(validations);
  }

  @Override
  public boolean isValidateObject(Object objectToValidate) {
    return validateObject(objectToValidate).isEmpty();
  }

  @Override
  public Set<Violation> validateObject(Object objectToValidate) {
    if (givenValidation.validateObject(objectToValidate).isEmpty()) {
      return thenValidation.validateObject(objectToValidate);
    }
    return emptySet();
  }

  public Validation getGivenValidation() {
    return givenValidation;
  }

  public Validation getThenValidation() {
    return thenValidation;
  }

  public static class Given {

    private final Validation givenValidation;

    private Given(Validation givenValidation) {
      this.givenValidation = givenValidation;
    }

    public GivenThenValidation then(Validation mustValidate) {
      return new GivenThenValidation(givenValidation, mustValidate);
    }

    public GivenThenValidation then(FieldCondition fieldCondition, FieldCondition... fieldConditions) {
      return new GivenThenValidation(givenValidation, createGivenThenValidation(fieldCondition, fieldConditions));
    }
  }
}
