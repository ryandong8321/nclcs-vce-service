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

--
-- Dumping data for table `sys_users`
--

LOCK TABLES `sys_users` WRITE;
/*!40000 ALTER TABLE `sys_users` DISABLE KEYS */;
INSERT INTO `sys_users` VALUES (1,'ryan','a8c66bdd478e81440036b0a1b56c648b','瑞恩','Ryan',NULL,NULL,'12345671','123@123.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'giggs','28c8edde3d61a0411511d3b1866f0636','教师','tutor',NULL,NULL,'12345678','1234@123.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,'rooney','665f644e43731ff9db3d341da5c827e1','校区助理','assistant',NULL,NULL,'123456791','222@123.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,'测试用户','665f644e43731ff9db3d341da5c827e1','教师也是校区助理','',NULL,NULL,'00000002222','111@123.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,'测试上课教师','d9b1d7db4cd6e70935368a1efb10e377','测试上课教师','',NULL,NULL,'12345679','123@123.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,'TestStudent','d9b1d7db4cd6e70935368a1efb10e377','测试学生','Michael Li','1','2212','1234567890','TestLi@gmail.com','4','5',11,'3222',NULL,NULL,NULL,NULL,'Box Hill','周六 VCE Unit 1 & 2 第一语言班'),(9,'nick','873617b13e37ed8f7555877af676d66b','测试学生在Thornbury','Thornbury nick',NULL,NULL,'12345679','123@123.com',NULL,NULL,NULL,NULL,NULL,NULL,'2016-01-31 23:53:06',NULL,'Box Hill','周五 VCE 预备班'),(10,'dick','665f644e43731ff9db3d341da5c827e1','测试群组关系的学生','Test Dick','','','1234567890','TestDick@gmail.com','','',10,'',NULL,NULL,NULL,NULL,'ThornBury H.S.','rooney 上课'),(11,'newusersfortestgroupautosave','7a3e21d444f36b7a6acc7eba381a3ce9','新用户学生','New Users','','','123123123','123@123.com','','',10,'',NULL,NULL,NULL,NULL,'Balwyn H.S.','Balwyn VCE UNIT 3 & 4 第二语言高级'),(12,'teststudent','28c8edde3d61a0411511d3b1866f0636','测试的学生','Test Student',NULL,NULL,'12345','123@123.com',NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-03 09:09:04',NULL,'Box Hill','周六 VCE Unit 1 & 2 第一语言班'),(13,'testnotin','28c8edde3d61a0411511d3b1866f0636','测试不在','Test Notin',NULL,NULL,'123456','123@123.com',NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-03 09:13:12',NULL,NULL,NULL),(14,'testnewuser','d9b1d7db4cd6e70935368a1efb10e377','新的用户','New User','Xin De Yong Hong','00000000','123456','123@321.com','1/20 Pendle st Box Hill VIC 3128','Box Hill Primary School',10,'G2',NULL,NULL,'2016-02-10 01:13:07',NULL,'Box Hill','周五 VCE 预备班'),(17,'testnewuser_1','d9b1d7db4cd6e70935368a1efb10e377','新的用户123','New User','Xin De Yong Hong','00000000','1234561','123@321.com','1/20 Pendle st Box Hill VIC 3128','Box Hill Primary School',10,'G2',NULL,NULL,'2016-02-10 09:19:15',NULL,'Box Hill','周六 VCE Unit 1 & 2 第一语言班'),(18,'testnewuser_2','d9b1d7db4cd6e70935368a1efb10e377','新的用户','New User','Xin De Yong Hong','00000000','123456','123@321.com','1/20 Pendle st Box Hill VIC 3128','Box Hill Primary School',10,'G2',NULL,NULL,'2016-02-10 09:22:25',NULL,'ThornBury H.S.','周日 VCE Unit 3 & 4 第二语言班'),(19,'testnewuser_3','d9b1d7db4cd6e70935368a1efb10e377','新的用户3','New User','Xin De Yong Hong','00000000','123456','123@321.com','1/20 Pendle st Box Hill VIC 3128','Box Hill Primary School',10,'G2',NULL,NULL,'2016-02-14 02:06:54',NULL,'ThornBury H.S.','周日 VCE Unit 3 & 4 第二语言班'),(20,'testnewuser_4','d9b1d7db4cd6e70935368a1efb10e377','新的用户4','New User','Xin De Yong Hong','00000000','123456','123@321.com','1/20 Pendle st Box Hill VIC 3128','Box Hill Primary School',10,'G2',NULL,NULL,'2016-02-14 02:07:29',NULL,'ThornBury H.S.','周日 VCE Unit 3 & 4 第二语言班'),(21,'testnewuser_5','d9b1d7db4cd6e70935368a1efb10e377','新的用户5','New User','Xin De Yong Hong','00000000','123456','123@321.com','1/20 Pendle st Box Hill VIC 3128','Box Hill Primary School',10,'G2',NULL,NULL,'2016-02-14 02:07:42',NULL,'Balwyn H.S.','Balwyn VCE UNIT 3 & 4 第二语言高级'),(22,'testnewuser_6','d9b1d7db4cd6e70935368a1efb10e377','新的用户','New User','Xin De Yong Hong','00000000','123456','123@321.com','1/20 Pendle st Box Hill VIC 3128','Box Hill Primary School',10,'G2',NULL,NULL,'2016-02-19 03:55:15',NULL,'ThornBury H.S.','rooney 上课'),(23,'testnewuser_7','d9b1d7db4cd6e70935368a1efb10e377','新的用户','New User','Xin De Yong Hong','00000000','123456','123@321.com','1/20 Pendle st Box Hill VIC 3128','Box Hill Primary School',10,'G2',NULL,NULL,'2016-02-19 03:55:54',NULL,'ThornBury H.S.','周日 VCE Unit 3 & 4 第二语言班'),(24,'testparent','d9b1d7db4cd6e70935368a1efb10e377','新的家长','New Parent','Xin De Jia Zhang','1234565','123456','123@321.com','1/20 Pendle st Box Hill VIC 3128',NULL,NULL,NULL,NULL,17,'2016-02-21 02:08:40',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_users` ENABLE KEYS */;
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
