package com.study.cart;

import lombok.Data;

@Data
public class CartDTO {

  private int cartno;
  private int count;
  private int contentsno;
  private String id;
  private String size;
}
