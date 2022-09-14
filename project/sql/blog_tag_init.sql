USE re;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 博客标签初始化
DROP TABLE IF EXISTS `tb_blog_tag`;
CREATE TABLE `tb_blog_tag`
(
    id   INT         NOT NULL COMMENT '标签id，自增' PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL COMMENT '标签名'
)COMMENT '博客标签';

INSERT INTO `tb_blog_tag`(name) VALUES
('java'),('python'),('shell'),('mysql'),('docker'),('k8s'),('go'),('elasticsearch'),('redis'),('spring'),('springboot'),('spring cloud'),('mybatis'),('mybatis plus'),('富文本'),('脚本'),('爬虫'),('jvm'),('监控'),('并发'),('容器');

SET FOREIGN_KEY_CHECKS = 1;