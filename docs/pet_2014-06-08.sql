# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.12)
# Database: pet
# Generation Time: 2014-06-08 09:28:05 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table dc_account
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_account`;

CREATE TABLE `dc_account` (
  `usr_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `treasure` int(10) NOT NULL DEFAULT '0',
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`usr_id`),
  CONSTRAINT `FK_ACCOUNT` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_device
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_device`;

CREATE TABLE `dc_device` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `usr_id` int(10) unsigned DEFAULT NULL,
  `token` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `terminal` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `os` varchar(25) CHARACTER SET utf8 DEFAULT NULL,
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_DEVICE` (`usr_id`),
  CONSTRAINT `FK_DEVICE` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_friend
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_friend`;

CREATE TABLE `dc_friend` (
  `usr_id` int(10) unsigned NOT NULL,
  `follow_id` int(10) unsigned NOT NULL,
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`usr_id`,`follow_id`),
  CONSTRAINT `FK_FRIEND` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_image
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_image`;

CREATE TABLE `dc_image` (
  `img_id` int(10) NOT NULL AUTO_INCREMENT,
  `usr_id` int(10) unsigned NOT NULL,
  `comment` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `like` int(10) NOT NULL DEFAULT '0',
  `likers` text CHARACTER SET utf8,
  `url` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `file` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`img_id`),
  KEY `FK_IMAGE` (`usr_id`),
  CONSTRAINT `FK_IMAGE` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_qq
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_qq`;

CREATE TABLE `dc_qq` (
  `usr_id` int(10) unsigned NOT NULL,
  `account` int(16) unsigned NOT NULL,
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`usr_id`),
  UNIQUE KEY `UN_ACCOUNT` (`account`),
  CONSTRAINT `FK_QQ` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_sticker
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_sticker`;

CREATE TABLE `dc_sticker` (
  `sti_id` int(10) NOT NULL AUTO_INCREMENT,
  `usr_id` int(10) NOT NULL,
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sti_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_user`;

CREATE TABLE `dc_user` (
  `usr_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '0',
  `tx` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `age` int(4) NOT NULL DEFAULT '0',
  `type` tinyint(1) NOT NULL DEFAULT '0',
  `code` varchar(6) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `inviter` int(10) NOT NULL,
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`usr_id`),
  UNIQUE KEY `UN_CODE` (`code`),
  UNIQUE KEY `UQ_NAME` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_value
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_value`;

CREATE TABLE `dc_value` (
  `usr_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `exp` int(10) unsigned NOT NULL DEFAULT '0',
  `lv` int(10) unsigned NOT NULL DEFAULT '0',
  `follow` int(10) unsigned NOT NULL DEFAULT '0',
  `follower` int(10) unsigned NOT NULL DEFAULT '0',
  `con_login` int(10) unsigned NOT NULL DEFAULT '0',
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`usr_id`),
  CONSTRAINT `FK_VALUE` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



# Dump of table dc_weibo
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_weibo`;

CREATE TABLE `dc_weibo` (
  `usr_id` int(10) unsigned NOT NULL,
  `account` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`usr_id`),
  UNIQUE KEY `UN_ACCOUNT` (`account`),
  CONSTRAINT `FK_WEIBO` FOREIGN KEY (`usr_id`) REFERENCES `dc_user` (`usr_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
