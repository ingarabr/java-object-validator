package com.github.inarabr.validation;

import com.github.inarabr.conditions.Exists;
import com.github.inarabr.validation.testobjects.AnotherBean;
import com.github.inarabr.validation.testobjects.SomeBean;
import org.junit.Test;

import java.util.Set;

import static com.github.inarabr.validation.FieldCondition.field;
import static com.github.inarabr.validation.FieldCondition.validation;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AndValidationTest {

  @Test
  public void shouldBeValidWhenAllValidationsEvaluatesSuccessfully() throws Exception {
    AndValidation and = AndValidation.and(
        validation(field(SomeBean.class).getValue(), Exists.mustHaveValue()),
        validation(field(SomeBean.class).getAnotherBean(), Exists.mustHaveValue())
    );
    Set<Violation> violations = and.validateObject(new SomeBean().value("mustHaveValue-field").anotherBean(new AnotherBean()));
    assertThat(violations.size(), is(0));
  }

  @Test
  public void shouldNotBeValidWhenOneValidationEvaluationToFalse() throws Exception {
    AndValidation and = AndValidation.and(
        validation(field(SomeBean.class).getValue(), Exists.mustHaveValue()),
        validation(field(SomeBean.class).getAnotherBean(), Exists.mustHaveValue())
    );
    Set<Violation> violations = and.validateObject(new SomeBean().anotherBean(new AnotherBean()));
    assertThat(violations.size(), is(1));
  }

  @Test
  public void shouldNotBeValidWhenAllValidationEvaluationToFalse() throws Exception {
    AndValidation and = AndValidation.and(
        validation(field(SomeBean.class).getValue(), Exists.mustHaveValue()),
        validation(field(SomeBean.class).getAnotherBean(), Exists.mustHaveValue())
    );
    Set<Violation> violations = and.validateObject(new SomeBean());
    assertThat(violations.size(), is(2));
  }

}
