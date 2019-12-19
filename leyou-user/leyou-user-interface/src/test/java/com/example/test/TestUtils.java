package com.example.test;


import com.example.utils.NumberUtils;
import com.example.utils.SmsUtils;
import com.fasterxml.jackson.databind.JsonSerializable;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;


public class TestUtils {

    @Test
    public void test1() {
        double scale = NumberUtils.scale(5.515555d, 2);
        System.out.println(scale);
    }

    @Test
    public void testSms() {
        SmsUtils.sendSms(
                "LTAI4FmWe5WkBkahkXoWqzHq",
                "2mdCyrybVhucpsnqPGNUyM6oWVyKYH",
                "15019965572",
                "乐忧商城",
                "SMS_180345992",
                "{'code':'10086'}");
    }

    @Test
    public void testBase64() {
        byte[] bytes = Base64.encodeBase64(StringUtils.getBytesUtf8("a974375135@163.com"));
        System.out.println(StringUtils.newString(bytes,"utf-8"));
        bytes = Base64.encodeBase64(StringUtils.getBytesUtf8("a974375135"));
        System.out.println(StringUtils.newString(bytes,"utf-8"));
    }

    @Test
    public void testString() throws UnsupportedEncodingException {
        byte[] bytes = "YTk3NDM3NTEzNUAxNjMuY29t".getBytes("utf-8");
        byte[] bytes1 = Base64.decodeBase64(bytes);
        System.out.println(new String(bytes1,"utf-8"));

    }

}
