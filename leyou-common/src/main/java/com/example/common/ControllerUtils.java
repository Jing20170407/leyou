package com.example.common;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControllerUtils {
    public static ResponseEntity<Object> getBindingResultEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> list = new ArrayList<>();
            bindingResult.getFieldErrors().forEach((fieldError -> {
                String msg = fieldError.getField() + " : " + fieldError.getDefaultMessage();
                list.add(msg);
            }));
            return ResponseEntity.badRequest().body(list);
        }
        return null;
    }
}
