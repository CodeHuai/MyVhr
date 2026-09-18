package com.myhr.model;

import lombok.Data;

/**
 * 操作员（对应 vhr 的 Hr 表 hr）
 *
 * TODO 仿写时按教程演进：
 *  1. 增加 roles 字段（List&lt;Role&gt;）并实现 UserDetails，接入 Spring Security 登录
 *  2. 实现 CredentialsContainer，登录后擦除明文密码
 */
@Data
public class Hr {

    private Long id;
    private String name;
    private String phone;
    private String telephone;
    private String address;
    private Boolean enabled;
    private String username;
    private String password;
    private String userface;
    private String remark;
}
