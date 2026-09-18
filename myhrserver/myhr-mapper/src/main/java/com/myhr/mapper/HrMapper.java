package com.myhr.mapper;

import com.myhr.model.Hr;
import org.apache.ibatis.annotations.Param;

/**
 * 操作员 Mapper（示例：演示 接口 + 同包 XML 的 MyBatis 写法）
 */
public interface HrMapper {

    /**
     * 按用户名加载操作员（登录时使用）
     */
    Hr loadHrByUsername(@Param("username") String username);
}
