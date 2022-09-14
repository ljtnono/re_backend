USE re;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 博客类型初始化
DROP TABLE IF EXISTS `tb_blog_type`;
CREATE TABLE `tb_blog_type`
(
    id           INT COMMENT '博客分类id' PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(20) NOT NULL COMMENT '分类名，不超过20个字符',
    create_time  DATETIME    NOT NULL COMMENT '创建时间',
    modify_time  DATETIME    NOT NULL COMMENT '修改时间',
    is_deleted   TINYINT(1)  NOT NULL COMMENT '是否删除 0 正常 1 已删除',
    view         INT         NULL COMMENT '类型总浏览量',
    favorite     INT         NULL COMMENT '类型总喜欢数'
) COMMENT '博客标签表';

INSERT INTO tb_blog_type(name, create_time, modify_time, is_deleted, view, favorite) VALUES
('技术文章', NOW(), NOW(), 0, 0, 0),
('学习生活', NOW(), NOW(), 0, 0, 0);

SET FOREIGN_KEY_CHECKS = 1;