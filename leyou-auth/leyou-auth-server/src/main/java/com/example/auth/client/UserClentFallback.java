package com.example.auth.client;

import com.example.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class UserClentFallback implements UserClient {
    @Override
    public Boolean checkData(String data, Integer type) {
        return null;
    }

    @Override
    public User queryUser(String username, String password) {
        return null;
    }
}
