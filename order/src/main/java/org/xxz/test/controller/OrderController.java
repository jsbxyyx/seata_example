package org.xxz.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxz.test.service.OrderService;


/**
 * @author jsbxyyx
 * @since 
 */
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/createOrder")
    public String createOrder(int userId, double amount) {
        int code = orderService.createOrder(userId, amount);
        return String.valueOf(code);
    }

}
