package com.github.inarabr.validation.utils;

public final class ReflectionHelper {

  private ReflectionHelper() {
  }

  public static String stripGetterInMethodName(String metodeNavn) {
    if (metodeNavn.startsWith("get") && metodeNavn.length() > 3) {
      return metodeNavn.substring(3, 4).toLowerCase() + metodeNavn.substring(4, metodeNavn.length());
    }
    if (metodeNavn.startsWith("is") && metodeNavn.length() > 2) {
      return metodeNavn.substring(2, 3).toLowerCase() + metodeNavn.substring(3, metodeNavn.length());
    }
    return metodeNavn;
  }
}
