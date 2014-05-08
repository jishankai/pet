# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.12)
# Database: pet
# Generation Time: 2014-05-08 06:50:17 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table dc_image
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_image`;

CREATE TABLE `dc_image` (
  `img_id` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL,
  `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `like` int(10) NOT NULL DEFAULT '0',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`img_id`),
  KEY `uid` (`uid`),
  CONSTRAINT `dc_image_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `dc_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table dc_sticker
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_sticker`;

CREATE TABLE `dc_sticker` (
  `sti_id` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL,
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sti_id`),
  KEY `uid` (`uid`),
  CONSTRAINT `dc_sticker_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `dc_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table dc_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dc_user`;

CREATE TABLE `dc_user` (
  `uid` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '0',
  `age` int(4) NOT NULL DEFAULT '0',
  `class` tinyint(1) NOT NULL DEFAULT '0',
  `treasure` int(10) NOT NULL DEFAULT '0',
  `code` varchar(6) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `inviter` int(10) NOT NULL,
  `create_time` int(10) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `name` (`name`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
