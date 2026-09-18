package com.myhr.service;

import com.myhr.mapper.HrMapper;
import com.myhr.model.Hr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 操作员 Service（示例：演示 service -> mapper 的调用链）
 */
@Service
public class HrService {

    @Autowired
    HrMapper hrMapper;

    public Hr loadHrByUsername(String username) {
        return hrMapper.loadHrByUsername(username);
    }
}
