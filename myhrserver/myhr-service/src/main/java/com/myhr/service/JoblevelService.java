package com.myhr.service;

import com.myhr.mapper.JoblevelMapper;
import com.myhr.model.JobLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JoblevelService {
    @Autowired
    private JoblevelMapper joblevelMapper;

    // 查询列表数据
    public List<JobLevel> selectJobLevels() {
        return joblevelMapper.selectJobLevels();
    }

    // 新增
    public Integer insertJobLevel(JobLevel jobLevel) {
        jobLevel.setEnabled(true);
        jobLevel.setCreateDate(new Date());
        return joblevelMapper.insertSelective(jobLevel);
    }

    // 更新
    public Integer updateJobLevel(JobLevel jobLevel) {
        return joblevelMapper.updateByPrimaryKeySelective(jobLevel);
    }

    // 删除
    public Integer deleteJobLevels(Long id) {
        return joblevelMapper.deleteById(id);
    }

    // 批量删除
    public Integer deleteByIds(Long[] idList) {
        return joblevelMapper.deleteByIds(idList);
    }
}
