package com.study.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
  
private int rnum;
private String content;
private String regdate;
private String id;
private int contentsno;
}
