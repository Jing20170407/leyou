package com.example.controller;

import com.example.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.util.Map;

@Controller
@RequestMapping("item")
public class ItemPageController {
    @Autowired
    private ItemService itemService;

    @GetMapping("{id}.html")
    public String getItemPage(@PathVariable("id") Long spuId, Model model){

        Map<String,Object> map = itemService.getItemModel(spuId);
        model.addAllAttributes(map);

        itemService.savePage(spuId);

        return "item";
    }

}
