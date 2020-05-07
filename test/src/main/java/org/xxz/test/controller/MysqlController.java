package org.xxz.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxz.test.service.MysqlService;

/**
 * @author tt
 */
@RestController
@RequestMapping("/mysql")
public class MysqlController {

    @Autowired
    MysqlService service;

    @RequestMapping("/test1")
    public String test1() throws Exception {
        service.test1();
        return "success";
    }

    @RequestMapping("/test2")
    public String test2() throws Exception {
        service.test2();
        return "success";
    }

    @RequestMapping("/test3")
    public String test3() throws Exception {
        service.test3();
        return "success";
    }

    @RequestMapping("/test4")
    public String test4() throws Exception {
        service.test4();
        return "success";
    }

    @RequestMapping("/test5")
    public String test5(@RequestParam(required = false, defaultValue = "1") int c) throws Exception {
        service.test5(c);
        return "success";
    }

    @RequestMapping("/test6")
    public String test6(@RequestParam(required = false, defaultValue = "1") int c) throws Exception {
        service.test6(c);
        return "success";
    }

    @RequestMapping("/test7")
    public String test7() throws Exception {
        service.test7();
        return "success";
    }

    @RequestMapping("/test8")
    public String test8() throws Exception {
        service.test8();
        return "success";
    }

    @RequestMapping("/test9")
    public String test9() throws Exception {
        service.test9();
        return "success";
    }

    @RequestMapping("/test10")
    public String test10() throws Exception {
        service.test10();
        return "success";
    }

    @RequestMapping("/test11")
    public String test11(@RequestParam(required = false, defaultValue = "1") int n) throws Exception {
        service.test11(n);
        return "success";
    }

    @RequestMapping("/test12")
    public String test12(@RequestParam(required = false, defaultValue = "1") int n) throws Exception {
        service.test12(n);
        return "success";
    }

    @RequestMapping("/test13")
    public String test13(@RequestParam(required = false, defaultValue = "1") int n) throws Exception {
        service.test13(n);
        return "success";
    }

    @RequestMapping("/test14")
    public String test14(@RequestParam(required = false, defaultValue = "1") int n) throws Exception {
        service.test14(n);
        return "success";
    }

    @RequestMapping("/test15")
    public String test15(@RequestParam(required = false, defaultValue = "1") int n) throws Exception {
        service.test15(n);
        return "success";
    }

}
