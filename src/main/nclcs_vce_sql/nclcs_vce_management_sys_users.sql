-- MySQL dump 10.13  Distrib 5.6.24, for osx10.8 (x86_64)
--
-- Host: 127.0.0.1    Database: nclcs_vce_management
-- ------------------------------------------------------
-- Server version	5.6.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_users`
--

DROP TABLE IF EXISTS `sys_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `user_password` varchar(128) COLLATE utf8_bin NOT NULL,
  `user_chinese_name` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `user_english_name` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `user_chinese_pinyin` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `user_home_phone` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `user_mobile_phone` varchar(16) COLLATE utf8_bin NOT NULL,
  `user_email` varchar(64) COLLATE utf8_bin NOT NULL,
  `user_home_address` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `user_day_school` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `user_is_learn_chinese` int(11) DEFAULT NULL,
  `user_day_school_grade` varchar(8) COLLATE utf8_bin DEFAULT NULL,
  `user_photo` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `user_children_id` int(11) DEFAULT NULL,
  `user_register_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_birth_date` varchar(8) COLLATE utf8_bin DEFAULT NULL,
  `user_vce_school_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `user_vce_class_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `sys_users_id_UNIQUE` (`user_id`),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`),
  KEY `FK_SYS_USERS_IS_LEARN_CHINESE_idx` (`user_is_learn_chinese`),
  CONSTRAINT `FK_SYS_USERS_IS_LEARN_CHINESE` FOREIGN KEY (`user_is_learn_chinese`) REFERENCES `sys_properties` (`property_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-24 22:57:15
