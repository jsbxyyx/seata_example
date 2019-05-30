package org.xxz.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxz.test.service.UserService;


/**
 * @author jsbxyyx
 * @since 
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/createUser")
    public String createOrder(int userId, String name) {
        int code = userService.createUser(userId, name);
        return String.valueOf(code);
    }

}
