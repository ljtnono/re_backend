SET NAMES utf8mb4;
USE re_sys;
-- 权限、菜单、路由相关表初始化sql脚本

-- 路由表
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
-- re_admin
(10000, 're_admin', -1, 'Root', 're_admin根路径重定向到登录页面', '/', '{"title":"主页","hideInMenu":true}', '{"name":"Login"}', NULL, NULL),
(10001, 're_admin', -1, 'Login', 're_admin登录页面', '/login', '{"title":"Login - 登录","hideInMenu":true}', NULL, NULL, NULL),
(10002, 're_admin', -1, '404', 're_admin的404页面', '*', '{"title":"404","name":"找不到页面","hideInMenu":true}', NULL, NULL, NULL),
-- re_admin主页面路由
(10100, 're_admin', -1, 'Home', 're_admin主页面', '/home', '{"title":"主页","hideInMenu":true}', NULL, NULL, NULL),
(10101, 're_admin', 10100, 'UpdatePassword', 're_admin修改密码页面', '/updatePassword', '{"title":"修改密码","hideInMenu":true}', NULL, NULL, NULL),
(10102, 're_admin', 10100, 'WriteArticle', 're_admin写文章页面', '/writeArticle', '{"title":"写文章","hideInMenu":true}', NULL, NULL, NULL),
(10103, 're_admin', 10100, 'Personal', 're_admin个人中心页面', '/personal', '{"title":"个人中心","hideInMenu":true}', NULL, NULL, NULL),
(10104, 're_admin', 10100, 'Workspace', 're_admin工作台页面', '/workspace', '{"title":"工作台","hideInMenu":true}', NULL, NULL, NULL),
-- re_admin博客管理相关
(10105, 're_admin', 10100, 'BlogArticle', 're_admin文章管理页面', '/blog/article', '{"title":"文章管理","hideInMenu":true}', NULL, NULL, NULL),
(10106, 're_admin', 10100, 'BlogCategory', 're_admin分类管理页面', '/blog/category', '{"title":"分类管理","hideInMenu":true}', NULL, NULL, NULL),
(10107, 're_admin', 10100, 'BlogComment', 're_admin评论管理页面', '/blog/comment', '{"title":"评论管理","hideInMenu":true}', NULL, NULL, NULL),
-- re_admin系统管理相关
(10108, 're_admin', 10100, 'SystemUser', 're_admin用户管理页面', '/system/user', '{"title":"用户管理","hideInMenu":true}', NULL, NULL, NULL),
(10109, 're_admin', 10100, 'SystemRole', 're_admin角色管理页面', '/system/role', '{"title":"角色管理","hideInMenu":true}', NULL, NULL, NULL),
(10110, 're_admin', 10100, 'SystemWebsiteConfig', 're_admin网站配置页面', '/system/websiteConfig', '{"title":"网站配置","hideInMenu":true}', NULL, NULL, NULL),
(10111, 're_admin', 10100, 'SystemMonitor', 're_admin系统监控页面', '/system/monitor', '{"title":"系统监控","hideInMenu":true}', NULL, NULL, NULL)

-- re_frontend

;

