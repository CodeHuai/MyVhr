package com.myhr.service;

import com.myhr.mapper.HrMapper;
import com.myhr.model.Hr;
import com.myhr.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作员 Service（示例：演示 service -> mapper 的调用链）
 */
@Service
public class HrService implements UserDetailsService {

    @Autowired
    private HrMapper hrMapper;


    @Override
    public UserDetails loadUserByUsername(String username) {
        if ("".equals(username) || username == null) {
            try {
                throw new Exception("用户名不为空");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Hr hr = hrMapper.loadUserByUsername(username);
        if (hr == null) {
            throw new UsernameNotFoundException("当前用户未找到！");
        }
        List<Role> roleList = hrMapper.getHrRolesById(hr.getId());
        hr.setRoles(roleList);
        return hr;
    }
}
