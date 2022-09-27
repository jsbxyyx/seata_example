package org.xxz.test.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * @author jsbxyyx
 * @since
 */
@Configuration
public class Config {

    private ApplicationContext ac;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
