package com.myhr.mapper;

import com.myhr.model.Position;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PositionMapper {
    // 查询全部的岗位数据
    public List<Position> getAllPositions();

    // 新增一个position
    public int insertSelective(Position position);
}
