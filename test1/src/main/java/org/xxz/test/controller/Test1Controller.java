package org.xxz.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxz.test.service.Test1Service;


/**
 * @author jsbxyyx
 * @since 
 */
@RestController
public class Test1Controller {

    @Autowired
    Test1Service test1Service;

    @RequestMapping("/test3")
    public String test3(String xx) {
        test1Service.test3();
        return "success";
    }

}
