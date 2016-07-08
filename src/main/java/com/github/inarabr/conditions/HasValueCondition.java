package com.github.inarabr.conditions;

import com.github.inarabr.validation.utils.Joiner;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class HasValueCondition<T> implements ValidationCondition<T> {

  private Set<T> validValues;

  private HasValueCondition(Set<T> validValues) {
    this.validValues = validValues;
  }

  @SafeVarargs
  public static <T> HasValueCondition<T> hasOneOfTheValues(T valueOne, T... otherValues) {
    Set<T> values = new LinkedHashSet<>();
    values.add(valueOne);
    Collections.addAll(values, otherValues);
    return new HasValueCondition<>(values);
  }

  public static <T> HasValueCondition<T> hasValue(T value) {
    return hasOneOfTheValues(value);
  }

  @Override
  public boolean isValid(T objectToValidate) {
    return validValues.contains(objectToValidate);
  }

  @Override
  public Collection<?> validValues() {
    return validValues;
  }

  @Override
  public String toString() {
    if (validValues.size() == 1) {
      return String.valueOf(validValues.iterator().next());
    }
    return this.getClass().getSimpleName() + " [" + Joiner.join(", ", validValues, new Joiner.ToString<T>() {
      @Override
      public String apply(T input) {
        return input.toString();
      }
    }) + "]";
  }
}
