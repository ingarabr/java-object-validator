package com.github.inarabr.validation.conditions;

import com.github.inarabr.validation.FieldCondition;
import com.github.inarabr.validation.Violation;
import com.github.inarabr.validation.testobjects.AnotherBean;
import com.github.inarabr.validation.testobjects.BeanWithCollection;
import com.github.inarabr.validation.testobjects.BeanWithList;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.github.inarabr.conditions.HasValue.hasValue;
import static com.github.inarabr.conditions.MustFulfilInCollection.inCollection;
import static com.github.inarabr.validation.FieldCondition.field;
import static com.github.inarabr.validation.FieldCondition.validation;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MustFulfilInCollectionTest {

  @Test
  public void shouldValidateObjectInCollectionWithSuccess() {
    BeanWithCollection obj = new BeanWithCollection()
        .someString("some text in the bean")
        .addAnotherBean(new AnotherBean().value("another bean value"));

    FieldCondition<String> validationForAnotherBean = validation(field(AnotherBean.class).getValue(), hasValue("another bean value"));
    FieldCondition validation = validation(field(BeanWithCollection.class).getAnotherBeans(), inCollection(validationForAnotherBean));

    assertThat(validation.isValidateObject(obj), is(true));
  }

  @Test
  public void shouldValidateObjectInCollectionWithFailure() {
    BeanWithCollection obj = new BeanWithCollection()
        .someString("some text in the bean")
        .addAnotherBean(new AnotherBean().value("value that does not exist in the validation"));

    FieldCondition<String> validationForAnotherBean = validation(field(AnotherBean.class).getValue(), hasValue("another bean value"));
    FieldCondition validation = validation(field(BeanWithCollection.class).getAnotherBeans(), inCollection(validationForAnotherBean));

    assertThat(validation.isValidateObject(obj), is(false));
  }

  @Test
  public void shouldValidateObjectInListWithFailure() {
    BeanWithList obj = new BeanWithList()
        .someString("some text in the bean")
        .addAnotherBean(new AnotherBean().value("value that does not exist in the validation"));

    FieldCondition<String> validationForAnotherBean = validation(field(AnotherBean.class).getValue(), hasValue("another bean value"));
    FieldCondition validation = validation(field(BeanWithList.class).getAnotherBeans(), inCollection(validationForAnotherBean));

    assertThat(validation.isValidateObject(obj), is(false));
  }

  @Test
  public void shouldValidateObjectInListWithSuccess() {
    BeanWithList obj = new BeanWithList()
        .someString("some text in the bean")
        .addAnotherBean(new AnotherBean().value("another bean value"));

    FieldCondition<?> validationForAnotherBean = validation(field(AnotherBean.class).getValue(), hasValue("another bean value"));
    FieldCondition<?> validation = validation(field(BeanWithList.class).getAnotherBeans(), inCollection(validationForAnotherBean));

    assertThat(validation.isValidateObject(obj), is(true));
  }

  @Test
  public void shouldPassViolationsWithCollectionIndex() {
    BeanWithList obj = new BeanWithList()
        .someString("some text in the bean")
        .addAnotherBean(new AnotherBean().value("unknown value one"))
        .addAnotherBean(new AnotherBean().value("unknown value two"));

    FieldCondition<?> validationForAnotherBean = validation(field(AnotherBean.class).getValue(), hasValue("another bean value"));
    FieldCondition<?> validation = validation(field(BeanWithList.class).getAnotherBeans(), inCollection(validationForAnotherBean));
    Set<Violation> violations = validation.validateObject(obj);

    assertThat(violations.size(), is(2));

    List<String> methodPaths = FluentIterable.from(violations)
        .transform(new Function<Violation, String>() {
          @Override
          public String apply(Violation input) {
            return input.getMethodPath();
          }
        })
        .toImmutableList();

    assertThat(methodPaths, hasItems("anotherBeans[0].value", "anotherBeans[1].value"));
  }

}
