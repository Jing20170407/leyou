package com.example.cart.controller;

import com.example.cart.pojo.Cart;
import com.example.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {

        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();

        boolean b = cartService.addCart(skuId,num);
        if (!b) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getCarts() {
        List<Cart> carts = cartService.getCartList();
        if (carts==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }

    @PutMapping
    public ResponseEntity<Void> updateCart(@RequestBody Cart cart) {

        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();

        boolean b = cartService.updateCart(skuId,num);
        if (!b) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId) {
        cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/list")
    public ResponseEntity<Void> pushCartList(@RequestBody List<Cart> carts) {
        boolean b = cartService.pushCartList(carts);
        if (!b) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


}
