package com.github.inarabr.validation;

public class Violation {

  private String methodPath;
  private Condition condition;
  private Object value;

  public Violation verdi(Object verdi) {
    this.value = verdi;
    return this;
  }

  public Violation condition(Condition condition) {
    this.condition = condition;
    return this;
  }

  public Violation methodPath(String methodPath) {
    this.methodPath = methodPath;
    return this;
  }

  public String getMethodPath() {
    return methodPath;
  }

  public Condition getCondition() {
    return condition;
  }

  public Object getValue() {
    return value;
  }

}
