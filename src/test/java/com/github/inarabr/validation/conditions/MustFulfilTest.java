package com.github.inarabr.validation.conditions;

import com.github.inarabr.validation.Validation;
import com.github.inarabr.validation.Violation;
import com.github.inarabr.validation.testobjects.AnotherBean;
import com.github.inarabr.validation.testobjects.SomeBean;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.github.inarabr.conditions.Exists.mustNotHaveValue;
import static com.github.inarabr.conditions.HasValue.hasOneOfTheValues;
import static com.github.inarabr.conditions.MustFulfil.mustFulfil;
import static com.github.inarabr.validation.AndValidation.and;
import static com.github.inarabr.validation.FieldCondition.field;
import static com.github.inarabr.validation.FieldCondition.validation;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class MustFulfilTest {

  @Test
  public void shouldShowViolationsWithAbsoluteMethodPath() {
    Validation andAnotherBean = and(
        validation(field(AnotherBean.class).getValue(), hasOneOfTheValues("foo")),
        validation(field(AnotherBean.class).getAStringValue(), hasOneOfTheValues("bar")));

    Validation andSomeBean = and(
        validation(field(SomeBean.class).getValue(), mustNotHaveValue()),
        validation(field(SomeBean.class).getAnotherBean(), mustFulfil(andAnotherBean)));

    SomeBean someBean = new SomeBean().anotherBean(new AnotherBean().value("foo"));

    Set<Violation> violations = andSomeBean.validateObject(someBean);

    List<String> methodPaths = FluentIterable.from(violations)
        .transform(new Function<Violation, String>() {
          @Override
          public String apply(Violation input) {
            return input.getMethodPath();
          }
        })
        .toList();

    assertThat(methodPaths, hasItems("anotherBean.aStringValue"));
  }

}