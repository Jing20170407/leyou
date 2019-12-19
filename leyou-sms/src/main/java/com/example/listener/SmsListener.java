package com.example.listener;

import com.example.config.SmsProperties;
import com.example.utils.SmsUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsProperties prop;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.sms.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.sms.exchange",
                    type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"
            ),
            key = {"leyou.sms.code"}
    ),ackMode = "MANUAL")
    public void sendCode(Map<String,String> map, Channel channel,
                         @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {

        SmsUtils.sendSms(
                prop.getAccessKeyID(),
                prop.getAccessKeySecret(),
                map.get("phone"),
                prop.getSignName(),
                prop.getTemplateCode(),
                "{'code':'"+map.get("code")+"'}"
        );

        channel.basicAck(tag, false);
    }
}
