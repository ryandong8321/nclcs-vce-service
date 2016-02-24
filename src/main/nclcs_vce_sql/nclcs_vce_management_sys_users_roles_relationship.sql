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
-- Table structure for table `sys_users_roles_relationship`
--

DROP TABLE IF EXISTS `sys_users_roles_relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_users_roles_relationship` (
  `relationship_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`relationship_id`),
  UNIQUE KEY `relationship_id_UNIQUE` (`relationship_id`),
  KEY `FK_USER_ROLES_RELATIONSHIP_USER_ID_idx` (`user_id`),
  KEY `FK_USERS_ROLES_RELATIONSHIP_ROLE_ID_idx` (`role_id`),
  CONSTRAINT `FK_USERS_ROLES_RELATIONSHIP_ROLE_ID` FOREIGN KEY (`role_id`) REFERENCES `sys_roles` (`role_id`) ON UPDATE NO ACTION,
  CONSTRAINT `FK_USERS_ROLES_RELATIONSHIP_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `sys_users` (`user_id`) ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_users_roles_relationship`
--

LOCK TABLES `sys_users_roles_relationship` WRITE;
/*!40000 ALTER TABLE `sys_users_roles_relationship` DISABLE KEYS */;
INSERT INTO `sys_users_roles_relationship` VALUES (5,1,1),(39,2,4),(40,5,4),(41,6,4),(42,7,4),(43,5,3),(44,6,3),(45,13,3),(116,8,5),(117,9,5),(118,10,5),(119,11,5),(120,12,5),(121,14,5),(122,17,5),(123,18,5),(124,19,5),(125,20,5),(126,21,5),(127,22,5),(128,23,5),(129,24,6);
/*!40000 ALTER TABLE `sys_users_roles_relationship` ENABLE KEYS */;
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
