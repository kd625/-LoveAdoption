# 爱心领养平台 (Love Adoption)

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.0-brightgreen" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MyBatis--Plus-3.5.1-blue" alt="MyBatis-Plus">
  <img src="https://img.shields.io/badge/Redis-latest-red" alt="Redis">
  <img src="https://img.shields.io/badge/MySQL-latest-orange" alt="MySQL">
</p>

## 项目介绍

爱心领养平台是一个专注于动物领养服务的在线平台，旨在为需要家庭的动物和有爱心的领养者之间搭建桥梁。平台提供了动物信息发布、领养申请、用户管理等功能，帮助流浪动物找到温暖的家，同时为爱心人士提供便捷的领养渠道。

本项目采用前后端分离架构，后端基于Spring Boot框架开发，提供RESTful API接口服务，前端可以通过这些接口获取数据并展示给用户。

## 功能特点

### 动物管理
- 动物信息发布：用户可以发布需要被领养的动物信息，包括名称、物种、年龄、性别、健康状况等
- 动物信息查询：支持多条件筛选查询，如按物种、年龄、性别等筛选
- 动物信息管理：支持对已发布的动物信息进行修改和删除

### 领养申请
- 领养申请提交：用户可以提交领养申请，说明领养理由
- 申请状态跟踪：用户可以查看自己的申请状态（待审核、通过、拒绝等）
- 申请审核：管理员可以审核领养申请，并提供审核意见

### 用户系统
- 用户注册登录：支持用户注册、登录功能
- 用户权限管理：区分普通用户和管理员权限
- 个人信息管理：用户可以管理自己的个人信息

### 其他功能
- 文件上传：支持图片上传功能，可用于上传动物照片
- 数据统计：提供平台数据统计功能，如领养成功率、动物分布等

## 技术栈

### 后端
- **核心框架**：Spring Boot 2.7.0
- **ORM框架**：MyBatis-Plus 3.5.1
- **数据库**：MySQL
- **缓存**：Redis
- **会话管理**：Spring Session with Redis
- **API文档**：Knife4j 3.0.3 (基于Swagger)
- **权限控制**：自定义注解实现
- **文件存储**：阿里云OSS
- **工具库**：Lombok, Apache Commons Lang3, Gson

### 开发环境
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis 6.0+

## 快速开始

### 环境准备
1. 安装JDK 1.8+
2. 安装Maven 3.6+
3. 安装MySQL 5.7+
4. 安装Redis 6.0+

### 数据库配置
1. 创建名为`LoveAdoption`的数据库
2. 导入项目根目录下的`sql`文件夹中的SQL脚本

### 项目配置
1. 修改`src/main/resources/application.yml`中的数据库连接信息
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/LoveAdoption
       username: your_username
       password: your_password
   ```

2. 配置Redis连接信息
   ```yaml
   spring:
     redis:
       host: localhost
       port: 6379
       database: 3
   ```

3. 配置阿里云OSS（如需使用文件上传功能）
   ```yaml
   la:
     alioss:
       access-key-id: your_access_key_id
       access-key-secret: your_access_key_secret
       endpoint: your_endpoint
       bucket-name: your_bucket_name
   ```

### 启动项目
1. 克隆项目到本地
   ```bash
   git clone https://github.com/yourusername/love-adoption.git
   ```

2. 进入项目目录
   ```bash
   cd love-adoption
   ```

3. 编译项目
   ```bash
   mvn clean package
   ```

4. 运行项目
   ```bash
   java -jar target/love-adoption-0.0.1-SNAPSHOT.jar
   ```

5. 访问API文档
   ```bash
   http://localhost:8080/api/doc.html
   ```

## 接口文档

项目集成了Knife4j，提供了详细的API文档，启动项目后可通过以下地址访问：
```
http://localhost:8080/api/doc.html
```

## 贡献指南

欢迎贡献代码，提交问题和功能需求！

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request
