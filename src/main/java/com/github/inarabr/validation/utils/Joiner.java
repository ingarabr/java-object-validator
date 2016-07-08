package com.github.inarabr.validation.utils;

import java.util.Collection;
import java.util.Iterator;

public final class Joiner {

  public static String join(String on, Collection<String> collection) {
    return join(on, collection, new StringToString());
  }

  public static <T> String join(String joinOn, Collection<T> collection, ToString<T> toString) {
    StringBuilder sb = new StringBuilder();
    Iterator<T> it = collection.iterator();
    while (it.hasNext()) {
      sb.append(toString.apply(it.next()));
      if (it.hasNext()) {
        sb.append(joinOn);
      }
    }
    return sb.toString();
  }

  public interface ToString<T> {
    String apply(T input);
  }

  private static final class StringToString implements ToString<String> {
    @Override
    public String apply(String input) {
      return input;
    }
  }

}
