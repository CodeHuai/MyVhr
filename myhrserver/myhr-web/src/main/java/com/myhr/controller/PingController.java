package com.myhr.controller;

import com.myhr.model.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 冒烟测试接口：项目搭好后访问 GET http://localhost:8081/ping 验证服务正常
 */
@RestController
public class PingController {

    @GetMapping("/ping")
    public RespBean ping() {
        return RespBean.ok("pong");
    }
}
