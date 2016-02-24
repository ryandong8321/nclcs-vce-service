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
-- Table structure for table `sys_properties`
--

DROP TABLE IF EXISTS `sys_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_properties` (
  `property_id` int(11) NOT NULL AUTO_INCREMENT,
  `property_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `property_parent_id` int(11) DEFAULT '-1',
  PRIMARY KEY (`property_id`),
  UNIQUE KEY `property_id_UNIQUE` (`property_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_properties`
--

LOCK TABLES `sys_properties` WRITE;
/*!40000 ALTER TABLE `sys_properties` DISABLE KEYS */;
INSERT INTO `sys_properties` VALUES (2,'系统属性信息',-1),(3,'上课日期',2),(4,'考试名称',2),(5,'是否在日校学习中文',2),(6,'班级',2),(7,'周五',3),(8,'周六',3),(9,'周日',3),(10,'是',5),(11,'否',5),(12,'VCE预备班',6),(13,'VCE UNIT 1&2 第一语言',6),(14,'VCE UNIT 3&4 第一语言',6),(15,'VCE UNIT 1&2 第二语言',6),(16,'VCE UNIT 3&4 第二语言',6),(17,'VCE UNIT 3&4 第二语言高级',6),(18,'Unit1 outcome 1',4),(19,'Unit1 outcome 2A',4),(20,'Unit1 outcome 2B',4),(21,'Unit1 outcome 3',4),(22,'Unit2 outcome 1',4),(23,'Unit2 outcome 2A',4),(24,'Unit2 outcome 2B',4),(25,'Unit2 outcome 3',4),(26,'Unit3 outcome 1',4),(27,'Unit3 outcome 2',4),(28,'Unit3 outcome 3',4),(29,'Unit4 outcome 1',4),(30,'Unit4 outcome 2A',4),(31,'Unit4 outcome 2B',4);
/*!40000 ALTER TABLE `sys_properties` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-24 22:43:25
