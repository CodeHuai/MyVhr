package com.myhr.service;

import com.myhr.mapper.PositionMapper;
import com.myhr.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PositionService {
    @Autowired
    private PositionMapper positionMapper;

    // 查询全部的岗位信息，无分页
    public List<Position> getAllPositions() {
        return positionMapper.getAllPositions();
    }

    // 新增一个pos
    public Integer addPosition(Position position) {
        // 设置默认的创建时间
        position.setCreateDate(new Date());
        // 设置默认启用状态
        position.setEnabled(true);
        Integer inserted = positionMapper.insertSelective(position);
        return inserted;
    }

    // 更新
    public Integer updateByPrimaryKeySelective(Position position) {
        Integer inserted = positionMapper.updateByPrimaryKeySelective(position);
        return inserted;
    }

    // 单个删除
    public Integer deleteByPrimaryKey(Long id) {
        Integer inserted = positionMapper.deleteByPrimaryKey(id);
        return inserted;
    }

    // 批量删除
    public Integer deletePositionsByIds(Long[] idList) {
        Integer inserted = positionMapper.deletePositionsByIds(idList);
        return inserted;
    }
}
