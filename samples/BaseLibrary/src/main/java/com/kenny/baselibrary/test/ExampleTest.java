package com.kenny.baselibrary.test;

import android.test.InstrumentationTestCase;

/**
 * description 单元测试案例
 * Created by kenny on 2016/2/17.
 * version 1.0
 */
public class ExampleTest extends InstrumentationTestCase {

    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}
