package com.github.inarabr.validation;

import com.github.inarabr.validation.testobjects.AnotherBean;
import com.github.inarabr.validation.testobjects.SomeBean;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static com.github.inarabr.conditions.Exists.mustNotHaveValue;
import static com.github.inarabr.conditions.HasValue.hasOneOfTheValues;
import static com.github.inarabr.conditions.MustFulfil.mustFulfil;
import static com.github.inarabr.validation.AndValidation.and;
import static com.github.inarabr.validation.FieldCondition.field;
import static com.github.inarabr.validation.FieldCondition.validation;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AndValidationCombinationTest {

  @Test
  public void shouldValidateWhitNestedValidations() {
    AndValidation innerRule = and(
        validation(field(AnotherBean.class).getValue(), hasOneOfTheValues("foo")),
        validation(field(AnotherBean.class).getAStringValue(), hasOneOfTheValues("bar")));

    Validation andSomeBean = and(
        validation(field(SomeBean.class).getValue(), mustNotHaveValue()),
        validation(field(SomeBean.class).getAnotherBean(), mustFulfil(innerRule)));

    SomeBean objectToValidate = new SomeBean().anotherBean(new AnotherBean().value("foo").aStringValue("bar"));
    assertThat(andSomeBean.isValidateObject(objectToValidate), is(true));
  }

  @Test
  public void shouldNotBeValidWhenNotAllValidationsEvauatesSuccessfullyInTheCombination() {
    Validation andAnotherBean = and(
        validation(field(AnotherBean.class).getValue(), hasOneOfTheValues("foo")),
        validation(field(AnotherBean.class).getAStringValue(), hasOneOfTheValues("bar")));

    Validation andSomeBean = and(
        validation(field(SomeBean.class).getValue(), mustNotHaveValue()),
        validation(field(SomeBean.class).getAnotherBean(), mustFulfil(andAnotherBean)));

    SomeBean invalid = new SomeBean().anotherBean(new AnotherBean().value("foo"));
    assertThat(andSomeBean.isValidateObject(invalid), is(false));
  }

  @Test
  public void shouldNotValidateAnUnknownClass() {
    Validation validation = and(validation(field(SomeBean.class).getValue(), hasOneOfTheValues("bar")));
    assertThat(validation.isValidateObject(new AnotherBean().value("bar")), is(false));
  }

  @Test
  public void shouldValidateAgainstMultipleFieldConditions() {
    Validation validation = and(
        validation(field(SomeBean.class).getAnotherBean().getValue(), hasOneOfTheValues("foo")),
        validation(field(SomeBean.class).getValue(), hasOneOfTheValues("bar")));

    assertTrue(validation.isValidateObject(new SomeBean().value("bar").anotherBean(new AnotherBean().value("foo"))));
    assertFalse(validation.isValidateObject(new SomeBean().value("bar").anotherBean(new AnotherBean().value("fail"))));
    assertFalse(validation.isValidateObject(new SomeBean().anotherBean(new AnotherBean().value("foo"))));
    assertFalse(validation.isValidateObject(new SomeBean().value("fail")));
  }

  @Test
  public void validationShouldBeThreadSafe() throws Exception {
    CyclicBarrier startBarrier = new CyclicBarrier(3);
    CyclicBarrier stopBarrier = new CyclicBarrier(4);
    ValidationRunnable t1 = new ValidationRunnable(startBarrier, stopBarrier, "1-1", "1-2");
    ValidationRunnable t2 = new ValidationRunnable(startBarrier, stopBarrier, "2-1", "2-2");
    ValidationRunnable t3 = new ValidationRunnable(startBarrier, stopBarrier, "3-1", "3-2");

    new Thread(t1).start();
    new Thread(t2).start();
    new Thread(t3).start();
    stopBarrier.await();

    assertValidationAnotherBean(t1, "1-1", "1-2");
    assertValidationAnotherBean(t2, "2-1", "2-2");
    assertValidationAnotherBean(t3, "3-1", "3-2");
  }

  private void assertValidationAnotherBean(ValidationRunnable t1, String v1, String v2) {
    assertThat(t1.getValidation().isValidateObject(new SomeBean()
        .value(v2)
        .anotherBean(
            new AnotherBean()
                .value(v1))
    ), is(true));
  }

  private class ValidationRunnable implements Runnable {
    private CyclicBarrier start;
    private CyclicBarrier stop;
    private final String v1;
    private final String v2;
    private Validation validation;

    ValidationRunnable(CyclicBarrier start, CyclicBarrier stop, String v1, String v2) {
      this.start = start;
      this.stop = stop;

      this.v1 = v1;
      this.v2 = v2;
    }

    @Override
    public void run() {
      try {
        start.await();
        validation = and(
            validation(field(SomeBean.class).getAnotherBean().getValue(), hasOneOfTheValues(v1)),
            validation(field(SomeBean.class).getValue(), hasOneOfTheValues(v2)));
        stop.await();
      } catch (InterruptedException | BrokenBarrierException e) {
        e.printStackTrace();
      }
    }

    public Validation getValidation() {
      return validation;
    }
  }

}
