package com.example.api;

import com.example.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
    @GetMapping("/check/{data}/{type}")
    Boolean checkData(@PathVariable("data")String data,
                      @PathVariable("type")Integer type);

    @PostMapping("/query")
    User queryUser(@RequestParam("username") String username,
                   @RequestParam("password") String password);
}
