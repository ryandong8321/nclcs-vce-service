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
-- Table structure for table `app_tutor_appointment_assignment_tutor`
--

DROP TABLE IF EXISTS `app_tutor_appointment_assignment_tutor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_tutor_appointment_assignment_tutor` (
  `appointment_tutor_id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_tutor_student_id` int(11) NOT NULL,
  `appointment_tutor_original_tutor_id` int(11) NOT NULL,
  `appointment_tutor_target_tutor_id` int(11) NOT NULL,
  `appointment_tutor_assignment_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `appointment_tutor_appointment_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `appointment_tutor_download_time` timestamp NULL DEFAULT NULL,
  `appointment_tutor_assignment_path` varchar(128) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`appointment_tutor_id`),
  UNIQUE KEY `appointment_tutor_id_UNIQUE` (`appointment_tutor_id`),
  KEY `FK_APP_TUTOR_APPOINTMENT_ASSIGNMENT_TUTOR_STUDENT_ID_idx` (`appointment_tutor_student_id`),
  KEY `FK_APP_TUTOR_APPOINTMENT_ASSIGNMENT_TUTOR_ORIGINAL_TUTOR_ID_idx` (`appointment_tutor_original_tutor_id`),
  KEY `FK_APP_TUTOR_APPOINTMENT_ASSIGNMENT_TUTOR_TARGET_TUTOR_ID_idx` (`appointment_tutor_target_tutor_id`),
  CONSTRAINT `FK_APP_TUTOR_APPOINTMENT_ASSIGNMENT_TUTOR_ORIGINAL_TUTOR_ID` FOREIGN KEY (`appointment_tutor_original_tutor_id`) REFERENCES `sys_users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_APP_TUTOR_APPOINTMENT_ASSIGNMENT_TUTOR_STUDENT_ID` FOREIGN KEY (`appointment_tutor_student_id`) REFERENCES `sys_users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_APP_TUTOR_APPOINTMENT_ASSIGNMENT_TUTOR_TARGET_TUTOR_ID` FOREIGN KEY (`appointment_tutor_target_tutor_id`) REFERENCES `sys_users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_tutor_appointment_assignment_tutor`
--

LOCK TABLES `app_tutor_appointment_assignment_tutor` WRITE;
/*!40000 ALTER TABLE `app_tutor_appointment_assignment_tutor` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_tutor_appointment_assignment_tutor` ENABLE KEYS */;
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
