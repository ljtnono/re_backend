-- MySQL dump 10.13  Distrib 8.0.27, for Linux (x86_64)

CREATE DATABASE xxl_job;
USE xxl_job;

-- Host: localhost    Database: xxl_job
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `xxl_job_group`
--

DROP TABLE IF EXISTS `xxl_job_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xxl_job_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `address_type` tinyint NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` text COMMENT '执行器地址列表，多地址逗号分隔',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xxl_job_group`
--

LOCK TABLES `xxl_job_group` WRITE;
/*!40000 ALTER TABLE `xxl_job_group` DISABLE KEYS */;
INSERT INTO `xxl_job_group` VALUES (2,'re-job','re定时任务执行器',0,'http://www.lingjiatong.cn:30086/','2022-12-26 14:46:54');
/*!40000 ALTER TABLE `xxl_job_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xxl_job_info`
--

DROP TABLE IF EXISTS `xxl_job_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xxl_job_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `job_group` int NOT NULL COMMENT '执行器主键ID',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `schedule_type` varchar(50) NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
  `schedule_conf` varchar(128) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
  `misfire_strategy` varchar(50) NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `trigger_next_time` bigint NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xxl_job_info`
--

LOCK TABLES `xxl_job_info` WRITE;
/*!40000 ALTER TABLE `xxl_job_info` DISABLE KEYS */;
INSERT INTO `xxl_job_info` VALUES (3,2,'头条热榜爬虫','2022-10-11 23:29:36','2022-10-14 20:04:52','lingjiatong','935188400@qq.com','CRON','0 0 1 * * ?','DO_NOTHING','FIRST','toutiaoHotSpider','','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2022-10-11 23:29:36','',1,1671987600000,1672074000000),(4,2,'百度图片爬虫','2022-10-14 20:04:20','2022-10-15 03:30:40','lingjiatong','935188400@qq.com','CRON','0 0 1 * * ?','DO_NOTHING','FIRST','baiduImageSpider','width=&height=&copyright=0&hd=0&latest=0&z=&ic=&word=日向雏田','SERIAL_EXECUTION',0,0,'BEAN','','GLUE代码初始化','2022-10-14 20:04:20','',0,0,0);
/*!40000 ALTER TABLE `xxl_job_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xxl_job_lock`
--

DROP TABLE IF EXISTS `xxl_job_lock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xxl_job_lock` (
  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xxl_job_lock`
--

