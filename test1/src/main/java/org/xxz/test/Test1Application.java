package org.xxz.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jsbxyyx
 * @since
 */
@SpringBootApplication(scanBasePackages = {"org.xxz"})
public class Test1Application {

    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);
    }

}
