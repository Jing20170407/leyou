package com.example.api;

import com.example.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecParamApi {
    @GetMapping("spec/params")
    List<SpecParam> getSpecParam(@RequestParam(value = "gid",required = false) Long gid,
                                 @RequestParam(value = "cid",required = false) Long cid);
}
