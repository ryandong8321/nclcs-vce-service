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

--
-- Dumping data for table `sys_notification`
--

LOCK TABLES `sys_notification` WRITE;
/*!40000 ALTER TABLE `sys_notification` DISABLE KEYS */;
INSERT INTO `sys_notification` VALUES (15,'发通知','发通知\r\n发通知','2016-01-31 07:10:48',1,NULL,NULL,'2,11,12,3,15,16',0),(17,'我还没发呢','我还没发呢','2016-01-31 09:28:45',1,NULL,NULL,'2,11,12',1),(18,'新用户 同学转班通知','新用户 同学由 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班班 转至 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班班\n 特此通知','2016-02-03 07:01:42',1,NULL,NULL,'2,12,3,15',1),(19,'测试群组关系的学生 同学转班通知','测试群组关系的学生 同学由 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班 转至 ThornBury H.S. 校区－rooney 上课\n特此通知','2016-02-14 05:01:01',1,NULL,NULL,'3,16,2,12',1),(20,'新用户学生 同学转班通知','新用户学生 同学由 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班 转至 Balwyn H.S. 校区－Balwyn VCE UNIT 3 & 4 第二语言高级\n特此通知','2016-02-14 05:02:42',1,NULL,NULL,'4,17,2,12',1),(21,'新的用户3 同学转班通知','新的用户3 同学由 Box Hill 校区－周五 VCE 预备班 转至 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班\n特此通知','2016-02-14 05:20:55',1,NULL,NULL,'3,15,2,11',1),(22,'新的用户1 同学转班通知','新的用户1 同学由 ThornBury H.S. 校区－rooney 上课 转至 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班\n特此通知','2016-02-14 06:18:40',1,NULL,NULL,'2,12,3,16',1),(23,'新的用户1 同学转班通知','新的用户1 同学由 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班 转至 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班\n特此通知','2016-02-14 06:19:43',1,NULL,NULL,'3,15,2,12',1),(24,'新的用户1 同学转班通知','新的用户1 同学由 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班 转至 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班\n特此通知','2016-02-14 06:21:17',1,NULL,NULL,'2,12,3,15',1),(25,'新的用户1111 同学转班通知','新的用户1111 同学由 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班 转至 ThornBury H.S. 校区－rooney 上课\n特此通知','2016-02-14 06:22:56',1,NULL,NULL,'3,16,2,12',1),(26,'新的用户1111 同学转班通知','新的用户1111 同学由 ThornBury H.S. 校区－rooney 上课 转至 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班\n特此通知','2016-02-14 06:24:10',1,NULL,NULL,'3,15,16',1),(27,'新的用户12 同学转班通知','新的用户12 同学由 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班 转至 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班\n特此通知','2016-02-14 06:27:40',1,NULL,NULL,'2,12,3,15',1),(28,'新的用户122 同学转班通知','新的用户122 同学由 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班 转至 Balwyn H.S. 校区－Balwyn VCE UNIT 3 & 4 第二语言高级\n特此通知','2016-02-14 06:33:48',1,NULL,NULL,'4,17,2,12',1),(29,'新的用户123 同学转班通知','新的用户123 同学由 Balwyn H.S. 校区－Balwyn VCE UNIT 3 & 4 第二语言高级 转至 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班\n特此通知','2016-02-14 06:41:27',1,NULL,NULL,'3,15,4,17',1),(30,'新的用户123321 同学转班通知','新的用户123321 同学由 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班 转至 Box Hill 校区－周五 VCE 预备班\n特此通知','2016-02-14 06:42:56',1,NULL,NULL,'2,11,3,15',1),(31,'新的用户123 同学转班通知','新的用户123 同学由 Box Hill 校区－周五 VCE 预备班 转至 Balwyn H.S. 校区－Balwyn VCE UNIT 3 & 4 第二语言高级\n特此通知','2016-02-14 08:01:31',1,NULL,NULL,'4,17,2,11',1),(32,'新的用户12321 同学转班通知','新的用户12321 同学由 Balwyn H.S. 校区－Balwyn VCE UNIT 3 & 4 第二语言高级 转至 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班\n特此通知','2016-02-14 08:05:46',1,NULL,NULL,'3,15,4,17',1),(33,'新的用户123 同学转班通知','新的用户123 同学由 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班 转至 Box Hill 校区－周五 VCE 预备班\n特此通知','2016-02-14 08:10:15',1,NULL,NULL,'2,11,3,15',1),(34,'新的用户12321 同学转班通知','新的用户12321 同学由 Box Hill 校区－周五 VCE 预备班 转至 Balwyn H.S. 校区－Balwyn VCE UNIT 3 & 4 第二语言高级\n特此通知','2016-02-14 09:41:48',1,NULL,NULL,'4,17,2,11',1),(35,'新的用户123456 同学转班通知','新的用户123456 同学由 Balwyn H.S. 校区－Balwyn VCE UNIT 3 & 4 第二语言高级 转至 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班\n特此通知','2016-02-14 09:42:55',1,NULL,NULL,'3,15,4,17',1),(36,'新的用户123 同学转班通知','新的用户123 同学由 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班 转至 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班\n特此通知','2016-02-14 09:47:10',1,NULL,NULL,'2,12,3,15',1),(37,'新的用户12321 同学转班通知','新的用户12321 同学由 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班 转至 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班\n特此通知','2016-02-14 09:49:40',1,NULL,NULL,'3,15,2,12',1),(38,'新的用户123 同学转班通知','新的用户123 同学由 ThornBury H.S. 校区－周日 VCE Unit 3 & 4 第二语言班 转至 Box Hill 校区－周五 VCE 预备班\n特此通知','2016-02-14 09:52:42',1,NULL,NULL,'2,11,3,15',1),(39,'新的用户12321 同学转班通知','新的用户12321 同学由 Box Hill 校区－周五 VCE 预备班 转至 ThornBury H.S. 校区－rooney 上课\n特此通知','2016-02-14 09:53:24',1,NULL,NULL,'3,16,2,11',1),(40,'新的用户123 同学转班通知','新的用户123 同学由 ThornBury H.S. 校区－rooney 上课 转至 Box Hill 校区－周六 VCE Unit 1 & 2 第一语言班\n特此通知','2016-02-15 07:03:13',1,NULL,NULL,'2,12,3,16',1),(41,'测试结果','测试结果','2016-02-23 12:47:53',1,NULL,NULL,'2,4,16',1);
/*!40000 ALTER TABLE `sys_notification` ENABLE KEYS */;
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
