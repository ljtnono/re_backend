CREATE DATABASE re collate utf8mb4_general_ci;
USE re;

DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `id`          int          NOT NULL AUTO_INCREMENT COMMENT '系统配置表id',
    `description` varchar(255) DEFAULT NULL COMMENT '配置名',
    `key`         varchar(255) NOT NULL COMMENT '配置键',
    `value`       longtext     NOT NULL COMMENT '配置值',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT ='系统配置表';


INSERT INTO `sys_config`(description, `key`, value)
VALUES ('header部分LOGO图片地址', 'HEADER_LOGO_URL', 'http://f.lingjiatong.cn/rootelement/sys/header_logo.png'),
       ('网站作者网名', 'NICK_NAME', '最后的疼爱'),
       ('作者头像图片地址', 'AVATAR_URL', 'http://f.lingjiatong.cn/rootelement/sys/avatar.png'),
       ('关于作者页面个人简介描述', 'ABOUT_AUTHOR',
        'Java菜鸟一枚\n\n喜欢折腾各种技术，web、linux、数据库、前端等\n\n爱生活、爱科学、爱设计、爱编程\n\nTalk is cheap, show me the code'),
       ('邮件ICON链接地址', 'SEND_ME_EMAIL', 'https://mail.qq.com/cgi-bin/loginpage'),
       ('个人github首页', 'GITHUB_AUTHOR', 'https://github.com/ljtnono'),
       ('footer部分版权声明', 'FOOTER_COPYRIGHT',
        '本站的文章和资源来自互联网或者站长 的原创，按照 CC BY -NC -SA 3.0 CN 协议发布和共享，转载或引用本站文章 应遵循相同协议。如果有侵犯版权的资 源请尽快联系站长，我们会在24h内删 除有争议的资源。'),
       ('footer部分网站驱动图片地址，url以逗号隔开', 'FOOTER_DRIVER',
        'https://res.hc-cdn.com/cnpm-header-and-footer/2.0.6/base/header-china/components/images/logo.svg, https://img.alicdn.com/tfs/TB13DzOjXP7gK0jSZFjXXc5aXXa-212-48.png, https://labs.mysql.com/common/logos/mysql-logo.svg?v2, https://redis.com/wp-content/themes/wpx/assets/images/icon-redis.svg, https://nginx.org/nginx.png, https://tomcat.apache.org/res/images/tomcat.png'),
       ('作者微信图片地址', 'AUTHOR_WX_QRCODE_URL', 'https://f.lingjiatong.cn/re/sys/author_qrcode.png'),
       ('footer部分关于本站', 'FOOTER_ABOUT_WEBSITE', '根元素,Java,css,html,爬虫,网络,IT,技术,博客 Talk is cheap, show me the code'),
       ('网站备案号', 'WEBSITE_ICP_CODE', '鄂ICP备18013706号'),
       ('本项目github地址', 'GITHUB_WEBSITE', 'https://github.com/ljtnono/re'),
       ('作者微信号', 'AUTHOR_WX', 'a935188400'),
       ('作者qq号', 'AUTHOR_QQ', '935188400'),
       ('作者github用户名', 'AUTHOR_GITHUB_USERNAME', 'ppjt'),
       ('作者微信支付码', 'AUTHOR_WX_PAY_QRCODE_URL', 'http://f.lingjiatong.cn/rootelement/sys/wx_pay_qrcode.png'),
       ('作者支付宝支付码', 'AUTHOR_ALIPAY_PAY_QRCODE_URL', 'http://f.lingjiatong.cn/rootelement/sys/alipay_pay_qrcode.png');


DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`
(
    `id`               int          NOT NULL COMMENT '主键id，自增',
    `name`             varchar(255) NOT NULL COMMENT '定时任务名',
    `cron`             varchar(255) DEFAULT NULL COMMENT 'cron表达式',
    `create_time`      datetime     DEFAULT NULL COMMENT '创建时间',
    `modify_time`      datetime     DEFAULT NULL COMMENT '最后修改时间',
    `effective_status` tinyint(1)   NOT NULL COMMENT '启用状态0 启用 1 不启用',
    `is_deleted`       tinyint(1)   NOT NULL COMMENT '是否删除 0 正常 1 删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='定时任务表';


DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`
(
    `id`          int          NOT NULL AUTO_INCREMENT COMMENT '主键id，自增',
    `user_id`     int          DEFAULT NULL COMMENT '日志操作用户id',
    `type`        tinyint(1)   DEFAULT NULL COMMENT '日志类型 1 用户日志 2 系统日志',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `modify_time` datetime     DEFAULT NULL COMMENT '最后修改时间',
    `op_name`     varchar(255) NOT NULL COMMENT '操作名',
    `op_detail`   varchar(255) NOT NULL COMMENT '操作详情',
    `result`      tinyint(1)   DEFAULT NULL COMMENT '操作结果 1 成功  2 失败',
    `ip`          varchar(255) DEFAULT NULL COMMENT '操作者ip地址',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='系统日志';



DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`
(
    `id`          int        NOT NULL AUTO_INCREMENT COMMENT '用户消息表id',
    `user_id`     int        NOT NULL COMMENT '用户id',
    `message`     varchar(1000) DEFAULT NULL COMMENT '消息',
    `create_time` datetime   NOT NULL COMMENT '创建时间',
    `modify_time` datetime   NOT NULL COMMENT '最后修改时间',
    `status`      tinyint(1) NOT NULL COMMENT '消息状态 0 未读 1 已读',
    `is_deleted`  tinyint(1) NOT NULL COMMENT '是否删除 1 已删除 0 正常',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `fk_sys_user_message_sys_user_1` (`user_id`) USING BTREE
) ENGINE = InnoDB COMMENT ='用户消息表';


DROP TABLE IF EXISTS `sys_timeline`;
CREATE TABLE `sys_timeline`
(
    `id`          int      NOT NULL AUTO_INCREMENT COMMENT '时间轴id',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `modify_time` datetime NOT NULL COMMENT '修改时间',
    `content`     varchar(255) DEFAULT NULL COMMENT '时间轴内容',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT ='时间轴';


DROP TABLE IF EXISTS `tb_auth_permission`;
CREATE TABLE `tb_auth_permission`
(
    `id`          int         NOT NULL AUTO_INCREMENT COMMENT '权限表id',
    `name`        varchar(20) NOT NULL COMMENT '权限名',
    `type`        tinyint(1)  NOT NULL COMMENT '权限类型 0 菜单 1 具体权限或按钮',
    `parent_id`   int         NOT NULL COMMENT '父权限id，顶层父菜单为-1',
    `expression`  varchar(40) NOT NULL COMMENT '权限表达式',
    `create_time` datetime    NOT NULL COMMENT '创建时间',
    `modify_time` datetime    NOT NULL COMMENT '最后修改时间',
    `is_deleted`  tinyint(1)  NOT NULL COMMENT '是否删除 1 删除 0 正常',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6085 COMMENT ='系统权限表';


INSERT INTO `tb_auth_permission`
VALUES (1000, '博客管理', 0, -1, 'blog', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1001, '博客管理', 0, 1000, 'blog:blog', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1002, '查看博客', 1, 1001, 'blog:blog:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1003, '新增博客', 1, 1001, 'blog:blog:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1004, '修改博客', 1, 1001, 'blog:blog:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1005, '删除博客', 1, 1001, 'blog:blog:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1020, '标签管理', 0, 1000, 'blog:tag', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1021, '查看标签', 1, 1020, 'blog:tag:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1022, '新增标签', 1, 1020, 'blog:tag:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1023, '修改标签', 1, 1020, 'blog:tag:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1024, '删除标签', 1, 1020, 'blog:tag:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1040, '评论管理', 0, 1000, 'blog:comment', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1041, '查看评论', 1, 1040, 'blog:comment:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1042, '导出评论', 1, 1040, 'blog:comment:export', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (1043, '删除评论', 1, 1040, 'blog:comment:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2000, '资源管理', 0, -1, 'rs', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2001, '图片管理', 0, 2000, 'rs:image', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2002, '查看图片', 1, 2001, 'rs:image:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2003, '上传图片', 1, 2001, 'rs:image:upload', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2004, '下载图片', 1, 2001, 'rs:image:download', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2005, '删除图片', 1, 2001, 'rs:image:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2006, '更新图片', 1, 2001, 'rs:image:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2020, '链接管理', 0, 2000, 'rs:link', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2021, '查看链接', 1, 2020, 'rs:link:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2022, '新增链接', 1, 2020, 'rs:link:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2023, '更新链接', 1, 2020, 'rs:link:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (2024, '删除链接', 1, 2020, 'rs:link:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (3000, '时间轴管理', 0, -1, 'timeline', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (3001, '查看时间轴', 1, 3000, 'timeline:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (3002, '删除时间轴', 1, 3000, 'timeline:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (3003, '修改时间轴', 1, 3000, 'timeline:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (4000, '技能管理', 0, -1, 'skill', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (4001, '查看技能', 1, 4000, 'skill:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (4002, '新增技能', 1, 4000, 'skill:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (4003, '删除技能', 1, 4000, 'skill:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (4004, '修改技能', 1, 4000, 'skill:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (5000, '消息管理', 0, -1, 'message', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (5001, '消息管理', 0, 5000, 'message:message', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (5002, '查看消息', 1, 5001, 'message:message:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (5003, '新增消息', 1, 5001, 'message:message:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (5004, '删除消息', 1, 5001, 'message:message:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (5005, '修改消息', 1, 5001, 'message:message:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (5020, '回收站管理', 0, 5000, 'message:recycle', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (5021, '查看回收站', 1, 5020, 'message:recycle:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (5022, '删除回收站消息', 1, 5020, 'message:recycle:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (5023, '恢复回收站消息', 1, 5020, 'message:recycle:recover', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6000, '系统管理', 0, -1, 'system', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6001, '用户管理', 0, 6000, 'system:user', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6002, '查看用户', 1, 6001, 'system:user:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6003, '新增用户', 1, 6001, 'system:user:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6004, '删除用户', 1, 6001, 'system:user:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6005, '修改用户', 1, 6001, 'system:user:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6020, '角色管理', 0, 6000, 'system:role', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6021, '查看角色', 1, 6020, 'system:role:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6022, '新增角色', 1, 6020, 'system:role:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6023, '删除角色', 1, 6020, 'system:role:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6024, '修改角色', 1, 6020, 'system:role:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6040, '权限管理', 0, 6000, 'system:permission', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6041, '查看权限', 1, 6040, 'system:permission:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6042, '新增权限', 1, 6040, 'system:permission:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6043, '删除权限', 1, 6040, 'system:permission:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6044, '修改权限', 1, 6040, 'system:permission:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6060, '日志管理', 0, 6000, 'system:log', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6061, '查看日志', 1, 6060, 'system:log:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6062, '新增日志', 1, 6060, 'system:log:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6063, '删除日志', 1, 6060, 'system:log:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6064, '导出日志', 1, 6060, 'system:log:export', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6080, '网站配置', 0, 6000, 'system:config', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6081, '查看配置', 1, 6080, 'system:config:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6082, '新增配置', 1, 6080, 'system:config:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6083, '删除配置', 1, 6080, 'system:config:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0),
       (6084, '修改配置', 1, 6080, 'system:config:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);


DROP TABLE IF EXISTS `tb_auth_role`;
CREATE TABLE `tb_auth_role`
(
    `id`          int                                                    NOT NULL AUTO_INCREMENT COMMENT '角色表id',
    `name`        varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名',
    `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
    `create_time` datetime                                               NOT NULL COMMENT '创建时间',
    `modify_time` datetime                                               NOT NULL COMMENT '最后修改时间',
    `is_deleted`  tinyint(1)                                             NOT NULL COMMENT '是否删除 1 删除 0 正常',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4 COMMENT ='系统角色表';

INSERT INTO `tb_auth_role`
VALUES (1, '超级管理员', '拥有所有权限', '2020-08-24 00:30:21', '2020-08-24 00:30:21', 0),
       (2, '测试', '拥有除了系统管理之外的权限', '2020-08-24 00:30:21', '2020-08-24 00:30:21', 0),
       (3, '游客', '只拥有各模块查看权限', '2020-08-24 00:30:21', '2020-08-24 00:30:21', 0);



DROP TABLE IF EXISTS `tb_auth_user`;
CREATE TABLE `tb_auth_user`
(
    `id`          int         NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `username`    varchar(30) NOT NULL COMMENT '用户名',
    `password`    varchar(32) NOT NULL COMMENT '密码',
    `phone`       varchar(20)  DEFAULT NULL COMMENT '手机号码',
    `email`       varchar(20) NOT NULL COMMENT '用户邮箱',
    `create_time` datetime    NOT NULL COMMENT '创建时间',
    `modify_time` datetime    NOT NULL COMMENT '最后修改时间',
    `is_deleted`  tinyint(1)  NOT NULL COMMENT '是否删除 1 删除 0 正常',
    `avatar_url`  varchar(255) DEFAULT NULL COMMENT '用户头像访问url',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `username_deleted` (`username`, `is_deleted`) USING BTREE COMMENT '不能同时存在两个同名用户',
    KEY `username_phone_email` (`username`, `phone`, `email`) USING BTREE COMMENT '模糊查询索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 3 COMMENT ='用户表';


INSERT INTO `tb_auth_user`
VALUES (1, 'lingjiatong', '80cea81e681679a81634e2b1846e6cb8', '16333333333', '935188400@qq.com', '2020-08-24 00:42:24',
        '2021-03-10 07:43:03', 0, 'https://ftp.ljtnono.cn/re/images/avatar.png'),
       (2, 'ljtnono', '80cea81e681679a81634e2b1846e6cb8', '15337106753', '935188400@qq.com', '2020-11-20 23:25:33',
        '2021-05-18 02:04:51', 1, 'https://ftp.ljtnono.cn/re/images/avatar.png');

DROP TABLE IF EXISTS `tb_blog_article`;
CREATE TABLE `tb_blog_article`
(
    `id`               int         NOT NULL AUTO_INCREMENT COMMENT '博客文章id',
    `title`            varchar(50) NOT NULL COMMENT '博客文章标题',
    `summary`          varchar(500)         DEFAULT NULL COMMENT '博客文章简介信息',
    `markdown_content` longtext COMMENT '博客文章markdown内容信息',
    `create_time`      datetime    NOT NULL COMMENT '博客文章创建时间',
    `modify_time`      datetime    NOT NULL COMMENT '博客文章修改时间',
    `is_deleted`       tinyint(1)  NOT NULL COMMENT '是否删除 1 删除  0正常',
    `type_id`          int                  DEFAULT NULL COMMENT '博客分类id',
    `user_id`          int                  DEFAULT NULL COMMENT '所属用户id，如果为-1，表示匿名用户',
    `cover_url`        varchar(255)         DEFAULT NULL COMMENT '封面图路径',
    `view`             int         NOT NULL DEFAULT '0' COMMENT '浏览量',
    `favorite`         int         NOT NULL DEFAULT '0' COMMENT '喜欢数',
    `is_draft`         tinyint(1)  NOT NULL COMMENT '是否是草稿 0 不是 1 是',
    `is_recommend`     tinyint(1)  NOT NULL COMMENT '是否推荐 0 不是 1 是',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `fk_blog_article_sys_user_1` (`user_id`) USING BTREE,
    KEY `fk_blog_article_blog_article_tag_1` (`type_id`) USING BTREE,
    KEY `fk_blog_article_rs_image_1` (`cover_url`) USING BTREE
) ENGINE = InnoDB COMMENT ='博客文章表';


DROP TABLE IF EXISTS `tb_blog_comment`;
CREATE TABLE `tb_blog_comment`
(
    `id`          int        NOT NULL AUTO_INCREMENT COMMENT '博客评论id',
    `article_id`  int        NOT NULL COMMENT '评论所属文章id',
    `text`        varchar(1000) DEFAULT NULL COMMENT '评论内容',
    `user_id`     int           DEFAULT NULL COMMENT '评论的用户id，如果为null表示匿名评论',
    `ip`          varchar(32)   DEFAULT NULL COMMENT '用户评论的ip地址',
    `create_time` datetime   NOT NULL COMMENT '创建时间',
    `modify_time` datetime   NOT NULL COMMENT '修改时间',
    `is_deleted`  tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `fk_blog_comment_blog_article_1` (`article_id`) USING BTREE
) ENGINE = InnoDB COMMENT ='博客评论表';


DROP TABLE IF EXISTS `tb_blog_tag`;
CREATE TABLE `tb_blog_tag`
(
    `id`   int         NOT NULL AUTO_INCREMENT COMMENT '标签id，自增',
    `name` varchar(20) NOT NULL COMMENT '标签名',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='博客标签';


DROP TABLE IF EXISTS `tb_blog_type`;
CREATE TABLE `tb_blog_type`
(
    `id`           int         NOT NULL AUTO_INCREMENT COMMENT '博客分类id',
    `name`         varchar(20) NOT NULL COMMENT '分类名，不超过20个字符',
    `create_time`  datetime    NOT NULL COMMENT '创建时间',
    `modify_time`  datetime    NOT NULL COMMENT '修改时间',
    `is_deleted`   tinyint(1)  NOT NULL COMMENT '是否删除 0 正常 1 已删除',
    `view`         int DEFAULT NULL COMMENT '类型总浏览量',
    `favorite`     int DEFAULT NULL COMMENT '类型总喜欢数',
    `is_recommend` tinyint(1)  NOT NULL COMMENT '是否推荐类型',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT ='博客标签表';


DROP TABLE IF EXISTS `tb_resource_book`;
CREATE TABLE `tb_resource_book`
(
    `id`           int NOT NULL COMMENT '主键id，自增',
    `name`         varchar(255) DEFAULT NULL COMMENT '书名',
    `author`       varchar(255) DEFAULT NULL COMMENT '作者',
    `publish_date` datetime     DEFAULT NULL COMMENT '出版日期',
    `category`     varchar(255) DEFAULT NULL COMMENT '分类',
    `download_url` varchar(255) DEFAULT NULL COMMENT '下载地址',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='书籍';



DROP TABLE IF EXISTS `tb_resource_git_repository`;
CREATE TABLE `tb_resource_git_repository`
(
    `id`           int          NOT NULL COMMENT '主键id，自增',
    `name`         varchar(255) NOT NULL COMMENT '仓库名',
    `url`          varchar(255) NOT NULL COMMENT '仓库地址',
    `star_num`     int          DEFAULT NULL COMMENT '仓库star数量',
    `fork_num`     int          DEFAULT NULL COMMENT '仓库fork数量',
    `watch_num`    int          DEFAULT NULL COMMENT '仓库watch数量',
    `language`     varchar(255) DEFAULT NULL COMMENT '主要语言',
    `platform`     tinyint(1)   DEFAULT NULL COMMENT '平台 1 github 2 gitee',
    `is_recommend` tinyint(1)   DEFAULT NULL COMMENT '是否推荐 0 是 1 不是',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='git仓库';

DROP TABLE IF EXISTS `tb_resource_image`;
CREATE TABLE `tb_resource_image`
(
    `id`          int          NOT NULL AUTO_INCREMENT COMMENT '图片表id',
    `image_id`    varchar(32)  NOT NULL COMMENT '图片id（UUID 32位）',
    `origin_name` varchar(255) NOT NULL COMMENT '原图片名（包含扩展名）',
    `url`         varchar(255) NOT NULL COMMENT '图片的url地址',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `modify_time` datetime     DEFAULT NULL COMMENT '修改时间',
    `size`        bigint       DEFAULT NULL COMMENT '图片大小',
    `save_path`   varchar(255) DEFAULT NULL COMMENT '图片的存储路径',
    `md5`         varchar(255) DEFAULT NULL COMMENT '图片md5值',
    `suffix`      varchar(255) DEFAULT NULL COMMENT '图片后缀名',
    `type`        tinyint(1)   DEFAULT NULL COMMENT '图片类型 1 博客类型 2 全局配置',
    `is_deleted`  tinyint(1)   DEFAULT NULL COMMENT '是否删除 0 正常 1 已删除',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `save_path` (`save_path`),
    KEY `md5` (`md5`)
) ENGINE = InnoDB COMMENT ='图片表';


DROP TABLE IF EXISTS `tb_resource_link`;
CREATE TABLE `tb_resource_link`
(
    `id`              int          NOT NULL AUTO_INCREMENT COMMENT '链接id',
    `url`             varchar(255) NOT NULL COMMENT '链接所指向的url',
    `title`           varchar(255) DEFAULT NULL COMMENT '链接标题',
    `article_id`      int          DEFAULT NULL COMMENT '引用博客id（只有当是博客链接时存在此字段）',
    `is_friend_link`  tinyint(1)   DEFAULT NULL COMMENT '是否是友链 0 是 1 不是',
    `is_invalid_link` tinyint(1)   DEFAULT NULL COMMENT '是否是无效链接 0 是 1 不是',
    `create_time`     datetime     DEFAULT NULL COMMENT '创建时间',
    `modify_time`     datetime     DEFAULT NULL COMMENT '修改时间',
    `icon_url`        varchar(255) DEFAULT NULL COMMENT '连接tab栏图标',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT ='链接表';


DROP TABLE IF EXISTS `tb_resource_music`;
CREATE TABLE `tb_resource_music`
(
    `id`  int NOT NULL COMMENT '主键，自增',
    `url` varchar(255) DEFAULT NULL COMMENT '访问地址',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='音乐表';


DROP TABLE IF EXISTS `tr_auth_role_permission`;
CREATE TABLE `tr_auth_role_permission`
(
    `id`            int NOT NULL AUTO_INCREMENT COMMENT '角色权限表id',
    `role_id`       int NOT NULL COMMENT '角色id',
    `permission_id` int NOT NULL COMMENT '权限id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `fk_sys_role_permission_sys_role_1` (`role_id`) USING BTREE,
    KEY `fk_sys_role_permission_sys_permission_1` (`permission_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 73 COMMENT ='角色权限表';
INSERT INTO `tr_auth_role_permission`
VALUES (1, 1, 1000),
       (2, 1, 1001),
       (3, 1, 1002),
       (4, 1, 1003),
       (5, 1, 1004),
       (6, 1, 1005),
       (7, 1, 1020),
       (8, 1, 1021),
       (9, 1, 1022),
       (10, 1, 1023),
       (11, 1, 1024),
       (12, 1, 1040),
       (13, 1, 1041),
       (14, 1, 1042),
       (15, 1, 1043),
       (16, 1, 2000),
       (17, 1, 2001),
       (18, 1, 2002),
       (19, 1, 2003),
       (20, 1, 2004),
       (21, 1, 2005),
       (22, 1, 2006),
       (23, 1, 2020),
       (24, 1, 2021),
       (25, 1, 2022),
       (26, 1, 2023),
       (27, 1, 2024),
       (28, 1, 3000),
       (29, 1, 3001),
       (30, 1, 3002),
       (31, 1, 3003),
       (32, 1, 4000),
       (33, 1, 4001),
       (34, 1, 4002),
       (35, 1, 4003),
       (36, 1, 4004),
       (37, 1, 5000),
       (38, 1, 5001),
       (39, 1, 5002),
       (40, 1, 5003),
       (41, 1, 5004),
       (42, 1, 5005),
       (43, 1, 5020),
       (44, 1, 5021),
       (45, 1, 5022),
       (46, 1, 5023),
       (47, 1, 6000),
       (48, 1, 6001),
       (49, 1, 6002),
       (50, 1, 6003),
       (51, 1, 6004),
       (52, 1, 6005),
       (53, 1, 6020),
       (54, 1, 6021),
       (55, 1, 6022),
       (56, 1, 6023),
       (57, 1, 6024),
       (58, 1, 6040),
       (59, 1, 6041),
       (60, 1, 6042),
       (61, 1, 6043),
       (62, 1, 6044),
       (63, 1, 6060),
       (64, 1, 6061),
       (65, 1, 6062),
       (66, 1, 6063),
       (67, 1, 6064),
       (68, 1, 6080),
       (69, 1, 6081),
       (70, 1, 6082),
       (71, 1, 6083),
       (72, 1, 6084);


DROP TABLE IF EXISTS `tr_auth_user_role`;
CREATE TABLE `tr_auth_user_role`
(
    `id`      int NOT NULL AUTO_INCREMENT COMMENT '用户角色表id',
    `user_id` int NOT NULL COMMENT '用户id',
    `role_id` int NOT NULL COMMENT '角色id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `fk_sys_user_role_sys_user_1` (`user_id`) USING BTREE,
    KEY `fk_sys_user_role_sys_role_1` (`role_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3 COMMENT ='用户角色表';
INSERT INTO `tr_auth_user_role`
VALUES (1, 1, 1),
       (2, 2, 2);


DROP TABLE IF EXISTS `tr_blog_article_tag`;
CREATE TABLE `tr_blog_article_tag`
(
    `id`         int NOT NULL COMMENT '博客标签关联表id，自增',
    `article_id` int NOT NULL COMMENT '文章id',
    `tag_id`     int NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='文章标签关联表';
