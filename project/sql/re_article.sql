-- MySQL dump 10.13  Distrib 8.0.27, for Linux (x86_64)
--
-- Host: localhost    Database: re_article
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
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '博客文章标题',
  `summary` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '博客文章简介信息',
  `markdown_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '博客文章markdown内容信息',
  `html_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '博客文章html内容信息',
  `category_id` int DEFAULT NULL COMMENT '博客分类id',
  `user_id` int DEFAULT NULL COMMENT '所属用户id',
  `cover_url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '封面图路径',
  `view` bigint NOT NULL DEFAULT '0' COMMENT '浏览量',
  `favorite` bigint NOT NULL DEFAULT '0' COMMENT '喜欢数',
  `is_recommend` tinyint(1) NOT NULL COMMENT '是否推荐 0 不是 1 是',
  `is_top` tinyint(1) NOT NULL COMMENT '是否置顶 0 不是 1 是',
  `creation_type` tinyint(1) NOT NULL COMMENT '创作类型 1原创 2 转载',
  `transport_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '转载说明信息',
  `quote_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '引用信息',
  `create_time` datetime NOT NULL COMMENT '博客文章创建时间',
  `modify_time` datetime NOT NULL COMMENT '博客文章修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除  0正常',
  `opt_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客文章';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (5,'DBeaver 22.1.0破解安装','DBeaver Enterprise（简称DBeaverEE）支持MongoDB、Redis、Apache Hive等，但是需要付费使用。\n下载地址：\n百度云下载: [https://pan.baidu.com/s/1k1BNHoc5N0M2Z6d0AjYkLQ](https://pan.baidu.com/s/1k1BNHoc5N0M2Z6d0AjYkLQ) 提取码：o36d。\n6.2及以上有几','# DBeaver 22.1.0破解安装\nDBeaver Enterprise（简称DBeaverEE）支持MongoDB、Redis、Apache Hive等，但是需要付费使用。\n\n下载地址：\n百度云下载: [https://pan.baidu.com/s/1k1BNHoc5N0M2Z6d0AjYkLQ](https://pan.baidu.com/s/1k1BNHoc5N0M2Z6d0AjYkLQ) 提取码：o36d。\n6.2及以上有几点需要注意的：\n\n```ini\n-Dlm.debug.mode=true```\n21.0版本开始自行安装jdk11，替换dbeaver.ini内由-vm指定的java路径，把地址换成自己安装的！\n如果你的dbeaver.ini内没有-vm参数，请在首行添加你安装jdk的java路径：\n```ini\n-vm\n/path/to/your/bin/java\n```',NULL,10,1,'http://f.lingjiatong.cn:30090/rootelement/sys/default_article_cover.gif',0,0,0,0,1,NULL,NULL,'2022-10-20 21:35:18','2022-10-20 21:35:18',0,'lingjiatong');
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名，不超过50个字符',
  `view` bigint DEFAULT '0' COMMENT '类型总浏览量',
  `favorite` bigint DEFAULT '0' COMMENT '类型总喜欢数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 0 正常 1 已删除',
  `opt_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作用户',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uidx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Java',0,0,'2022-10-17 19:46:56','2022-10-17 19:46:56',0,'lingjiatong'),(2,'Linux',0,0,'2022-10-17 19:46:56','2022-10-17 19:46:56',0,'lingjiatong'),(3,'Mysql',0,0,'2022-10-17 19:46:56','2022-10-17 19:46:56',0,'lingjiatong'),(4,'爬虫',0,0,'2022-10-17 19:46:56','2022-10-17 19:46:56',0,'lingjiatong'),(5,'个人随笔',0,0,'2022-10-17 19:46:56','2022-10-17 19:46:56',0,'lingjiatong'),(6,'个人生活',0,0,'2022-10-17 19:46:56','2022-10-17 19:46:56',0,'lingjiatong'),(7,'微服务',0,0,'2022-10-17 19:46:56','2022-10-17 19:46:56',0,'lingjiatong'),(8,'k8s',0,0,'2022-10-17 19:46:56','2022-10-17 19:46:56',0,'lingjiatong'),(9,'Spring',0,0,'2022-10-17 19:46:56','2022-10-17 19:46:56',0,'lingjiatong'),(10,'破解',0,0,'2022-10-19 22:18:14','2022-10-19 22:18:18',0,'lingjiatong');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 0 正常 1 已删除',
  `opt_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作用户',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uidx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客标签';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (3,'DBeaver','2022-10-20 21:35:19','2022-10-20 21:35:19',0,'lingjiatong'),(4,'破解','2022-10-20 21:35:19','2022-10-20 21:35:19',0,'lingjiatong');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_article_tag`
--

DROP TABLE IF EXISTS `tr_article_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tr_article_tag` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `tag_id` bigint NOT NULL COMMENT '标签id',
  `article_id` bigint NOT NULL COMMENT '文章id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_article_id_tag_id` (`article_id`,`tag_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章标签关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_article_tag`
--

LOCK TABLES `tr_article_tag` WRITE;
/*!40000 ALTER TABLE `tr_article_tag` DISABLE KEYS */;
INSERT INTO `tr_article_tag` VALUES (3,3,5),(4,4,5);
/*!40000 ALTER TABLE `tr_article_tag` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-26  6:46:00
