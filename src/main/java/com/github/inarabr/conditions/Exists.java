package com.github.inarabr.conditions;

import java.util.Collection;
import java.util.Collections;

public class Exists<T> implements ValidationCondition<T> {

  private boolean mustHaveValue;

  private static final Exists<?> HAS_VALUE_INSTANCE = new Exists<>(true);
  private static final Exists<?> HAS_NOT_VALUE_INSTANCE = new Exists<>(false);

  private Exists(boolean mustHaveValue) {
    this.mustHaveValue = mustHaveValue;
  }

  public static Exists mustHaveValue() {
    return HAS_VALUE_INSTANCE;
  }

  public static Exists mustNotHaveValue() {
    return HAS_NOT_VALUE_INSTANCE;
  }

  @Override
  public boolean isValid(T objectToValidate) {
    if (objectToValidate instanceof Collection) {
      boolean isEmptyCollection = ((Collection) objectToValidate).isEmpty();
      return mustHaveValue != isEmptyCollection;
    }
    return mustHaveValue ? objectToValidate != null : objectToValidate == null;
  }

  @Override
  public Collection<?> validValues() {
    return Collections.singleton(mustHaveValue ? "<NOT_EMPTY>" : "<EMPTY>");
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

}
