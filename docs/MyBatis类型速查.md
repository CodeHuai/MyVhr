# MyBatis 类型速查（javaType / jdbcType / 别名）

## 1. 心智模型

resultMap 是**翻译官**：把"数据库的一行"翻译成"Java 的一个对象"。

- **javaType**：Java 这边——属性是什么类型
- **jdbcType**：数据库那边——这一列的 SQL 类型（`java.sql.Types` 的常量名）
- 真正干转换活的是 **TypeHandler**：拿两边的类型挑一个合适的转换器

## 2. 什么时候必须写

| 位置 | javaType | jdbcType |
|---|---|---|
| `<result>` / `<id>` | 可省（反射 setter 就能推出类型），vhr 都没写 | 可省（驱动知道列类型）；vhr 全写了，是 MyBatis Generator 的生成习惯 |
| `<association>` | **必须**：告诉 MyBatis new 什么类来装嵌套列 | — |
| `<collection>` | 用 **ofType** 指定集合元素类型 | — |
| `#{}` 普通参数 | — | 一般可省 |
| 存储过程 OUT 参数 | — | **必须**：OUT 值是数据库执行完往回灌的，执行前 Java 侧没值可推，驱动要先登记出参类型 |

vhr 原文例证（MenuMapper.xml / DepartmentMapper.xml）：

```xml
<association property="meta" javaType="org.javaboy.vhr.model.Meta">
<collection property="children" ofType="org.javaboy.vhr.model.Menu">
call addDep(...,#{result,mode=OUT,jdbcType=INTEGER},#{id,mode=OUT,jdbcType=INTEGER})
```

## 3. Java ↔ JDBC 对照（背这个）

| Java 类型 | jdbcType |
|---|---|
| Byte / byte | TINYINT |
| Short / short | SMALLINT |
| Integer / int | INTEGER |
| **Long / long** | **BIGINT** |
| String | VARCHAR |
| Boolean / boolean | BIT（vhr 的 enabled 都用它）或 BOOLEAN |
| Date | TIMESTAMP（带时分秒）/ DATE（只有日期） |
| Double / double | DOUBLE |
| Float / float | REAL |
| BigDecimal | DECIMAL |
| 枚举 Enum | **没有专属 jdbcType**：按名字存 → VARCHAR（默认）；按序号存 → INTEGER（见第 4 节） |

记忆钩子：整数一家按宽度排队——byte < short < int < long 对应 TINYINT < SMALLINT < INTEGER < BIGINT；float 对应的偏偏是 REAL，是个例外，单独记。

## 4. 枚举类型：JdbcType 里没有 ENUM，形态由 TypeHandler 决定

`java.sql.Types`（也就是 JdbcType 的取值范围）**没有 ENUM 这个值**，枚举落库只有两种形态，选哪个 handler 就长什么样：

| TypeHandler | 落库形态 | 列 / jdbcType | 读回来 |
|---|---|---|---|
| `EnumTypeHandler`（**默认**，零配置） | `name()` 字符串，如 "ENABLED" | VARCHAR | `Enum.valueOf(枚举类, 字符串)`——库里的串必须和常量名**完全一致（含大小写）** |
| `EnumOrdinalTypeHandler` | `ordinal()` 序号 0/1/2 | INTEGER | 按位置取常量；**枚举常量顺序一变（中间插一个/挪一个），历史数据全错位**，慎用 |

- 默认行为：属性/参数类型是枚举，MyBatis 自动挑 EnumTypeHandler，resultMap 和 `#{}` **什么都不用写**
- 换序号方案或自定义转换时，单点指定 typeHandler：

```xml
<result property="status" column="status" javaType="com.myhr.model.Status"
        typeHandler="org.apache.ibatis.type.EnumOrdinalTypeHandler"/>

#{status, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}
```

- 通用知识备用：vhr / myhr 源码里目前都没有枚举字段（2026-09-23 核过）

## 5. MyBatis 内置别名（javaType 的简写）

别名大小写不敏感，习惯小写：

| 别名 | Java 类型 |
|---|---|
| string | String |
| int / integer | Integer |
| long | Long |
| boolean / bool | Boolean |
| date | Date |
| double | Double |
| float | Float |
| bigdecimal | BigDecimal |
| object | Object |

原始类型（非包装类）前面加下划线：`_int`→int、`_long`→long、`_boolean`→boolean。

## 6. 大小写惯例

| 填什么 | 惯例 |
|---|---|
| jdbcType | **全大写**（VARCHAR、BIT、BIGINT）——值就是 java.sql.Types 常量名 |
| javaType 别名 | 小写（string、long） |
| javaType 完整类名 | 和类名**一模一样**（org.javaboy.vhr.model.Meta），错一个字母直接 ClassNotFound |
