package com.myhr.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    // 数据主键
    private Long id;

    // 组件在前端的位置
    private String url;

    // 前端路由
    private String path;

    // 组件名称
    private String component;

    // 组件name
    private String name;

    // 组件icon
    private String iconCls;

    // 路由元数据
    private Meta meta;

    // 夫路由id
    private long parentId;

    // 是否启用
    private Boolean enabled;

    // 对应的角色
    private List<Role> roles;

    // 子菜单列表
    private List<Menu> children;
}
