-- Artalk 评论库初始化（在 docker-entrypoint-initdb.d 中第一个执行）
CREATE DATABASE IF NOT EXISTS artalk DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'artalk'@'%' IDENTIFIED BY 're#artalk2022';
GRANT ALL PRIVILEGES ON artalk.* TO 'artalk'@'%';
FLUSH PRIVILEGES;
