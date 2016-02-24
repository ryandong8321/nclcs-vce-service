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
-- Table structure for table `sys_users_groups_relationship`
--

DROP TABLE IF EXISTS `sys_users_groups_relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_users_groups_relationship` (
  `relationship_id` int(11) NOT NULL AUTO_INCREMENT,
  `relationship_user_id` int(11) NOT NULL,
  `relationship_group_id` int(11) NOT NULL,
  PRIMARY KEY (`relationship_id`),
  UNIQUE KEY `relationship_id_UNIQUE` (`relationship_id`),
  KEY `FK_USERS_GROUPS_RELATIONSHIP_USER_ID_idx` (`relationship_user_id`),
  KEY `FK_USERS_GROUPS_RELATIONSHIP_GROUP_ID_idx` (`relationship_group_id`),
  CONSTRAINT `FK_USERS_GROUPS_RELATIONSHIP_GROUP_ID` FOREIGN KEY (`relationship_group_id`) REFERENCES `sys_groups` (`group_id`) ON UPDATE NO ACTION,
  CONSTRAINT `FK_USERS_GROUPS_RELATIONSHIP_USER_ID` FOREIGN KEY (`relationship_user_id`) REFERENCES `sys_users` (`user_id`) ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=238 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_users_groups_relationship`
--

LOCK TABLES `sys_users_groups_relationship` WRITE;
/*!40000 ALTER TABLE `sys_users_groups_relationship` DISABLE KEYS */;
INSERT INTO `sys_users_groups_relationship` VALUES (1,1,1),(4,5,2),(9,6,3),(46,5,4),(187,21,17),(188,11,17),(216,5,11),(217,9,11),(218,14,11),(225,5,16),(226,10,16),(227,22,16),(228,7,15),(229,18,15),(230,20,15),(231,19,15),(232,23,15),(233,2,12),(234,8,12),(235,12,12),(236,17,12),(237,24,12);
/*!40000 ALTER TABLE `sys_users_groups_relationship` ENABLE KEYS */;
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
