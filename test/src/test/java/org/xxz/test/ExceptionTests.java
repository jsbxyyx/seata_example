package org.xxz.test;

/**
 * @author jsbxyyx
 */
public class ExceptionTests {

    public static void main(String[] args) throws Throwable {
        Object obj = null;
        try {
            obj.toString();
        } catch (Exception e) {
            throw e.getCause();
        }
    }

}
