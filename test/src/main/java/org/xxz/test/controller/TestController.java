package org.xxz.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxz.test.service.TestService;


/**
 * @author jsbxyyx
 * @since 
 */
@RestController
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping("/test1")
    public String test1() {
        testService.test1();
        return "success";
    }

    @RequestMapping("/test2")
    public String test2() {
        testService.test2();
        return "success";
    }

}