-- 菜单表
DROP TABLE IF EXISTS menu;
CREATE TABLE menu (
    id BIGINT NOT NULL PRIMARY KEY COMMENT '主键id',
    project_name VARCHAR(100) NOT NULL COMMENT '菜单所属项目名称',
    parent_id BIGINT NOT NULL COMMENT '父菜单id，没有则为-1',
    name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    title VARCHAR(50) NOT NULL COMMENT '菜单标题',
    icon VARCHAR(50) NULL COMMENT '菜单的图标',
    path VARCHAR(200) NULL COMMENT '菜单的路径'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单表';

INSERT INTO menu(id, project_name, parent_id, name, title, icon, path) VALUES
-- re_admin相关菜单
(10000, 're_admin', -1, 'Blog', '博客管理', 'icon-blog', NULL),
(10001, 're_admin', 10000, 'BlogArticle', '文章管理', NULL, '/blog/article'),
(10002, 're_admin', 10000, 'BlogCategory', '分类管理', NULL, '/blog/category'),
(10003, 're_admin', 10000, 'BlogComment', '评论管理', NULL, '/blog/comment'),
(10100, 're_admin', -1, 'System', '系统管理', 'icon-setting', NULL),
(10101, 're_admin', 10100, 'SystemUser', '用户管理', NULL, '/system/user'),
(10102, 're_admin', 10100, 'SystemRole', '角色管理', NULL, '/system/role'),
(10103, 're_admin', 10100, 'SystemWebsiteConfig', '网站配置', NULL, '/system/websiteConfig'),
(10104, 're_admin', 10100, 'SystemMonitor', '系统监控', NULL, '/system/monitor')

-- re_frontend相关菜单
;

-- 用户表
DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id          BIGINT       NOT NULL COMMENT '主键id，雪花算法' PRIMARY KEY,
    username    VARCHAR(64)  NOT NULL COMMENT '用户名，4-20位字符串只允许英文和数字下划线',
    password    VARCHAR(32)  NOT NULL COMMENT '密码，md5加密形式',
    phone       VARCHAR(20)  NULL COMMENT '手机号码',
    email       VARCHAR(20)  NOT NULL COMMENT '用户邮箱',
    avatar_url  LONGTEXT     NULL COMMENT '用户头像访问url',
    create_time DATETIME     NOT NULL COMMENT '创建时间',
    modify_time DATETIME     NOT NULL COMMENT '最后修改时间',
    is_deleted  TINYINT(1)   NOT NULL COMMENT '是否删除 1 删除 0 正常',
    CONSTRAINT uidx_email UNIQUE (email),
    CONSTRAINT uidx_username UNIQUE (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

INSERT INTO user(id, username, password, phone, email, avatar_url, create_time, modify_time, is_deleted) VALUES
(1, 'lingjiatong', '80cea81e681679a81634e2b1846e6cb8', '15337106753', '935188400@qq.com', 'http://f.lingjiatong.cn:30090/rootelement/sys/avatar.jpeg', '2020-08-24 00:42:24', '2020-08-24 00:42:24', 0)

;

-- 角色表
DROP TABLE IF EXISTS role;
CREATE TABLE role (
    id          BIGINT       NOT NULL COMMENT '主键id，雪花算法' PRIMARY KEY,
    name        VARCHAR(30)  NOT NULL COMMENT '角色名',
    description VARCHAR(200) NULL COMMENT '角色描述',
    create_time DATETIME     NOT NULL COMMENT '创建时间',
    modify_time DATETIME     NOT NULL COMMENT '最后修改时间',
    is_deleted  TINYINT(1)   NOT NULL COMMENT '是否删除 1 删除 0 正常',
    CONSTRAINT uidx_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

INSERT INTO role(id, name, description, create_time, modify_time, is_deleted) VALUES
(1, '超级管理员', '拥有所有权限', '2020-08-24 00:30:21', '2020-08-24 00:30:21', 0)

;

-- 权限表
DROP TABLE IF EXISTS permission;
CREATE TABLE permission (
    id BIGINT NOT NULL PRIMARY KEY COMMENT '主键id',
    parent_id BIGINT NOT NULL COMMENT '父权限id，不存在则为-1',
    project_name VARCHAR(100) NOT NULL COMMENT '权限所属项目名称',
    menu_id BIGINT NOT NULL COMMENT '权限所属菜单id，不存在则为-1',
    name LONGTEXT NOT NULL COMMENT '权限名称',
    type TINYINT(1) NOT NULL COMMENT '权限类型 0 菜单项 1 具体某个权限',
    expression VARCHAR(200) NOT NULL COMMENT '权限表达式',
    create_time DATETIME    NOT NULL COMMENT '创建时间',
    modify_time DATETIME    NOT NULL COMMENT '最后修改时间',
    is_deleted  TINYINT(1)  NOT NULL COMMENT '是否删除 1 删除 0 正常'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限表';

INSERT INTO permission(id, parent_id, project_name, menu_id, name, type, expression, create_time, modify_time, is_deleted) VALUES
-- re_admin相关权限
(10000, -1, 're_admin', -1, '工作台', 0, 'workspace', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
-- 博客管理相关
(10100, -1, 're_admin', 10000, '博客管理', 0, 'blog', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
-- 文章管理相关
(10101, 10100, 're_admin', 10001, '文章管理', 0, 'blog:article', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10102, 10101, 're_admin', 10001, '文章管理-读', 1, 'blog:article:read', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10103, 10101, 're_admin', 10001, '文章管理-写', 1, 'blog:article:write', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
-- 分类管理相关
(10111, 10100, 're_admin', 10002, '分类管理', 0, 'blog:category', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10112, 10111, 're_admin', 10002, '分类管理-读', 1, 'blog:category:read', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10113, 10111, 're_admin', 10002, '分类管理-写', 1, 'blog:category:write', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
-- 评论管理相关
(10121, 10100, 're_admin', 10003, '评论管理', 0, 'blog:comment', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10122, 10121, 're_admin', 10003, '评论管理-读', 1, 'blog:comment:read', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10123, 10121, 're_admin', 10003, '评论管理-写', 1, 'blog:comment:write', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),


-- 系统管理相关
(10200, -1, 're_admin', 10100, '系统管理', 0, 'system', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
-- 用户管理相关
(10201, 10200, 're_admin', 10101, '用户管理', 0, 'system:user', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10202, 10201, 're_admin', 10101, '用户管理-读', 1, 'system:user:read', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10203, 10201, 're_admin', 10101, '用户管理-写', 1, 'system:user:write', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
-- 角色管理相关
(10211, 10200, 're_admin', 10102, '角色管理', 0, 'system:role', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10212, 10211, 're_admin', 10102, '角色管理-读', 1, 'system:role:read', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10213, 10211, 're_admin', 10102, '角色管理-写', 1, 'system:role:write', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
-- 网站配置相关
(10221, 10200, 're_admin', 10103, '网站配置', 0, 'system:websiteConfig', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10222, 10221, 're_admin', 10103, '网站配置-读', 1, 'system:websiteConfig:read', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
(10223, 10221, 're_admin', 10103, '网站配置-写', 1, 'system:websiteConfig:write', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0),
-- 系统监控相关
(10231, 10200, 're_admin', 10104, '系统监控', 0, 'system:monitor', '2022-12-29 18:00:00', '2022-12-29 18:00:00', 0)

-- re_frontend相关权限
;

-- 用户角色表
DROP TABLE IF EXISTS tr_user_role;
CREATE TABLE tr_user_role (
    id      BIGINT NOT NULL COMMENT '主键id，雪花算法' PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户id',
    role_id BIGINT NOT NULL COMMENT '角色id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色表';

INSERT INTO tr_user_role(id, user_id, role_id) VALUES
(1000, 1, 1)
;

-- 角色权限表
DROP TABLE IF EXISTS tr_role_permission;
CREATE TABLE tr_role_permission (
    id            BIGINT NOT NULL COMMENT '主键id，雪花算法' PRIMARY KEY,
    permission_id BIGINT NOT NULL COMMENT '权限id',
    role_id       BIGINT NOT NULL COMMENT '角色id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限表';

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

(1021, 10231, 1)

;

-- 角色菜单表
DROP TABLE IF EXISTS tr_role_menu;
CREATE TABLE tr_role_menu (
    id            BIGINT NOT NULL COMMENT '主键id，雪花算法' PRIMARY KEY,
    role_id       BIGINT NOT NULL COMMENT '角色id',
    menu_id       BIGINT NOT NULL COMMENT '菜单id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单表';

INSERT INTO tr_role_menu(id, role_id, menu_id) VALUES
(1000, 1, 10000),
(1001, 1, 10001),
(1002, 1, 10002),
(1003, 1, 10003),
(1004, 1, 10100),
(1005, 1, 10101),
(1006, 1, 10102),
(1007, 1, 10103),
(1008, 1, 10104)
;


-- oauth 认证表

-- auto-generated definition
DROP TABLE IF EXISTS oauth_client_details;
CREATE TABLE oauth_client_details
(
    client_id               VARCHAR(256) CHARSET utf8  NOT NULL PRIMARY KEY,
    resource_ids            VARCHAR(256) CHARSET utf8  NULL,
    client_secret           VARCHAR(256) CHARSET utf8  NULL,
    scope                   VARCHAR(256) CHARSET utf8  NULL,
    authorized_grant_types  VARCHAR(256) CHARSET utf8  NULL,
    web_server_redirect_uri VARCHAR(256) CHARSET utf8  NULL,
    authorities             VARCHAR(256) CHARSET utf8  NULL,
    access_token_validity   INT                        NULL,
    refresh_token_validity  INT                        NULL,
    additional_information  VARCHAR(4096) CHARSET utf8 NULL,
    autoapprove             VARCHAR(256) CHARSET utf8  NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'oauth2客户端表';

INSERT INTO oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES
('re_admin', NULL, 'e10adc3949ba59abbe56e057f20f883e', 'all', 'password,refresh_token,verify_code', '', NULL, NULL, NULL, NULL, NULL)
;


