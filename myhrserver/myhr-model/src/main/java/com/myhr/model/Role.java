package com.myhr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    // 用户主键
    private Long id;

    // 角色编码
    private String name;

    // 角色名称（中文）
    private String nameZh;
}
