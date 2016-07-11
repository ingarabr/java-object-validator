package com.github.inarabr.validation;

import com.github.inarabr.validation.testobjects.SomeBean;
import com.google.common.base.Function;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.inarabr.conditions.Exists.mustHaveValue;
import static com.github.inarabr.conditions.Exists.mustNotHaveValue;
import static com.github.inarabr.conditions.HasValue.hasOneOfTheValues;
import static com.github.inarabr.validation.FieldCondition.field;
import static com.github.inarabr.validation.FieldCondition.validation;
import static com.google.common.collect.Iterables.transform;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class FieldConditionTest {

  @Test
  public void shouldValidateAFieldConditionWhenOneValueIsValidInput() {
    FieldCondition fieldCondition = validation(field(SomeBean.class).getValue(), hasOneOfTheValues("foo"));
    assertThat(fieldCondition.isValidateObject(new SomeBean().value("foo")), is(true));
  }

  @Test
  public void shouldValidateAFieldConditionWhenMultipleValuesAreValidInput() {
    FieldCondition fieldCondition = validation(field(SomeBean.class).getValue(), hasOneOfTheValues("foo1", "foo2"));
    assertThat(fieldCondition.isValidateObject(new SomeBean().value("foo1")), is(true));
    assertThat(fieldCondition.isValidateObject(new SomeBean().value("foo2")), is(true));
  }

  @Test
  public void shouldNotValidateAFieldConditionWhenTheInputIsntAValidValue() {
    FieldCondition fieldCondition = validation(field(SomeBean.class).getValue(), hasOneOfTheValues("foo"));
    assertThat(fieldCondition.isValidateObject(new SomeBean().value("bar")), is(false));
  }

  @Test
  public void shouldValidateAFieldConditionThatExpectsToBeEmpty() {
    FieldCondition fieldCondition = validation(field(SomeBean.class).getValue(), mustNotHaveValue());
    assertThat(fieldCondition.isValidateObject(new SomeBean()), is(true));
  }

  @Test
  public void shouldNotValidateAFieldConditionThatNotEmpty() {
    FieldCondition fieldCondition = validation(field(SomeBean.class).getValue(), mustNotHaveValue());
    assertThat(fieldCondition.isValidateObject(new SomeBean().value("foo")), is(false));
  }

  @Test
  public void shouldValidateAFieldConditionThatNotEmpty() {
    FieldCondition fieldCondition = validation(field(SomeBean.class).getValue(), mustHaveValue());
    assertThat(fieldCondition.isValidateObject(new SomeBean().value("foo")), is(true));
  }

  @Test
  public void shouldNotValidateAFieldConditionThatExpectsAFieldToHAveAValue() {
    FieldCondition fieldCondition = validation(field(SomeBean.class).getValue(), mustHaveValue());
    assertThat(fieldCondition.isValidateObject(new SomeBean()), is(false));
  }

  @Test
  public void shouldReturnFalseWhenAFiledInTheHierarchicallyDoesNotHaveAValue() {
    FieldCondition<String> fieldCondition = validation(field(SomeBean.class).getAnotherBean().getValue(), hasOneOfTheValues("foo"));
    assertThat(fieldCondition.isValidateObject(new SomeBean()), is(false));
  }

  @Test
  public void shouldEvaluateAStackPathOfTheMethodsThatIsCalled() {
    FieldCondition<String> fieldCondition = validation(field(SomeBean.class).getAnotherBean().getValue(), null);

    assertThat(fieldCondition.getStackPath(), is("anotherBean.value"));
  }

  @Test
  public void shouldCastIllegalStateExeptionWhenFieldIsNotUsedBeforeWeAreStartingCreatingANewOnw() {
    field(SomeBean.class).getValue();
    try {
      field(SomeBean.class).getValue();
    } catch (IllegalStateException e) {
      return;
    }
    Assert.fail("expected an exception to have occurred");
  }

  @Test
  public void shouldReturnNullWhenValidationIsCalledWithoutAnyFieldIsCalled() {
    assertThat(validation("", mustNotHaveValue()), is(nullValue()));
  }

  @Test
  public void shouldOnlyEvaluateGetterMethods() throws Exception {
    FieldCondition<?> fieldCondition = validation(field(SomeBean.class).toString(), mustHaveValue());
    Iterable<String> methodName = transform(fieldCondition.getStack(), new Function<Method, String>() {
      @Override
      public String apply(Method input) {
        return input.getName();
      }
    });
    assertThat(methodName, not(hasItem("toString")));
  }

}
