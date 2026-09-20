package com.myhr.mapper;

import com.myhr.model.Menu;

import java.util.List;

public interface MenuMapper {
    // 根据 hr 的id 查询出来对应的菜单数据
    List<Menu> getMenusByHrId(Long hrId);
}
