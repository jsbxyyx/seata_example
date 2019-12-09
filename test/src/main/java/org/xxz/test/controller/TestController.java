package org.xxz.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        testService.test1_oracle();
        return "success";
    }

    @RequestMapping("/test2")
    public String test2() {
        testService.test2_oracle();
        return "success";
    }

    @RequestMapping("/test3")
    public String test3() {
        testService.test3_mysql();
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
        testService.test8_mysql();
        return "success";
    }

    @RequestMapping("/test9")
    public String test9(@RequestParam(required = false) boolean r) {
        testService.test9(r);
        return "success";
    }

    @RequestMapping("/test10")
    public String test10() {
        testService.test10_oracle();
        return "success";
    }

    @RequestMapping("/test11")
    public String test11() {
        testService.test11();
        return "success";
    }

    @RequestMapping("/test12")
    public String test12() {
        testService.test12();
        return "success";
    }

    @RequestMapping("/test13")
    public String test13() {
        testService.test13_mysql();
        return "success";
    }

    @RequestMapping("/test14")
    public String test14() {
        testService.test14();
        return "success";
    }

    @RequestMapping("/test15")
    public String test15() {
        testService.test15_mysql();
        return "success";
    }

    @RequestMapping("/test16")
    public String test16() {
        testService.test16_oracle();
        return "success";
    }

    @RequestMapping("/test17")
    public String test17() {
        testService.test17_oracle();
        return "success";
    }

    @RequestMapping("/test18")
    public String test18() {
        testService.test18_oracle();
        return "success";
    }

    @RequestMapping("/test19")
    public String test19() {
        testService.test19_oracle();
        return "success";
    }

    @RequestMapping("/test20")
    public String test20() {
        testService.test20_oracle();
        return "success";
    }

    @RequestMapping("/test21")
    public String test21() {
        testService.test21_oracle();
        return "success";
    }

    @RequestMapping("/test22")
    public String test22(Integer cs) {
        testService.test22_oracle(cs);
        return "success";
    }

    @RequestMapping("/test23")
    public String test23(Integer cs) {
        testService.test23_mysql(cs);
        return "success";
    }

    @RequestMapping("/test24")
    public String test24(Integer cs) {
        testService.test24_oracle(cs);
        return "success";
    }

    @RequestMapping("/test25")
    public String test25(Integer c) {
        testService.test25_mysql(c);
        return "success";
    }

    @RequestMapping("/test26")
    public String test26(@RequestParam(required = false, defaultValue = "0") int c) {
        testService.test26(c);
        return "success";
    }

    @RequestMapping("/test27")
    public String test27() {
        testService.test27();
        return "success";
    }

    @RequestMapping("/test28")
    public String test28() {
        testService.test28();
        return "success";
    }
}
