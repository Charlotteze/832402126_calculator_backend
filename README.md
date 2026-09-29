# Calculator Backend（学号_calculator_backend）

Front-End and Back-End Separation Calculator System — Backend 前后端分离计算器系统·后端

## 项目介绍

前后端分离计算器系统的后端服务，负责：
- 表达式校验与解析（调度场算法，禁止 eval/exec）
- 表达式计算（结果一律由后端生成）
- 异常处理（非法表达式、除零、括号不匹配等）
- 计算历史的数据库持久化（增、查、删、清空）

## 技术栈

| 组件 | 版本 |
|---|---|
| Java | JDK 1.8 |
| Spring Boot | 2.7.18 |
| Spring Web | REST API |
| Spring Data JPA | 数据访问 |
| H2 Database | 嵌入式数据库（文件持久化） |

## 运行环境

- JDK 8+
- Maven 3.6+
- 任意操作系统（本项目在 Windows 10 上开发并验证）

## 安装与启动

```bash
# 1. 编译
mvn compile

# 2. 开发模式启动
mvn spring-boot:run

# 3. 或打包后运行
mvn package
java -jar target/calculator-backend-0.0.1-SNAPSHOT.jar
```

启动后服务监听 `http://localhost:8080`。

## 配置说明

文件：`src/main/resources/application.properties`

| 配置项 | 说明 |
|---|---|
| `server.port=8080` | 服务端口 |
| `spring.datasource.url=jdbc:h2:file:./data/calculator` | H2 文件模式（数据持久化，重启不丢） |
| `spring.jpa.hibernate.ddl-auto=update` | 自动创建/更新表结构 |
| `spring.h2.console.enabled=true` | 可选：H2 控制台（http://localhost:8080/h2-console） |

CORS 已全局配置（`config/WebConfig.java`），允许前端跨域访问 `/api/**`。

## 数据库初始化

无需手动初始化。应用首次启动时，Hibernate 自动创建表：

```sql
calculation_history(id BIGINT, expression VARCHAR, result VARCHAR, created_at TIMESTAMP)
```

数据文件保存在项目根目录 `./data/calculator.mv.db`。删除该文件可重置数据库。

## API 接口

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/calculate` | 计算表达式，成功后写入历史 |
| GET | `/api/history` | 查询全部历史（时间倒序） |
| DELETE | `/api/history/{id}` | 删除指定历史记录 |
| DELETE | `/api/history` | 清空全部历史（附加功能） |

请求示例：

```json
POST /api/calculate
{"expression": "12+8"}
```

成功响应：

```json
{"success": true, "expression": "12+8", "result": "20"}
```

错误响应（HTTP 400）：

```json
{"success": false, "message": "Division by zero"}
```

## 前后端连接

前端通过 `http://localhost:8080/api` 调用本服务。跨域已在 `WebConfig` 中放开（允许所有来源）。

部署到公网后，请同步修改前端 `src/js/api.js` 中的 `API_BASE` 为本服务实际地址。

## 目录结构

```
src/main/java/com/example/calculator/
├── CalculatorApplication.java   # 启动类
├── controller/                  # REST API 入口
├── service/                     # 业务逻辑（计算、历史）
├── model/                       # 实体与统一响应
├── repository/                  # 数据库访问（JPA）
├── util/                        # 表达式解析器
└── config/                      # CORS 等配置
```
