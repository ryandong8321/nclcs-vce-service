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
-- Table structure for table `sys_groups`
--

DROP TABLE IF EXISTS `sys_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_groups` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `group_category` int(11) NOT NULL DEFAULT '0' COMMENT '0-校区,1-班级',
  `group_parent_id` int(11) NOT NULL DEFAULT '-1',
  `group_date_info` int(11) DEFAULT NULL,
  `group_class_info` int(11) DEFAULT NULL,
  `group_time_info` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `sys_groups_UNIQUE` (`group_id`),
  KEY `FK_SYS_GROUPS_DATE_INFO_idx` (`group_date_info`),
  KEY `FK_SYS_GROUPS_CLASS_INFO_idx` (`group_class_info`),
  CONSTRAINT `FK_SYS_GROUPS_CLASS_INFO` FOREIGN KEY (`group_class_info`) REFERENCES `sys_properties` (`property_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_SYS_GROUPS_DATE_INFO` FOREIGN KEY (`group_date_info`) REFERENCES `sys_properties` (`property_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
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
