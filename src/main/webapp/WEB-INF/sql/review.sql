use webtest;

create table review(
   rnum int not null auto_increment primary key,
   content varchar(500) not null,
   regdate date not null,
   id varchar(10) not null,
   contentsno int(7) not null,
   foreign key (contentsno) references contents(contentsno)
);

select * from review where contentsno;

select * from review
where contentsno = 13
order by rnum DESC
limit 0 , 3;

insert into review(content, regdate, id, contentsno)
values('올라갔다!!!!!!',sysdate(),'user1',6);
