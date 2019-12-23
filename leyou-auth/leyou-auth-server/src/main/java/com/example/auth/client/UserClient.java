package com.example.auth.client;

import com.example.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-user-server", fallback = UserClentFallback.class)
public interface UserClient extends UserApi {

}
