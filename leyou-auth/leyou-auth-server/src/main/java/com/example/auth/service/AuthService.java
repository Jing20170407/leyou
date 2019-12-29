package com.example.auth.service;

import com.example.auth.client.UserClient;
import com.example.auth.config.JwtPropertise;
import com.example.auth.entity.UserInfo;
import com.example.auth.utils.JwtUtils;
import com.example.auth.utils.RsaUtils;
import com.example.pojo.User;
import com.example.utils.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;

@Service
@EnableConfigurationProperties(JwtPropertise.class)
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private JwtPropertise prop;
    @Autowired
    private UserClient userClient;

    public String accredit(String username, String password){
        //验证用户名密码
        User user = userClient.queryUser(username, password);
        if (user == null) {
            return null;
        }

        //生成jwt,token
        try {
            UserInfo userInfo = new UserInfo(user.getId(),user.getUsername());
            PrivateKey privateKey = RsaUtils.getPrivateKey(prop.getPriKeyPath());

            String token = JwtUtils.generateToken(userInfo, privateKey, prop.getExpire());

            return token;
        } catch (Exception e) {
            LOGGER.info("登录失败",e.toString());
        }
        return null;
    }


    public UserInfo verify(HttpServletRequest request,HttpServletResponse response){
        try {
            //获取token
            String token = CookieUtils.getCookieValue(request, prop.getCookieName());
            //校验token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, RsaUtils.getPublicKey(prop.getPubKeyPath()));
            //重新设置token
            token = JwtUtils.generateToken(userInfo, prop.getPrivateKey(), prop.getExpire());
            //重新设置cookie
            CookieUtils.setCookie(request, response, prop.getCookieName(), token, prop.getExpire() * 60);

            return userInfo;
        } catch (Exception e) {
            LOGGER.info("验证失败",e.toString());
        }


        return null;
    }
}
