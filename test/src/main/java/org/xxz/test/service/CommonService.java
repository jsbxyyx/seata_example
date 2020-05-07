package org.xxz.test.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author jsbxyyx
 */
@Service
public class CommonService {

    @Resource
    private RestTemplate restTemplate;

    public void error() {
        restTemplate.getForObject("http://127.0.0.1:8003/common/error?error=0", String.class);
    }

    public void noerror() {
        restTemplate.getForObject("http://127.0.0.1:8003/common/error?error=1", String.class);
    }

}
