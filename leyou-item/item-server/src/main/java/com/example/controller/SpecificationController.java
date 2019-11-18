package com.example.controller;

import com.example.Interface.AddInterface;
import com.example.Interface.UpdateInterface;
import com.example.common.ControllerUtils;
import com.example.service.SpecGroupService;
import com.example.service.SpecParamService;
import com.example.pojo.SpecGroup;
import com.example.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecGroupService specGroupService;

    @Autowired
    private SpecParamService specParamService;

    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> getSpecGroup(@PathVariable("cid") Long cid) {
        List<SpecGroup> specGroups = specGroupService.getSpecGroupByCid(cid);
        if (CollectionUtils.isEmpty(specGroups)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specGroups);
    }

    @PostMapping("/group")
    public ResponseEntity<Object> addSpecGroup(@Validated(AddInterface.class) @RequestBody SpecGroup specGroup, BindingResult bindingResult) {
        ResponseEntity<Object> bindingResultEntity = ControllerUtils.getBindingResultEntity(bindingResult);
        if (bindingResultEntity != null) {
            return bindingResultEntity;
        }
        specGroupService.addSpecGroup(specGroup);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<Object> delectSpecGroup(@PathVariable("id") Long id) {
        boolean bo = specGroupService.delectSpecGroup(id);
        if (!bo) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/group")
    public ResponseEntity updateSpecGroup(@Validated(UpdateInterface.class) @RequestBody SpecGroup specGroup, BindingResult bindingResult) {
        ResponseEntity<Object> bindingResultEntity = ControllerUtils.getBindingResultEntity(bindingResult);
        if (bindingResultEntity != null) {
            return bindingResultEntity;
        }
        specGroupService.updateSpecGroup(specGroup);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> getSpecParam(@RequestParam("gid") Long gid) {
        List<SpecParam> specParams = specParamService.getSpecGroupByGid(gid);
        if (CollectionUtils.isEmpty(specParams)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specParams);
    }
}
