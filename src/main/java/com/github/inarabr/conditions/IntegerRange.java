package com.github.inarabr.conditions;

import java.util.ArrayList;
import java.util.Collection;

public class IntegerRange implements ValidationCondition<Integer> {

  private final int min;
  private final int max;

  public IntegerRange(int min, int max) {
    this.min = min;
    this.max = max;
  }

  public static IntegerRange inRangeOf(int min, int max) {
    return new IntegerRange(min, max);
  }

  public static IntegerRange hasMinValue(int min) {
    return new IntegerRange(min, Integer.MAX_VALUE);
  }

  public static IntegerRange hasMaxValue(int max) {
    return new IntegerRange(Integer.MIN_VALUE, max);
  }

  @Override
  public boolean isValid(Integer objectToValidate) {
    return objectToValidate != null &&
        objectToValidate >= min &&
        objectToValidate <= max;
  }

  @Override
  public Collection<?> validValues() {
    ArrayList<String> values = new ArrayList<>();
    values.add("min: " + min);
    values.add("max:" + max);
    return values;
  }

}
