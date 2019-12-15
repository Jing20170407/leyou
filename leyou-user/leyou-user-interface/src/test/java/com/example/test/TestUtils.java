package com.example.test;


import com.example.utils.NumberUtils;
import org.junit.Test;



public class TestUtils {

    @Test
    public void test1() {
        double scale = NumberUtils.scale(5.515555d, 2);
        System.out.println(scale);
    }

}
