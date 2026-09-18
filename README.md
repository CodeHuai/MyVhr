# myhr — 人事管理系统（仿 vhr 后端骨架）

参照 [vhr（微人事）](https://github.com/lenve/vhr) 搭建的多模块 Spring Boot 后端工程，用于跟着教程仿写学习。

## 技术栈与版本

| 依赖 | 版本 | 说明 |
|---|---|---|
| Spring Boot | 2.7.18 | 2.x 最终版，与 vhr 的 2.4.0 写法一致（javax 命名空间） |
| Java | 1.8 | |
| MyBatis (mybatis-spring-boot-starter) | 2.3.2 | 对应 Boot 2.7 的适配版（vhr 原版为 2.1.0） |
| Druid (druid-spring-boot-starter) | 1.2.20 | 数据库连接池 |
| Spring Security | 5.7.11（Boot 管理） | 认证授权 |
| MySQL Connector | 8.0.33（Boot 管理） | |
| POI | 5.2.5 | Excel 导入导出 |
| Redis / RabbitMQ / WebSocket / Mail / Thymeleaf | Boot starter 管理 | 缓存、异步消息、在线聊天、邮件 |

与 vhr 原项目的差异：
- Spring Boot 2.4.0 → 2.7.18（安全补丁 + 兼容写法）
- FastDFS **未引入**（原依赖 fastdfs-client-java 已停更），文件存储建议后续用 MinIO 或本地磁盘 + Nginx
- model 模块额外引入了 Lombok，省去实体类 getter/setter

## 模块结构

```
myhr (root, pom)
├── myhrserver (pom)
│   ├── myhr-model     ← web + security + lombok（实体类，被所有模块依赖）
│   ├── myhr-mapper    ← model + mybatis + druid + flyway（默认关闭）
│   ├── myhr-service   ← mapper + redis + cache + amqp + poi
│   └── myhr-web       ← service + mysql + websocket（主服务，端口 8081）
└── mailserver         ← amqp + redis + mail + thymeleaf + model（邮件服务，端口 8082）
```

依赖方向严格单向：`web → service → mapper → model`；mailserver 独立进程，仅依赖 model。
基础包名 `com.myhr`，主启动类 `com.myhr.MyhrApplication`（在基础包根下，保证组件扫描覆盖所有模块）。

## 运行前准备

1. **MySQL**：建库 `myhr`（目前业务表还没建，跟着教程逐步建表或导入脚本）
   ```sql
   CREATE DATABASE IF NOT EXISTS myhr DEFAULT CHARACTER SET utf8mb4;
   ```
2. **Redis**：`127.0.0.1:6379`，密码 `123456`
3. **RabbitMQ**：`127.0.0.1:5672`，账号 `MrHuai / 123456`
4. 以上连接信息都按本地已调通的 vhr 环境预填在 `application.yml` 里，不同的话自行修改。

## 启动

```bash
# 方式一：命令行
mvn spring-boot:run -pl myhrserver/myhr-web

# 方式二：IDEA 里直接运行 com.myhr.MyhrApplication
```

启动后访问冒烟接口验证：`GET http://localhost:8081/ping`，返回 `{"status":200,"msg":"pong",...}` 即正常。

## 仿写路线（对应 vhr 教程章节）

1. 登录模块：`Hr` 实现 `UserDetails`、完善 `HrMapper.loadHrByUsername` 关联查询角色、登录/注销 JSON 响应、JWT + Redis
2. 动态菜单：`Menu`/`MenuRole` 实体 + `menus_cache` 缓存
3. 基础信息管理：部门、职位、职称、权限组（Permission）等 CRUD + POI 导入导出
4. 员工管理：Employee + 套娃查询（EmpQuery 动态 SQL）
5. 在线聊天：`WebSocketConfig` + `@ServerEndpoint` 聊天端点，消息经 RabbitMQ 中转
6. 邮件服务：mailserver 定义队列/绑定，`MailReceiver` 消费并发送 Thymeleaf 模板邮件（先在 `mailserver/src/main/resources/application.yml` 配好邮箱授权码）

## 常见问题

- Mapper XML 与接口同包放在 `src/main/java` 下，pom 已配置资源打包，无需挪到 resources。
- Boot 2.7 中 `WebSecurityConfigurerAdapter` 已标记过时但仍可用，与教程写法一致；想用新写法可改成 `SecurityFilterChain` Bean。
- flyway 已引入但默认 `enabled: false`，与原项目一致，需要时再打开。
