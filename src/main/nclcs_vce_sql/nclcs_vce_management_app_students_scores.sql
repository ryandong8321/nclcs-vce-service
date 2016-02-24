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
-- Table structure for table `app_students_scores`
--

DROP TABLE IF EXISTS `app_students_scores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_students_scores` (
  `app_students_scores_id` int(11) NOT NULL AUTO_INCREMENT,
  `app_students_scores_user_id` int(11) NOT NULL,
  `app_students_scores_sys_property_id` int(11) NOT NULL,
  `app_students_scores_value` int(11) NOT NULL,
  PRIMARY KEY (`app_students_scores_id`),
  UNIQUE KEY `app_students_scores_id_UNIQUE` (`app_students_scores_id`),
  KEY `FK_app_students_scores_sys_user_id_idx` (`app_students_scores_user_id`),
  KEY `FK_app_students_scores_sys_property_id_idx` (`app_students_scores_sys_property_id`),
  CONSTRAINT `FK_app_students_scores_sys_property_id` FOREIGN KEY (`app_students_scores_sys_property_id`) REFERENCES `sys_properties` (`property_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_app_students_scores_sys_user_id` FOREIGN KEY (`app_students_scores_user_id`) REFERENCES `sys_users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_students_scores`
--

LOCK TABLES `app_students_scores` WRITE;
/*!40000 ALTER TABLE `app_students_scores` DISABLE KEYS */;
INSERT INTO `app_students_scores` VALUES (3,8,18,0),(4,8,19,70),(5,8,20,0),(6,8,21,0),(7,8,22,100),(8,8,23,90),(9,8,24,0),(10,8,25,0),(11,8,26,0),(12,8,27,0),(13,8,28,0),(14,8,29,0),(15,8,30,0),(16,8,31,0),(17,17,18,0),(18,17,19,0),(19,17,20,0),(20,17,21,40),(21,17,22,0),(22,17,23,100),(23,17,24,0),(24,17,25,0),(25,17,26,0),(26,17,27,0),(27,17,28,0),(28,17,29,0),(29,17,30,0),(30,17,31,0);
/*!40000 ALTER TABLE `app_students_scores` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-24 22:43:24
