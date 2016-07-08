package com.github.inarabr.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;

public class OrValidation implements Validation, SubValidation {

  private final Set<Validation> validations;

  private OrValidation(Set<Validation> validations) {
    this.validations = validations;
  }

  public static OrValidation or(Validation... validations) {
    Set<Validation> set = new HashSet<>();
    Collections.addAll(set, validations);
    return new OrValidation(set);
  }

  @Override
  public Collection<Validation> getSubValidation() {
    return Collections.unmodifiableCollection(validations);
  }

  @Override
  public boolean isValidateObject(Object motObjekt) {
    for (Validation validation : validations) {
      if (validation.isValidateObject(motObjekt)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Set<Violation> validateObject(Object motObjekt) {
    Set<Violation> violations = new HashSet<>();
    for (Validation validation : validations) {
      violations.addAll(validation.validateObject(motObjekt));
    }
    if (validations.size() == violations.size()) {
      return violations;
    }
    return emptySet();
  }

}
