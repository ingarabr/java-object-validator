package com.github.inarabr.validation.testobjects;

public class SomeBean {

  private String value;
  private AnotherBean anotherBean;

  public String getValue() {
    return value;
  }

  public AnotherBean getAnotherBean() {
    return anotherBean;
  }

  public SomeBean anotherBean(AnotherBean minAndreKlasse) {
    this.anotherBean = minAndreKlasse;
    return this;
  }

  public SomeBean value(String verdi) {
    this.value = verdi;
    return this;
  }
}