LOCK TABLES `xxl_job_lock` WRITE;
/*!40000 ALTER TABLE `xxl_job_lock` DISABLE KEYS */;
INSERT INTO `xxl_job_lock` VALUES ('schedule_lock');
/*!40000 ALTER TABLE `xxl_job_lock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xxl_job_log`
--

DROP TABLE IF EXISTS `xxl_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xxl_job_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_group` int NOT NULL COMMENT '执行器主键ID',
  `job_id` int NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int NOT NULL COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int NOT NULL COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `alarm_status` tinyint NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`),
  KEY `I_handle_code` (`handle_code`)
) ENGINE=InnoDB AUTO_INCREMENT=901 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xxl_job_log`
--

LOCK TABLES `xxl_job_log` WRITE;
/*!40000 ALTER TABLE `xxl_job_log` DISABLE KEYS */;
INSERT INTO `xxl_job_log` VALUES (869,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-11-25 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-11-25 01:04:46',200,'',0),(870,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-11-26 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-11-26 01:04:21',200,'',0),(871,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-11-27 01:00:02',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-11-27 01:04:35',200,'',0),(872,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-11-28 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-11-28 01:04:19',200,'',0),(873,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-11-29 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-11-29 01:04:59',200,'',0),(874,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-11-30 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-11-30 01:00:17',200,'',0),(875,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-01 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-01 01:00:17',200,'',0),(876,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-02 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-02 01:00:18',200,'',0),(877,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-03 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-03 01:00:17',200,'',0),(878,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-04 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-04 01:00:18',200,'',0),(879,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-05 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-05 01:00:17',200,'',0),(880,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-06 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-06 01:00:18',200,'',0),(881,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-07 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-07 01:00:18',200,'',0),(882,2,3,NULL,'toutiaoHotSpider','',NULL,0,'2022-12-08 01:00:00',500,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>',NULL,0,NULL,3),(883,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-09 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-09 01:00:18',200,'',0),(884,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-10 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-10 01:00:19',200,'',0),(885,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-11 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-11 01:00:17',200,'',0),(886,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-12 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-12 01:00:19',200,'',0),(887,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-13 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-13 01:00:19',200,'',0),(888,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-14 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-14 01:00:20',200,'',0),(889,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-15 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-15 01:00:19',200,'',0),(890,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-16 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-16 01:00:19',200,'',0),(891,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-17 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-17 01:00:49',200,'',0),(892,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-18 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-18 01:00:20',200,'',0),(893,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-19 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-19 01:00:19',200,'',0),(894,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-20 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-20 01:00:18',200,'',0),(895,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-21 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-21 01:00:18',200,'',0),(896,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-22 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-22 01:00:19',200,'',0),(897,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-23 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-23 01:00:20',200,'',0),(898,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-24 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-24 01:00:18',200,'',0),(899,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-25 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-25 01:00:20',200,'',0),(900,2,3,'http://www.lingjiatong.cn:30086/','toutiaoHotSpider','',NULL,0,'2022-12-26 01:00:00',200,'任务触发类型：Cron触发<br>调度机器：10.244.1.221<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://www.lingjiatong.cn:30086/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://www.lingjiatong.cn:30086/<br>code：200<br>msg：null','2022-12-26 01:00:20',200,'',0);
/*!40000 ALTER TABLE `xxl_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xxl_job_log_report`
--

DROP TABLE IF EXISTS `xxl_job_log_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xxl_job_log_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `trigger_day` datetime DEFAULT NULL COMMENT '调度-时间',
  `running_count` int NOT NULL DEFAULT '0' COMMENT '运行中-日志数量',
  `suc_count` int NOT NULL DEFAULT '0' COMMENT '执行成功-日志数量',
  `fail_count` int NOT NULL DEFAULT '0' COMMENT '执行失败-日志数量',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_trigger_day` (`trigger_day`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xxl_job_log_report`
--

LOCK TABLES `xxl_job_log_report` WRITE;
/*!40000 ALTER TABLE `xxl_job_log_report` DISABLE KEYS */;
INSERT INTO `xxl_job_log_report` VALUES (1,'2022-10-11 00:00:00',0,0,0,NULL),(2,'2022-10-10 00:00:00',0,0,0,NULL),(3,'2022-10-09 00:00:00',0,0,0,NULL),(4,'2022-10-12 00:00:00',0,0,0,NULL),(5,'2022-10-13 00:00:00',0,1,0,NULL),(6,'2022-10-14 00:00:00',0,2,3,NULL),(7,'2022-10-15 00:00:00',0,17,17,NULL),(8,'2022-10-16 00:00:00',0,6,0,NULL),(9,'2022-10-17 00:00:00',0,1,0,NULL),(10,'2022-10-18 00:00:00',0,6,0,NULL),(11,'2022-10-19 00:00:00',0,1,0,NULL),(12,'2022-10-20 00:00:00',0,1,0,NULL),(13,'2022-10-21 00:00:00',0,1,0,NULL),(14,'2022-10-22 00:00:00',0,4,0,NULL),(15,'2022-10-23 00:00:00',0,1,0,NULL),(16,'2022-10-24 00:00:00',0,1,0,NULL),(17,'2022-10-25 00:00:00',0,1,0,NULL),(18,'2022-10-26 00:00:00',0,1,0,NULL),(19,'2022-10-27 00:00:00',0,1,0,NULL),(20,'2022-10-28 00:00:00',0,1,0,NULL),(21,'2022-10-29 00:00:00',0,1,0,NULL),(22,'2022-10-30 00:00:00',0,1,0,NULL),(23,'2022-10-31 00:00:00',0,1,0,NULL),(24,'2022-11-01 00:00:00',0,1,0,NULL),(25,'2022-11-02 00:00:00',0,1,0,NULL),(26,'2022-11-03 00:00:00',0,1,0,NULL),(27,'2022-11-04 00:00:00',0,1,0,NULL),(28,'2022-11-05 00:00:00',0,1,0,NULL),(29,'2022-11-06 00:00:00',0,1,0,NULL),(30,'2022-11-07 00:00:00',0,1,0,NULL),(31,'2022-11-08 00:00:00',0,1,0,NULL),(32,'2022-11-09 00:00:00',0,1,0,NULL),(33,'2022-11-10 00:00:00',0,1,0,NULL),(34,'2022-11-11 00:00:00',0,1,0,NULL),(35,'2022-11-12 00:00:00',0,5,0,NULL),(36,'2022-11-13 00:00:00',0,1,0,NULL),(37,'2022-11-14 00:00:00',0,1,0,NULL),(38,'2022-11-15 00:00:00',0,1,0,NULL),(39,'2022-11-16 00:00:00',0,1,0,NULL),(40,'2022-11-17 00:00:00',0,1,0,NULL),(41,'2022-11-18 00:00:00',0,1,0,NULL),(42,'2022-11-19 00:00:00',0,1,0,NULL),(43,'2022-11-20 00:00:00',0,1,0,NULL),(44,'2022-11-21 00:00:00',0,1,0,NULL),(45,'2022-11-22 00:00:00',0,1,0,NULL),(46,'2022-11-23 00:00:00',0,1,0,NULL),(47,'2022-11-24 00:00:00',0,1,0,NULL),(48,'2022-11-25 00:00:00',0,1,0,NULL),(49,'2022-11-26 00:00:00',0,1,0,NULL),(50,'2022-11-27 00:00:00',0,1,0,NULL),(51,'2022-11-28 00:00:00',0,1,0,NULL),(52,'2022-11-29 00:00:00',0,1,0,NULL),(53,'2022-11-30 00:00:00',0,1,0,NULL),(54,'2022-12-01 00:00:00',0,1,0,NULL),(55,'2022-12-02 00:00:00',0,1,0,NULL),(56,'2022-12-03 00:00:00',0,1,0,NULL),(57,'2022-12-04 00:00:00',0,1,0,NULL),(58,'2022-12-05 00:00:00',0,1,0,NULL),(59,'2022-12-06 00:00:00',0,1,0,NULL),(60,'2022-12-07 00:00:00',0,1,0,NULL),(61,'2022-12-08 00:00:00',0,0,1,NULL),(62,'2022-12-09 00:00:00',0,1,0,NULL),(63,'2022-12-10 00:00:00',0,1,0,NULL),(64,'2022-12-11 00:00:00',0,1,0,NULL),(65,'2022-12-12 00:00:00',0,1,0,NULL),(66,'2022-12-13 00:00:00',0,1,0,NULL),(67,'2022-12-14 00:00:00',0,1,0,NULL),(68,'2022-12-15 00:00:00',0,1,0,NULL),(69,'2022-12-16 00:00:00',0,1,0,NULL),(70,'2022-12-17 00:00:00',0,1,0,NULL),(71,'2022-12-18 00:00:00',0,1,0,NULL),(72,'2022-12-19 00:00:00',0,1,0,NULL),(73,'2022-12-20 00:00:00',0,1,0,NULL),(74,'2022-12-21 00:00:00',0,1,0,NULL),(75,'2022-12-22 00:00:00',0,1,0,NULL),(76,'2022-12-23 00:00:00',0,1,0,NULL),(77,'2022-12-24 00:00:00',0,1,0,NULL),(78,'2022-12-25 00:00:00',0,1,0,NULL),(79,'2022-12-26 00:00:00',0,1,0,NULL);
/*!40000 ALTER TABLE `xxl_job_log_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xxl_job_logglue`
--

DROP TABLE IF EXISTS `xxl_job_logglue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xxl_job_logglue` (
  `id` int NOT NULL AUTO_INCREMENT,
  `job_id` int NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xxl_job_logglue`
--

LOCK TABLES `xxl_job_logglue` WRITE;
/*!40000 ALTER TABLE `xxl_job_logglue` DISABLE KEYS */;
/*!40000 ALTER TABLE `xxl_job_logglue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xxl_job_registry`
--

DROP TABLE IF EXISTS `xxl_job_registry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xxl_job_registry` (
  `id` int NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(50) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xxl_job_registry`
--

LOCK TABLES `xxl_job_registry` WRITE;
/*!40000 ALTER TABLE `xxl_job_registry` DISABLE KEYS */;
INSERT INTO `xxl_job_registry` VALUES (56,'EXECUTOR','re-job','http://www.lingjiatong.cn:30086/','2022-12-26 14:46:52');
/*!40000 ALTER TABLE `xxl_job_registry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xxl_job_user`
--

DROP TABLE IF EXISTS `xxl_job_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xxl_job_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `role` tinyint NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xxl_job_user`
--

LOCK TABLES `xxl_job_user` WRITE;
/*!40000 ALTER TABLE `xxl_job_user` DISABLE KEYS */;
INSERT INTO `xxl_job_user` VALUES (2,'lingjiatong','80cea81e681679a81634e2b1846e6cb8',1,'');
/*!40000 ALTER TABLE `xxl_job_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-26  6:47:01
