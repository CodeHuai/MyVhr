package com.myhr.mapper;

import com.myhr.model.JobLevel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JoblevelMapper {
    // 增加joblevel
    int insertSelective(JobLevel jobLevel);

    // 删除一个
    int deleteById(Long id);

    // 批量删除
    Integer deleteByIds(Long[] idList);

    // 修改
    int updateByPrimaryKeySelective(JobLevel jobLevel);

    // 查询列表数据
    List<JobLevel> selectJobLevels();
}
