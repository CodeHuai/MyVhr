# myhr — 人事管理系统（仿 vhr 后端）

参照本地 vhr（微人事）源码逐文件仿写的多模块 Spring Boot 后端，学习目的：完整走一遍 Spring Security 认证、动态菜单、权限过滤、业务 CRUD、Excel 导入导出的开发流程。

- vhr 源码（只读参照）：`D:\PersonCodeDir\java\vhr`
- 前端：直接用 vhr 的 vuehr（`D:\PersonCodeDir\java\vhr\vuehr`）
- 分阶段任务清单：根目录《仿写清单.md》

## 技术栈与版本

| 依赖 | 版本 | 说明 |
|---|---|---|
| Spring Boot | 2.7.18 | 2.x 最终版，与 vhr 的 2.4.0 写法一致（javax 命名空间） |
| Java | 1.8 | |
| MyBatis (mybatis-spring-boot-starter) | 2.3.2 | 对应 Boot 2.7 的适配版（vhr 原版为 2.1.0） |
| Druid (druid-spring-boot-starter) | 1.2.20 | 数据库连接池 |
| Spring Security | 5.7.11（Boot 管理） | Session 会话方案 + 单会话顶号（照抄 vhr，非 JWT） |
| MySQL Connector | 8.0.33（Boot 管理） | |
| POI | 5.2.5 | Excel 导入导出 |
| Redis / RabbitMQ / WebSocket / Mail / Thymeleaf | Boot starter 管理 | 缓存、异步消息、在线聊天、邮件（后三者暂缓） |

与 vhr 原项目的差异：

- Spring Boot 2.4.0 → 2.7.18（差异多体现为废弃警告，不影响运行）
- model 模块额外引入了 Lombok
- fastdfs-client-java 到阶段 5（操作员头像）再照 vhr 引入，暂未加

## 模块结构

```
myhr (root, pom)
├── myhrserver (pom)
│   ├── myhr-model     ← web + security + lombok（实体类，被所有模块依赖）
│   ├── myhr-mapper    ← model + mybatis + druid + flyway（默认关闭）
│   ├── myhr-service   ← mapper + redis + cache + amqp + poi
│   └── myhr-web       ← service + mysql + websocket（主服务，端口 10002）
└── mailserver         ← amqp + redis + mail + thymeleaf + model（邮件服务，暂缓）
```

依赖方向严格单向：`web → service → mapper → model`；mailserver 独立进程，仅依赖 model。
基础包名 `com.myhr`，主启动类 `com.myhr.MyhrApplication`（在基础包根下，保证组件扫描覆盖所有模块）。

## 运行前准备

1. **MySQL**：库 `myvhr`，已导入 vhr.sql 全部 22 张表及基础数据（含 addDep / deleteDep 两个存储过程）
2. **Redis**：`127.0.0.1:6379`，密码 `123456`（菜单缓存 menus_cache 依赖）
3. **RabbitMQ**：`127.0.0.1:5672`，账号 `MrHuai / 123456`（邮件服务暂缓，目前未实际使用，可不启动）
4. 连接信息已按本地环境预填在 `application.yml`，不同的话自行修改。

## 启动

```bash
# 方式一：命令行
mvn spring-boot:run -pl myhrserver/myhr-web

# 方式二：IDEA 里直接运行 com.myhr.MyhrApplication
```

冒烟验证：`GET http://localhost:10002/ping` 返回 `{"status":200,"msg":"pong",...}` 即正常。

## 本地端口约定

| 服务 | 端口 | 说明 |
|---|---|---|
| 原 vhr 后端 | 10001 | 参照对象 |
| myhr 后端 | 10002 | 本工程 |
| vuehr 前端 devServer | 10086 | `npm run serve` |

⚠️ 联调 myhr 时，把 `vuehr/vue.config.js` 里 `/` 和 `/ws` 两条 proxy 的 `target` 从 `localhost:10001` 改成 `localhost:10002`，否则前端请求打到的是原 vhr 后端。

## 进度（对应《仿写清单.md》）

- [x] 阶段 0：环境与数据准备（数据已导入，联调待验证）
- [ ] 阶段 1：登录与认证（进行中）
- [ ] 阶段 2：动态菜单
- [ ] 阶段 3：基础信息管理（职位/职称/部门）
- [ ] 阶段 4：权限组 + 动态鉴权
- [ ] 阶段 5：操作员管理与个人中心
- [ ] 阶段 6：员工管理（含 Excel 导入导出）
- [ ] 阶段 7：工资账套
- [ ] 阶段 8：收尾
- 暂缓：在线聊天（WebSocket）、邮件服务（RabbitMQ + mailserver），启用要点见《仿写清单.md》文末

## 常见问题

- Mapper XML 与接口同包放在 `src/main/java` 下，pom 已配置资源打包，无需挪到 resources。
- Boot 2.7 中 `WebSecurityConfigurerAdapter` 已标记过时但仍可用，照 vhr 的写法用即可。
- flyway 已引入但默认 `enabled: false`，与原项目一致。
- 默认登录账号：admin / 123456。
