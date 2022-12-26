-- MySQL dump 10.13  Distrib 8.0.27, for Linux (x86_64)
--
-- Host: localhost    Database: re_sys
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
-- Table structure for table `oauth_client_details`
--

DROP TABLE IF EXISTS `oauth_client_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `access_token_validity` int DEFAULT NULL,
  `refresh_token_validity` int DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='oauth2客户端表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_client_details`
--

LOCK TABLES `oauth_client_details` WRITE;
/*!40000 ALTER TABLE `oauth_client_details` DISABLE KEYS */;
INSERT INTO `oauth_client_details` VALUES ('client',NULL,'e10adc3949ba59abbe56e057f20f883e','all','password,refresh_token,verify_code','',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `oauth_client_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名',
  `type` tinyint(1) NOT NULL COMMENT '权限类型 0 菜单 1 具体权限或按钮',
  `parent_id` int NOT NULL COMMENT '父权限id，顶层父菜单为-1',
  `expression` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限表达式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1000,'博客管理',0,-1,'blog','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1001,'博客管理',0,1000,'blog:blog','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1002,'查看博客',1,1001,'blog:blog:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1003,'新增博客',1,1001,'blog:blog:add','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1004,'修改博客',1,1001,'blog:blog:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1005,'删除博客',1,1001,'blog:blog:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1020,'标签管理',0,1000,'blog:tag','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1021,'查看标签',1,1020,'blog:tag:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1022,'新增标签',1,1020,'blog:tag:add','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1023,'修改标签',1,1020,'blog:tag:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1024,'删除标签',1,1020,'blog:tag:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1040,'评论管理',0,1000,'blog:comment','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1041,'查看评论',1,1040,'blog:comment:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1042,'导出评论',1,1040,'blog:comment:export','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(1043,'删除评论',1,1040,'blog:comment:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2000,'资源管理',0,-1,'rs','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2001,'图片管理',0,2000,'rs:image','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2002,'查看图片',1,2001,'rs:image:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2003,'上传图片',1,2001,'rs:image:upload','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2004,'下载图片',1,2001,'rs:image:download','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2005,'删除图片',1,2001,'rs:image:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2006,'更新图片',1,2001,'rs:image:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2020,'链接管理',0,2000,'rs:link','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2021,'查看链接',1,2020,'rs:link:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2022,'新增链接',1,2020,'rs:link:add','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2023,'更新链接',1,2020,'rs:link:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(2024,'删除链接',1,2020,'rs:link:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(3000,'时间轴管理',0,-1,'timeline','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(3001,'查看时间轴',1,3000,'timeline:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(3002,'删除时间轴',1,3000,'timeline:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(3003,'修改时间轴',1,3000,'timeline:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(4000,'技能管理',0,-1,'skill','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(4001,'查看技能',1,4000,'skill:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(4002,'新增技能',1,4000,'skill:add','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(4003,'删除技能',1,4000,'skill:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(4004,'修改技能',1,4000,'skill:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(5000,'消息管理',0,-1,'message','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(5001,'消息管理',0,5000,'message:message','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(5002,'查看消息',1,5001,'message:message:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(5003,'新增消息',1,5001,'message:message:add','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(5004,'删除消息',1,5001,'message:message:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(5005,'修改消息',1,5001,'message:message:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(5020,'回收站管理',0,5000,'message:recycle','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(5021,'查看回收站',1,5020,'message:recycle:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(5022,'删除回收站消息',1,5020,'message:recycle:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(5023,'恢复回收站消息',1,5020,'message:recycle:recover','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6000,'系统管理',0,-1,'system','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6001,'用户管理',0,6000,'system:user','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6002,'查看用户',1,6001,'system:user:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6003,'新增用户',1,6001,'system:user:add','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6004,'删除用户',1,6001,'system:user:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6005,'修改用户',1,6001,'system:user:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6020,'角色管理',0,6000,'system:role','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6021,'查看角色',1,6020,'system:role:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6022,'新增角色',1,6020,'system:role:add','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6023,'删除角色',1,6020,'system:role:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6024,'修改角色',1,6020,'system:role:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6040,'权限管理',0,6000,'system:permission','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6041,'查看权限',1,6040,'system:permission:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6042,'新增权限',1,6040,'system:permission:add','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6043,'删除权限',1,6040,'system:permission:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6044,'修改权限',1,6040,'system:permission:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6060,'日志管理',0,6000,'system:log','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6061,'查看日志',1,6060,'system:log:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6062,'新增日志',1,6060,'system:log:add','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6063,'删除日志',1,6060,'system:log:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6064,'导出日志',1,6060,'system:log:export','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6080,'网站配置',0,6000,'system:config','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6081,'查看配置',1,6080,'system:config:view','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6082,'新增配置',1,6080,'system:config:add','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6083,'删除配置',1,6080,'system:config:delete','2020-08-24 00:23:16','2020-08-24 00:23:16',0),(6084,'修改配置',1,6080,'system:config:update','2020-08-24 00:23:16','2020-08-24 00:23:16',0);
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uidx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'超级管理员','拥有所有权限','2020-08-24 00:30:21','2020-08-24 00:30:21',0);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置名',
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置键',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'header部分LOGO图片地址','HEADER_LOGO_URL','http://f.lingjiatong.cn:30090/rootelement/sys/header_logo.png'),(2,'网站作者网名','NICK_NAME','杂技程序员老凌'),(3,'作者头像图片地址','AVATAR_URL','http://f.lingjiatong.cn:30090/rootelement/sys/avatar.jpeg'),(4,'关于作者页面个人简介描述','ABOUT_AUTHOR','Java菜鸟一枚\n\n喜欢折腾各种技术，web、linux、数据库、前端等\n\n爱生活、爱科学、爱设计、爱编程\n\nTalk is cheap, show me the code'),(5,'邮件ICON链接地址','SEND_ME_EMAIL','https://mail.qq.com/cgi-bin/loginpage'),(6,'个人github首页','GITHUB_AUTHOR','https://github.com/ljtnono'),(7,'footer部分版权声明','FOOTER_COPYRIGHT','本站的文章和资源来自互联网或者站长 的原创，按照 CC BY -NC -SA 3.0 CN 协议发布和共享，转载或引用本站文章 应遵循相同协议。如果有侵犯版权的资 源请尽快联系站长，我们会在24h内删 除有争议的资源。'),(8,'footer部分网站驱动图片地址，url以逗号隔开','FOOTER_DRIVER','https://res.hc-cdn.com/cnpm-header-and-footer/2.0.6/base/header-china/components/images/logo.svg, https://img.alicdn.com/tfs/TB13DzOjXP7gK0jSZFjXXc5aXXa-212-48.png, https://labs.mysql.com/common/logos/mysql-logo.svg?v2, https://redis.com/wp-content/themes/wpx/assets/images/icon-redis.svg, https://nginx.org/nginx.png, https://tomcat.apache.org/res/images/tomcat.png'),(9,'作者微信图片地址','AUTHOR_WX_QRCODE_URL','http://f.lingjiatong.cn:30090/rootelement/sys/author_wx_qrcode.jpeg'),(10,'footer部分关于本站','FOOTER_ABOUT_WEBSITE','根元素,Java,css,html,爬虫,网络,IT,技术,博客 Talk is cheap, show me the code'),(11,'网站备案号','WEBSITE_ICP_CODE','鄂ICP备18013706号'),(12,'本项目github地址','GITHUB_WEBSITE','https://github.com/ljtnono/re_frontend'),(13,'作者微信号','AUTHOR_WX','wxlive_zzz'),(14,'作者qq号','AUTHOR_QQ','935188400'),(15,'作者github用户名','AUTHOR_GITHUB_USERNAME','ppjt'),(16,'作者微信支付码','AUTHOR_WX_PAY_QRCODE_URL','http://f.lingjiatong.cn:30090/rootelement/sys/wx_pay_qrcode.jpeg'),(17,'作者支付宝支付码','AUTHOR_ALIPAY_PAY_QRCODE_URL','http://f.lingjiatong.cn:30090/rootelement/sys/alipay_pay_qrcode.jpeg');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_friend_link`
--

DROP TABLE IF EXISTS `sys_friend_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_friend_link` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '链接名',
  `url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '链接访问地址',
  `type` tinyint(1) NOT NULL COMMENT '链接类型 1 官方网站 2 个人网站',
  `master` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '网站主体 官方网站没有此项信息，个人网站需要此项信息',
  `master_email` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '网站主体电子邮箱',
  `favicon_url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '网站图标标志',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  `opt_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作用户',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uidx_name` (`name`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='友情链接';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_friend_link`
--

LOCK TABLES `sys_friend_link` WRITE;
/*!40000 ALTER TABLE `sys_friend_link` DISABLE KEYS */;
INSERT INTO `sys_friend_link` VALUES (1,'华为云','https://www.huaweicloud.com/',1,NULL,NULL,'https://res-static.hc-cdn.cn/cloudbu-site/china/zh-cn/组件验证/pep-common-header/logo-en.png','2022-10-16 00:12:01','2022-10-16 00:12:01','lingjiatong'),(2,'阿里云','https://www.aliyun.com/',1,NULL,NULL,'https://img.alicdn.com/tfs/TB13DzOjXP7gK0jSZFjXXc5aXXa-212-48.png','2022-10-16 00:12:01','2022-10-16 00:12:01','lingjiatong'),(3,'mysql','https://www.mysql.com/',1,NULL,NULL,'https://labs.mysql.com/common/logos/mysql-logo.svg?v2','2022-10-16 00:12:01','2022-10-16 00:12:01','lingjiatong'),(4,'redis','https://redis.io',1,NULL,NULL,'https://redis.com/wp-content/themes/wpx/assets/images/icon-redis.svg','2022-10-16 00:12:01','2022-10-16 00:12:01','lingjiatong'),(5,'nginx','https://www.nginx.com/',1,NULL,NULL,'https://nginx.org/nginx.png','2022-10-16 00:12:01','2022-10-16 00:12:01','lingjiatong'),(6,'tomcat','https://tomcat.apache.org/',1,NULL,NULL,'https://nginx.org/nginx.png','2022-10-16 00:12:01','2022-10-16 00:12:01','lingjiatong');
/*!40000 ALTER TABLE `sys_friend_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_log` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `user_id` int DEFAULT NULL COMMENT '日志操作用户id',
  `type` tinyint(1) DEFAULT NULL COMMENT '日志类型 1 用户日志 2 系统日志',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `op_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作名',
  `op_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作详情',
  `result` tinyint(1) DEFAULT NULL COMMENT '操作结果 1 成功  2 失败',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作者ip地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题，最大200个字符',
  `link` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '通知链接地址',
  `type` tinyint(1) NOT NULL COMMENT '通知类型 1 系统通知（重要） 2 常规通知 3 日常新闻',
  `news_state` tinyint(1) DEFAULT NULL COMMENT '新闻类型通知类型 0 普通 1 热榜 2 新闻',
  `news_date` date DEFAULT NULL COMMENT '新闻类型通知日期',
  `start_time` datetime NOT NULL COMMENT '消息开始显示时间',
  `end_time` datetime NOT NULL COMMENT '消息结束显示时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  `opt_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作用户',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uidx_title_type` (`title`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='前端通知数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1040991183060639744,'31省份昨增本土1452+10351','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227161004648650919436%22%2C%22hot_board_impr_id%22%3A%22202211121405510101330380770575F0CA%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%221%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%2231%E7%9C%81%E4%BB%BD%E6%98%A8%E5%A2%9E%E6%9C%AC%E5%9C%9F1452%2B10351%22%7D&rank=1&style_id=40132&topic_id=7161004648650919436',3,0,'2022-11-12','2022-11-12 14:07:19','2022-11-15 14:07:19','2022-11-12 14:07:19','2022-11-12 14:07:19','system'),(1040991184981630976,'专家双十一解析购物成瘾根源','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%221%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227164684719173402638%22%2C%22hot_board_impr_id%22%3A%22202211121405510101330380770575F0CA%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%222%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E4%B8%93%E5%AE%B6%E5%8F%8C%E5%8D%81%E4%B8%80%E8%A7%A3%E6%9E%90%E8%B4%AD%E7%89%A9%E6%88%90%E7%98%BE%E6%A0%B9%E6%BA%90%22%7D&rank=2&style_id=40132&topic_id=7164684719173402638',3,0,'2022-11-12','2022-11-12 14:07:19','2022-11-15 14:07:19','2022-11-12 14:07:19','2022-11-12 14:07:19','system'),(1040991186151841792,'划重点 优化疫情防控二十条来了','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227161004648650903052%22%2C%22hot_board_impr_id%22%3A%22202211121405510101330380770575F0CA%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%223%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E5%88%92%E9%87%8D%E7%82%B9+%E4%BC%98%E5%8C%96%E7%96%AB%E6%83%85%E9%98%B2%E6%8E%A7%E4%BA%8C%E5%8D%81%E6%9D%A1%E6%9D%A5%E4%BA%86%22%7D&rank=3&style_id=40132&topic_id=7161004648650903052',3,0,'2022-11-12','2022-11-12 14:07:19','2022-11-15 14:07:19','2022-11-12 14:07:19','2022-11-12 14:07:19','system'),(1040991187670179840,'天舟五号货运飞船发射成功','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227161332325962337824%22%2C%22hot_board_impr_id%22%3A%22202211121405510101330380770575F0CA%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%224%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E5%A4%A9%E8%88%9F%E4%BA%94%E5%8F%B7%E8%B4%A7%E8%BF%90%E9%A3%9E%E8%88%B9%E5%8F%91%E5%B0%84%E6%88%90%E5%8A%9F%22%7D&rank=4&style_id=40132&topic_id=7161332325962337824',3,1,'2022-11-12','2022-11-12 14:07:20','2022-11-15 14:07:20','2022-11-12 14:07:20','2022-11-12 14:07:20','system'),(1040991188399988736,'前市委书记权色交易低价卖地获刑16年','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227164599861109588006%22%2C%22hot_board_impr_id%22%3A%22202211121405510101330380770575F0CA%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%225%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E5%89%8D%E5%B8%82%E5%A7%94%E4%B9%A6%E8%AE%B0%E6%9D%83%E8%89%B2%E4%BA%A4%E6%98%93%E4%BD%8E%E4%BB%B7%E5%8D%96%E5%9C%B0%E8%8E%B7%E5%88%9116%E5%B9%B4%22%7D&rank=5&style_id=40132&topic_id=7164599861109588006',3,0,'2022-11-12','2022-11-12 14:07:20','2022-11-15 14:07:20','2022-11-12 14:07:20','2022-11-12 14:07:20','system'),(1040991188995579904,'郑州在重点文物建方舱被叫停','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227164821502687182852%22%2C%22hot_board_impr_id%22%3A%22202211121405510101330380770575F0CA%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%226%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E9%83%91%E5%B7%9E%E5%9C%A8%E9%87%8D%E7%82%B9%E6%96%87%E7%89%A9%E5%BB%BA%E6%96%B9%E8%88%B1%E8%A2%AB%E5%8F%AB%E5%81%9C%22%7D&rank=6&style_id=40132&topic_id=7164821502687182852',3,0,'2022-11-12','2022-11-12 14:07:20','2022-11-15 14:07:20','2022-11-12 14:07:20','2022-11-12 14:07:20','system'),(1040991189578588160,'中国力克伊朗提前晋级男篮世界杯','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%225%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227164921069768281636%22%2C%22hot_board_impr_id%22%3A%22202211121405510101330380770575F0CA%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%227%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E4%B8%AD%E5%9B%BD%E5%8A%9B%E5%85%8B%E4%BC%8A%E6%9C%97%E6%8F%90%E5%89%8D%E6%99%8B%E7%BA%A7%E7%94%B7%E7%AF%AE%E4%B8%96%E7%95%8C%E6%9D%AF%22%7D&rank=7&style_id=40132&topic_id=7164921069768281636',3,1,'2022-11-12','2022-11-12 14:07:20','2022-11-15 14:07:20','2022-11-12 14:07:20','2022-11-12 14:07:20','system'),(1040991190174179328,'卢拉演讲落泪：要让巴西人再吃上饭','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%220%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227164806093284212770%22%2C%22hot_board_impr_id%22%3A%22202211121405510101330380770575F0CA%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%228%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E5%8D%A2%E6%8B%89%E6%BC%94%E8%AE%B2%E8%90%BD%E6%B3%AA%EF%BC%9A%E8%A6%81%E8%AE%A9%E5%B7%B4%E8%A5%BF%E4%BA%BA%E5%86%8D%E5%90%83%E4%B8%8A%E9%A5%AD%22%7D&rank=8&style_id=40132&topic_id=7164806093284212770',3,2,'2022-11-12','2022-11-12 14:07:20','2022-11-15 14:07:20','2022-11-12 14:07:20','2022-11-12 14:07:20','system'),(1040991190908182528,'今年双11和往年有何不同','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%224%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227164205738582081574%22%2C%22hot_board_impr_id%22%3A%22202211121405510101330380770575F0CA%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%229%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E4%BB%8A%E5%B9%B4%E5%8F%8C11%E5%92%8C%E5%BE%80%E5%B9%B4%E6%9C%89%E4%BD%95%E4%B8%8D%E5%90%8C%22%7D&rank=9&style_id=40132&topic_id=7164205738582081574',3,0,'2022-11-12','2022-11-12 14:07:21','2022-11-15 14:07:21','2022-11-12 14:07:21','2022-11-12 14:07:21','system'),(1040991191956758528,'女子看店疑睡觉没戴口罩被贴黄牌','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%221%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227164963738422870046%22%2C%22hot_board_impr_id%22%3A%22202211121405510101330380770575F0CA%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%2210%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E5%A5%B3%E5%AD%90%E7%9C%8B%E5%BA%97%E7%96%91%E7%9D%A1%E8%A7%89%E6%B2%A1%E6%88%B4%E5%8F%A3%E7%BD%A9%E8%A2%AB%E8%B4%B4%E9%BB%84%E7%89%8C%22%7D&rank=10&style_id=40132&topic_id=7164963738422870046',3,1,'2022-11-12','2022-11-12 14:07:21','2022-11-15 14:07:21','2022-11-12 14:07:21','2022-11-12 14:07:21','system'),(1042381106728706048,'跨省旅游不再与风险区联动管理','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227165605946402537476%22%2C%22hot_board_impr_id%22%3A%22202211160100000101511960840A14C0FF%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%221%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E8%B7%A8%E7%9C%81%E6%97%85%E6%B8%B8%E4%B8%8D%E5%86%8D%E4%B8%8E%E9%A3%8E%E9%99%A9%E5%8C%BA%E8%81%94%E5%8A%A8%E7%AE%A1%E7%90%86%22%7D&rank=1&style_id=40132&topic_id=7165605946402537476',3,0,'2022-11-15','2022-11-16 10:10:22','2022-11-19 10:10:22','2022-11-16 10:10:22','2022-11-16 10:10:22','system'),(1042381107030695936,'中美会晤持续三个多小时','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%221%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227165444884000669699%22%2C%22hot_board_impr_id%22%3A%22202211160100000101511960840A14C0FF%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%222%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E4%B8%AD%E7%BE%8E%E4%BC%9A%E6%99%A4%E6%8C%81%E7%BB%AD%E4%B8%89%E4%B8%AA%E5%A4%9A%E5%B0%8F%E6%97%B6%22%7D&rank=2&style_id=40132&topic_id=7165444884000669699',3,1,'2022-11-15','2022-11-16 10:10:22','2022-11-19 10:10:22','2022-11-16 10:10:22','2022-11-16 10:10:22','system'),(1042381108314152960,'我国成功发射遥感三十四号03星','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227166073864147959840%22%2C%22hot_board_impr_id%22%3A%22202211160100000101511960840A14C0FF%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%223%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E6%88%91%E5%9B%BD%E6%88%90%E5%8A%9F%E5%8F%91%E5%B0%84%E9%81%A5%E6%84%9F%E4%B8%89%E5%8D%81%E5%9B%9B%E5%8F%B703%E6%98%9F%22%7D&rank=3&style_id=40132&topic_id=7166073864147959840',3,0,'2022-11-15','2022-11-16 10:10:23','2022-11-19 10:10:23','2022-11-16 10:10:23','2022-11-16 10:10:23','system'),(1042381108909744128,'“20条”出台后多地取消区域全员核酸','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%221%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227165723807909412898%22%2C%22hot_board_impr_id%22%3A%222022111501000001020803722403AD9C05%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%221%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E2%80%9C20%E6%9D%A1%E2%80%9D%E5%87%BA%E5%8F%B0%E5%90%8E%E5%A4%9A%E5%9C%B0%E5%8F%96%E6%B6%88%E5%8C%BA%E5%9F%9F%E5%85%A8%E5%91%98%E6%A0%B8%E9%85%B8%22%7D&rank=1&style_id=40132&topic_id=7165723807909412898',3,1,'2022-11-14','2022-11-16 10:10:23','2022-11-19 10:10:23','2022-11-16 10:10:23','2022-11-16 10:10:23','system'),(1042381109266259968,'中美会晤释放哪些关键信息','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%220%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227165859948310724644%22%2C%22hot_board_impr_id%22%3A%22202211160100000101511960840A14C0FF%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%2212%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E4%B8%AD%E7%BE%8E%E4%BC%9A%E6%99%A4%E9%87%8A%E6%94%BE%E5%93%AA%E4%BA%9B%E5%85%B3%E9%94%AE%E4%BF%A1%E6%81%AF%22%7D&rank=12&style_id=40132&topic_id=7165859948310724644',3,0,'2022-11-15','2022-11-16 10:10:23','2022-11-19 10:10:23','2022-11-16 10:10:23','2022-11-16 10:10:23','system'),(1042381110109315072,'尹力任北京市委书记','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227165420065087553567%22%2C%22hot_board_impr_id%22%3A%2220221114010000010140048146015BE490%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%221%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E5%B0%B9%E5%8A%9B%E4%BB%BB%E5%8C%97%E4%BA%AC%E5%B8%82%E5%A7%94%E4%B9%A6%E8%AE%B0%22%7D&rank=1&style_id=40132&topic_id=7165420065087553567',3,1,'2022-11-13','2022-11-16 10:10:23','2022-11-19 10:10:23','2022-11-16 10:10:23','2022-11-16 10:10:23','system'),(1042381110839123968,'石家庄关闭市内免费核酸检测点','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%228%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227165489235565215784%22%2C%22hot_board_impr_id%22%3A%222022111501000001020803722403AD9C05%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%226%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E7%9F%B3%E5%AE%B6%E5%BA%84%E5%85%B3%E9%97%AD%E5%B8%82%E5%86%85%E5%85%8D%E8%B4%B9%E6%A0%B8%E9%85%B8%E6%A3%80%E6%B5%8B%E7%82%B9%22%7D&rank=6&style_id=40132&topic_id=7165489235565215784',3,1,'2022-11-14','2022-11-16 10:10:23','2022-11-19 10:10:23','2022-11-16 10:10:23','2022-11-16 10:10:23','system'),(1042381111531184128,'上头条看免费超清世界杯直播','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227163109307985251851%22%2C%22hot_board_impr_id%22%3A%22202211160100000101511960840A14C0FF%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%225%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E4%B8%8A%E5%A4%B4%E6%9D%A1%E7%9C%8B%E5%85%8D%E8%B4%B9%E8%B6%85%E6%B8%85%E4%B8%96%E7%95%8C%E6%9D%AF%E7%9B%B4%E6%92%AD%22%7D&rank=5&style_id=40132&topic_id=7163109307985251851',3,0,'2022-11-15','2022-11-16 10:10:23','2022-11-19 10:10:23','2022-11-16 10:10:23','2022-11-16 10:10:23','system'),(1042381112089026560,'潮州失控特斯拉涉事司机还原经过','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%221%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227165310483619119137%22%2C%22hot_board_impr_id%22%3A%2220221114010000010140048146015BE490%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%2211%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E6%BD%AE%E5%B7%9E%E5%A4%B1%E6%8E%A7%E7%89%B9%E6%96%AF%E6%8B%89%E6%B6%89%E4%BA%8B%E5%8F%B8%E6%9C%BA%E8%BF%98%E5%8E%9F%E7%BB%8F%E8%BF%87%22%7D&rank=11&style_id=40132&topic_id=7165310483619119137',3,1,'2022-11-13','2022-11-16 10:10:24','2022-11-19 10:10:24','2022-11-16 10:10:24','2022-11-16 10:10:24','system'),(1042381112718172160,'《湿地公约》第十四届缔约方大会闭幕','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227165424521283895331%22%2C%22hot_board_impr_id%22%3A%222022111501000001020803722403AD9C05%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%223%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E3%80%8A%E6%B9%BF%E5%9C%B0%E5%85%AC%E7%BA%A6%E3%80%8B%E7%AC%AC%E5%8D%81%E5%9B%9B%E5%B1%8A%E7%BC%94%E7%BA%A6%E6%96%B9%E5%A4%A7%E4%BC%9A%E9%97%AD%E5%B9%95%22%7D&rank=3&style_id=40132&topic_id=7165424521283895331',3,0,'2022-11-14','2022-11-16 10:10:24','2022-11-19 10:10:24','2022-11-16 10:10:24','2022-11-16 10:10:24','system'),(1045034442275725312,'梅西首秀破门 阿根廷爆冷1-2沙特','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227168022862438252575%22%2C%22hot_board_impr_id%22%3A%2220221123010000010212166074018F8FBD%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%221%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E6%A2%85%E8%A5%BF%E9%A6%96%E7%A7%80%E7%A0%B4%E9%97%A8+%E9%98%BF%E6%A0%B9%E5%BB%B7%E7%88%86%E5%86%B71-2%E6%B2%99%E7%89%B9%22%7D&rank=1&style_id=40132&topic_id=7168022862438252575',3,1,'2022-11-22','2022-11-23 17:53:47','2022-11-26 17:53:47','2022-11-23 17:53:47','2022-11-23 17:53:47','system'),(1045034442867122176,'墨西哥VS波兰','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227168766667253055540%22%2C%22hot_board_impr_id%22%3A%2220221123010000010212166074018F8FBD%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%222%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E5%A2%A8%E8%A5%BF%E5%93%A5VS%E6%B3%A2%E5%85%B0%22%7D&rank=2&style_id=40132&topic_id=7168766667253055540',3,1,'2022-11-22','2022-11-23 17:53:47','2022-11-26 17:53:47','2022-11-23 17:53:47','2022-11-23 17:53:47','system'),(1045034444557426688,'我国航天员有望十年内登上月球','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227166976181605109287%22%2C%22hot_board_impr_id%22%3A%2220221123010000010212166074018F8FBD%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%223%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E6%88%91%E5%9B%BD%E8%88%AA%E5%A4%A9%E5%91%98%E6%9C%89%E6%9C%9B%E5%8D%81%E5%B9%B4%E5%86%85%E7%99%BB%E4%B8%8A%E6%9C%88%E7%90%83%22%7D&rank=3&style_id=40132&topic_id=7166976181605109287',3,0,'2022-11-22','2022-11-23 17:53:47','2022-11-26 17:53:47','2022-11-23 17:53:47','2022-11-23 17:53:47','system'),(1045034446482612224,'丹麦0-0战平突尼斯','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227168653553198694407%22%2C%22hot_board_impr_id%22%3A%2220221123010000010212166074018F8FBD%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%224%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E4%B8%B9%E9%BA%A60-0%E6%88%98%E5%B9%B3%E7%AA%81%E5%B0%BC%E6%96%AF%22%7D&rank=4&style_id=40132&topic_id=7168653553198694407',3,0,'2022-11-22','2022-11-23 17:53:48','2022-11-26 17:53:48','2022-11-23 17:53:48','2022-11-23 17:53:48','system'),(1046848694288424960,'世界杯小组赛法国VS丹麦','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227168654297880677891%22%2C%22hot_board_impr_id%22%3A%222022112701000501015120313503DEA248%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%221%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E4%B8%96%E7%95%8C%E6%9D%AF%E5%B0%8F%E7%BB%84%E8%B5%9B%E6%B3%95%E5%9B%BDVS%E4%B8%B9%E9%BA%A6%22%7D&rank=1&style_id=40132&topic_id=7168654297880677891',3,1,'2022-11-26','2022-11-28 18:02:58','2022-12-01 18:02:58','2022-11-28 18:02:58','2022-11-28 18:02:58','system'),(1046848695685128192,'乌市市长致歉 已成立火灾调查组追责','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%225%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227169985792759041574%22%2C%22hot_board_impr_id%22%3A%22202211260100050102120991990F09F3B8%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%221%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E4%B9%8C%E5%B8%82%E5%B8%82%E9%95%BF%E8%87%B4%E6%AD%89+%E5%B7%B2%E6%88%90%E7%AB%8B%E7%81%AB%E7%81%BE%E8%B0%83%E6%9F%A5%E7%BB%84%E8%BF%BD%E8%B4%A3%22%7D&rank=1&style_id=40132&topic_id=7169985792759041574',3,1,'2022-11-25','2022-11-28 18:02:59','2022-12-01 18:02:59','2022-11-28 18:02:59','2022-11-28 18:02:59','system'),(1046848698340122624,'突尼斯0-1澳大利亚','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227169788534528835623%22%2C%22hot_board_impr_id%22%3A%222022112701000501015120313503DEA248%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%227%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E7%AA%81%E5%B0%BC%E6%96%AF0-1%E6%BE%B3%E5%A4%A7%E5%88%A9%E4%BA%9A%22%7D&rank=7&style_id=40132&topic_id=7169788534528835623',3,0,'2022-11-26','2022-11-28 18:02:59','2022-12-01 18:02:59','2022-11-28 18:02:59','2022-11-28 18:02:59','system'),(1046848699384504320,'荷兰VS厄瓜多尔','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227169628896269172743%22%2C%22hot_board_impr_id%22%3A%22202211260100050102120991990F09F3B8%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%222%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E8%8D%B7%E5%85%B0VS%E5%8E%84%E7%93%9C%E5%A4%9A%E5%B0%94%22%7D&rank=2&style_id=40132&topic_id=7169628896269172743',3,0,'2022-11-25','2022-11-28 18:02:59','2022-12-01 18:02:59','2022-11-28 18:02:59','2022-11-28 18:02:59','system'),(1046848700189810688,'居家健康监测怎么做？非必要不外出','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227170124436878655524%22%2C%22hot_board_impr_id%22%3A%222022112701000501015120313503DEA248%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%223%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E5%B1%85%E5%AE%B6%E5%81%A5%E5%BA%B7%E7%9B%91%E6%B5%8B%E6%80%8E%E4%B9%88%E5%81%9A%EF%BC%9F%E9%9D%9E%E5%BF%85%E8%A6%81%E4%B8%8D%E5%A4%96%E5%87%BA%22%7D&rank=3&style_id=40132&topic_id=7170124436878655524',3,0,'2022-11-26','2022-11-28 18:03:00','2022-12-01 18:03:00','2022-11-28 18:03:00','2022-11-28 18:03:00','system'),(1046848700928008192,'从海博会看中国海洋产业挺进“新蓝海”','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227169413559976198144%22%2C%22hot_board_impr_id%22%3A%22202211260100050102120991990F09F3B8%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%223%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E4%BB%8E%E6%B5%B7%E5%8D%9A%E4%BC%9A%E7%9C%8B%E4%B8%AD%E5%9B%BD%E6%B5%B7%E6%B4%8B%E4%BA%A7%E4%B8%9A%E6%8C%BA%E8%BF%9B%E2%80%9C%E6%96%B0%E8%93%9D%E6%B5%B7%E2%80%9D%22%7D&rank=3&style_id=40132&topic_id=7169413559976198144',3,0,'2022-11-25','2022-11-28 18:03:00','2022-12-01 18:03:00','2022-11-28 18:03:00','2022-11-28 18:03:00','system'),(1046848701523599360,'新疆：严厉打击暴力抗拒防疫等行为','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227169875535806332935%22%2C%22hot_board_impr_id%22%3A%222022112701000501015120313503DEA248%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%224%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E6%96%B0%E7%96%86%EF%BC%9A%E4%B8%A5%E5%8E%89%E6%89%93%E5%87%BB%E6%9A%B4%E5%8A%9B%E6%8A%97%E6%8B%92%E9%98%B2%E7%96%AB%E7%AD%89%E8%A1%8C%E4%B8%BA%22%7D&rank=4&style_id=40132&topic_id=7169875535806332935',3,0,'2022-11-26','2022-11-28 18:03:00','2022-12-01 18:03:00','2022-11-28 18:03:00','2022-11-28 18:03:00','system'),(1046848704035987456,'卡塔尔1-3塞内加尔 两连败出线渺茫','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227167584513210600998%22%2C%22hot_board_impr_id%22%3A%22202211260100050102120991990F09F3B8%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%224%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E5%8D%A1%E5%A1%94%E5%B0%941-3%E5%A1%9E%E5%86%85%E5%8A%A0%E5%B0%94+%E4%B8%A4%E8%BF%9E%E8%B4%A5%E5%87%BA%E7%BA%BF%E6%B8%BA%E8%8C%AB%22%7D&rank=4&style_id=40132&topic_id=7167584513210600998',3,1,'2022-11-25','2022-11-28 18:03:01','2022-12-01 18:03:01','2022-11-28 18:03:01','2022-11-28 18:03:01','system'),(1046848705160060928,'波兰2比0胜沙特小组登顶','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227168654297880661507%22%2C%22hot_board_impr_id%22%3A%222022112701000501015120313503DEA248%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%222%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E6%B3%A2%E5%85%B02%E6%AF%940%E8%83%9C%E6%B2%99%E7%89%B9%E5%B0%8F%E7%BB%84%E7%99%BB%E9%A1%B6%22%7D&rank=2&style_id=40132&topic_id=7168654297880661507',3,1,'2022-11-26','2022-11-28 18:03:01','2022-12-01 18:03:01','2022-11-28 18:03:01','2022-11-28 18:03:01','system'),(1050486408250433536,'江泽民在上海逝世 享年96岁','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%225%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227171723947984031262%22%2C%22hot_board_impr_id%22%3A%22202212010100000101581160430BA7C981%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%221%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E6%B1%9F%E6%B3%BD%E6%B0%91%E5%9C%A8%E4%B8%8A%E6%B5%B7%E9%80%9D%E4%B8%96+%E4%BA%AB%E5%B9%B496%E5%B2%81%22%7D&rank=1&style_id=40132&topic_id=7171723947984031262',3,1,'2022-11-30','2022-12-08 18:57:57','2022-12-11 18:57:57','2022-12-08 18:57:57','2022-12-08 18:57:57','system'),(1050486408627920896,'北京天安门下半旗为江泽民志哀','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%225%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227171728543162830366%22%2C%22hot_board_impr_id%22%3A%22202212010100000101581160430BA7C981%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%222%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E5%8C%97%E4%BA%AC%E5%A4%A9%E5%AE%89%E9%97%A8%E4%B8%8B%E5%8D%8A%E6%97%97%E4%B8%BA%E6%B1%9F%E6%B3%BD%E6%B0%91%E5%BF%97%E5%93%80%22%7D&rank=2&style_id=40132&topic_id=7171728543162830366',3,0,'2022-11-30','2022-12-08 18:57:57','2022-12-11 18:57:57','2022-12-08 18:57:57','2022-12-08 18:57:57','system'),(1050486410481803264,'航天追梦者永远青春','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227171010761966223393%22%2C%22hot_board_impr_id%22%3A%22202212010100000101581160430BA7C981%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%223%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E8%88%AA%E5%A4%A9%E8%BF%BD%E6%A2%A6%E8%80%85%E6%B0%B8%E8%BF%9C%E9%9D%92%E6%98%A5%22%7D&rank=3&style_id=40132&topic_id=7171010761966223393',3,0,'2022-11-30','2022-12-08 18:57:57','2022-12-11 18:57:57','2022-12-08 18:57:57','2022-12-08 18:57:57','system'),(1050486411089977344,'联合国安理会为江泽民默哀一分钟','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227171785087355191333%22%2C%22hot_board_impr_id%22%3A%22202212010100000101581160430BA7C981%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%224%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E8%81%94%E5%90%88%E5%9B%BD%E5%AE%89%E7%90%86%E4%BC%9A%E4%B8%BA%E6%B1%9F%E6%B3%BD%E6%B0%91%E9%BB%98%E5%93%80%E4%B8%80%E5%88%86%E9%92%9F%22%7D&rank=4&style_id=40132&topic_id=7171785087355191333',3,2,'2022-11-30','2022-12-08 18:57:57','2022-12-11 18:57:57','2022-12-08 18:57:57','2022-12-08 18:57:57','system'),(1050486412230828032,'江泽民同志遗像','https://www.toutiao.com/amos_land_page/?category_name=topic_innerflow&event_type=hot_board&log_pb=%7B%22category_name%22%3A%22topic_innerflow%22%2C%22cluster_type%22%3A%222%22%2C%22enter_from%22%3A%22click_category%22%2C%22entrance_hotspot%22%3A%22outside%22%2C%22event_type%22%3A%22hot_board%22%2C%22hot_board_cluster_id%22%3A%227171734151073103880%22%2C%22hot_board_impr_id%22%3A%22202212010100000101581160430BA7C981%22%2C%22jump_page%22%3A%22hot_board_page%22%2C%22location%22%3A%22news_hot_card%22%2C%22page_location%22%3A%22hot_board_page%22%2C%22rank%22%3A%225%22%2C%22source%22%3A%22trending_tab%22%2C%22style_id%22%3A%2240132%22%2C%22title%22%3A%22%E6%B1%9F%E6%B3%BD%E6%B0%91%E5%90%8C%E5%BF%97%E9%81%97%E5%83%8F%22%7D&rank=5&style_id=40132&topic_id=7171734151073103880',3,1,'2022-11-30','2022-12-08 18:57:58','2022-12-11 18:57:58','2022-12-08 18:57:58','2022-12-08 18:57:58','system');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_role_permission`
--

DROP TABLE IF EXISTS `tr_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tr_role_permission` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `permission_id` bigint NOT NULL COMMENT '权限id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_role_permission`
--

LOCK TABLES `tr_role_permission` WRITE;
/*!40000 ALTER TABLE `tr_role_permission` DISABLE KEYS */;
INSERT INTO `tr_role_permission` VALUES (1,1000,1);
/*!40000 ALTER TABLE `tr_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_user_role`
--

DROP TABLE IF EXISTS `tr_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tr_user_role` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_user_role`
--

LOCK TABLES `tr_user_role` WRITE;
/*!40000 ALTER TABLE `tr_user_role` DISABLE KEYS */;
INSERT INTO `tr_user_role` VALUES (1,1,1);
/*!40000 ALTER TABLE `tr_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名，4-20位字符串只允许英文和数字下划线',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码，md5加密形式',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号码',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户邮箱',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户头像访问url',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uidx_email` (`email`) USING BTREE,
  UNIQUE KEY `uidx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'lingjiatong','80cea81e681679a81634e2b1846e6cb8','16333333333','935188400@qq.com','http://f.lingjiatong.cn:30090/rootelement/sys/avatar.jpeg','2020-08-24 00:42:24','2021-03-10 07:43:03',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-26  6:46:25
