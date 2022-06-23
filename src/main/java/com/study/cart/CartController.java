package com.study.cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {
  private static final Logger log = LoggerFactory.getLogger(CartController.class);


  @GetMapping("/cart/list")
  public String list() {
    
    
    
    return "/cart/list";
  }
}
