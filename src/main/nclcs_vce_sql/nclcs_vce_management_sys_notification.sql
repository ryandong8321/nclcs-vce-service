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
-- Table structure for table `sys_notification`
--

DROP TABLE IF EXISTS `sys_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_notification` (
  `notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `notification_title` varchar(64) COLLATE utf8_bin NOT NULL,
  `notification_message` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `notification_create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `notification_create_user_id` int(11) NOT NULL,
  `notification_create_role_id` int(11) DEFAULT NULL,
  `notification_group_id` int(11) DEFAULT NULL,
  `notification_receive_group_ids` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `notification_is_send` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`notification_id`),
  UNIQUE KEY `id_UNIQUE` (`notification_id`),
  KEY `FK_sys_notification_user_id_idx` (`notification_create_user_id`),
  KEY `FK_sys_notification_role_id_idx` (`notification_create_role_id`),
  KEY `FK_sys_notification_group_id_idx` (`notification_group_id`),
  CONSTRAINT `FK_sys_notification_group_id` FOREIGN KEY (`notification_group_id`) REFERENCES `sys_groups` (`group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_sys_notification_role_id` FOREIGN KEY (`notification_create_role_id`) REFERENCES `sys_roles` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_sys_notification_user_id` FOREIGN KEY (`notification_create_user_id`) REFERENCES `sys_users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-24 22:57:13
