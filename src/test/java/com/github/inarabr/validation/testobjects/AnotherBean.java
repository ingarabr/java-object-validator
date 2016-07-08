package com.github.inarabr.validation.testobjects;

public class AnotherBean {

  private String value;
  private String aStringValue;

  public String getValue() {
    return value;
  }

  public String getAStringValue() {
    return aStringValue;
  }

  public AnotherBean value(String verdi) {
    this.value = verdi;
    return this;
  }

  public AnotherBean aStringValue(String aStringValue) {
    this.aStringValue = aStringValue;
    return this;
  }
}

