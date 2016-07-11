package com.github.inarabr.conditions;

import com.github.inarabr.validation.Validation;
import com.github.inarabr.validation.Violation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class MustFulfilInCollection<T> implements ValidationCondition<Collection<T>>, Validation {

  private final Validation validation;

  public MustFulfilInCollection(Validation validation) {
    this.validation = validation;
  }

  public static MustFulfilInCollection inCollection(Validation validation) {
    return new MustFulfilInCollection(validation);
  }

  @Override
  public boolean isValid(Collection<T> collectionWithObjectToValidate) {
    for (T objectToValidate : collectionWithObjectToValidate) {
      if (!validation.isValidateObject(objectToValidate)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Collection<?> validValues() {
    return Collections.emptyList();
  }

  @Override
  public boolean isValidateObject(Object objectToValidate) {
    if (objectToValidate instanceof Collection) {
      for (Object o : (Collection) objectToValidate) {
        if (!validation.isValidateObject(o)) {
          return false;
        }
      }
    }
    return false;
  }

  @Override
  public Set<Violation> validateObject(Object objectToValidate) {
    Set<Violation> violations = new HashSet<>();
    if (objectToValidate instanceof Collection) {
      Collection collection = (Collection) objectToValidate;
      int index = 0;
      for (Object item : collection) {
        violations.addAll(violationWithIndexInMethodPath(item, index));
        index++;
      }
    }
    return violations;
  }

  private Set<Violation> violationWithIndexInMethodPath(Object o, int index) {
    Set<Violation> violations = validation.validateObject(o);
    for (Violation violation : violations) {
      violation.methodPath(String.format("[%d].%s", index, violation.getMethodPath()));
    }
    return violations;
  }

}
