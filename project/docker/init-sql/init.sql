SET NAMES utf8mb4;
-- Artalk 评论库初始化（在 docker-entrypoint-initdb.d 中第一个执行）
CREATE DATABASE IF NOT EXISTS artalk DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'artalk'@'%' IDENTIFIED BY 're#artalk2022';
GRANT ALL PRIVILEGES ON artalk.* TO 'artalk'@'%';
FLUSH PRIVILEGES;
-- Nacos 配置中心库（使用 MySQL 存储，替代内嵌 derby）
-- 表结构来自 nacos/nacos-server:v3.1.1 镜像内 conf/mysql-schema.sql
CREATE DATABASE IF NOT EXISTS nacos DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE nacos;

/******************************************/
/*   表名称 = config_info                  */
/******************************************/
CREATE TABLE `config_info` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                               `group_id` varchar(128) DEFAULT NULL COMMENT 'group_id',
                               `content` longtext NOT NULL COMMENT 'content',
                               `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
                               `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                               `src_user` text COMMENT 'source user',
                               `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
                               `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                               `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
                               `c_desc` varchar(256) DEFAULT NULL COMMENT 'configuration description',
                               `c_use` varchar(64) DEFAULT NULL COMMENT 'configuration usage',
                               `effect` varchar(64) DEFAULT NULL COMMENT '配置生效的描述',
                               `type` varchar(64) DEFAULT NULL COMMENT '配置的类型',
                               `c_schema` text COMMENT '配置的模式',
                               `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/******************************************/
/*   表名称 = config_info  since 2.5.0                */
/******************************************/
CREATE TABLE `config_info_gray` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                    `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                    `content` longtext NOT NULL COMMENT 'content',
                                    `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
                                    `src_user` text COMMENT 'src_user',
                                    `src_ip` varchar(100) DEFAULT NULL COMMENT 'src_ip',
                                    `gmt_create` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_create',
                                    `gmt_modified` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_modified',
                                    `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                                    `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
                                    `gray_name` varchar(128) NOT NULL COMMENT 'gray_name',
                                    `gray_rule` text NOT NULL COMMENT 'gray_rule',
                                    `encrypted_data_key` varchar(256) NOT NULL DEFAULT '' COMMENT 'encrypted_data_key',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_configinfogray_datagrouptenantgray` (`data_id`,`group_id`,`tenant_id`,`gray_name`),
                                    KEY `idx_dataid_gmt_modified` (`data_id`,`gmt_modified`),
                                    KEY `idx_gmt_modified` (`gmt_modified`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='config_info_gray';

/******************************************/
/*   表名称 = config_tags_relation         */
/******************************************/
CREATE TABLE `config_tags_relation` (
                                        `id` bigint(20) NOT NULL COMMENT 'id',
                                        `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
                                        `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
                                        `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                        `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                        `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
                                        `nid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
                                        PRIMARY KEY (`nid`),
                                        UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
                                        KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   表名称 = group_capacity               */
/******************************************/
CREATE TABLE `group_capacity` (
                                  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
                                  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
                                  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
                                  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
                                  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
                                  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
                                  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
                                  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   表名称 = his_config_info              */
/******************************************/
CREATE TABLE `his_config_info` (
                                   `id` bigint(20) unsigned NOT NULL COMMENT 'id',
                                   `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
                                   `data_id` varchar(255) NOT NULL COMMENT 'data_id',
                                   `group_id` varchar(128) NOT NULL COMMENT 'group_id',
                                   `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
                                   `content` longtext NOT NULL COMMENT 'content',
                                   `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   `src_user` text COMMENT 'source user',
                                   `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
                                   `op_type` char(10) DEFAULT NULL COMMENT 'operation type',
                                   `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
                                   `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
                                   `publish_type` varchar(50)  DEFAULT 'formal' COMMENT 'publish type gray or formal',
                                   `gray_name` varchar(50)  DEFAULT NULL COMMENT 'gray name',
                                   `ext_info`  longtext DEFAULT NULL COMMENT 'ext info',
                                   PRIMARY KEY (`nid`),
                                   KEY `idx_gmt_create` (`gmt_create`),
                                   KEY `idx_gmt_modified` (`gmt_modified`),
                                   KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   表名称 = tenant_capacity              */
/******************************************/
CREATE TABLE `tenant_capacity` (
                                   `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
                                   `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
                                   `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
                                   `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
                                   `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
                                   `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
                                   `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `kp` varchar(128) NOT NULL COMMENT 'kp',
                               `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
                               `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
                               `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
                               `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
                               `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
                               `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
                               KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

