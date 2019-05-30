package org.xxz.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jsbxyyx
 * @since
 */
@Service
public class AppService {

    static final AtomicInteger gen = new AtomicInteger(10);

    @Autowired
    RestTemplate restTemplate;

    public int call() {
        int id = gen.getAndIncrement();
        order(id);
        user(id);
        return 1;
    }

    public void order(int id) {
        try {
            String orderResponse = restTemplate.getForObject("http://127.0.0.1:8001/createOrder?userId="+ id + "&amount=10", String.class);
            System.out.println("orderResponse=" + orderResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void user(int id) {
        try {
            String userResponse = restTemplate.getForObject("http://127.0.0.1:8002/createUser?userId="+ id + "&name=admin", String.class);
            System.out.println("userResponse=" + userResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}