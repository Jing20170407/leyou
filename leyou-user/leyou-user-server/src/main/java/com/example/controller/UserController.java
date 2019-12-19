package com.example.controller;

import com.example.pojo.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data")String data,
                                             @PathVariable("type")Integer type) {
        Boolean boo = userService.checkData(data, type);
        if (boo == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(boo);
    }

    @PostMapping("/code")
    public ResponseEntity<Void> getCode(@RequestParam("phone") String phone) {
        Boolean b = userService.sendSms(phone);
        if (!b) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Validated User user,@RequestParam("code") String code) {
        Boolean b = userService.registerUser(user, code);
        if (!b) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username,
                                          @RequestParam("password") String password) {
        User user = userService.queryUser(username, password);
        if (user==null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }

}
