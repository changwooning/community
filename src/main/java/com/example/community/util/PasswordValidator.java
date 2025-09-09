package com.example.community.util;

import java.util.regex.Pattern;

public class PasswordEncoder {

  private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$";

  public static boolean isValid(String password) {
    return Pattern.matches(PASSWORD_PATTERN, password);
  }

}
