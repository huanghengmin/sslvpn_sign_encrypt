-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: localhost    Database: sslvpn
-- ------------------------------------------------------
-- Server version	5.7.17

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `user_name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `sex` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `modifiedPasswordTime` datetime DEFAULT NULL,
  `status` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `depart` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `title` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `start_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `end_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `start_hour` int(11) DEFAULT NULL,
  `end_hour` int(11) DEFAULT NULL,
  `description` text COLLATE utf8_bin,
  `remote_ip` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `mac` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `ip_type` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='账户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin','S8W2gMnH8VWiT9pXRMPQxA==','男','0571-88888888','2010-07-04 13:52:36','2016-05-11 10:49:11',NULL,'有效','信息中心','主任','初始化管理员','xiaom@hzih.net','0.0.0.0','192.168.254.254',0,24,'这是一个默认的超级用户信息','192.168.2.176','5C-63-BF-1D-72-07',1),(2,'authadmin','S8W2gMnH8VWiT9pXRMPQxA==','男','0571-88888888','2012-04-12 14:22:35','2013-05-07 18:27:30',NULL,'有效','信息中心','主任','授权管理员','xiaom@hzih.net','0.0.0.0','192.168.200.254',1,22,'这是一个默认的授权用户信息','',NULL,1),(3,'configadmin','S8W2gMnH8VWiT9pXRMPQxA==','男','0571-88888888','2012-06-12 18:04:01','2013-05-07 18:27:45',NULL,'有效','信息中心','主任','配置管理员','xiaom@hzih.net','0.0.0.0','192.168.200.254',9,23,'这是一个默认的配置用户信息','',NULL,1),(4,'auditadmin','S8W2gMnH8VWiT9pXRMPQxA==','男','0571-88888888','2012-07-03 10:19:57','2014-08-26 13:01:36',NULL,'有效','信息中心','主任','审计管理员','xiaom@hzih.net','0.0.0.0','192.168.200.254',7,18,'这是一个默认的审计用户信息',NULL,NULL,1),(6,'test','S8W2gMnH8VWiT9pXRMPQxA==','男','6556119','2017-05-08 16:38:36','2017-05-08 16:39:49','2017-05-08 16:38:36','有效','信息部','主任','test','hello@hzih.net','0.0.0.0','192.168.1.254',9,18,'这是一个用户信息',NULL,'',1);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_role`
--

DROP TABLE IF EXISTS `account_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_role` (
  `account_id` bigint(20) NOT NULL DEFAULT '0',
  `role_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`account_id`,`role_id`),
  KEY `FK410D03481FCE46BD` (`role_id`),
  KEY `FK410D034811351AF7` (`account_id`),
  KEY `FK410D0348A472BB1A` (`role_id`),
  KEY `FK410D0348CEF66D7A` (`account_id`),
  KEY `FK410D034824B3696E` (`role_id`),
  KEY `FK410D0348B5F52EA6` (`account_id`),
  CONSTRAINT `FK410D034811351AF7` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK410D0348A472BB1A` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_role`
--

LOCK TABLES `account_role` WRITE;
/*!40000 ALTER TABLE `account_role` DISABLE KEYS */;
INSERT INTO `account_role` VALUES (1,1),(2,2),(3,3),(4,4),(6,6);
/*!40000 ALTER TABLE `account_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backup`
--

DROP TABLE IF EXISTS `backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `backup` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `backup_account_id` bigint(20) DEFAULT '0',
  `backup_time` varchar(50) DEFAULT NULL,
  `backup_all` tinyint(4) DEFAULT '0',
  `backup_server` tinyint(4) DEFAULT '0',
  `backup_pki` tinyint(4) DEFAULT '0',
  `backup_net` tinyint(4) DEFAULT '0',
  `backup_file` varchar(50) DEFAULT NULL,
  `backup_desc` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_backup_account` (`backup_account_id`),
  CONSTRAINT `FK_backup_account` FOREIGN KEY (`backup_account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backup`
--

LOCK TABLES `backup` WRITE;
/*!40000 ALTER TABLE `backup` DISABLE KEYS */;
/*!40000 ALTER TABLE `backup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_log`
--

DROP TABLE IF EXISTS `equipment_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equipment_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `level` varchar(10) DEFAULT NULL,
  `log_time` datetime DEFAULT NULL,
  `equipment_name` varchar(255) DEFAULT NULL,
  `log_info` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_log`
--

LOCK TABLES `equipment_log` WRITE;
/*!40000 ALTER TABLE `equipment_log` DISABLE KEYS */;
INSERT INTO `equipment_log` VALUES (1,'WARN','2017-05-05 16:50:19','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(2,'WARN','2017-05-05 16:50:19','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(3,'WARN','2017-05-05 17:28:20','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(4,'WARN','2017-05-05 17:28:20','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(5,'WARN','2017-05-05 19:05:31','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(6,'WARN','2017-05-05 19:05:31','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(7,'WARN','2017-05-05 19:42:35','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(8,'WARN','2017-05-05 19:42:36','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(9,'WARN','2017-05-05 20:55:13','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(10,'WARN','2017-05-05 20:55:13','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(11,'WARN','2017-05-08 16:21:50','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(12,'WARN','2017-05-08 16:21:50','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(13,'WARN','2017-05-12 11:36:04','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(14,'WARN','2017-05-12 11:36:05','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(15,'WARN','2017-05-15 14:30:45','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(16,'WARN','2017-05-15 14:30:45','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(17,'WARN','2017-05-15 14:49:31','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(18,'WARN','2017-05-15 14:49:31','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(19,'WARN','2017-05-15 15:12:44','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(20,'WARN','2017-05-15 15:12:44','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(21,'WARN','2017-05-15 17:42:49','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(22,'WARN','2017-05-15 17:42:49','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(23,'WARN','2017-05-16 08:59:54','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(24,'WARN','2017-05-16 08:59:54','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(25,'WARN','2017-05-16 09:06:42','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(26,'WARN','2017-05-16 09:06:42','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(27,'WARN','2017-05-16 09:20:32','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(28,'WARN','2017-05-16 09:20:32','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(29,'WARN','2017-05-16 09:43:29','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(30,'WARN','2017-05-16 09:43:29','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(31,'WARN','2017-05-16 09:49:50','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(32,'WARN','2017-05-16 09:49:50','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(33,'WARN','2017-05-16 09:54:05','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(34,'WARN','2017-05-16 09:54:05','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(35,'WARN','2017-05-16 10:01:02','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(36,'WARN','2017-05-16 10:01:02','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(37,'WARN','2017-05-16 10:04:18','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(38,'WARN','2017-05-16 10:04:18','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(39,'WARN','2017-05-16 10:16:19','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(40,'WARN','2017-05-16 10:16:19','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(41,'WARN','2017-05-16 11:06:32','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(42,'WARN','2017-05-16 11:06:32','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(43,'WARN','2017-05-16 11:11:38','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(44,'WARN','2017-05-16 11:11:38','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(45,'WARN','2017-05-16 11:13:58','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(46,'WARN','2017-05-16 11:13:58','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(47,'WARN','2017-05-16 11:17:10','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(48,'WARN','2017-05-16 11:17:10','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(49,'WARN','2017-05-16 16:34:00','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(50,'WARN','2017-05-16 16:34:00','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(51,'WARN','2017-05-17 09:00:09','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(52,'WARN','2017-05-17 09:00:09','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(53,'WARN','2017-05-17 09:32:21','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(54,'WARN','2017-05-17 09:32:21','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(55,'WARN','2017-05-17 13:08:13','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(56,'WARN','2017-05-17 13:08:13','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(57,'WARN','2017-05-17 13:12:47','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(58,'WARN','2017-05-17 13:12:47','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(59,'WARN','2017-05-17 13:38:01','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(60,'WARN','2017-05-17 13:38:01','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(61,'WARN','2017-05-17 13:41:17','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(62,'WARN','2017-05-17 13:41:17','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(63,'WARN','2017-05-17 13:43:18','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(64,'WARN','2017-05-17 13:43:19','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(65,'WARN','2017-05-17 13:48:50','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(66,'WARN','2017-05-17 13:48:50','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(67,'WARN','2017-05-17 13:59:15','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(68,'WARN','2017-05-17 13:59:15','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(69,'WARN','2017-05-17 14:37:11','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(70,'WARN','2017-05-17 14:37:11','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(71,'WARN','2017-05-17 14:41:30','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(72,'WARN','2017-05-17 14:41:30','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(73,'WARN','2017-05-17 14:44:38','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(74,'WARN','2017-05-17 14:44:38','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(75,'WARN','2017-05-17 14:46:32','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(76,'WARN','2017-05-17 14:46:32','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(77,'WARN','2017-05-17 14:54:57','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(78,'WARN','2017-05-17 14:54:57','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(79,'WARN','2017-05-19 15:57:48','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(80,'WARN','2017-05-19 15:57:48','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null'),(81,'WARN','2017-05-23 11:20:45','sslvpn','硬盘更改:原硬盘:Disk /dev/sda: 8012 MB,原硬盘序列号:Disk identifier: 0xc948c9d3,更改为:硬盘:null,序列号:null'),(82,'WARN','2017-05-23 11:20:45','sslvpn','内存更改:原内存容量:1865492kB,更改为:现有容量:null');
/*!40000 ALTER TABLE `equipment_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_source_nets`
--

DROP TABLE IF EXISTS `group_source_nets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_source_nets` (
  `source_net_id` int(11) NOT NULL DEFAULT '0',
  `group_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`source_net_id`,`group_id`),
  KEY `FK46FE425AD95A9C2F` (`source_net_id`),
  KEY `FK46FE425ABD119723` (`group_id`),
  CONSTRAINT `FK_group_source_nets_groups` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `FK_group_source_nets_source_nets` FOREIGN KEY (`source_net_id`) REFERENCES `source_nets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_source_nets`
