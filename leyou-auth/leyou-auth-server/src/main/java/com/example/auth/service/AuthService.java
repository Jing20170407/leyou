package com.example.auth.service;

import com.example.auth.client.UserClient;
import com.example.auth.config.JwtPropertise;
import com.example.auth.entity.UserInfo;
import com.example.auth.utils.JwtUtils;
import com.example.auth.utils.RsaUtils;
import com.example.pojo.User;
import com.example.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;

@Service
@EnableConfigurationProperties(JwtPropertise.class)
public class AuthService {


    @Autowired
    private JwtPropertise prop;
    @Autowired
    private UserClient userClient;

    public void accredit(String username, String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //验证用户名密码
        User user = userClient.queryUser(username, password);
        if (user == null) {
            throw new RuntimeException("用户名密码错误");
        }

        //生成jwt,token
        UserInfo userInfo = new UserInfo(user.getId(),user.getUsername());
        PrivateKey privateKey = RsaUtils.getPrivateKey(prop.getPriKeyPath());

        String token = JwtUtils.generateToken(userInfo,privateKey , prop.getExpire());

        //添加到cookie

        CookieUtils.setCookie(request,response,prop.getCookieName(),token,prop.getExpire());
    }


    public UserInfo verify(HttpServletRequest request) throws Exception {
        //获取token
        String leyoutoken = CookieUtils.getCookieValue(request, prop.getCookieName());
        //校验token
        UserInfo userInfo = JwtUtils.getInfoFromToken(leyoutoken, RsaUtils.getPublicKey(prop.getPubKeyPath()));
        return userInfo;
    }
}
