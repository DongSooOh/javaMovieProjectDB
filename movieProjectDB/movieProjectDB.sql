create database movieproject;

use movieProject;

create table t_movie
(
 mno int primary key auto_increment,
 mtitle varchar(50) not null,
 mgenre varchar(20) not null
);

create table t_reserve
(
 rno int primary key auto_increment,
 rid varchar(50) not null,
 rtitle varchar(50) not null,
 rgenre varchar(20) not null,
 reservationDate timestamp default current_timestamp
);

create table t_member
(
 id varchar(20) primary key,
 pwd varchar(20) not null,
 name varchar(20) not null,
 email varchar(40) not null,
 joinDate date
);

insert into t_movie(mtitle, mgenre)
values("듄", "판타지"), ("파묘", "미스터리"), ("쿵푸팬더", "애니"), ("범죄도시", "액션");

select mno as 영화번호, mtitle as 영화제목, mgenre as 장르 from t_movie;

select rno as 예매번호, rid as ID, rtitle as 영화제목, rgenre as 장르, reservationDate as 발급일시 from t_reserve;

select id as 아이디, pwd as 비밀번호, name as 이름, email as 이메일_주소, joinDate as 가입일자 from t_member;