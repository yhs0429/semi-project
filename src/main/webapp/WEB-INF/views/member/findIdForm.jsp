<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<title>아이디찾기</title>
	<meta charset="utf-8">
</head>
<body>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<script type="text/javascript">
$(function(){
	$("#findid").click(function(){
		let mname = $('#mname').val();
		let email = $('#email').val();

		let url =  "http://localhost:8000/member/findId/?mname=" + mname + "&email=" + email;

		fetch(url)
		.then(response => response.json())
		.then((data) => alert(data.id));
	});
});
</script>
<div class="container">

<h1 class="col-sm-offset-2 col-sm-10">아이디찾기</h1>
  <form class="form-horizontal" 
        action="/member/findId"
        method="post">

        
    <div class="form-group">
      <label class="control-label col-sm-2" for="mname">이름</label>
      <div class="col-sm-4">
        <input type="text" class="form-control" id="mname" 
        placeholder="Enter name" name="mname" required="required" 
        value='${c_id_val}'>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-2" for="email">이메일</label>
      <div class="col-sm-4">          
        <input type="email" class="form-control" id="email" 
        placeholder="Enter email" name="email" required="required">
      </div>
    </div>
					<p class="w3-center">
						<button type="button" id = "findid" class="w3-button w3-block w3-black w3-ripple w3-margin-top w3-round">아이디찾기</button>
						<button type="button" onclick="history.back()" class="w3-button w3-block w3-black w3-ripple w3-margin-top w3-margin-bottom w3-round">취소</button>
					</p>
				</div>
			</form>
		</div>
	</div>
</body>
</html>