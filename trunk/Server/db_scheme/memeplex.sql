/*
SQLyog Enterprise - MySQL GUI v8.1 
MySQL - 5.0.41 : Database - memeplex
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`memeplex` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `memeplex`;

/*Table structure for table `comments` */

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
  `comment_srl` int(11) NOT NULL auto_increment,
  `document_srl` int(11) NOT NULL,
  `nick_name` varchar(50) default NULL,
  `location` varchar(255) default NULL,
  `timestamp` timestamp NULL default CURRENT_TIMESTAMP,
  `device_id` varchar(50) default NULL,
  `content` text,
  PRIMARY KEY  (`comment_srl`),
  UNIQUE KEY `idx_device_id` (`device_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `document_tags` */

DROP TABLE IF EXISTS `document_tags`;

CREATE TABLE `document_tags` (
  `document_srl` int(11) NOT NULL,
  `tag_srl` int(11) NOT NULL,
  PRIMARY KEY  (`document_srl`,`tag_srl`),
  KEY `idx_tag` (`tag_srl`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `documents` */

DROP TABLE IF EXISTS `documents`;

CREATE TABLE `documents` (
  `document_srl` int(11) NOT NULL,
  `nick_name` varchar(50) default NULL,
  `picture_path` varchar(255) default NULL,
  `audio_path` varchar(255) default NULL,
  `location` varchar(255) default NULL,
  `timestamp` timestamp NULL default CURRENT_TIMESTAMP,
  `device_id` varchar(50) default NULL,
  `content` text,
  PRIMARY KEY  (`document_srl`),
  UNIQUE KEY `idx_device_id` (`device_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `tags` */

DROP TABLE IF EXISTS `tags`;

CREATE TABLE `tags` (
  `tag_srl` int(11) NOT NULL auto_increment,
  `name` varchar(50) default NULL,
  `score_day` int(11) default NULL,
  `score_week` int(11) default NULL,
  `score_month` int(11) default NULL,
  `score_year` int(11) default NULL,
  PRIMARY KEY  (`tag_srl`),
  KEY `idx_name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
