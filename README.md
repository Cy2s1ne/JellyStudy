# JellyStudy - 智能学习平台

## 项目简介
JellyStudy是一个基于Java Spring Boot和Dubbo微服务架构设计的智能学习平台，旨在提供智能化的问题生成、知识检索和智能问答功能。该平台利用人工智能技术，为用户提供个性化的学习体验、知识管理和社区问答服务。

## 系统架构
项目采用微服务架构，基于Apache Dubbo实现服务间的远程调用，使用Nacos作为服务注册和发现中心。系统分为以下几个主要模块：

- **AI服务模块**：提供智能问答、内容理解和生成功能
- **知识管理模块**：负责知识的存储、检索和管理
- **问题生成模块**：自动生成针对特定知识点的练习题
- **问答社区模块**：支持用户间的问答交流和讨论

## 技术栈

### 后端技术
- **核心框架**：Spring Boot 3.3.4
- **微服务框架**：Apache Dubbo 3.3.0
- **服务注册与发现**：Nacos
- **数据存储**：MongoDB
- **缓存**：Redis
- **分布式追踪**：Zipkin

### 开发工具
- **构建工具**：Maven
- **编程语言**：Java 17
- **API文档**：Swagger
- **版本控制**：Git

## 模块说明

### 核心模块
1. **ai-lib & ai-app**
   - 提供AI能力支持
   - 接入大模型API进行问题生成和智能问答

2. **knowledge-lib & knowledge-app**
   - 知识管理与检索服务
   - 支持知识点的存储、标记和检索

3. **question-answer-lib & question-answer-app**
   - 社区问答服务
   - 支持用户提问、回答和评论功能
   - 包含用户管理、问题管理等功能

4. **question-generation-lib & question-generation-app**
   - 自动题目生成服务
   - 根据知识点自动生成练习题和答案解析

## 安装与部署

### 环境要求
- JDK 17+
- Maven 3.8+
- MongoDB 5.0+
- Redis 6.0+
- Nacos 2.0+

### 构建步骤
1. 克隆代码库
```bash
git clone https://github.com/your-username/JellyStudy.git
cd JellyStudy
```

2. 编译项目
```bash
mvn clean package -DskipTests
```

3. 启动服务
```bash
# 按照以下顺序启动各个服务
java -jar ai-app/target/ai-app-0.0.1-SNAPSHOT.jar
java -jar knowledge-app/target/knowledge-app-0.0.1-SNAPSHOT.jar
java -jar question-generation-app/target/question-generation-app-0.0.1-SNAPSHOT.jar
java -jar question-answer-app/target/question-answer-app-0.0.1-SNAPSHOT.jar
```

## API文档
应用启动后，可通过以下地址访问API文档：
- AI服务: http://localhost:8082/swagger-ui.html
- 知识服务: http://localhost:8083/swagger-ui.html
- 问题生成服务: http://localhost:8084/swagger-ui.html
- 问答社区服务: http://localhost:8085/swagger-ui.html

## 功能特性
- **智能问答**：基于大模型的问答功能，支持自然语言理解和生成
- **知识管理**：知识点的分类、标签和关联管理
- **题库生成**：根据知识点自动生成练习题和解析
- **社区互动**：问题提出、回答、点赞、评论等社交功能
- **个性化学习**：基于用户行为和兴趣的个性化推荐

5. 

## 版本历史
- **0.0.1-SNAPSHOT**：初始版本，基础功能实现

## 联系方式
- 项目维护者：Cy2s1ne
- 电子邮件：Cy1609@outlook.com