# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.12)
# Database: pet
# Generation Time: 2014-09-01 03:55:22 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table dc_animal
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_animal`;

CREATE TABLE `dc_animal` (
  `aid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '宠物编号',
  `name` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '名字',
  `tx` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '头像地址',
  `gender` tinyint(1) NOT NULL DEFAULT '0' COMMENT '性别',
  `from` tinyint(1) NOT NULL COMMENT '星球',
  `type` smallint(3) NOT NULL DEFAULT '0' COMMENT '种族',
  `age` int(4) NOT NULL DEFAULT '0' COMMENT '年龄',
  `address` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '邮寄地址',
  `master_id` int(10) unsigned NOT NULL COMMENT '主人编号',
  `items` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '礼物',
  `d_rq` int(10) NOT NULL COMMENT '日人气',
  `w_rq` int(10) NOT NULL COMMENT '周人气',
  `m_rq` int(10) NOT NULL COMMENT '月人气',
  `t_rq` int(10) NOT NULL COMMENT '总人气',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`aid`),
  UNIQUE KEY `UQ_NAME` (`name`),
  KEY `FK_ANIMAL&USER` (`master_id`),
  CONSTRAINT `FK_ANIMAL&USER` FOREIGN KEY (`master_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

LOCK TABLES `dc_animal` WRITE;
/*!40000 ALTER TABLE `dc_animal` DISABLE KEYS */;

INSERT INTO `dc_animal` (`aid`, `name`, `tx`, `gender`, `from`, `type`, `age`, `address`, `master_id`, `items`, `d_rq`, `w_rq`, `m_rq`, `t_rq`, `create_time`, `update_time`)
VALUES
	(1,'','',0,0,0,0,'',1,'',0,0,0,0,0,'2014-08-27 14:34:28');

/*!40000 ALTER TABLE `dc_animal` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table dc_circle
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_circle`;

CREATE TABLE `dc_circle` (
  `aid` int(10) unsigned NOT NULL COMMENT '宠物编号',
  `usr_id` int(10) unsigned NOT NULL COMMENT '用户编号',
  `rank` int(10) unsigned NOT NULL COMMENT '人气排名',
  `t_contri` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '贡献度',
  `d_contri` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '贡献度',
  `w_contri` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '贡献度',
  `m_contri` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '贡献度',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`aid`,`usr_id`),
  KEY `FK_CIRCLE&USER` (`usr_id`),
  CONSTRAINT `FK_CIRCLE&ANIMAL` FOREIGN KEY (`aid`) REFERENCES `dc_animal` (`aid`) ON DELETE CASCADE,
  CONSTRAINT `FK_CIRCLE&USER` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_device
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_device`;

CREATE TABLE `dc_device` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '设备编号',
  `uid` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '唯一标示',
  `usr_id` int(10) unsigned NOT NULL COMMENT '用户编号',
  `token` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '令牌',
  `terminal` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '平台',
  `os` varchar(25) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '操作系统',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_UID` (`uid`),
  KEY `FK_DEVICE` (`usr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

LOCK TABLES `dc_device` WRITE;
/*!40000 ALTER TABLE `dc_device` DISABLE KEYS */;

INSERT INTO `dc_device` (`id`, `uid`, `usr_id`, `token`, `terminal`, `os`, `create_time`, `update_time`)
VALUES
	(13,'359575048790065',86,'','','',1403406144,'2014-06-25 02:25:56'),
	(14,'866194017682224',175,'','','',1403419288,'2014-07-10 03:32:08'),
	(15,'6583c8bd9aa110058ab5bee47b926e7481a29e97',19,'','','',1403420098,'2014-06-22 06:57:04'),
	(16,'359093052908359',28,'','','',1403421122,'2014-06-22 09:00:31'),
	(17,'7cfbbc4fb9d9107569b986ce89deb777acb00c1c',61,'','','',1403421780,'2014-06-22 13:54:26'),
	(18,'0c1315d298353601af34e081e9e322c55580e145',64,'','','',1403422233,'2014-06-22 17:30:19'),
	(19,'9c1e6c2b852979d7f03cc7042dbea19532abc6e6',161,'','','',1403422976,'2014-07-03 09:51:36'),
	(20,'4453d94b4d3400139d0fd8d060c7c7dfa32b8bae',26,'','','',1403425194,'2014-06-22 08:21:08'),
	(21,'f3dd40b5b8669d40acb2a594fabeac3ef109d87a',0,'','','',0,'0000-00-00 00:00:00'),
	(22,'867746012862397',72,'','','',1403438617,'2014-06-23 02:12:45'),
	(23,'0b4d3617ec648c5166f43a495274a8ec3f349376',169,'','','',1403445916,'2014-07-04 06:04:28'),
	(24,'2f51704cb1d2608895b952fc294554b4b6d5091a',65,'','','',1403474981,'2014-06-22 22:11:46'),
	(25,'80a8468856eb89325a0ee0e9c56670ab48d30cfd',68,'','','',1403487453,'2014-06-23 01:38:13'),
	(26,'863360024177903',208,'','','',1403487817,'2014-08-04 06:24:13'),
	(27,'862751027846323',189,'','','',1403488061,'2014-07-14 08:32:03'),
	(28,'8304c2c5b0dc119853e2fe14c6d107dec597a029',70,'','','',1403488692,'2014-06-23 01:59:09'),
	(29,'353769059953581',73,'','','',1403490525,'2014-06-23 02:49:59'),
	(30,'7b5d162abd33f4519c688cc16e3c4abd3bfe7ac2',74,'','','',1403501053,'2014-06-23 05:49:20'),
	(31,'166d6fc331503b362538dfe41d465d740de0ff27',138,'','','',1403517947,'2014-07-01 03:41:33'),
	(32,'bc43e1a4616a3cf493951f17d42fab277c5f6573',0,'','','',1403518968,'2014-06-23 10:22:48'),
	(33,'355136058169784',0,'','','',1403522934,'2014-06-23 11:28:54'),
	(34,'56445fcb77ebc14dae9d6723c9d22ef9a1398bfb',87,'','','',1403540350,'2014-06-25 02:44:01'),
	(35,'8ee7db97cb88da3affcb02806b9f57b81198b742',84,'','','',1403660227,'2014-06-25 01:53:55'),
	(36,'fe1582a6446405ee44316ca772759c07e2126520',147,'','','',1403662239,'2014-07-02 06:55:28'),
	(37,'111572ff68bf7564ae1c8f6e2424df9a907b3cfb',0,'','','',1403665203,'2014-07-11 02:20:40'),
	(38,'357512052610089',195,'','','',1403678647,'2014-07-15 09:04:04'),
	(39,'0da84aeac93bbf6da5e8dc68f85f8a948425aac5',95,'','','',1403747718,'2014-06-26 06:46:37'),
	(40,'238fbf0fb7c721bd9ba04d1813d5218866a40bb4',0,'','','',1403750709,'2014-06-26 02:45:09'),
	(41,'7eaeecd9156bfe0f34d0d119be995b6f833db342',0,'','','',1403751506,'2014-06-26 02:58:26'),
	(42,'bba1a8f320818f8c8f5f18870117bcc203feb59f',94,'','','',1403751746,'2014-06-26 06:44:03'),
	(43,'89853301a285371a236778eb7b1cacd61da4f372',179,'','','',1403866332,'2014-07-10 13:08:10'),
	(44,'311ca2871769912304a2d056dcaa57eccdae9097',0,'','','',1403874175,'2014-06-27 13:02:55'),
	(45,'357457043499699',143,'','','',1404105933,'2014-07-01 12:25:51'),
	(46,'2d32722eb2e3a1a1a76b5bc25949f67547ea82f9',164,'','','',1404113600,'2014-07-04 02:35:42'),
	(47,'6c395440da71c701397c287e5b8bb0a31bd480c6',146,'','','',1404271053,'2014-07-02 03:21:15'),
	(48,'863753029852050',165,'','','',1404363568,'2014-07-04 02:42:03'),
	(49,'faca8c9fd1df447acc87d9fcc8ad2c3c39cfa29b',166,'','','',1404441889,'2014-07-04 02:50:52'),
	(50,'2acb47db2b85732728b8512539c61195edda06e0',172,'','','',1404713129,'2014-07-07 06:07:13'),
	(51,'339fbb57ec359b1006784282fdee48f7bca54be6',182,'','','',1404873558,'2014-07-11 02:21:32'),
	(52,'1b79fbae63c9b6789b9d2c9b57b2603bf39a7c88',181,'','','',1404890772,'2014-07-11 02:17:58'),
	(53,'18522fb91cf0bdaa74766d257ee71ca6174f9733',178,'','','',1404893967,'2014-07-10 10:56:57'),
	(54,'865019024078457',174,'','','',1404895882,'2014-07-10 00:50:28'),
	(56,'867070012797713',202,'','','',1404957617,'2014-07-23 06:03:41'),
	(57,'c4f93b51c4b2491af0599f2da7c18eb56328b0c9',180,'','','',1405043243,'2014-07-11 01:48:45'),
	(58,'d1a8085fef970c31ac671af811c78464a035e733',156,'','','',1405070026,'2014-07-11 09:17:08'),
	(59,'d5ae2c3982338ff32cd1230805ea63bfe807ab33',186,'','','',1405302813,'2014-07-14 01:59:48'),
	(60,'455152e23522e28f3d892ae782e4a06f7e7f25f4',187,'','','',1405306488,'2014-07-14 02:55:27'),
	(61,'54755be1074e70d5e23a196880caa2f6d4be0886',194,'','','',1405395179,'2014-07-15 08:55:40'),
	(62,'21f2f97b54aeac4c34af248a80fba57acd9874f4',193,'','','',1405405641,'2014-07-15 06:56:01'),
	(63,'72a64d7e509058a55b930733ab0f06a1d89f9401',0,'','','',1405425145,'2014-07-15 11:52:25'),
	(65,'',0,'','','',1405475866,'2014-07-16 01:57:46'),
	(66,'fc3d8e8ccb3f890aa4096b541345d796bcd8c4ca',196,'','','',1405476593,'2014-07-16 02:11:01'),
	(67,'d7114b2fe8031af94589409e637222dffe173c6f',197,'','','',1405477471,'2014-07-16 02:46:32'),
	(68,'839432ba4c3d2e3adcc1ce9dff98574332a7098b',0,'','','',1405504912,'2014-07-16 10:01:52'),
	(69,'null',0,'','','',1405577872,'2014-07-17 06:28:07'),
	(70,'9CD678C38B22E69F34B854D16F126F6C',0,'','','',1405578826,'2014-07-17 06:33:46'),
	(72,'46FECCD6E9B00CA0A9AAA2D39E909D46',199,'','','',1405579640,'2014-07-17 06:51:00'),
	(73,'f3d8304357294ba08f7e1472643183c069feadd8',200,'','','',1405648914,'2014-07-18 02:31:12'),
	(74,'354156053724490',201,'','','',1405836873,'2014-07-20 06:53:26'),
	(76,'000000000000000',0,'','','',1406103371,'2014-07-23 08:16:11'),
	(77,'168bfc3011d8f467e88bf67a5b1d0e793f6a3234',0,'','','',1406179241,'2014-07-24 05:20:41'),
	(78,'355546057043468',0,'','','',1406531358,'2014-07-28 07:09:18'),
	(79,'d591f0fac597894b3c7c1f6e1109fa37ab9e2c0c',0,'','','',1406597852,'2014-07-29 01:37:32'),
	(80,'820174868e10024d4eeb2933e2e355b25505ed2e',0,'','','',1406800265,'2014-07-31 09:51:05'),
	(81,'bc1ba308981ad4d1e2b8e1dd2a09aa7886e5a91c',0,'','','',1406857905,'2014-08-01 01:51:45'),
	(82,'6fa872b520fe09e8b573076c1355e6e45c91f83c',203,'','','',1406863561,'2014-08-01 11:07:12'),
	(83,'d465b685f9d7ba610d1aa005e935edd9e17e9e09',0,'','','',1407116241,'2014-08-04 01:37:21'),
	(84,'694c67dce6044a4a203bb64cb2e34400dbf88ab1',206,'','','',1407117187,'2014-08-04 02:39:15'),
	(85,'ae01c7308d6e88e7e56aad3247c080d586972ba2',0,'','','',1407118271,'2014-08-04 02:11:11'),
	(100012,'67dc2b7db959610f880f3434a6ac2adb864bf825',121,'','','',1403406030,'2014-08-08 10:27:48');

/*!40000 ALTER TABLE `dc_device` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table dc_follow
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_follow`;

CREATE TABLE `dc_follow` (
  `aid` int(10) unsigned NOT NULL COMMENT '宠物编号',
  `usr_id` int(10) unsigned NOT NULL COMMENT '用户编号',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`aid`,`usr_id`),
  KEY `FK_USER&FOLLOW` (`usr_id`),
  CONSTRAINT `FK_ANIMAL&FOLLOW` FOREIGN KEY (`aid`) REFERENCES `dc_animal` (`aid`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER&FOLLOW` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

LOCK TABLES `dc_follow` WRITE;
/*!40000 ALTER TABLE `dc_follow` DISABLE KEYS */;

INSERT INTO `dc_follow` (`aid`, `usr_id`, `create_time`, `update_time`)
VALUES
	(1,1,0,'2014-08-27 14:34:44');

/*!40000 ALTER TABLE `dc_follow` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table dc_image
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_image`;

CREATE TABLE `dc_image` (
  `img_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '图片编号',
  `aid` int(10) unsigned NOT NULL COMMENT '宠物编号',
  `topic_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '活动编号',
  `cmt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '说明',
  `url` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '地址',
  `likes` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '点赞数',
  `likers` text COLLATE utf8_bin NOT NULL COMMENT '点赞用户',
  `comments` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`img_id`),
  KEY `FK_IMAGE&ANIMAL` (`aid`),
  CONSTRAINT `FK_IMAGE&ANIMAL` FOREIGN KEY (`aid`) REFERENCES `dc_animal` (`aid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_item`;

CREATE TABLE `dc_item` (
  `item_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '物品编号',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '名称',
  `icon` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '标志',
  `desc` text CHARACTER SET utf8 NOT NULL COMMENT '描述',
  `img` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '图片地址',
  `price` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '价格',
  `rq` int(10) NOT NULL DEFAULT '0' COMMENT '人气变化',
  `exp` int(10) NOT NULL DEFAULT '0' COMMENT '增加经验',
  `type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '类别',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

LOCK TABLES `dc_item` WRITE;
/*!40000 ALTER TABLE `dc_item` DISABLE KEYS */;

INSERT INTO `dc_item` (`item_id`, `name`, `icon`, `desc`, `img`, `price`, `rq`, `exp`, `type`, `create_time`, `update_time`)
VALUES
	(1,'可口可乐','','非常猫狗之可口可乐，喝完就变身','',1,0,0,0,1404031105,'2014-06-30 09:33:25');

/*!40000 ALTER TABLE `dc_item` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table dc_mail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_mail`;

CREATE TABLE `dc_mail` (
  `mail_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '邮件编号',
  `usr_id` int(10) unsigned NOT NULL COMMENT '用户编号',
  `tx` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '头像地址',
  `name` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '发信人',
  `gender` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '性别',
  `from_id` int(10) unsigned NOT NULL COMMENT '发信人编号',
  `body` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '内容',
  `is_read` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '读否',
  `is_system` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否来自系统',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '删否',
  PRIMARY KEY (`mail_id`),
  KEY `FK_MAIL` (`usr_id`),
  CONSTRAINT `FK_MAIL` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_news
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_news`;

CREATE TABLE `dc_news` (
  `nid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '通知编号',
  `aid` int(10) unsigned NOT NULL COMMENT '宠物编号',
  `type` tinyint(1) NOT NULL COMMENT '类别',
  `content` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '内容',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`nid`),
  KEY `FK_NEWS&ANIMAL` (`aid`),
  CONSTRAINT `FK_NEWS&ANIMAL` FOREIGN KEY (`aid`) REFERENCES `dc_animal` (`aid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_sticker
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_sticker`;

CREATE TABLE `dc_sticker` (
  `sti_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '贴图编号',
  `usr_id` int(10) unsigned NOT NULL COMMENT '用户编号',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`sti_id`),
  KEY `FK_STICKER` (`usr_id`),
  CONSTRAINT `FK_STICKER` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_talk
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_talk`;

CREATE TABLE `dc_talk` (
  `talk_id` int(10) unsigned NOT NULL COMMENT '编号',
  `usra_id` int(10) unsigned NOT NULL COMMENT '用户A编号',
  `usrb_id` int(10) unsigned NOT NULL COMMENT '用户B编号',
  `content` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '聊天内容',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`talk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_topic
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_topic`;

CREATE TABLE `dc_topic` (
  `topic_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '活动编号',
  `topic` varchar(12) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '活动名称',
  `to` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '面向对象',
  `des` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '描述',
  `reward` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '奖励',
  `remark` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '说明',
  `img` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '图片地址',
  `status` tinyint(1) unsigned NOT NULL COMMENT '状态',
  `start_time` int(10) unsigned NOT NULL COMMENT '开始时间',
  `end_time` int(10) unsigned NOT NULL COMMENT '结束时间',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

LOCK TABLES `dc_topic` WRITE;
/*!40000 ALTER TABLE `dc_topic` DISABLE KEYS */;

INSERT INTO `dc_topic` (`topic_id`, `topic`, `to`, `des`, `reward`, `remark`, `img`, `status`, `start_time`, `end_time`, `create_time`, `update_time`)
VALUES
	(1,'活动测试',0,'活动测试2014','1:1','','',1,1403449200,1404399600,1404184365,'2014-06-30 09:32:32'),
	(3,'萌宠时装秀',0,'谁是最萌最可爱最令人爱不释手的？不来比比看怎能知道！欢迎大家携带自家的萌宠来参与我们为您精心准备的萌宠时装秀~','1:1','1.阿猫阿狗官方活动结束后，人气排名前三，分别获得第一、二、三等奖。\r\n2.必须是自家的萌宠照哦！\r\n3.奖品以实物为准。\r\n','3.jpg',1,1404162000,1406797200,1404352369,'2014-07-07 03:30:07'),
	(4,'狗狗选美大赛',1,'一年一度的狗狗选美大赛，盛大开幕！您家的萌宠已迫不及待的想要来参赛啦~我们为您提供最真诚的舞台，每一只狗狗都会受到同样的爱戴~赶快来参加吧！','一等奖=齐来特狗粮一包\r\n二等奖=狗狗玩具一个\r\n三等奖=狗狗项圈一个','','4.jpg',1,1404950400,1409475600,1405047781,'2014-07-11 03:03:01');

/*!40000 ALTER TABLE `dc_topic` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table dc_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_user`;

CREATE TABLE `dc_user` (
  `usr_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `name` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '姓名',
  `tx` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '头像地址',
  `gender` tinyint(1) NOT NULL DEFAULT '0' COMMENT '性别',
  `city` smallint(3) NOT NULL COMMENT '城市',
  `weibo` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '微博账号',
  `qq` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT 'QQ账号',
  `age` int(4) NOT NULL DEFAULT '0' COMMENT '年龄',
  `exp` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '经验',
  `lv` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '等级',
  `gold` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '金币',
  `items` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '邀请码',
  `con_login` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '连续登录时间',
  `login_time` int(10) unsigned DEFAULT '0' COMMENT '上次登录时间',
  `vip` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'VIP值',
  `aid` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '宠物编号',
  `code` varchar(6) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '邀请码',
  `inviter` int(10) unsigned NOT NULL COMMENT '邀请者',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`usr_id`),
  UNIQUE KEY `UN_CODE` (`code`),
  UNIQUE KEY `UQ_NAME` (`name`),
  KEY `FK_USER&CIRCLE` (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

LOCK TABLES `dc_user` WRITE;
/*!40000 ALTER TABLE `dc_user` DISABLE KEYS */;

INSERT INTO `dc_user` (`usr_id`, `name`, `tx`, `gender`, `city`, `weibo`, `qq`, `age`, `exp`, `lv`, `gold`, `items`, `con_login`, `login_time`, `vip`, `aid`, `code`, `inviter`, `create_time`, `update_time`)
VALUES
	(1,'','',0,0,'','',0,0,0,0,'',0,0,0,0,'',0,0,'2014-08-27 14:34:22');

/*!40000 ALTER TABLE `dc_user` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
