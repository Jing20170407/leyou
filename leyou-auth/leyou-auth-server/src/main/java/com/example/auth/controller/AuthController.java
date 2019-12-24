package com.example.auth.controller;

import com.example.auth.config.JwtPropertise;
import com.example.auth.entity.UserInfo;
import com.example.auth.service.AuthService;
import com.example.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableConfigurationProperties(JwtPropertise.class)
public class AuthController {

    @Autowired
    private JwtPropertise prop;

    @Autowired
    private AuthService authService;

    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        String token = authService.accredit(username, password);

        if (token == null) {
            return ResponseEntity.badRequest().build();
        }
        //添加到cookie
        CookieUtils.setCookie(request, response, prop.getCookieName(), token, prop.getExpire() * 60);

        return ResponseEntity.ok().build();
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(HttpServletRequest request,HttpServletResponse response) {
        UserInfo userInfo = authService.verify(request,response);
        if (userInfo == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userInfo);
    }
}
