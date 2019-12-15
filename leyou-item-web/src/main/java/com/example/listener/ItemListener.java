package com.example.listener;

import com.example.service.ItemService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemListener {

    @Autowired
    private ItemService itemService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "item.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "item.server.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.insert", "item.update"}
    ))
    public void listen(String msg) {
        itemService.savePage(Long.parseLong(msg));
    }
}
