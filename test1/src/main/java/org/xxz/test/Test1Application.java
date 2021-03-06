package org.xxz.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author jsbxyyx
 * @since
 */
@SpringBootApplication(scanBasePackages = {"org.xxz"})
@EnableDiscoveryClient
public class Test1Application {

    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);
    }

}
