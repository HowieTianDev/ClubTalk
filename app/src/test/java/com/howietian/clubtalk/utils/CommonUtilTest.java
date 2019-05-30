package com.howietian.clubtalk.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommonUtilTest {




    @Test
    public void dip2px() {
    }
    @Before
    public void setUp() throws Exception {
        System.out.println("开始测试");
    }
    @After
    public void tearDown() throws Exception {
        System.out.println("结束测试");
    }
    @Test
    public void isPhoneNum() {
        assertEquals(false,CommonUtil.isPhoneNum("aabbccc"));
    }

    @Test
    public void isStringFormatCorrect() {
    }

    @Test
    public void getFilePathFromContentUri() {
    }
}