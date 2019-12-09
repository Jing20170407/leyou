package com.example.api;

import com.example.pojo.SpecGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SpecGroupApi {

    @GetMapping("spec/groups/{cid}")
    List<SpecGroup> getSpecGroup(@PathVariable("cid") Long cid);
}
