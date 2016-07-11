package com.github.inarabr.validation;

import com.github.inarabr.validation.testobjects.AnotherBean;
import com.github.inarabr.validation.testobjects.SomeBean;
import org.junit.Before;
import org.junit.Test;

import static com.github.inarabr.conditions.HasValue.hasValue;
import static com.github.inarabr.validation.FieldCondition.field;
import static com.github.inarabr.validation.FieldCondition.validation;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GivenThenValidationTest {

  private static final String SOME_BEAN_VALUE = "someBean-value";
  private static final String ANOTHER_BEAN_VALUE = "anotherBean-value";
  private static final String DUMMY_VALUE = "dummyValue";
  private Validation validation;

  @Before
  public void setupBasicValidationRule() {
    validation = GivenThenValidation
        .given(
            validation(field(SomeBean.class).getValue(), hasValue(SOME_BEAN_VALUE))
        )
        .then(
            validation(field(SomeBean.class).getAnotherBean().getValue(), hasValue(ANOTHER_BEAN_VALUE))
        );
  }

  @Test
  public void shouldViolateGivenAndNotEvaluateThan() {
    SomeBean objectToValidate = new SomeBean().value(DUMMY_VALUE).anotherBean(new AnotherBean().value(DUMMY_VALUE));
    assertThat(validation.isValidateObject(objectToValidate), is(true));
  }

  @Test
  public void shouldNotViolateGivenAndThenBlocksValidations() {
    SomeBean objectToValidate = new SomeBean().value(SOME_BEAN_VALUE).anotherBean(new AnotherBean().value(ANOTHER_BEAN_VALUE));

    assertThat(validation.isValidateObject(objectToValidate), is(true));
  }

  @Test
  public void shouldViolateTheViolationInTheThanBlock() {
    SomeBean objectToValidate = new SomeBean().value(SOME_BEAN_VALUE).anotherBean(new AnotherBean().value(DUMMY_VALUE));
    assertThat(validation.isValidateObject(objectToValidate), is(false));
  }

}
