package com.github.inarabr.conditions;

import java.util.ArrayList;
import java.util.Collection;

public class IntegerRangeCondition implements ValidationCondition<Integer> {

  private final int min;
  private final int max;

  public IntegerRangeCondition(int min, int max) {
    this.min = min;
    this.max = max;
  }

  public static IntegerRangeCondition inRangeOf(int min, int max) {
    return new IntegerRangeCondition(min, max);
  }

  public static IntegerRangeCondition hasMinValue(int min) {
    return new IntegerRangeCondition(min, Integer.MAX_VALUE);
  }

  public static IntegerRangeCondition hasMaxValue(int max) {
    return new IntegerRangeCondition(Integer.MIN_VALUE, max);
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
