package com.myhr.mail.receiver;

import org.springframework.stereotype.Component;

/**
 * 邮件消息消费者（对应 vhr 的 MailReceiver）
 *
 * TODO 仿写时按教程演进：
 *  1. 定义邮件常量类（队列名、路由键、交换机名），并在配置类中声明 Queue/Binding
 *  2. 打开下面的 @RabbitListener，收到 Employee 消息后用 JavaMailSender + TemplateEngine 发送入职欢迎邮件
 *  3. 处理发送失败的重投（消息重回队列 / 死信）
 */
@Component
public class MailReceiver {

    /*
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;

    @RabbitListener(queues = "myhr.mail.queue")
    public void handle(Employee employee) {
        Context context = new Context();
        context.setVariable("name", employee.getName());
        // ... 渲染 mailTemplate.html 并发送
    }
    */
}
