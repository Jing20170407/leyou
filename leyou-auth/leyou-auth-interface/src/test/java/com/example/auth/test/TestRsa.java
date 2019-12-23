package com.example.auth.test;

import com.example.auth.utils.RsaUtils;
import org.junit.Test;

public class TestRsa {

    @Test
    public void testGenRsa() {
        try {
            RsaUtils.generateKey("D:\\tmp\\rsa\\rsa.pub", "D:\\tmp\\rsa\\rsa.pri", "leyou@Login(Auth}*^31)&heiMa%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
