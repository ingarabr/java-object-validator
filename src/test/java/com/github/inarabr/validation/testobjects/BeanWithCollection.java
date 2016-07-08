package com.github.inarabr.validation.testobjects;

import java.util.ArrayList;
import java.util.Collection;

public class BeanWithCollection {

  private String someString;
  private Collection<AnotherBean> anotherBeans = new ArrayList<>();

  public String getSomeString() {
    return someString;
  }

  public Collection<AnotherBean> getAnotherBeans() {
    return anotherBeans;
  }

  public BeanWithCollection someString(String someString) {
    this.someString = someString;
    return this;
  }

  public BeanWithCollection addAnotherBean(AnotherBean anotherBean) {
    anotherBeans.add(anotherBean);
    return this;
  }

}
