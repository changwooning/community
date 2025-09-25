package com.example.community.enums;

public enum SortType {
  CREATED_AT("createdAt"),
  VIEWS("views");

  private final String field;

  SortType(String field) {
    this.field = field;
  }

  public String getField() {
    return field;
  }


}
