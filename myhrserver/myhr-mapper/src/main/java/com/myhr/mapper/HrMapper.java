package com.myhr.mapper;

import com.myhr.model.Hr;
import com.myhr.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作员 Mapper（示例：演示 接口 + 同包 XML 的 MyBatis 写法）
 */
@Mapper
public interface HrMapper {

    /**
     * 按用户名加载操作员（登录时使用）
     */
    Hr loadUserByUsername(String username);

    /**
     * 根据 hrRole-Id 查询对应的 角色信息
     *
     * @param hrrid hrrid
     * @return 对应的角色信息
     */
    List<Role> getHrRolesById(Long hrrid);

}
