package com.myhr.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Meta implements Serializable {
    private static final long serialVersionUID = 1L;

    // 前端路由的相关属性
    private Boolean keepAlive;

    // 需要的权限数据
    private Boolean requireAuth;
}
