package com.kenny.baselibrary.test;

import android.test.InstrumentationTestCase;


/**
 * 单元测试案例
 * @author kenny
 * @time 2016/2/17 22:38
 */
public class ExampleTest extends InstrumentationTestCase {

    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}
