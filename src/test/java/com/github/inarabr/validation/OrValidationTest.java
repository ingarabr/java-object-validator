package com.github.inarabr.validation;

import com.github.inarabr.validation.testobjects.SomeBean;
import org.junit.Test;

import java.util.Set;

import static com.github.inarabr.conditions.Exists.mustHaveValue;
import static com.github.inarabr.validation.FieldCondition.field;
import static com.github.inarabr.validation.FieldCondition.validation;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrValidationTest {

  @Test
  public void shouldValidateOnFirstValidationCondition() {
    OrValidation or = OrValidation.or(
        validation(field(SomeBean.class).getValue(), mustHaveValue()),
        validation(field(SomeBean.class).getAnotherBean(), mustHaveValue())
    );
    Set<Violation> violations = or.validateObject(new SomeBean().value("must-have-value-field"));
    assertThat(violations.size(), is(0));
  }

  @Test
  public void shouldValidateOnSecond() {
    OrValidation or = OrValidation.or(
        validation(field(SomeBean.class).getAnotherBean(), mustHaveValue()),
        validation(field(SomeBean.class).getValue(), mustHaveValue())
    );
    Set<Violation> violations = or.validateObject(new SomeBean().value("must-have-value-field"));
    assertThat(violations.size(), is(0));
  }

  @Test
  public void shouldViolateAllValidations() {
    OrValidation or = OrValidation.or(
        validation(field(SomeBean.class).getAnotherBean(), mustHaveValue()),
        validation(field(SomeBean.class).getValue(), mustHaveValue())
    );
    Set<Violation> violations = or.validateObject(new SomeBean());
    assertThat(violations.size(), is(2));
  }

}
