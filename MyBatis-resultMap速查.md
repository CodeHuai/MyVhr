# MyBatis resultMap 速查（装包 / association / collection / extends）

## 1. 心智模型：装包说明书

resultMap 把"数据库的平铺一行"装成"Java 的一个对象"：

- `column="xxx" property="yyy"`：列 → 属性，靠**列名**对号入座
- **没人认领的列被静默丢弃，不报错**——接口 200、数据缺一块却查无实错时，先想到这条
- 父子同查列名撞车，靠 **SQL 别名**区分（id2 / path2 ...）：
  - 别名只加在**列**上；property 永远不带 2（前端 JSON 键名来自 property）
  - meta 不是列，是 Java 里包出来的对象，所以"meta 没有 2"——它底下的列（keepAlive2、requireAuth2）才有

## 2. association vs collection

| | association | collection |
|---|---|---|
| 关系 | 一对一 / 多对一 | 一对多 |
| 属性那头 | 一个对象 | 一个 List |
| 指定类型用 | javaType | ofType |
| vhr 例子 | Menu.meta | Hr.roles、Menu.children |

口诀：**看 Java 字段的类型**——字段是对象用 association，是集合用 collection，跟几表联查没有直接关系。

## 3. `<id>` 的第二职业：分组合并

联查"一父多子"出来的多行平铺数据，MyBatis 按 `<id>` 认人：

- 同 id 的后续行**不再 new 对象**，只把 collection 的部分接着挂进同一个对象
- 一父三子 = 3 行 → 1 个父对象 + children 里 3 个元素
- 所以 `<id>` 不只是主键映射，它是**多行合并成一个对象的依据**

## 4. extends：resultMap 的继承

`Menus2 = BaseResultMap 的全部映射 + 一个 collection`，只写增量：

- 不继承就得逐字抄 10 行（8 列 + meta 的 association）
- 表加列时：继承结构只改 Base 一处；抄两份的要改两处，**漏一处就是第 1 节的静默丢弃**
- BaseResultMap 有双重身份：也被平铺查询直接用（vhr 的 selectByPrimaryKey 就指它）

vhr 通用惯例（`xxxWith...` 都是这么长的）：

| resultMap | extends | 追加什么 | 阶段 |
|---|---|---|---|
| Menus2 | BaseResultMap | children collection | 阶段 2 |
| MenuWithRole | BaseResultMap | roles collection | 阶段 4 |
| HrWithRoles | BaseResultMap | roles collection | 阶段 5 |
| AllEmployeeInfo | BaseResultMap | 5 个 association | 阶段 6 |

## 5. 嵌套结果 vs 嵌套查询

| | 嵌套结果（映射列） | 嵌套查询（select 属性） |
|---|---|---|
| SQL 次数 | 一次联查平铺，resultMap 拆包 | 主查询一次，每个元素再发一条 SQL |
| 写法 | association/collection 里套 result 映射 | collection 上 `select="某查询" column="id"` |
| vhr 例子 | Menus2、HrWithRoles、MenuWithRole | DepartmentWithChildren（递归） |
| 适合 | 层级固定（菜单就两层） | 层级不定（部门树任意深） |

## 6. 案例复盘：getMenusByHrId 翻车两连（2026-09-20）

同一条 select 上错了两个属性，两段现象：

```xml
<select id="getMenusByHrId" resultMap="BaseResultMap" parameterType="String">  ← 翻车原版
```

**第一炸：parameterType="String"（声明的类型 ≠ 实际传的类型）**

- 机制：parameterType 写死参数类型 → 解析期就锁定 StringTypeHandler → 运行期真值是 Long → 桥方法强转炸
- 报错特征三件套：`ParameterMapping{javaType=class java.lang.String}` + `Could not set parameters` + `ClassCastException`
- 规律：**不写 parameterType = 运行时看真值现挑 handler（自适应）；写了它就信你，错了也照用**
- vhr 的手写业务查询都不写这属性

**第二哑：resultMap="BaseResultMap"（指错说明书）**

- 机制：id2/path2 等 10 个子列明明查回来了，但 BaseResultMap 里没人认领 → 静默丢弃 → children 一直是 null
- 现象：接口 200，侧边栏只见一级菜单空壳
- 修：resultMap 换成 Menus2（collection 在那份说明书里）

正解（对照 vhr 原文）：

```xml
<select id="getMenusByHrId" resultMap="Menus2">
```
