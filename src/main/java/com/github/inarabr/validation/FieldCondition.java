package com.github.inarabr.validation;

import com.github.inarabr.conditions.ValidationCondition;
import com.github.inarabr.validation.utils.Joiner;
import com.github.inarabr.validation.utils.ReflectionHelper;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Collections.unmodifiableList;

public class FieldCondition<K extends Class> implements Validation {

  private final K entryClass;
  private final List<Method> stack;
  private final MethodInterceptor interceptor;
  private Condition condition;
  private static final ThreadLocal<FieldCondition> PROGRESS = new ThreadLocal<>();

  private FieldCondition(K entryClass) {
    if (PROGRESS.get() != null) {
      PROGRESS.remove();
      throw new IllegalStateException("There is already a unfinished validation that isn't used yet!");
    }
    this.entryClass = entryClass;
    this.stack = new ArrayList<>();
    this.interceptor = new StackMethodInterceptor(stack);
    PROGRESS.set(this);
  }

  @SuppressWarnings("unchecked")
  public static <T> T field(Class<T> klasse) {
    FieldCondition<Class<T>> felt = new FieldCondition<>(klasse);
    return (T) Enhancer.create(klasse, felt.interceptor);
  }

  @SuppressWarnings({"unchecked", "unused"})
  public static <T, K extends Class> FieldCondition<K> validation(T field, Condition<T> condition) {
    FieldCondition<K> fieldCondition = PROGRESS.get();
    if (fieldCondition != null) {
      PROGRESS.remove();
      fieldCondition.condition = condition;
    }
    return fieldCondition;
  }

  public Condition getCondition() {
    return condition;
  }

  public K getEntryClass() {
    return entryClass;
  }

  public List<Method> getStack() {
    return unmodifiableList(stack);
  }

  public String getStackPath() {
    return Joiner.join(".", stack, new Joiner.ToString<Method>() {
      @Override
      public String apply(Method input) {
        return ReflectionHelper.stripGetterInMethodName(input.getName());
      }
    });
  }

  @Override
  public boolean isValidateObject(Object objectToValidate) {
    return validateObject(objectToValidate).isEmpty();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Set<Violation> validateObject(Object objectToValidate) {
    Object current = objectToValidate;
    if (!this.getEntryClass().isInstance(objectToValidate)) {
      return createViolationResult(current);
    }
    for (Method method : stack) {
      if (current == null) {
        return createViolationResult(null);
      }
      try {
        current = method.invoke(current);
      } catch (InvocationTargetException | IllegalAccessException e) {
        throw new IllegalStateException(e);
      }
    }
    if (condition instanceof Validation) {
      Set<Violation> violations = ((Validation) condition).validateObject(current);
      if (violations.isEmpty()) {
        return Collections.emptySet();
      }
      return createViolationResultForNestedValidation(violations);
    }

    if (((ValidationCondition) condition).isValid(current)) {
      return Collections.emptySet();
    }
    return createViolationResult(current);
  }

  private Set<Violation> createViolationResultForNestedValidation(Set<Violation> violations) {
    for (Violation violation : violations) {
      if (violation.getMethodPath().startsWith("[")) {
        violation.methodPath(String.format("%s%s", getStackPath(), violation.getMethodPath()));
      } else {
        violation.methodPath(String.format("%s.%s", getStackPath(), violation.getMethodPath()));
      }
    }
    return violations;
  }

  private Set<Violation> createViolationResult(Object current) {
    return Collections.singleton(new Violation()
        .condition(condition)
        .methodPath(getStackPath())
        .verdi(current)
    );
  }

  private static class StackMethodInterceptor implements MethodInterceptor {

    private final List<Method> stack;

    private StackMethodInterceptor(List<Method> stack) {
      this.stack = stack;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
      if (isGetter(method)) {
        stack.add(method);
      }
      Class<?> returnType = method.getReturnType();
      if (isNotEnhanceable(returnType)) {
        return null;
      }
      return Enhancer.create(returnType, this);
    }

    private boolean isNotEnhanceable(Class<?> type) {
      return type.isPrimitive()
          || Modifier.isFinal(type.getModifiers())
          || hasNotDefaultConstructor(type);
    }

    private boolean hasNotDefaultConstructor(Class<?> type) {
      for (Constructor<?> constructor : type.getConstructors()) {
        if (constructor.getParameterTypes().length == 0) {
          return false;
        }
      }
      return true;
    }

    private boolean isGetter(Method method) {
      return method.getName().startsWith("get") || method.getName().startsWith("is");
    }

  }

}
