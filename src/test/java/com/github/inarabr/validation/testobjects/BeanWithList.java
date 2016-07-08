package com.github.inarabr.validation.testobjects;

import java.util.ArrayList;
import java.util.List;

public class BeanWithList {

  private String someString;
  private List<AnotherBean> anotherBeans = new ArrayList<>();

  public String getSomeString() {
    return someString;
  }

  public List<AnotherBean> getAnotherBeans() {
    return anotherBeans;
  }

  public BeanWithList someString(String someString) {
    this.someString = someString;
    return this;
  }

  public BeanWithList addAnotherBean(AnotherBean anotherBean) {
    anotherBeans.add(anotherBean);
    return this;
  }

}
