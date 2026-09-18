package com.myhr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主启动类（对应 vhr 的 VhrApplication）
 * 注意：放在 com.myhr 基础包下，保证能扫描到 model/mapper/service 里的组件
 */
@SpringBootApplication
@MapperScan(basePackages = "com.myhr.mapper")
public class MyhrApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyhrApplication.class, args);
    }
}