--

LOCK TABLES `group_source_nets` WRITE;
/*!40000 ALTER TABLE `group_source_nets` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_source_nets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deny_access` tinyint(4) DEFAULT '0',
  `group_name` varchar(50) DEFAULT NULL,
  `group_desc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (1,0,'一级组','一级组'),(2,0,'二级组','二级组'),(3,0,'三级组','三级组');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cn` varchar(100) DEFAULT NULL,
  `serial_number` varchar(100) DEFAULT NULL,
  `subject_dn` varchar(200) DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `trusted_ip` varchar(50) DEFAULT NULL,
  `trusted_port` int(11) DEFAULT NULL,
  `protocol` varchar(16) DEFAULT NULL,
  `remote_ip` varchar(50) DEFAULT NULL,
  `remote_netmask` varchar(50) DEFAULT NULL,
  `bytes_received` bigint(20) DEFAULT '0',
  `bytes_sent` bigint(20) DEFAULT '0',
  `status` tinyint(4) DEFAULT '0',
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `ID` bigint(20) NOT NULL DEFAULT '0',
  `CODE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `SEQ` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (100,'TOP_QXGL','权限管理',NULL,0,0),(101,'SECOND_YHGL','用户管理',NULL,100,1),(102,'SECOND_JSGL','角色管理',NULL,100,2),(103,'SECOND_AQCL','安全策略',NULL,100,3),(110,'TOP_WLGL','网络管理',NULL,0,0),(111,'SECOND_JKGL','接口管理',NULL,110,1),(112,'SECOND_LTCS','连通测试',NULL,110,2),(113,'SECOND_LYGL','路由管理',NULL,110,3),(114,'SECOND_PZGL','安全配置',NULL,110,4),(120,'TOP_XTGL','系统管理',NULL,0,0),(121,'SECOND_XTGL','系统管理',NULL,120,1),(122,'SECOND_ZSGL','证书管理',NULL,120,2),(123,'SECOND_FWGL','服务管理',NULL,120,3),(124,'SECOND_SJRB','双机热备',NULL,120,4),(130,'TOP_SJGL','审计管理',NULL,0,0),(131,'SECOND_GLRZ','管理日志',NULL,130,1),(132,'SECOND_RZXZ','日志下载',NULL,130,2),(133,'SECOND_RZZJ','日志主机',NULL,130,3),(140,'TOP_BJGL','报警管理',NULL,0,0),(141,'SECOND_SBBJ','设备报警',NULL,140,1),(150,'TOP_ZYGL','资源管理',NULL,0,0),(151,'SECOND_ZWZY','子网资源',NULL,150,1),(160,'TOP_FWGL','服务管理',NULL,0,0),(161,'SECOND_FWZT','服务状态',NULL,160,1),(162,'SECOND_JBPZ','基本配置',NULL,160,2),(163,'SECOND_ZSPZ','证书配置',NULL,160,3),(170,'TOP_ZDGL','终端管理',NULL,0,0),(171,'SECOND_ZDYH','终端用户',NULL,170,1),(172,'SECOND_ZDFZ','终端分组',NULL,170,2),(173,'SECOND_ZDZX','终端在线',NULL,170,3),(174,'SECOND_ZDRZ','终端日志',NULL,170,4),(175,'SECOND_ZDJL','终端记录',NULL,170,5),(180,'TOP_XTPZ','系统配置',NULL,0,0),(181,'SECOND_CYPZ','策略配置',NULL,180,1),(182,'SECOND_SJPZ','时间配置',NULL,180,2),(190,'TOP_DXGL','吊销管理',NULL,0,0),(191,'SECOND_DXLB','吊销列表',NULL,190,1),(192,'SECOND_DXWJ','吊销文件',NULL,190,2),(193,'SECOND_DXGX','吊销更新',NULL,190,3),(200,'TOP_JKGL','监控管理',NULL,0,0),(201,'SECOND_ZJJK','主机监控',NULL,200,1),(202,'SECOND_JKBJ','监控报警',NULL,200,2),(210,'TOP_BBSJ','版本升级',NULL,0,0),(211,'SECOND_KFBB','客户版本',NULL,210,1),(212,'SECOND_FWBB','服务版本',NULL,210,2),(213,'SECOND_BFHF','备份恢复',NULL,210,3);
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'初始化管理员','初始化管理员','2010-07-04 15:07:08','2016-04-26 17:36:28'),(2,'授权管理员','授权管理员','2012-07-03 10:06:20','2015-12-31 13:09:06'),(3,'配置管理员','配置管理员','2012-03-14 12:33:05','2015-12-31 13:10:43'),(4,'审计管理员','审计管理员','2012-06-12 18:37:24','2015-12-31 13:09:46'),(6,'测试管理员','测试管理员','2017-05-08 16:34:22',NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `permission_id` bigint(20) NOT NULL DEFAULT '0',
  `role_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`permission_id`,`role_id`),
  KEY `FKBD40D53851BABF58` (`role_id`),
  KEY `FKBD40D53852A81638` (`permission_id`),
  KEY `FKBD40D538A472BB1A` (`role_id`),
  KEY `FKBD40D53852B0B87A` (`permission_id`),
  KEY `FKBD40D53824B3696E` (`role_id`),
  KEY `FKBD40D5387AC257CE` (`permission_id`),
  CONSTRAINT `FKBD40D5387AC257CE` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`ID`),
  CONSTRAINT `FKBD40D538A472BB1A` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (100,1),(101,1),(102,1),(103,1),(110,1),(111,1),(112,1),(113,1),(114,1),(120,1),(121,1),(122,1),(123,1),(124,1),(130,1),(131,1),(132,1),(133,1),(140,1),(141,1),(150,1),(151,1),(160,1),(161,1),(162,1),(163,1),(170,1),(171,1),(172,1),(173,1),(174,1),(175,1),(180,1),(181,1),(182,1),(190,1),(191,1),(192,1),(193,1),(200,1),(201,1),(202,1),(210,1),(211,1),(212,1),(213,1),(170,2),(171,2),(172,2),(110,3),(111,3),(112,3),(113,3),(114,3),(120,3),(121,3),(122,3),(123,3),(124,3),(130,3),(133,3),(150,3),(151,3),(160,3),(161,3),(162,3),(163,3),(180,3),(181,3),(182,3),(190,3),(191,3),(192,3),(193,3),(200,3),(202,3),(210,3),(211,3),(212,3),(213,3),(130,4),(131,4),(132,4),(140,4),(141,4),(170,4),(173,4),(174,4),(175,4),(200,4),(201,4),(100,6),(101,6),(102,6),(103,6),(110,6),(111,6),(112,6),(113,6),(114,6),(120,6),(121,6),(122,6),(123,6),(124,6),(130,6),(131,6),(132,6),(133,6),(140,6),(141,6),(150,6),(151,6),(160,6),(161,6),(162,6),(163,6),(170,6),(171,6),(172,6),(173,6),(174,6),(175,6),(180,6),(181,6),(182,6),(190,6),(191,6),(192,6),(193,6),(200,6),(201,6),(202,6);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `safe_policy`
--

DROP TABLE IF EXISTS `safe_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `safe_policy` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `timeout` int(11) DEFAULT NULL,
  `passwordLength` int(11) DEFAULT NULL,
  `errorLimit` int(11) DEFAULT NULL,
  `remoteDisabled` tinyint(1) DEFAULT NULL,
  `macDisabled` tinyint(1) DEFAULT NULL,
  `passwordRules` varchar(255) DEFAULT NULL,
  `lockTime` int(10) NOT NULL DEFAULT '24' COMMENT '锁定时间(小时)',
  `maxSession` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='安全策略表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `safe_policy`
--

LOCK TABLES `safe_policy` WRITE;
/*!40000 ALTER TABLE `safe_policy` DISABLE KEYS */;
INSERT INTO `safe_policy` VALUES (1,1800,0,3,0,0,'^[0-9a-zA-Z!$#%@^&amp;amp;amp;amp;amp;amp;amp;*()~_+]{8,20}$',1,NULL);
/*!40000 ALTER TABLE `safe_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server`
--

DROP TABLE IF EXISTS `server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server` (
  `id` int(11) NOT NULL,
  `listen` varchar(50) DEFAULT NULL,
  `port` int(11) DEFAULT '1194',
  `protocol` varchar(11) DEFAULT 'udp',
  `server_net` varchar(30) DEFAULT '10.8.0.0',
  `server_mask` varchar(30) DEFAULT '255.255.255.0',
  `check_crl` int(11) DEFAULT '1',
  `traffic_server` int(11) DEFAULT '0',
  `client_to_client` int(11) DEFAULT '1',
  `duplicate_cn` int(11) DEFAULT '0',
  `keep_alive` int(11) DEFAULT '60',
  `keep_alive_interval` int(11) DEFAULT '10',
  `cipher` varchar(50) DEFAULT 'DES-EDE3-CBC',
  `comp_lzo` int(11) DEFAULT '1',
  `max_clients` int(11) DEFAULT '300',
  `log_append` int(11) DEFAULT '1',
  `log_flag` int(11) DEFAULT '1',
  `verb` int(11) DEFAULT '3',
  `mute` int(11) DEFAULT '5',
  `client_dns_type` int(11) DEFAULT '0',
  `client_first_dns` varchar(50) DEFAULT NULL,
  `client_second_dns` varchar(50) DEFAULT NULL,
  `default_domain_suffix` varchar(50) DEFAULT 'sslvpn',
  `use_connect_script` int(11) DEFAULT '1',
  `use_disconnect_script` int(11) DEFAULT '1',
  `use_learn_address_script` int(11) DEFAULT '0',
  `local` varchar(50) DEFAULT NULL,
  `server` varchar(50) DEFAULT NULL,
  `dynamic_net` varchar(100) DEFAULT NULL,
  `static_net` varchar(100) DEFAULT NULL,
  `group_default_net` varchar(250) DEFAULT NULL,
  `private_net` varchar(250) DEFAULT NULL,
  `allow_ping_server` int(11) DEFAULT NULL,
  `allow_private_net` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server`
--

LOCK TABLES `server` WRITE;
/*!40000 ALTER TABLE `server` DISABLE KEYS */;
INSERT INTO `server` VALUES (1,'192.168.3.120',1194,'udp','11.8.0.0','255.255.255.0',0,0,1,0,60,10,'DES-EDE3-CBC',1,5000,1,1,3,5,0,'','','sslvpn.com',1,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server_certificate`
--

DROP TABLE IF EXISTS `server_certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server_certificate` (
  `id` int(11) NOT NULL,
  `certificate` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `pwd` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server_certificate`
--

LOCK TABLES `server_certificate` WRITE;
/*!40000 ALTER TABLE `server_certificate` DISABLE KEYS */;
/*!40000 ALTER TABLE `server_certificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server_source_nets`
--

DROP TABLE IF EXISTS `server_source_nets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server_source_nets` (
  `source_net_id` int(11) NOT NULL,
  `server_id` int(11) NOT NULL,
  PRIMARY KEY (`source_net_id`,`server_id`),
  KEY `FK275C751ED95A9C2F` (`source_net_id`),
  KEY `FK275C751EA6E2CD4E` (`server_id`),
  CONSTRAINT `FK275C751EA6E2CD4E` FOREIGN KEY (`server_id`) REFERENCES `server` (`id`),
  CONSTRAINT `FK275C751ED95A9C2F` FOREIGN KEY (`source_net_id`) REFERENCES `source_nets` (`id`),
  CONSTRAINT `FK_server_source_nets_server` FOREIGN KEY (`server_id`) REFERENCES `server` (`id`),
  CONSTRAINT `FK_server_source_nets_source_nets` FOREIGN KEY (`source_net_id`) REFERENCES `source_nets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server_source_nets`
--

LOCK TABLES `server_source_nets` WRITE;
/*!40000 ALTER TABLE `server_source_nets` DISABLE KEYS */;
/*!40000 ALTER TABLE `server_source_nets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `source_nets`
--

DROP TABLE IF EXISTS `source_nets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `source_nets` (
  `id` int(11) NOT NULL,
  `net` varchar(30) DEFAULT NULL,
  `net_mask` varchar(30) DEFAULT NULL,
  `level` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `source_nets`
--

LOCK TABLES `source_nets` WRITE;
/*!40000 ALTER TABLE `source_nets` DISABLE KEYS */;
/*!40000 ALTER TABLE `source_nets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `static_ip`
--

DROP TABLE IF EXISTS `static_ip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `static_ip` (
  `client_end` int(11) NOT NULL,
  `sever_end` int(11) DEFAULT NULL,
  PRIMARY KEY (`client_end`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `static_ip`
--

LOCK TABLES `static_ip` WRITE;
/*!40000 ALTER TABLE `static_ip` DISABLE KEYS */;
INSERT INTO `static_ip` VALUES (1,2),(5,6),(9,10),(13,14),(17,18),(21,22),(25,26),(29,30),(33,34),(37,38),(41,42),(45,46),(49,50),(53,54),(57,58),(61,62),(65,66),(69,70),(73,74),(77,78),(81,82),(85,86),(89,90),(93,94),(97,98),(101,102),(105,106),(109,110),(113,114),(117,118),(121,122),(125,126),(129,130),(133,134),(137,138),(141,142),(145,146),(149,150),(153,154),(157,158),(161,162),(165,166),(169,170),(173,174),(177,178),(181,182),(185,186),(189,190),(193,194),(197,198),(201,202),(205,206),(209,210),(213,214),(217,218),(221,222),(225,226),(229,230),(233,234),(237,238),(241,242),(245,246),(249,250),(253,254);
/*!40000 ALTER TABLE `static_ip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trust_certificate`
--

DROP TABLE IF EXISTS `trust_certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trust_certificate` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `file` varchar(50) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `subject` varchar(200) DEFAULT NULL,
  `notbefore` varchar(50) DEFAULT NULL,
  `notafter` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trust_certificate`
--

LOCK TABLES `trust_certificate` WRITE;
/*!40000 ALTER TABLE `trust_certificate` DISABLE KEYS */;
/*!40000 ALTER TABLE `trust_certificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cn` varchar(100) NOT NULL DEFAULT '',
  `subject` varchar(200) DEFAULT '',
  `dynamic_ip` int(11) DEFAULT '1',
  `static_ip` varchar(30) DEFAULT '',
  `allow_all_client` int(11) DEFAULT '1',
  `serial_number` varchar(50) DEFAULT '',
  `enabled` int(11) DEFAULT '1',
  `real_address` varchar(30) DEFAULT '',
  `byte_received` bigint(20) DEFAULT '0',
  `byte_send` bigint(20) DEFAULT '0',
  `connected_since` datetime DEFAULT NULL,
  `virtual_address` varchar(50) DEFAULT '',
  `last_ref` datetime DEFAULT NULL,
  `net_id` varchar(30) DEFAULT '',
  `terminal_id` varchar(30) DEFAULT '',
  `description` varchar(30) DEFAULT '',
  `view_flag` int(11) DEFAULT '0',
  `gps_flag` int(11) DEFAULT '0',
  `id_card` varchar(100) DEFAULT NULL,
  `deny_access` int(11) DEFAULT NULL,
  `allow_all_subnet` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `quota_cycle` int(11) DEFAULT NULL,
  `quota_bytes` bigint(20) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `revoked` int(11) DEFAULT NULL,
  `count_bytes_cycle` bigint(20) DEFAULT NULL,
  `max_bytes` bigint(20) DEFAULT NULL,
  `issueCa` varchar(200) DEFAULT NULL,
  `orgCode` varchar(30) DEFAULT NULL,
  `orgName` varchar(30) DEFAULT NULL,
  `employeeCode` varchar(30) DEFAULT NULL,
  `create_Date` datetime DEFAULT NULL,
  `end_Date` datetime DEFAULT NULL,
  `province` varchar(30) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `organization` varchar(30) DEFAULT NULL,
  `institutions` varchar(30) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  `revoke_status` varchar(30) DEFAULT NULL,
  `download_speed` varchar(255) DEFAULT NULL,
  `upload_speed` varchar(255) DEFAULT NULL,
  `level` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cn` (`cn`),
  UNIQUE KEY `serial_number` (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_gps`
--

DROP TABLE IF EXISTS `user_gps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_gps` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `longitude` varchar(50) DEFAULT NULL,
  `latitude` varchar(50) DEFAULT NULL,
  `readTime` varchar(50) DEFAULT NULL,
  `insertTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF022CE36C9DE2D4E` (`user_id`),
  CONSTRAINT `FKF022CE36C9DE2D4E` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_gps`
--

LOCK TABLES `user_gps` WRITE;
/*!40000 ALTER TABLE `user_gps` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_gps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group` (
  `group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`group_id`,`user_id`),
  KEY `FK72A9010BC9DE2D4E` (`user_id`),
  KEY `FK72A9010BBD119723` (`group_id`),
  CONSTRAINT `FK72A9010BBD119723` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `FK72A9010BC9DE2D4E` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_group`
--

LOCK TABLES `user_group` WRITE;
/*!40000 ALTER TABLE `user_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_oper_log`
--

DROP TABLE IF EXISTS `user_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_oper_log` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_time` datetime DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `audit_module` varchar(255) DEFAULT NULL,
  `audit_info` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=287 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_oper_log`
--

LOCK TABLES `user_oper_log` WRITE;
/*!40000 ALTER TABLE `user_oper_log` DISABLE KEYS */;
INSERT INTO `user_oper_log` VALUES (1,'2017-05-05 17:30:40','INFO','test','用户登录','用户名不存在'),(2,'2017-05-05 17:30:59','INFO','configadmin','用户登录','用户登录成功'),(3,'2017-05-05 17:59:22','INFO','configadmin','同步时间','同步时间成功'),(4,'2017-05-05 17:59:57','INFO','configadmin','资源管理','添加成功'),(5,'2017-05-05 18:00:01','INFO','configadmin','LDAP配置','接入配置保存成功'),(6,'2017-05-05 18:00:07','INFO','configadmin','资源管理','开启绑定成功'),(7,'2017-05-05 18:00:51','INFO','configadmin','资源管理','添加成功'),(8,'2017-05-05 18:02:28','INFO','configadmin','资源管理','关闭绑定成功'),(9,'2017-05-05 18:03:01','INFO','configadmin','资源管理','删除成功'),(10,'2017-05-05 18:03:04','INFO','configadmin','资源管理','删除成功'),(11,'2017-05-05 18:44:48','INFO','configadmin','用户登录','用户登录成功'),(12,'2017-05-05 18:45:14','INFO','configadmin','资源管理','开启绑定成功'),(13,'2017-05-05 18:46:04','INFO','configadmin','LDAP配置','接入配置保存成功'),(14,'2017-05-05 18:46:10','INFO','configadmin','资源管理','开启绑定成功'),(15,'2017-05-05 18:46:42','INFO','configadmin','资源管理','开启绑定成功'),(16,'2017-05-05 18:46:46','INFO','configadmin','LDAP配置','接入配置保存成功'),(17,'2017-05-05 18:49:48','INFO','configadmin','资源管理','关闭绑定失败'),(18,'2017-05-05 18:51:31','INFO','configadmin','资源管理','关闭绑定失败'),(19,'2017-05-05 18:51:43','INFO','configadmin','LDAP配置','接入配置保存成功'),(20,'2017-05-05 18:52:12','INFO','configadmin','资源管理','开启绑定失败'),(21,'2017-05-05 19:06:17','INFO','admin','用户登录','密码错误次数:1'),(22,'2017-05-05 19:06:27','INFO','configadmin','用户登录','用户登录成功'),(23,'2017-05-05 19:09:16','INFO','configadmin','LDAP配置','接入配置保存成功'),(24,'2017-05-05 19:09:21','INFO','configadmin','资源管理','开启绑定成功'),(25,'2017-05-05 19:09:40','INFO','configadmin','资源管理','添加成功'),(26,'2017-05-05 19:09:56','INFO','configadmin','资源管理','删除成功'),(27,'2017-05-05 19:10:12','INFO','configadmin','资源管理','关闭绑定成功'),(28,'2017-05-05 19:10:44','INFO','configadmin','用户登录','用户登录成功'),(29,'2017-05-05 19:11:47','INFO','configadmin','资源管理','开启绑定成功'),(30,'2017-05-05 19:12:08','INFO','configadmin','资源管理','添加成功'),(31,'2017-05-05 19:13:07','INFO','configadmin','资源管理','关闭绑定成功'),(32,'2017-05-05 19:13:37','INFO','configadmin','资源管理','删除成功'),(33,'2017-05-08 16:22:52','INFO','configadmin','用户登录','用户登录成功'),(34,'2017-05-08 16:23:20','INFO','configadmin','LDAP配置','接入配置保存成功'),(35,'2017-05-08 16:25:23','INFO','configadmin','资源管理','开启绑定成功'),(36,'2017-05-08 16:25:41','INFO','configadmin','资源管理','添加成功'),(37,'2017-05-08 16:25:58','INFO','configadmin','资源管理','删除成功'),(38,'2017-05-08 16:31:54','INFO','admin','用户登录','用户登录成功'),(39,'2017-05-08 16:33:43','INFO','admin','角色管理','用户新增角色信息成功'),(40,'2017-05-08 16:34:27','INFO','admin','角色管理','用户新增角色信息成功'),(41,'2017-05-08 16:34:51','INFO','admin','角色管理','用户删除角色信息成功'),(42,'2017-05-08 16:35:50','INFO','admin','用户管理','用户新增账户test信息成功'),(43,'2017-05-08 16:35:55','INFO','admin','用户管理','用户删除账户test信息成功'),(44,'2017-05-08 16:38:36','INFO','admin','用户管理','用户新增账户test信息成功'),(45,'2017-05-08 16:38:54','INFO','admin','用户登录','用户退出成功'),(46,'2017-05-08 16:39:01','INFO','test','用户登录','IP地址不允许访问'),(47,'2017-05-08 16:39:17','INFO','admin','用户登录','用户登录成功'),(48,'2017-05-08 16:39:49','INFO','admin','用户管理','用户修改账户test信息成功'),(49,'2017-05-08 16:39:54','INFO','admin','用户登录','用户退出成功'),(50,'2017-05-08 16:40:03','INFO','test','用户登录','用户登录成功'),(51,'2017-05-08 16:40:22','INFO','test','管理员日志审计','用户读取管理员日志审计信息成功'),(52,'2017-05-08 16:40:54','INFO','test','管理员日志审计','用户读取管理员日志审计信息成功'),(53,'2017-05-08 16:41:15','ERROR','test','接口管理','用户读取接口信息失败 '),(54,'2017-05-08 16:41:17','ERROR','test','接口管理','用户读取接口信息失败 '),(55,'2017-05-08 16:41:21','ERROR','test','接口管理','用户读取接口信息失败 '),(56,'2017-05-08 16:41:25','INFO','test','路由管理','用户读取路由信息成功 '),(57,'2017-05-08 16:41:27','INFO','test','路由管理','用户读取路由信息成功 '),(58,'2017-05-08 16:41:31','INFO','test','配置管理','用户获取管理服务、集控采集数据接口设定IP地址成功 '),(59,'2017-05-08 16:41:31','INFO','test','配置管理','用户获取管理客户机地址成功 '),(60,'2017-05-08 16:41:50','INFO','test','配置管理','更新出错'),(61,'2017-05-08 16:41:52','INFO','test','配置管理','用户获取管理客户机地址成功 '),(62,'2017-05-08 16:41:56','INFO','test','配置管理','用户获取管理服务、集控采集数据接口设定IP地址成功 '),(63,'2017-05-08 16:41:56','INFO','test','配置管理','用户获取管理客户机地址成功 '),(64,'2017-05-08 16:41:59','ERROR','test','接口管理','用户读取接口信息失败 '),(65,'2017-05-08 16:42:02','ERROR','test','接口管理','用户读取可新增接口名失败 '),(66,'2017-05-08 16:42:20','ERROR','test','接口管理','用户读取可新增接口名失败 '),(67,'2017-05-08 16:43:47','INFO','test','资源管理','添加成功'),(68,'2017-05-08 16:44:34','INFO','test','资源管理','删除成功'),(69,'2017-05-08 16:44:43','INFO','test','资源管理','关闭绑定成功'),(70,'2017-05-12 11:36:40','INFO','admin','用户登录','用户登录成功'),(71,'2017-05-15 14:37:11','INFO','configadmin','用户登录','用户登录成功'),(72,'2017-05-15 14:49:48','INFO','configadmin','用户登录','用户登录成功'),(73,'2017-05-15 15:13:17','INFO','configadmin','用户登录','用户登录成功'),(74,'2017-05-15 15:26:32','INFO','configadmin','LDAP配置','接入配置保存成功'),(75,'2017-05-15 17:43:16','INFO','configadmin','用户登录','用户登录成功'),(76,'2017-05-16 09:00:42','INFO','configadmin','用户登录','用户登录成功'),(77,'2017-05-16 09:07:00','INFO','configadmin','用户登录','用户登录成功'),(78,'2017-05-16 09:15:07','INFO','configadmin','用户登录','用户登录成功'),(79,'2017-05-16 09:20:48','INFO','configadmin','用户登录','用户登录成功'),(80,'2017-05-16 09:21:37','INFO','configadmin','资源管理','开启绑定成功'),(81,'2017-05-16 09:21:47','INFO','configadmin','资源管理','关闭绑定成功'),(82,'2017-05-16 09:21:54','INFO','configadmin','资源管理','开启绑定成功'),(83,'2017-05-16 09:47:43','INFO','configadmin','用户登录','用户登录成功'),(84,'2017-05-16 09:50:03','INFO','configadmin','用户登录','用户登录成功'),(85,'2017-05-16 09:50:20','INFO','configadmin','资源管理','添加成功'),(86,'2017-05-16 09:54:15','INFO','configadmin','用户登录','用户登录成功'),(87,'2017-05-16 09:54:31','INFO','configadmin','资源管理','添加成功'),(88,'2017-05-16 10:01:14','INFO','configadmin','用户登录','用户登录成功'),(89,'2017-05-16 10:04:28','INFO','configadmin','用户登录','用户登录成功'),(90,'2017-05-16 10:04:47','INFO','configadmin','资源管理','删除成功'),(91,'2017-05-16 10:04:59','INFO','configadmin','资源管理','关闭绑定成功'),(92,'2017-05-16 10:13:25','INFO','configadmin','LDAP配置','接入配置保存成功'),(93,'2017-05-16 10:16:36','INFO','configadmin','用户登录','用户登录成功'),(94,'2017-05-16 11:06:44','INFO','configadmin','用户登录','用户登录成功'),(95,'2017-05-16 11:07:44','INFO','configadmin','资源管理','开启绑定成功'),(96,'2017-05-16 11:07:53','INFO','configadmin','资源管理','关闭绑定成功'),(97,'2017-05-16 11:07:59','INFO','configadmin','资源管理','开启绑定成功'),(98,'2017-05-16 11:08:14','INFO','configadmin','资源管理','添加成功'),(99,'2017-05-16 11:11:59','INFO','configadmin','用户登录','用户登录成功'),(100,'2017-05-16 11:14:10','INFO','configadmin','用户登录','用户登录成功'),(101,'2017-05-16 11:17:22','INFO','configadmin','用户登录','用户登录成功'),(102,'2017-05-16 11:17:44','INFO','configadmin','资源管理','删除成功'),(103,'2017-05-16 11:18:09','INFO','configadmin','资源管理','关闭绑定成功'),(104,'2017-05-16 16:34:25','INFO','configadmin','用户登录','用户登录成功'),(105,'2017-05-16 16:34:42','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(106,'2017-05-16 16:36:56','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(107,'2017-05-16 16:45:08','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(108,'2017-05-16 16:46:33','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(109,'2017-05-16 16:46:34','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(110,'2017-05-16 16:47:34','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(111,'2017-05-16 16:47:35','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(112,'2017-05-16 16:47:35','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(113,'2017-05-16 16:47:58','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(114,'2017-05-16 16:47:59','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(115,'2017-05-16 16:48:00','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(116,'2017-05-16 16:48:03','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(117,'2017-05-16 16:48:19','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(118,'2017-05-16 16:48:19','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(119,'2017-05-16 16:48:20','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(120,'2017-05-16 16:48:30','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(121,'2017-05-16 16:48:31','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(122,'2017-05-16 16:48:31','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(123,'2017-05-16 16:48:32','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(124,'2017-05-16 16:49:26','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(125,'2017-05-16 16:49:27','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(126,'2017-05-16 16:49:28','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(127,'2017-05-16 16:50:03','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(128,'2017-05-16 16:50:04','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(129,'2017-05-16 16:50:05','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(130,'2017-05-16 16:50:06','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(131,'2017-05-16 16:50:06','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(132,'2017-05-16 16:50:07','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(133,'2017-05-16 16:50:07','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(134,'2017-05-16 16:50:08','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(135,'2017-05-16 16:50:17','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(136,'2017-05-16 16:50:21','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(137,'2017-05-16 16:51:51','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(138,'2017-05-16 16:51:52','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(139,'2017-05-16 16:51:53','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(140,'2017-05-16 16:53:11','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(141,'2017-05-16 16:53:12','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(142,'2017-05-16 16:54:01','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(143,'2017-05-16 16:54:02','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(144,'2017-05-16 16:54:03','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(145,'2017-05-16 17:02:13','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(146,'2017-05-16 17:02:14','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(147,'2017-05-16 17:02:16','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(148,'2017-05-16 17:02:18','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(149,'2017-05-16 17:07:26','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(150,'2017-05-16 17:07:27','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(151,'2017-05-16 17:07:27','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(152,'2017-05-16 17:07:28','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(153,'2017-05-16 17:07:29','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(154,'2017-05-16 17:07:58','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(155,'2017-05-16 17:07:59','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(156,'2017-05-16 17:07:59','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(157,'2017-05-16 17:08:00','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(158,'2017-05-16 17:08:24','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(159,'2017-05-16 17:08:25','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(160,'2017-05-16 17:08:26','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(161,'2017-05-16 17:10:00','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(162,'2017-05-16 17:10:05','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(163,'2017-05-16 17:15:18','info','configadmin','双击热备','用户删除双机热备配置信息成功'),(164,'2017-05-16 17:15:19','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(165,'2017-05-16 17:15:23','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(166,'2017-05-16 17:15:24','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(167,'2017-05-16 17:19:30','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(168,'2017-05-16 17:19:31','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(169,'2017-05-16 17:19:32','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(170,'2017-05-16 17:19:41','info','configadmin','双击热备','用户删除双机热备配置信息成功'),(171,'2017-05-16 17:19:42','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(172,'2017-05-16 17:30:27','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(173,'2017-05-16 17:30:27','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(174,'2017-05-16 17:30:28','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(175,'2017-05-16 17:31:10','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(176,'2017-05-16 17:31:11','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(177,'2017-05-16 17:31:12','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(178,'2017-05-16 17:31:30','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(179,'2017-05-16 17:31:31','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(180,'2017-05-16 17:31:32','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(181,'2017-05-17 09:00:32','INFO','configadmin','用户登录','用户登录成功'),(182,'2017-05-17 09:00:44','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(183,'2017-05-17 09:00:46','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(184,'2017-05-17 09:00:51','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(185,'2017-05-17 09:01:28','info','configadmin','双击热备','用户删除双机热备配置信息成功'),(186,'2017-05-17 09:03:04','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(187,'2017-05-17 09:32:38','INFO','configadmin','用户登录','用户登录成功'),(188,'2017-05-17 09:32:48','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(189,'2017-05-17 09:32:50','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(190,'2017-05-17 09:32:52','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(191,'2017-05-17 09:33:39','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(192,'2017-05-17 09:33:40','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(193,'2017-05-17 09:34:42','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(194,'2017-05-17 09:34:58','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(195,'2017-05-17 09:34:59','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(196,'2017-05-17 09:35:35','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(197,'2017-05-17 09:35:35','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(198,'2017-05-17 09:35:56','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(199,'2017-05-17 09:35:57','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(200,'2017-05-17 09:36:10','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(201,'2017-05-17 09:36:11','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(202,'2017-05-17 10:22:22','INFO','configadmin','用户登录','用户登录成功'),(203,'2017-05-17 10:22:30','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(204,'2017-05-17 10:23:35','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(205,'2017-05-17 13:08:27','INFO','configadmin','用户登录','用户登录成功'),(206,'2017-05-17 13:32:17','INFO','configadmin','用户登录','用户登录成功'),(207,'2017-05-17 13:32:22','error','configadmin','双击热备','用户查找双机热备配置信息失败'),(208,'2017-05-17 13:32:41','INFO','configadmin','双击热备','用户更新双机热备配置信息成功!'),(209,'2017-05-17 13:32:44','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(210,'2017-05-17 13:32:45','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(211,'2017-05-17 13:32:45','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(212,'2017-05-17 13:32:46','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(213,'2017-05-17 13:34:21','INFO','configadmin','双击热备','用户更新双机热备配置信息成功!'),(214,'2017-05-17 13:35:41','INFO','configadmin','双击热备','用户更新双机热备配置信息成功!'),(215,'2017-05-17 13:37:14','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(216,'2017-05-17 13:38:28','INFO','configadmin','用户登录','用户登录成功'),(217,'2017-05-17 13:39:06','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(218,'2017-05-17 13:41:30','INFO','configadmin','用户登录','用户登录成功'),(219,'2017-05-17 13:41:47','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(220,'2017-05-17 13:43:32','INFO','configadmin','用户登录','用户登录成功'),(221,'2017-05-17 13:43:41','error','configadmin','双击热备','用户查找双机热备配置信息失败'),(222,'2017-05-17 13:44:39','INFO','configadmin','双击热备','用户更新双机热备配置信息成功!'),(223,'2017-05-17 13:44:41','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(224,'2017-05-17 13:44:52','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(225,'2017-05-17 13:49:18','INFO','configadmin','用户登录','用户登录成功'),(226,'2017-05-17 13:49:24','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(227,'2017-05-17 13:50:11','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(228,'2017-05-17 13:50:12','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(229,'2017-05-17 13:50:26','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(230,'2017-05-17 13:50:59','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(231,'2017-05-17 13:50:59','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(232,'2017-05-17 13:51:16','INFO','configadmin','双击热备','用户添加监控服务信息成功!'),(233,'2017-05-17 13:54:53','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(234,'2017-05-17 13:54:54','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(235,'2017-05-17 13:55:11','INFO','configadmin','双击热备','用户添加监控服务信息成功!'),(236,'2017-05-17 13:55:19','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(237,'2017-05-17 13:55:32','INFO','configadmin','双击热备','用户更新监控服务信息成功!'),(238,'2017-05-17 13:55:38','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(239,'2017-05-17 13:55:47','INFO','configadmin','双击热备','用户删除监控服务信息成功!'),(240,'2017-05-17 13:55:53','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(241,'2017-05-17 13:56:10','INFO','configadmin','双击热备','用户添加监控服务信息成功!'),(242,'2017-05-17 13:56:59','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(243,'2017-05-17 13:57:00','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(244,'2017-05-17 13:57:03','INFO','configadmin','双击热备','用户删除监控服务信息成功!'),(245,'2017-05-17 13:57:16','INFO','configadmin','双击热备','用户添加监控服务信息成功!'),(246,'2017-05-17 13:57:23','INFO','configadmin','双击热备','用户更新监控服务信息成功!'),(247,'2017-05-17 13:58:09','INFO','configadmin','双击热备','用户更新监控服务信息成功!'),(248,'2017-05-17 13:59:29','INFO','configadmin','用户登录','用户登录成功'),(249,'2017-05-17 13:59:35','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(250,'2017-05-17 13:59:44','INFO','configadmin','双击热备','用户更新监控服务信息成功!'),(251,'2017-05-17 13:59:55','INFO','configadmin','双击热备','用户删除监控服务信息成功!'),(252,'2017-05-17 14:00:11','INFO','configadmin','双击热备','用户添加监控服务信息成功!'),(253,'2017-05-17 14:37:30','INFO','configadmin','用户登录','用户登录成功'),(254,'2017-05-17 14:41:50','INFO','configadmin','用户登录','用户登录成功'),(255,'2017-05-17 14:44:54','INFO','configadmin','用户登录','用户登录成功'),(256,'2017-05-17 14:45:22','INFO','configadmin','双击热备','用户更新双机热备配置信息成功!'),(257,'2017-05-17 14:46:46','INFO','configadmin','用户登录','用户登录成功'),(258,'2017-05-17 14:46:52','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(259,'2017-05-17 14:47:14','INFO','configadmin','双击热备','用户添加监控服务信息成功!'),(260,'2017-05-17 14:47:37','INFO','configadmin','双击热备','用户添加监控服务信息成功!'),(261,'2017-05-17 14:48:23','INFO','configadmin','双击热备','用户更新监控服务信息成功!'),(262,'2017-05-17 14:48:34','INFO','configadmin','双击热备','用户删除监控服务信息成功!'),(263,'2017-05-17 14:55:30','INFO','configadmin','用户登录','用户登录成功'),(264,'2017-05-17 14:55:38','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(265,'2017-05-17 14:56:00','INFO','configadmin','双击热备','用户删除监控服务信息成功!'),(266,'2017-05-17 15:06:30','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(267,'2017-05-17 15:06:33','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(268,'2017-05-17 15:06:34','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(269,'2017-05-17 15:07:08','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(270,'2017-05-17 15:07:09','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(271,'2017-05-17 15:07:09','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(272,'2017-05-17 15:07:10','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(273,'2017-05-19 15:58:27','INFO','configadmin','用户登录','用户登录成功'),(274,'2017-05-19 15:58:40','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(275,'2017-05-19 15:59:05','INFO','configadmin','双击热备','用户更新双机热备配置信息成功!'),(276,'2017-05-19 15:59:13','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(277,'2017-05-19 15:59:14','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(278,'2017-05-19 15:59:31','INFO','configadmin','双击热备','用户更新双机热备配置信息成功!'),(279,'2017-05-19 15:59:34','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(280,'2017-05-19 15:59:35','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(281,'2017-05-19 16:01:05','INFO','configadmin','双击热备','用户更新双机热备配置信息成功!'),(282,'2017-05-19 16:01:28','INFO','configadmin','双击热备','用户添加监控服务信息成功!'),(283,'2017-05-19 16:01:38','INFO','configadmin','双击热备','用户更新监控服务信息成功!'),(284,'2017-05-19 16:01:40','INFO','configadmin','双击热备','用户删除监控服务信息成功!'),(285,'2017-05-19 16:01:45','info','configadmin','双击热备','用户查找双机热备配置信息成功'),(286,'2017-05-19 16:01:46','info','configadmin','双击热备','用户查找双机热备配置信息成功');
/*!40000 ALTER TABLE `user_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_source_nets`
--

DROP TABLE IF EXISTS `user_source_nets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_source_nets` (
  `user_id` int(11) NOT NULL,
  `source_net_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`source_net_id`),
  KEY `FK9DF2A5A6D95A9C2F` (`source_net_id`),
  KEY `FK9DF2A5A6C9DE2D4E` (`user_id`),
  CONSTRAINT `FK9DF2A5A6C9DE2D4E` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK9DF2A5A6D95A9C2F` FOREIGN KEY (`source_net_id`) REFERENCES `source_nets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_source_nets`
--

LOCK TABLES `user_source_nets` WRITE;
/*!40000 ALTER TABLE `user_source_nets` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_source_nets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'sslvpn'
--

--
-- Dumping routines for database 'sslvpn'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-23 11:40:46
