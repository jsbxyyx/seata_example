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

}