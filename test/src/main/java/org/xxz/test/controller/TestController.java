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

    @RequestMapping("/test3")
    public String test3() {
        testService.test3();
        return "success";
    }

    @RequestMapping("/test4")
    public String test4() {
        testService.test4();
        return "success";
    }

    @RequestMapping("/test5")
    public String test5() {
        testService.test5();
        return "success";
    }

    @RequestMapping("/test6")
    public String test6() {
        testService.test6();
        return "success";
    }

    @RequestMapping("/test7")
    public String test7() throws Exception {
        testService.test7();
        return "success";
    }

    @RequestMapping("/test8")
    public String test8() {
        testService.test8();
        return "success";
    }

    @RequestMapping("/test9")
    public String test9() {
        testService.test9();
        return "success";
    }

    @RequestMapping("/test10")
    public String test10() {
        testService.test10();
        return "success";
    }

}
