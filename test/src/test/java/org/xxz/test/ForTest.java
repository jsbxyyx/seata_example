package org.xxz.test;

import java.util.concurrent.TimeUnit;

/**
 * @author jsbxyyx
 */
public class ForTest {

    public static void main(String[] args) throws Exception {
        // break label; 用于跳出多层循环
        retry:
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            break retry;
        }
        System.out.println(22);

        // continue label 用于跳出内层循环
        retry2:
        for (;;) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(11);
            for (;;) {
                System.out.println(22);
                continue retry2;
            }
        }
    }

}
