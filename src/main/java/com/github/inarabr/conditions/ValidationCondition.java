package com.github.inarabr.conditions;

import com.github.inarabr.validation.Condition;

import java.util.Collection;

public interface ValidationCondition<T> extends Condition<T> {

  boolean isValid(T objectToValidate);

  Collection<?> validValues();
}
