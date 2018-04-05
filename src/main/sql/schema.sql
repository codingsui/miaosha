-- 创建数据库

CREATE DATABASE seckill;

-- 使用数据库
use seckill;

CREATE TABLE seckill(
  seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  name varchar(120) NOT NULL COMMENT '商品名称',
  number int NOT NULL COMMENT '库存数量',
  start_time TIMESTAMP NOT NULL COMMENT '开启时间',
  end_time TIMESTAMP NOT NULL COMMENT '结束时间',
  creare_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建时间',
  PRIMARY KEY (seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(creare_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀';


  INSERT INTO seckill(name,number,start_time,end_time)
  values
('1000元秒杀iphone6',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
('500元秒杀ipad2',200,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
('300元秒杀小米4',300,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
('200元秒杀红迷',400,'2015-11-01 00:00:00','2015-11-02 00:00:00');


-- 明细表
CREATE TABLE success_killed(
  seckill_id bigint NOT NULL COMMENT '秒杀商品id',
  user_phone bigint NOT NULL COMMENT '手机号',
  state tinyint NOT NULL DEFAULT -1 COMMENT '状态 -1：无效 0:成功 1：已付款',
  creare_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建时间',
  PRIMARY KEY(seckill_id,user_phone),
  KEY idx_create_time(creare_time)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';


