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

--
-- Dumping data for table `sys_groups`
--

LOCK TABLES `sys_groups` WRITE;
/*!40000 ALTER TABLE `sys_groups` DISABLE KEYS */;
INSERT INTO `sys_groups` VALUES (1,'新金山总校',0,-1,NULL,NULL,NULL),(2,'Box Hill',0,1,NULL,NULL,''),(3,'ThornBury H.S.',0,1,NULL,NULL,''),(4,'Balwyn H.S.',0,1,NULL,NULL,''),(5,'Mckinnon S.C.',0,1,NULL,NULL,''),(6,'Ringwood S.C.',0,1,NULL,NULL,''),(7,'Blackburn H.S.',0,1,NULL,NULL,''),(8,'Thomas Carr College',0,1,NULL,NULL,''),(9,'Macleod',0,1,NULL,NULL,''),(10,'City',0,1,NULL,NULL,''),(11,'周五 VCE 预备班',1,2,7,12,'11:00'),(12,'周六 VCE Unit 1 & 2 第一语言班',1,2,8,13,'10:00'),(14,'测试用校区',0,1,NULL,NULL,NULL),(15,'周日 VCE Unit 3 & 4 第二语言班',1,3,9,16,'11:00'),(16,'rooney 上课',1,3,8,12,'10:00'),(17,'Balwyn VCE UNIT 3 & 4 第二语言高级',1,4,9,17,'11:00');
/*!40000 ALTER TABLE `sys_groups` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-24 22:43:26
