<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">

</head>
<body>

<div class="container">
  <div class="row">
  <h4><img src="/svg/box2-heart.svg"> 상품 정보</h4>
  <img class="img-rounded" src="/contents/storage/${dto.filename}" style="width:250px">

  <div class="col-sm-6" id="mid_content">
  <h4><img src="/svg/rulers.svg"> 사이즈 및 수량</h4>
  <ul class="list-group">
    <li class="list-group-item">사이즈 :
    <c:choose>
     <c:when test="${dto.cateno==1 }">
     <select class="form-select" aria-label="Default select example">
  		<option selected>사이즈 선택</option>
  		<option value="1">L</option>
  		<option value="2">M</option>
  		<option value="3">S</option>
	 </select>
     </c:when>

     <c:when test="${dto.cateno==2 }">								 <!-- 클릭 비활성화 -->
     <select class="form-select" aria-label="Default select example" disabled="disabled">
  		<option selected>가방이 사이즈가 어딨냐잉~</option>
  	 </select>
     </c:when>

     <c:when test="${dto.cateno==3 }">
     <select class="form-select" aria-label="Default select example">
  		<option selected>사이즈 선택</option>
  		<option value="220">220</option>
  		<option value="230">230</option>
  		<option value="240">240</option>
  		<option value="250">250</option>
  	 </select>
     </c:when>
	</c:choose>
<!-- 이건 나중에 조인해보자 ! 	<li class="list-group-item">분류 : ${dto.catename} -->
	<li class="list-group-item">상품명 : ${dto.pname }
    <li class="list-group-item">설명 : ${dto.detail }
    <li class="list-group-item">가격 : ${dto.price }
    <li class="list-group-item">재고 : ${dto.stock }
    <li class="list-group-item">등록일 : ${dto.rdate }
  </ul>
  </div>
  </div>
</div>
</body>
</html>