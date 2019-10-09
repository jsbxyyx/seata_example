CREATE DATABASE `test`;

CREATE TABLE `t_order` (
  `user_id` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `context` varchar(128) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;



/******************* oracle **********************/
create table test_escape (
  "sid" varchar2(50) primary key,
  "param" varchar2(50),
  "createTime" date
)

create table test_keyword(
  "in" varchar2(50) primary key,
  "desc" varchar2(50)
)

create table test_str(
  id varchar(50) primary key,
  name varchar(50)
)

create table test(
  id number generated by default on null as identity primary key,
  name varchar(50),
  name2 varchar(50)
);

create table test1(
  id int primary key,
  name varchar(45),
  name2 varchar(45)
);
create sequence test1_seq;


CREATE TABLE undo_log (
  id number(20) NOT NULL,
  branch_id number(20) NOT NULL,
  xid varchar2(100) NOT NULL,
  context varchar2(128) NOT NULL,
  rollback_info blob NOT NULL,
  log_status number(11) NOT NULL,
  log_created timestamp NOT NULL,
  log_modified timestamp NOT NULL,
  ext varchar2(100) DEFAULT NULL,
  PRIMARY KEY (id)
);
create unique index index_ux_undo_log on undo_log (xid, branch_id);
create sequence undo_log_seq;

/******************** oracle *********************/


