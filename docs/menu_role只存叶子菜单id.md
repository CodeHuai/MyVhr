# menu_role 只存叶子菜单的 id

> 2026-09-20，看 vhr `MenuMapper.xml` 的 `getMenusByHrId` 时产生的问题。

## 问题

`getMenusByHrId` 的四表联查里，`menu_role` 为什么关联的是二级菜单（`mr.mid = m2.id`）？
把条件换成 `mr.mid = m1.id`（关联一级菜单），为什么查不出任何数据？

```sql
select distinct m1.*, m2.id as id2, m2.name as name2, ...
from menu m1, menu m2, hr_role hrr, menu_role mr
where m1.id = m2.parentId
  and hrr.hrid = #{hrid}
  and hrr.rid = mr.rid
  and mr.mid = m2.id          -- 换成 m1.id 就查不出来
  and m2.enabled = true
order by m1.id, m2.id;
```

## 结论

**不是 SQL 写法问题，是数据问题：`menu_role` 表里存的 mid 全是叶子（二级）菜单的 id，从来没有一级菜单的 id。**

- `mr.mid = m2.id`：m2 是叶子菜单，正好是 menu_role 里存的那些 id → 配得上，出结果；
- `mr.mid = m1.id`：m1 被 `m1.id = m2.parentId` 限定为"必须是父节点"（id 1~6），而 menu_role 里没有 1~6 的记录 → 等值条件永远配不上 → 空集。

## 数据验证（vhr.sql）

menu 表的形状：

- id 1~6：`所有`、`员工资料`、`人事管理`…——都有孩子（父节点）
- id 7~28：`基本资料`、`高级资料`…——全是叶子（没有任何行的 parentId 指向它们）

menu_role 里 rid=6（hrid=3 只有这个角色）的 mid：

```
7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28
```

全部 ≥ 7。自己验证：

```sql
select distinct mid from menu_role where mid <= 6;   -- 空结果
```

## 根因：为什么 menu_role 里只存叶子菜单的 id

权限管理界面（`PermissMana.vue:149`）保存时用的是：

```js
let selectedKeys = tree.getCheckedKeys(true);
```

`getCheckedKeys(true)` 的参数 `true` 表示**只收集叶子节点**的勾选 id，一级菜单的 id 根本不会被提交给后端写库。

## 引申：一级菜单的可见性是"推导"出来的

一级菜单不直接记权限——只要角色有它下面任何一个二级菜单的权限，`m1.id = m2.parentId` 就把这个爹"带"出来。权限永远只挂在叶子菜单上，父级跟着子级走。
