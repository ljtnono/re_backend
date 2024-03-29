<h1 align="center">ROOTELEMENT 根元素个人博客后端</h1>

博客访问地址：[http://re.lingjiatong.cn:30150](http://re.lingjiatong.cn:30150)

这是一个使用SpringBoot作为后端，前端使用vue.js的博客系统，使用的是mavon-editor插件作为markdown文本编辑器<br>
本博客部分内容参考温志怀博客和思欲主题，详情请移步  [温志怀博客](http://www.wenzhihuai.com) | [欲思主题](https://yusi123.com/) | [GitCafe](https://gitcafe.net/) | [崔庆才博客](https://cuiqingcai.com/)<br>

<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/ljtnono/re_backend.svg)](https://github.com/ljtnono/re_backend/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/ljtnono/re_backend.svg)](https://github.com/ljtnono/re_backend/network)
[![GitHub issues](https://img.shields.io/github/issues/ljtnono/re_backend.svg)](https://github.com/ljtnono/re_backend/issues)

</div>

<div align="center">
    <img src="https://avatars.githubusercontent.com/u/37091714?v=4" style="border-radius: 50% !important; width: 260px; height: 260px;"/>
</div>


## 后端项目说明

本项目为博客后端接口项目，使用的是微服务架构，主要技术栈是SpringCloud alibaba，mybatis-plus，mysql，minio，elasticsearch等。

### 项目目录结构

```txt

re_backend  
├── api                                         // API微服务
├──  └── api-frontend                           // 前端页面接口微服务
├──  └── api-backend                            // 后端管理系统页面接口微服务
├──  └── api-file                               // 文件服务接口微服务
├── project                                     // 项目文档和脚本
│    └── doc                                    // 项目文档
│    └── k8s                                    // k8s部署文件
│    └── shell                                  // 脚本
│    └── sql                                    // sql文件
├── re-common                                   // 通用模块
├── re-gateway                                  // 网关微服务
├── re-job                                      // 定时任务微服务
├── service
├──  │── re-service-article                     // 博客文章模块
├──  │      └── re-service-article-api          // 博客文章微服务feign接口层
├──  │      └── re-service-article-server       // 博客文章微服务应用层
├──  │── re-service-sys                         // 系统相关模块
├──         └── re-service-sys-api              // 系统模块feign接口层
├──         └── re-service-sys-server           // 系统模块应用层

```

### 微服务端口设定

| 防火墙共享名称 | 描述              | k8s服务名称                   | k8s名称空间 | port（service端口号） | nodePort（节点端口） | targetPort（容器端口） | spring cloud微服务名称    | spring cloud微服务描述 | 开发环境端口号 | 防火墙暴露端口号 |
| -------------- | ----------------- |---------------------------| ----------- | --------------------- |----------------| ---------------------- | ------------------------- |-------------------|:--------| ---------------- |
| RE-GATEWAY     | RE博客API网关服务 | re-gateway                | rootelement | 8080                  | 30152          | 8080                   | re-gateway                | API网关             | 8152    | 30152            |
| -              | -                 | api-frontend              | rootelement | 8080                  | 30153          | 8080                   | api-frontend              | 博客前端接口            | 8153    | -                |
| -              | -                 | api-backend               | rootelement | 8080                  | 30154          | 8080                   | api-backend               | 博客后台管理接口          | 8154    | -                |
| -              | -                 | api-file                  | rootelement | 8080                  | 30157          | 8080                   | api-backend               | 博客文件服务管理接口        | 8157    | -                |
| -              | -                 | re-auth                   | rootelement | 8080                  | 30155          | 8080                   | re-auth                   | 认证微服务             | 8155    | -                |
| -              | -                 | re-job                    | rootelement | 8080                  | 30156          | 8080                   | re-job                    | 定时任务微服务           | 8156    | -                |
| -              | -                 | re-service-article-server | rootelement | 8080                  | 30200          | 8080                   | re-service-article-server | 文章微服务             | 8200    | -                |
| -              | -                 | re-service-sys-server     | rootelement | 8080                  | 30201          | 8080                   | re-service-sys-server     | 系统设置微服务           | 8201    | -                |
