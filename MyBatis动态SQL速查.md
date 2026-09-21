# MyBatis 动态 SQL 速查（trim / 尾逗号 / 案例）

## 1. trim：包装工 + 清洁工

| 属性 | 干什么 |
|---|---|
| prefix | 内容**前面**包什么（如 `(`） |
| suffix | 内容**后面**包什么（如 `)`） |
| prefixOverrides | 内容**开头**若是指定串，剥掉（如头逗号策略） |
| suffixOverrides | 内容**结尾**若是指定串，剥掉（如尾逗号策略） |

工作三步：收集 if 通过的碎片 → 剥头/剥尾 → 包前后缀。
**内容为空时连 prefix/suffix 都不包**——`<where>` 条件全空就不输出 where，靠的就是这条。

## 2. 为什么每个字段都带尾逗号，连最后一个也带

逗号活不到数据库：`suffixOverrides=","` 专门收走最后一个。

```
碎片：name, createDate, enabled,   → 剥尾逗号 → name, createDate, enabled
      → 包装 → ( name, createDate, enabled )
```

本质：**XML 静态、if 运行时，"谁是最后一项"写 XML 时不可知**，逗号没法写在"两项之间"，只能统一策略——每项带尾逗号最后剥（vhr/MBG），或每项带头逗号最前剥。删掉最后一项的逗号在顺序不变时碰巧能跑，但加列/挪序就得人工重新推演谁是最后一项，改漏就错。模板要的就是每一项长得一模一样。

关联：`<set>` = `trim(prefix="set", suffixOverrides=",")` 的语法糖，selective 更新用它。

## 3. insertSelective 是 id，不是标签

- 真标签是 DTD 固定的那套：select / insert / update / delete / sql / resultMap / trim / if / foreach / set / where...
- `insertSelective`、`getAllPositions`、`deleteByPrimaryKey` 全是 **id**（接口方法名的镜像）
- MBG 命名家族：insert（全列）/ insertSelective（非空列）/ updateByPrimaryKey / updateByPrimaryKeySelective / selectByPrimaryKey / deleteByPrimaryKey
- 自检法：XML 里敲 `<` 看自动补全列表，列表里没有的就不是标签

## 4. 案例复盘：PositionMapper.xml 翻车四处（2026-09-20）

1. **trim 的 prefix/suffix 写反**：`prefix=")" suffix="("` → 拼出 `)name(` 垃圾 SQL，必炸。记法：prefix 包在前、suffix 包在后，名字就是位置。
2. **insertSelective 只拼了 name 一列**：Service 设置的 createDate/enabled 根本进不了 SQL，靠表列默认值（CURRENT_TIMESTAMP / 1）"蒙对"——**数据对了 ≠ 链路通了**。识别法：看 MyBatis 日志 `==> Preparing:` 的列清单，三列必须齐上。
3. **getAllPositions 多写 `where enabled = true`**：禁用的职位从列表消失，永远无法再启用。vhr 是全量查——列表页的启用/禁用开关要求禁用行还在列表里躺着。
4. 小项：`value` → `values`（MySQL 认单数但照抄）；resultMap 主键行用 `<id>` 不用 `<result>`。

验收法：MyBatis 不检查 SQL（启动全不报），**`==> Preparing:` 日志就是 SQL 唯一的"编译器"**，自测步骤不能省。

## 5. 链路三层分工速记（本次顺带）

| 层 | 说的语言 | 名字示例 | 返回什么 |
|---|---|---|---|
| Mapper | SQL：选择性插入 | insertSelective | 行数/结果集 |
| Service | 业务：添加职位 | addPosition | Integer / List / boolean，**不说 RespBean** |
| Controller | HTTP：判断+翻译 | addPosition | RespBean（msg 是 HTTP 世界的词汇） |

- **RespBean 只在 web 层出现**：Service 说了 HTTP 的话就只能伺候 HTTP 客户端
- 详细说明需要原因码：Service 报**事实**（-2/-1/1），Controller 把事实译成文案（部门删除案例）
- **URL 是前后端合同**：完整路径 = 类注解 + 方法注解拼接；REST 风格 = URL 只有名词、动词在 method 里（POST /system/basic/pos/，不是 /addPos）；vuehr 调什么你就映射什么
