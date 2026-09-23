package com.myhr.mapper;

import com.myhr.model.Position;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PositionMapper {
    // 查询全部的岗位数据
    List<Position> getAllPositions();

    // 新增一个position
    int insertSelective(Position position);

    // 更新操作
    int updateByPrimaryKeySelective(Position position);

    // 删除操作
    int deleteByPrimaryKey(Long id);

    // 批量删除操作
    Integer deletePositionsByIds(Long[] idList);
}