CREATE TABLE `users` (
                         `username` varchar(50) NOT NULL PRIMARY KEY COMMENT 'username',
                         `password` varchar(500) NOT NULL COMMENT 'password',
                         `enabled` boolean NOT NULL COMMENT 'enabled'
);

CREATE TABLE `roles` (
                         `username` varchar(50) NOT NULL COMMENT 'username',
                         `role` varchar(50) NOT NULL COMMENT 'role',
                         UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE `permissions` (
                               `role` varchar(50) NOT NULL COMMENT 'role',
                               `resource` varchar(128) NOT NULL COMMENT 'resource',
                               `action` varchar(8) NOT NULL COMMENT 'action',
                               UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

-- prod 命名空间（与各服务 application-prd.yml 中 maven profile 过滤的 namespace 一致）
INSERT INTO tenant_info (kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified)
VALUES ('1', 'f2cc4448-831f-41da-9aa8-b89d4a81d90c', 'prod', '生产环境命名空间', 'docker-init', 1767225600000, 1767225600000);

-- 各微服务的运行配置（对应原 application-local.yml 的容器内网地址版）
-- 如需修改配置：改这里后重建数据卷，或直接在 Nacos 控制台编辑（持久化在 MySQL）
INSERT INTO config_info (data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES
('api-backend-prod.yaml', 'DEFAULT_GROUP', 'server:
  port: 9101
  servlet:
    context-path: /api-backend
spring:
  data:
    redis:
      host: re-redis
      port: 6379
      database: 0
      lettuce:
        pool:
          max-idle: 16
          max-active: 32
          min-idle: 8
minio:
  endpoint: http://re-minio:9000
  accessKey: re-minio
  secretKey: re#minio2022
  bucketName: re

# Sa-Token配置
sa-token:
  # token名称（同时也是cookie名称）
  token-name: Authorization
  # token前缀
  token-prefix: Bearer
  # jwt秘钥
  jwt-secret-key: asdasdasifhueuiwyurfewbfjsdafjk
  # token有效期，单位秒（默认30天，这里设置为7天）
  timeout: 604800
  # token临时有效期（指定时间内无操作就视为token过期）单位：秒
  activity-timeout: -1
  # 是否允许同一账号并发登录
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token
  is-share: false
', '801ed9be2a6d1e410f16803bdee51bb3', '2026-10-01 00:00:00', '2026-10-01 00:00:00', NULL, NULL, NULL, 'f2cc4448-831f-41da-9aa8-b89d4a81d90c', NULL, NULL, NULL, 'yaml', NULL, ''),
('api-file-prod.yaml', 'DEFAULT_GROUP', 'server:
  port: 9103
  servlet:
    context-path: /api-file
spring:
  data:
    redis:
      host: re-redis
      port: 6379
      database: 0
      lettuce:
        pool:
          max-idle: 16
          max-active: 32
          min-idle: 8
minio:
  endpoint: http://re-minio:9000
  accessKey: re-minio
  secretKey: re#minio2022
  bucketName: re

# Sa-Token配置
sa-token:
  # token名称（同时也是cookie名称）
  token-name: Authorization
  # token前缀
  token-prefix: Bearer
  # jwt秘钥
  jwt-secret-key: asdasdasifhueuiwyurfewbfjsdafjk
  # token有效期，单位秒（默认30天，这里设置为7天）
  timeout: 604800
  # token临时有效期（指定时间内无操作就视为token过期）单位：秒
  activity-timeout: -1
  # 是否允许同一账号并发登录
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token
  is-share: false
', 'cdde1c982e843881e42572bc72ed0c5d', '2026-10-01 00:00:00', '2026-10-01 00:00:00', NULL, NULL, NULL, 'f2cc4448-831f-41da-9aa8-b89d4a81d90c', NULL, NULL, NULL, 'yaml', NULL, ''),
('re-auth-prod.yaml', 'DEFAULT_GROUP', 'server:
  port: 9104
  servlet:
    context-path: /re-auth
spring:
  datasource:
    dynamic:
      primary: master
      datasource:
        master:
          url: jdbc:mysql://re-mysql:3306/re
          username: root
          password: re#mysql2022
          driver-class-name: com.mysql.cj.jdbc.Driver
          type: com.zaxxer.hikari.HikariDataSource
          hikari:
            maximum-pool-size: 20
            min-idle: 10
            idle-timeout: 500000
            max-lifetime: 540000
            connection-timeout: 60000
            connection-test-query: SELECT 1
  data:
    redis:
      host: re-redis
      port: 6379
      database: 0
      lettuce:
        pool:
          max-idle: 16
          max-active: 32
          min-idle: 8
mybatis-plus:
  mapper-locations: classpath:/mapper/**.xml

sa-token:
  # token名称（同时也是cookie名称）
  token-name: Authorization
  # token前缀
  token-prefix: Bearer
  # jwt秘钥
  jwt-secret-key: asdasdasifhueuiwyurfewbfjsdafjk
  # token有效期，单位秒（默认30天，这里设置为7天）
  timeout: 604800
  # token临时有效期（指定时间内无操作就视为token过期）单位：秒
  activity-timeout: -1
  # 是否允许同一账号并发登录（为true时允许一起登录，为false时新登录挤掉旧登录）
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token（为true时所有登录共用一个token，为false时每次登录新建一个token）
  is-share: false
', 'dab957a227e12af32d2b008de2e84dd2', '2026-10-01 00:00:00', '2026-10-01 00:00:00', NULL, NULL, NULL, 'f2cc4448-831f-41da-9aa8-b89d4a81d90c', NULL, NULL, NULL, 'yaml', NULL, ''),
('re-gateway-prod.yaml', 'DEFAULT_GROUP', 'server:
  port: 9100
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          \'[/**]\':
            allowedOriginPatterns: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
            allowCredentials: true
            exposedHeaders: "Content-Disposition,Content-Type,Cache-Control"
      routes:
        - id: re-auth
          uri: lb://re-auth
          predicates:
            - Path=/re-auth/**
        - id: re-service-sys-server
          uri: lb://re-service-sys-server
          predicates:
            - Path=/sys/**
        - id: re-service-article-server
          uri: lb://re-service-article-server
          predicates:
            - Path=/article/**
        - id: api-backend
          uri: lb://api-backend
          predicates:
            - Path=/api-backend/**
        - id: api-frontend
          uri: lb://api-frontend
          predicates:
            - Path=/api-frontend/**
        - id: api-file
          uri: lb://api-file
          predicates:
            - Path=/api-file/**
  data:
    redis:
      host: re-redis
      port: 6379
      database: 0
      lettuce:
        pool:
          max-idle: 16
          max-active: 32
          min-idle: 8
# Sa-Token配置
sa-token:
  # token名称（同时也是cookie名称）
  token-name: Authorization
  # token前缀
  token-prefix: Bearer
  # jwt秘钥
  jwt-secret-key: asdasdasifhueuiwyurfewbfjsdafjk
  # token有效期，单位秒（默认30天，这里设置为7天）
  timeout: 604800
  # token临时有效期（指定时间内无操作就视为token过期）单位：秒
  activity-timeout: -1
  # 是否允许同一账号并发登录
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token
  is-share: false
', 'f052e05fa91bbb42a4f8a3ac7d1a4785', '2026-10-01 00:00:00', '2026-10-01 00:00:00', NULL, NULL, NULL, 'f2cc4448-831f-41da-9aa8-b89d4a81d90c', NULL, NULL, NULL, 'yaml', NULL, ''),
('re-service-article-server-prod.yaml', 'DEFAULT_GROUP', 'server:
  port: 9112
  servlet:
    context-path: /article
spring:
  datasource:
    dynamic:
      primary: master
      datasource:
        master:
          url: jdbc:mysql://re-mysql:3306/re
          username: root
          password: re#mysql2022
          driver-class-name: com.mysql.cj.jdbc.Driver
          type: com.zaxxer.hikari.HikariDataSource
          hikari:
            maximum-pool-size: 20
            min-idle: 10
            idle-timeout: 500000
            max-lifetime: 540000
            connection-timeout: 60000
            connection-test-query: SELECT 1
  data:
    redis:
      host: re-redis
      port: 6379
      database: 0
      lettuce:
        pool:
          max-idle: 16
          max-active: 32
          min-idle: 8
  elasticsearch:
    rest:
      uris: re-elasticsearch
      port: 9200
mybatis-plus:
  mapper-locations: classpath:/mapper/**.xml

minio:
  endpoint: http://re-minio:9000
  accessKey: re-minio
  secretKey: re#minio2022
  bucketName: re

# Sa-Token配置
sa-token:
  # token名称（同时也是cookie名称）
  token-name: Authorization
  # token前缀
  token-prefix: Bearer
  # jwt秘钥
  jwt-secret-key: asdasdasifhueuiwyurfewbfjsdafjk
  # token有效期，单位秒（默认30天，这里设置为7天）
  timeout: 604800
  # token临时有效期（指定时间内无操作就视为token过期）单位：秒
  activity-timeout: -1
  # 是否允许同一账号并发登录
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token
  is-share: false
', '2119b96793d1ad541168c66fb0a188e4', '2026-10-01 00:00:00', '2026-10-01 00:00:00', NULL, NULL, NULL, 'f2cc4448-831f-41da-9aa8-b89d4a81d90c', NULL, NULL, NULL, 'yaml', NULL, ''),
('re-service-sys-server-prod.yaml', 'DEFAULT_GROUP', 'server:
  port: 9111
  servlet:
    context-path: /sys
spring:
  datasource:
    dynamic:
      primary: master
      datasource:
        master:
          url: jdbc:mysql://re-mysql:3306/re
          username: root
          password: re#mysql2022
          driver-class-name: com.mysql.cj.jdbc.Driver
          type: com.zaxxer.hikari.HikariDataSource
          hikari:
            maximum-pool-size: 20
            min-idle: 10
            idle-timeout: 500000
            max-lifetime: 540000
            connection-timeout: 60000
            connection-test-query: SELECT 1
  data:
    redis:
      host: re-redis
      port: 6379
      database: 0
      lettuce:
        pool:
          max-idle: 16
          max-active: 32
          min-idle: 8
mybatis-plus:
  mapper-locations: classpath:/mapper/**.xml

# 邮件发送配置（绑定邮箱、修改密码验证码），请填写真实的SMTP信息
spring.mail:
  host: smtp.qq.com
  port: 465
  username: 935188400@qq.com
  # SMTP授权码（不是邮箱登录密码）
  password: cdzeazlagrvpbbdi
  protocol: smtp
  properties:
    mail.smtp.auth: true
    mail.smtp.ssl.enable: true
    mail.smtp.socketFactory.class: javax.net.ssl.SSLSocketFactory

minio:
  endpoint: http://re-minio:9000
  accessKey: re-minio
  secretKey: re#minio2022
  bucketName: re

# Sa-Token配置
sa-token:
  # token名称（同时也是cookie名称）
  token-name: Authorization
  # token前缀
  token-prefix: Bearer
  # jwt秘钥
  jwt-secret-key: asdasdasifhueuiwyurfewbfjsdafjk
  # token有效期，单位秒（默认30天，这里设置为7天）
  timeout: 604800
  # token临时有效期（指定时间内无操作就视为token过期）单位：秒
  activity-timeout: -1
  # 是否允许同一账号并发登录
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token
  is-share: false
', '56eb16abb4e89548075a752201e9af33', '2026-10-01 00:00:00', '2026-10-01 00:00:00', NULL, NULL, NULL, 'f2cc4448-831f-41da-9aa8-b89d4a81d90c', NULL, NULL, NULL, 'yaml', NULL, ''),
('api-frontend-prod.yaml', 'DEFAULT_GROUP', 'server:
  port: 9102
  servlet:
    context-path: /api-frontend
spring:
  data:
    redis:
      host: re-redis
      port: 6379
      database: 0
      lettuce:
        pool:
          max-idle: 16
          max-active: 32
          min-idle: 8
minio:
  endpoint: http://re-minio:9000
  accessKey: re-minio
  secretKey: re#minio2022
  bucketName: re

# Sa-Token配置
sa-token:
  # token名称（同时也是cookie名称）
  token-name: Authorization
  # token前缀
  token-prefix: Bearer
  # jwt秘钥
  jwt-secret-key: asdasdasifhueuiwyurfewbfjsdafjk
  # token有效期，单位秒（默认30天，这里设置为7天）
  timeout: 604800
  # token临时有效期（指定时间内无操作就视为token过期）单位：秒
  activity-timeout: -1
  # 是否允许同一账号并发登录
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个token
  is-share: false
', '1d02e8d605ac06bb22776ddd354546de', '2026-10-01 00:00:00', '2026-10-01 00:00:00', NULL, NULL, NULL, 'f2cc4448-831f-41da-9aa8-b89d4a81d90c', NULL, NULL, NULL, 'yaml', NULL, '');

-- MySQL dump 10.13  Distrib 8.4.11, for Linux (x86_64)
--
-- Host: localhost    Database: re
-- ------------------------------------------------------
-- Server version	8.4.11

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
-- Current Database: `re`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `re` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `re`;

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
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` bigint NOT NULL COMMENT '主键id',
  `project_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单所属项目名称',
  `parent_id` bigint NOT NULL COMMENT '父菜单id，没有则为-1',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单标题',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单的图标',
  `route_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单路由路径',
  `route_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单路由名称',
  `component_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单组件路径',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_route_path` (`route_path`),
  UNIQUE KEY `uidx_route_name` (`route_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission` (
  `id` bigint NOT NULL COMMENT '主键id',
  `project_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限所属项目名称',
  `menu_id` bigint NOT NULL COMMENT '权限所属菜单id，不存在则为-1',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
  `expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限表达式',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_menu_id_name_expression` (`menu_id`,`name`,`expression`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `route`
--

DROP TABLE IF EXISTS `route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `route` (
  `id` bigint NOT NULL COMMENT '主键id',
  `project_name` varchar(20) NOT NULL COMMENT '路由所属项目名称',
  `path` varchar(200) NOT NULL COMMENT '路由路径',
  `name` varchar(50) NOT NULL COMMENT '路由命名',
  `component` varchar(200) NOT NULL COMMENT '命名视图组件',
  `redirect` longtext COMMENT '重定向',
  `props` longtext COMMENT '路由属性',
  `alias` longtext COMMENT '路由别名',
  `before_enter` longtext COMMENT '进入路由之前钩子函数',
  `meta` longtext COMMENT '路由元信息',
  `case_sensitive` tinyint(1) DEFAULT '1' COMMENT '匹配规则是否大小写敏感，0 敏感 1 不敏感',
  `path_to_regex_options` longtext COMMENT '编译正则的选项',
  `parent_id` bigint NOT NULL,
  `menu_id` bigint DEFAULT NULL COMMENT '对应的菜单id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_name` (`name`),
  UNIQUE KEY `uidx_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='前端路由表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `route`
--

LOCK TABLES `route` WRITE;
/*!40000 ALTER TABLE `route` DISABLE KEYS */;
INSERT INTO `route` VALUES (1001,'re_admin','/home','Home','src/view/Home.vue','{\"name\": \"Workspace\"}',NULL,NULL,NULL,'{\"title\":\"主页\",\"hideInMenu\":true}',1,NULL,-1,NULL),(1002,'re_admin','/updatePassword','UpdatePassword','src/view/UpdatePassword.vue',NULL,NULL,NULL,NULL,'{\"title\":\"修改密码\",\"hideInMenu\":true}',1,NULL,1001,NULL),(1003,'re_admin','/writeArticle','WriteArticle','src/view/WriteArticle.vue',NULL,NULL,NULL,NULL,'{\"title\":\"写文章\",\"hideInMenu\":true}',1,NULL,1001,NULL),(1004,'re_admin','/personal','Personal','src/view/Personal.vue',NULL,NULL,NULL,NULL,'{\"title\":\"个人中心\",\"hideInMenu\":true}',1,NULL,1001,NULL),(1005,'re_admin','/workspace','Workspace','src/view/Workspace.vue',NULL,NULL,NULL,NULL,'{\"title\":\"工作台\",\"hideInMenu\":true}',1,NULL,1001,NULL),(1006,'re_admin','/blog/article','BlogArticle','src/view/blog/ArticleManage.vue',NULL,NULL,NULL,NULL,'{\"title\":\"文章管理\",\"hideInMenu\":true}',1,NULL,1015,10001),(1007,'re_admin','/blog/category','BlogCategory','src/view/blog/CategoryManage.vue',NULL,NULL,NULL,NULL,'{\"title\":\"分类管理\",\"hideInMenu\":true}',1,NULL,1015,10002),(1008,'re_admin','/blog/comment','BlogComment','src/view/blog/CommentManage.vue',NULL,NULL,NULL,NULL,'{\"title\":\"评论管理\",\"hideInMenu\":true}',1,NULL,1015,10003),(1009,'re_admin','/system/user','SystemUser','src/view/system/UserManage.vue',NULL,NULL,NULL,NULL,'{\"title\":\"用户管理\",\"hideInMenu\":true}',1,NULL,1014,10101),(1010,'re_admin','/system/role','SystemRole','src/view/system/RoleManage.vue',NULL,NULL,NULL,NULL,'{\"title\":\"角色管理\",\"hideInMenu\":true}',1,NULL,1014,10102),(1011,'re_admin','/system/menu','SystemMenu','src/view/system/MenuManage.vue',NULL,NULL,NULL,NULL,'{\"title\":\"菜单管理\",\"hideInMenu\":true}',1,NULL,1014,10105),(1012,'re_admin','/system/websiteConfig','SystemWebsiteConfig','src/view/system/WebsiteConfig.vue',NULL,NULL,NULL,NULL,'{\"title\":\"网站配置\",\"hideInMenu\":true}',1,NULL,1014,10103),(1013,'re_admin','/system/monitor','SystemMonitor','src/view/system/SystemMonitor.vue',NULL,NULL,NULL,NULL,'{\"title\":\"系统监控\",\"hideInMenu\":true}',1,NULL,1014,10104),(1014,'re_admin','/system','System','src/view/system/System.vue',NULL,NULL,NULL,NULL,'{\"title\":\"系统管理\",\"hideInMenu\":true}',1,NULL,1001,10100),(1015,'re_admin','/blog','Blog','src/view/blog/Blog.vue',NULL,NULL,NULL,NULL,'{\"title\":\"博客管理\",\"hideInMenu\":true}',1,NULL,1001,10000),(1320756855008546816,'re_admin','/blog/test','Test','src/view/blog/Test.vue',NULL,NULL,NULL,NULL,'{\"title\":\"测试\",\"hideInMenu\":true}',1,NULL,1015,1320756854660419584);
/*!40000 ALTER TABLE `route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sp_baidu_img`
--

DROP TABLE IF EXISTS `sp_baidu_img`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sp_baidu_img` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '标题',
  `src` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片的链接地址',
  `width` int DEFAULT NULL COMMENT '图片的宽度',
  `height` int DEFAULT NULL COMMENT '图片的高度',
  `format` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片的格式',
  `unique_md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一md5值，由title,src,width,height,format计算而来',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uidx_unique_md5` (`unique_md5`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='百度图片爬虫数据表';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `sp_toutiao_rb`
--

DROP TABLE IF EXISTS `sp_toutiao_rb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sp_toutiao_rb` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `link` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '链接',
  `hot_value` bigint NOT NULL COMMENT '热度',
  `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0 普通 1 热榜 2 新闻',
  `query_word` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '查询关键字',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uidx_title` (`title`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='头条热榜爬虫数据表';
/*!40101 SET character_set_client = @saved_cs_client */;


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
-- Table structure for table `tr_role_menu`
--

DROP TABLE IF EXISTS `tr_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tr_role_menu` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;


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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限表';
/*!40101 SET character_set_client = @saved_cs_client */;


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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL COMMENT '主键id，雪花算法',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名，4-20位字符串只允许英文和数字下划线',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码，md5加密形式',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户邮箱',
  `avatar_url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '用户头像访问url',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_username` (`username`),
  UNIQUE KEY `uidx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `user_login_log`
--

DROP TABLE IF EXISTS `user_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `username` varchar(64) NOT NULL COMMENT '用户名，4-20位字符串只允许英文和数字下划线',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `ip` varchar(15) DEFAULT NULL COMMENT '登录ip',
  `ua` varchar(500) DEFAULT NULL COMMENT 'User-Agent',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1554866456343121921 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-30 16:35:04
SET NAMES utf8mb4;
USE re;
-- 权限、菜单、用户初始数据（表结构以上方 mysqldump 段为准，此处仅插入数据）

-- 页面路由（re_admin vue2 时代的动态路由数据，现版本未使用，仅保留备份）
DROP TABLE IF EXISTS page_route;
CREATE TABLE page_route (
    id BIGINT NOT NULL PRIMARY KEY COMMENT '主键id',
    project_name VARCHAR(100) NOT NULL COMMENT '路由所属项目名称',
    parent_id BIGINT NOT NULL COMMENT '父路由信息，不存在则为-1',
    name VARCHAR(50) NOT NULL COMMENT '路由名称',
    description VARCHAR(200) NULL COMMENT '路由描述',
    path VARCHAR(200) NULL COMMENT '路由路径',
    meta LONGTEXT NULL COMMENT '元信息',
    redirect LONGTEXT NULL COMMENT '重定向信息',
    alias LONGTEXT NULL COMMENT '别名',
    props LONGTEXT NULL COMMENT '路由参数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='页面路由表';

INSERT INTO page_route(id, project_name, parent_id, name, description, path, meta, redirect, alias, props) VALUES
(10000, 're_admin', -1, 'Root', 're_admin根路径重定向到登录页面', '/', '{"title":"主页","hideInMenu":true}', '{"name":"Login"}', NULL, NULL),
(10001, 're_admin', -1, 'Login', 're_admin登录页面', '/login', '{"title":"Login - 登录","hideInMenu":true}', NULL, NULL, NULL),
(10002, 're_admin', -1, '404', 're_admin的404页面', '*', '{"title":"404","name":"找不到页面","hideInMenu":true}', NULL, NULL, NULL),
(10100, 're_admin', -1, 'Home', 're_admin主页面', '/home', '{"title":"主页","hideInMenu":true}', NULL, NULL, NULL),
(10101, 're_admin', 10100, 'UpdatePassword', 're_admin修改密码页面', '/updatePassword', '{"title":"修改密码","hideInMenu":true}', NULL, NULL, NULL),
(10102, 're_admin', 10100, 'WriteArticle', 're_admin写文章页面', '/writeArticle', '{"title":"写文章","hideInMenu":true}', NULL, NULL, NULL),
(10103, 're_admin', 10100, 'Personal', 're_admin个人中心页面', '/personal', '{"title":"个人中心","hideInMenu":true}', NULL, NULL, NULL),
(10104, 're_admin', 10100, 'Workspace', 're_admin工作台页面', '/workspace', '{"title":"工作台","hideInMenu":true}', NULL, NULL, NULL),
(10105, 're_admin', 10100, 'BlogArticle', 're_admin文章管理页面', '/blog/article', '{"title":"文章管理","hideInMenu":true}', NULL, NULL, NULL),
(10106, 're_admin', 10100, 'BlogCategory', 're_admin分类管理页面', '/blog/category', '{"title":"分类管理","hideInMenu":true}', NULL, NULL, NULL),
(10107, 're_admin', 10100, 'BlogComment', 're_admin评论管理页面', '/blog/comment', '{"title":"评论管理","hideInMenu":true}', NULL, NULL, NULL),
(10108, 're_admin', 10100, 'SystemUser', 're_admin用户管理页面', '/system/user', '{"title":"用户管理","hideInMenu":true}', NULL, NULL, NULL),
(10109, 're_admin', 10100, 'SystemRole', 're_admin角色管理页面', '/system/role', '{"title":"角色管理","hideInMenu":true}', NULL, NULL, NULL),
(10110, 're_admin', 10100, 'SystemWebsiteConfig', 're_admin网站配置页面', '/system/websiteConfig', '{"title":"网站配置","hideInMenu":true}', NULL, NULL, NULL),
(10111, 're_admin', 10100, 'SystemMonitor', 're_admin系统监控页面', '/system/monitor', '{"title":"系统监控","hideInMenu":true}', NULL, NULL, NULL);

-- 菜单初始数据（component_path 与 route 表组件路径对应，父菜单 component_path 为空串）
INSERT INTO menu(id, project_name, parent_id, title, icon, route_path, route_name, component_path) VALUES
(10000, 're_admin', -1, '博客管理', 'icon-blog', '/blog', 'Blog', ''),
(10001, 're_admin', 10000, '文章管理', NULL, '/blog/article', 'BlogArticle', 'src/view/blog/ArticleManage.vue'),
(10002, 're_admin', 10000, '分类管理', NULL, '/blog/category', 'BlogCategory', 'src/view/blog/CategoryManage.vue'),
(10003, 're_admin', 10000, '评论管理', NULL, '/blog/comment', 'BlogComment', 'src/view/blog/CommentManage.vue'),
(10100, 're_admin', -1, '系统管理', 'icon-setting', '/system', 'System', ''),
(10101, 're_admin', 10100, '用户管理', NULL, '/system/user', 'SystemUser', 'src/view/system/UserManage.vue'),
(10102, 're_admin', 10100, '角色管理', NULL, '/system/role', 'SystemRole', 'src/view/system/RoleManage.vue'),
(10103, 're_admin', 10100, '网站配置', NULL, '/system/websiteConfig', 'SystemWebsiteConfig', 'src/view/system/WebsiteConfig.vue'),
(10104, 're_admin', 10100, '系统监控', NULL, '/system/monitor', 'SystemMonitor', 'src/view/system/SystemMonitor.vue');

-- 用户初始数据（admin / adminADMIN+++，MD5 小写无盐，与 re-auth 校验逻辑一致）
INSERT INTO user(id, username, password, phone, email, avatar_url, create_time, modify_time, is_deleted) VALUES
(1, 'admin', '9e104d79db75f8c2256ddc572a62f514', NULL, 'admin@example.com', NULL, '2026-10-01 00:00:00', '2026-10-01 00:00:00', 0);

-- 角色初始数据
INSERT INTO role(id, name, description, remark, create_time, modify_time) VALUES
(1, '超级管理员', '拥有所有权限', NULL, '2020-08-24 00:30:21', '2020-08-24 00:30:21');

-- 权限初始数据
INSERT INTO permission(id, project_name, menu_id, name, expression) VALUES
(10000, 're_admin', -1, '工作台', 'workspace'),
(10100, 're_admin', 10000, '博客管理', 'blog'),
(10101, 're_admin', 10001, '文章管理', 'blog:article'),
(10102, 're_admin', 10001, '文章管理-读', 'blog:article:read'),
(10103, 're_admin', 10001, '文章管理-写', 'blog:article:write'),
(10111, 're_admin', 10002, '分类管理', 'blog:category'),
(10112, 're_admin', 10002, '分类管理-读', 'blog:category:read'),
(10113, 're_admin', 10002, '分类管理-写', 'blog:category:write'),
(10121, 're_admin', 10003, '评论管理', 'blog:comment'),
(10122, 're_admin', 10003, '评论管理-读', 'blog:comment:read'),
(10123, 're_admin', 10003, '评论管理-写', 'blog:comment:write'),
(10200, 're_admin', 10100, '系统管理', 'system'),
(10201, 're_admin', 10101, '用户管理', 'system:user'),
(10202, 're_admin', 10101, '用户管理-读', 'system:user:read'),
(10203, 're_admin', 10101, '用户管理-写', 'system:user:write'),
(10211, 're_admin', 10102, '角色管理', 'system:role'),
(10212, 're_admin', 10102, '角色管理-读', 'system:role:read'),
(10213, 're_admin', 10102, '角色管理-写', 'system:role:write'),
(10221, 're_admin', 10103, '网站配置', 'system:websiteConfig'),
(10222, 're_admin', 10103, '网站配置-读', 'system:websiteConfig:read'),
(10223, 're_admin', 10103, '网站配置-写', 'system:websiteConfig:write'),
(10231, 're_admin', 10104, '系统监控', 'system:monitor');

-- 用户角色关联
INSERT INTO tr_user_role(id, user_id, role_id) VALUES
(1000, 1, 1);

-- 角色菜单关联
INSERT INTO tr_role_menu(id, role_id, menu_id) VALUES
(1000, 1, 10000),
(1001, 1, 10001),
(1002, 1, 10002),
(1003, 1, 10003),
(1004, 1, 10100),
(1005, 1, 10101),
(1006, 1, 10102),
(1007, 1, 10103),
(1008, 1, 10104);

-- 角色权限关联
INSERT INTO tr_role_permission(id, permission_id, role_id) VALUES
(1000, 10000, 1),
(1001, 10100, 1),
(1002, 10101, 1),
(1003, 10102, 1),
(1004, 10103, 1),
(1005, 10111, 1),
(1006, 10112, 1),
(1007, 10113, 1),
(1008, 10121, 1),
(1009, 10122, 1),
(1010, 10123, 1),
(1011, 10200, 1),
(1012, 10201, 1),
(1013, 10202, 1),
(1014, 10203, 1),
(1015, 10211, 1),
(1016, 10212, 1),
(1017, 10213, 1),
(1018, 10221, 1),
(1019, 10222, 1),
(1020, 10223, 1),
(1021, 10231, 1);
