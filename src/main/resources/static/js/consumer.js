$(function () {//페이지가 로딩될때
   showList();
   showPage();
});//page loading function end  (showList,showPage 호~출)
 
let reviewUL = $(".chat"); // 클래스가 .chat 것을  reviewUL 이 가르킴 (ul 단 에있음)
let reviewPageFooter = $(".panel-footer"); //  똑같음! 

function showList() {
    getList({ contentsno: contentsno, sno: sno, eno: eno }) // 값들은 디테일.jsp 에 있음!
    .then(list => { // json형식으로 갖고온 데이터  getList 요청했을때 json형식으로 
      let str = ""
 
      for (var i = 0; i < list.length ; i++) { //json 형식 list 반복
        str += "<li class='list-group-item' data-rnum='" + list[i].rnum + "'>";
        str += "<div><div class='header'><strong class='primary-font'>" + list[i].id + "</strong>";
        str += "<small class='pull-right text-muted'>" + list[i].regdate + "</small></div>";
        str += replaceAll(list[i].content, '\n', '<br>') + "</div></li>";
      }
 
      reviewUL.html(str); 
    });
 
}//showList() end
 
function replaceAll(str, searchStr, replaceStr) {
  return str.split(searchStr).join(replaceStr);
}
    let param = '';
    param = "nPage=" + nPage;
    param += "&contentsno=" + contentsno;
   
/* producer.js getPage 파람 으로 들어가는놈들 */
 
function showPage(){
    getPage(param)
    .then(paging => {
      console.log(paging);
      let str = "<div><small class='text-muted'>" + paging + "</small></div>";
 
      reviewPageFooter.html(str); 
});
}





let modal = $(".modal");
let modalInputContent = modal.find("textarea[name='content']"); 
 
let modalModBtn = $("#modalModBtn");
let modalRemoveBtn = $("#modalRemoveBtn");
let modalRegisterBtn = $("#modalRegisterBtn");
 
$("#modalCloseBtn").on("click", function (e) {
   modal.modal('hide');
});
  
$("#addReviewBtn").on("click", function (e) {
  modalInputContent.val("");
  modal.find("button[id !='modalCloseBtn']").hide();
 
  modalRegisterBtn.show();
 
  $(".modal").modal("show");
 
});
 
modalRegisterBtn.on("click", function (e) {
 
  if (modalInputContent.val() == '') {
    alert("리뷰를 입력하세요")
    return;
  }
 
  let review = { // json 객체
    content: modalInputContent.val(),
    id: sessionid, 
    contentsno: contentsno
  };
  add(review)
    .then(result => {
      modal.find("input").val("");
      modal.modal("hide");
 
      showList();
      showPage();
 
    }); //end add
 
}); //end modalRegisterBtn.on

$(".chat").on("click", "li", function (e) {
 
  let rnum = $(this).data("rnum");
  
  
   get(rnum)
    .then(review => {
 	 
      modalInputContent.val(review.content);
      modal.data("rnum", review.rnum);
 	 
      modal.find("button[id !='modalCloseBtn']").hide();
      
 	   if(review.id == sessionid){
     	 modalModBtn.show();
      	 modalRemoveBtn.show();
      	}
      	
      $(".modal").modal("show");

    });
});

modalModBtn.on("click", function (e) {
 
  let review = { rnum: modal.data("rnum"), content: modalInputContent.val() };
  update(review)
    .then(result => {
      modal.modal("hide");
      showList();
      showPage();
    });
 
});//modify

//댓글 삭제
modalRemoveBtn.on("click", function (e) {
 
  let rnum = modal.data("rnum"); 
  remove(rnum)
    .then(result => {
      modal.modal("hide");
      showList();
      showPage();
    });
 
});//remove  */