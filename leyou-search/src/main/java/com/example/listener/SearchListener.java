package com.example.listener;

import com.example.client.SpuClient;
import com.example.pojo.Goods;
import com.example.pojo.Spu;
import com.example.repository.GoodsRepository;
import com.example.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SearchListener {

    @Autowired
    private SearchService searchService;

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private GoodsRepository goodsRepository;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "search.queue", durable = "true"),
            exchange = @Exchange(
                    value = "item.server.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.insert", "item.update"}))
    public void listen(String msg) throws IOException {
        //获取spu
        Long spuid = Long.parseLong(msg);
        Spu spu = spuClient.getSpuById(spuid);
        //转换goods
        Goods goods = searchService.buildGoods(spu);
        //保存到elasticsearch
        goodsRepository.save(goods);
    }
}
