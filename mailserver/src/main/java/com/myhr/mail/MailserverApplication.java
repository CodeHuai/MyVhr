package com.myhr.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 邮件服务启动类（独立进程，对应 vhr 的 mailserver）
 * 职责：消费 RabbitMQ 中的邮件消息，用 Thymeleaf 模板渲染并发送
 */
@SpringBootApplication
public class MailserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailserverApplication.class, args);
    }
}
