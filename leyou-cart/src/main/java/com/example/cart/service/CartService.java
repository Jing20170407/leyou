package com.example.cart.service;

import com.example.auth.entity.UserInfo;
import com.example.cart.Interceptor.LoginInterceptor;
import com.example.cart.client.ItemClient;
import com.example.cart.config.RedisProperties;
import com.example.cart.pojo.Cart;
import com.example.common.utils.JsonUtils;
import com.example.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties(RedisProperties.class)
public class CartService {

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ItemClient itemClient;


    /**
     * 添加指定数量的商品到购物车
     * @param skuId
     * @param num
     * @return
     */
    public boolean addCart(Long skuId, Integer num) {

        //判断num
        if (num < 1) {
            return false;
        }
        //获取sku
        Sku sku = itemClient.getSkuById(skuId);
        if (sku == null) {
            return false;
        }
        //封装cart
        Cart cart = new Cart();
        cart.setSkuId(sku.getId());
        String image = StringUtils.substringBefore(sku.getImages(), ",");
        cart.setImage(image);
        cart.setNum(num);
        cart.setPrice(sku.getPrice());
        cart.setTitle(sku.getTitle());
        cart.setOwnSpec(JsonUtils.parseMap(sku.getOwnSpec(),Integer.class,String.class));

        //从购物车获取该skuid的数据
        Object get = redisTemplate.opsForHash().get(getCartKey(), cart.getSkuId().toString());
        String temp = null;
        if (get != null) {
            temp = get.toString();
        }
        if (StringUtils.isBlank(temp)) {
            //没有，该id的数据
            String serialize = JsonUtils.serialize(cart);
            redisTemplate.opsForHash().put(getCartKey(), cart.getSkuId().toString(), serialize);
        } else {
            //有，该id的数据
            //获取cart，添加num
            Cart parse = JsonUtils.parse(temp, Cart.class);
            parse.setNum(parse.getNum() + num);

            //保存到redis
            String serialize = JsonUtils.serialize(parse);
            redisTemplate.opsForHash().put(getCartKey(), parse.getSkuId().toString(), serialize);
        }

        return true;
    }

    /**
     * 添加一个商品到购物车
     * @param skuId
     * @return
     */
    public boolean addCart(Long skuId) {
        return addCart(skuId, 1);
    }


    /**
     * 获取redis购物车key，以userid区分
     * @return
     */
    private String getCartKey() {
        UserInfo user = LoginInterceptor.getLocalUser();
        return redisProperties.getCartName() + user.getId();
    }

    /**
     * 获取购物车，以list格式返回
     * @return
     */
    public List<Cart> getCartList() {
        //拿到购物车
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(getCartKey());
        if (CollectionUtils.isEmpty(entries)) {
            return new ArrayList<>();
        }

        //转换成list
        List<Cart> carts = entries.values().stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());

        return carts;
    }


    /**
     * 更新cart数量
     * @param skuId
     * @param num
     * @return
     */
    public boolean updateCart(Long skuId, Integer num) {
        //判断num
        if (num < 1) {
            return false;
        }

        //查询购物车有无此商品
        Object cart = redisTemplate.opsForHash().get(getCartKey(), skuId.toString());
        if (cart == null) {
            return false;
        }
        //修改sku数量
        Cart parse = JsonUtils.parse(cart.toString(), Cart.class);
        parse.setNum(num);

        //保存到redis
        String serialize = JsonUtils.serialize(parse);
        redisTemplate.opsForHash().put(getCartKey(), skuId.toString(),serialize);
        return true;
    }

    public void deleteCart(Long skuId) {
        redisTemplate.opsForHash().delete(getCartKey(), skuId.toString());
    }

    /**
     * 用sku id封装成cart
     * @param skuId
     * @param num
     * @return
     */
    private Cart skuToCart(Long skuId,Integer num) {
        //获取sku
        Sku sku = itemClient.getSkuById(skuId);
        if (sku == null) {
            return null;
        }
        //封装cart
        Cart cart = new Cart();
        cart.setSkuId(sku.getId());
        String image = StringUtils.substringBefore(sku.getImages(), ",");
        cart.setImage(image);
        cart.setNum(num);
        cart.setPrice(sku.getPrice());
        cart.setTitle(sku.getTitle());
        cart.setOwnSpec(JsonUtils.parseMap(sku.getOwnSpec(),Integer.class,String.class));

        return cart;
    }

    public boolean pushCartList(List<Cart> carts) {
        //验证参数可用性
        if (CollectionUtils.isEmpty(carts)) {
            return false;
        }

        //获取购物车
        BoundHashOperations hashOper = getHashOper();
        //有无此商品
        carts.forEach(cart -> {
            Object o = hashOper.get(cart.getSkuId().toString());
            if (o == null) {
                //无，插入商品
                hashOper.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
            } else {
                //有，修改数量
                Cart parse = JsonUtils.parse(o.toString(), Cart.class);
                parse.setNum(parse.getNum() + cart.getNum());
                hashOper.put(cart.getSkuId().toString(), JsonUtils.serialize(parse));
            }
        });

        return true;
    }

    private BoundHashOperations getHashOper() {
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(getCartKey());
        return hashOps;
    }
}
