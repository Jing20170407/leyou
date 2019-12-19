package com.example.service;

import com.example.config.SmsProperties;
import com.example.mapper.UserMapper;
import com.example.pojo.User;
import com.example.utils.CodecUtils;
import com.example.utils.NumberUtils;
import com.example.utils.SmsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

@Service
@EnableConfigurationProperties(SmsProperties.class)
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsProperties prop;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String REDIS_CODE_KEY = "user:code:";

    public Boolean checkData(String data, Integer type) {
        //数据格式校验
        if (StringUtils.isBlank(data)) {
            return null;
        }

        if (type == null) {
            return null;
        }

        //唯一性校验
        User user = new User();
        if (type == 1) {
            //用户名
            user.setUsername(data);
        } else if (type == 2) {
            //手机
            user.setPhone(data);
        } else {
            return null;
        }


        return userMapper.selectCount(user)==0;
    }

    public Boolean sendSms(String phone) {


        if (StringUtils.isBlank(phone)||!phone.matches("^1[35796]\\d{9}$")) {
            return false;
        }

        //生成验证码
        String code = NumberUtils.generateCode(6);

        //发送短信
/*        SmsUtils.sendSms(
                prop.getAccessKeyID(),
                prop.getAccessKeySecret(),
                phone,
                prop.getSignName(),
                prop.getTemplateCode(),
                "{'code':'"+ code +"'}");*/
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        amqpTemplate.convertAndSend("leyou.sms.code",map);

        //保存到redis
        redisTemplate.opsForValue().set(REDIS_CODE_KEY + phone, code, 5, TimeUnit.MINUTES);

        return true;
    }

    public Boolean registerUser(User user,String code) {
        //检验验证码
        String redisCode = redisTemplate.opsForValue().get(REDIS_CODE_KEY + user.getPhone());
        if (!(redisCode != null && redisCode.equals(code))) {
            return false;
        }
        //检查唯一性
        if (!(checkData(user.getUsername(), 1) && checkData(user.getPassword(), 2))) {
            return false;
        }

        //密码加密加盐
        String salt = CodecUtils.generateSalt();
        String password = CodecUtils.md5Hex(user.getPassword(), salt);

        user.setSalt(salt);
        user.setPassword(password);
        user.setCreated(LocalDateTime.now());
        //插入数据

        userMapper.insertSelective(user);

        redisTemplate.delete(REDIS_CODE_KEY + user.getPhone());
        return true;
    }

    public User queryUser(String username, String password) {
        //查询是否有此用户
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        if (user == null) {
            return null;
        }
        //验证密码是否正确
        String md5Hex = CodecUtils.md5Hex(password, user.getSalt());
        if (!user.getPassword().equals(md5Hex)) {
            return null;
        }

        return user;
    }
}

