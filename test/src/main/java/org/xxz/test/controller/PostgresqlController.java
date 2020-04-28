package org.xxz.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxz.test.service.PostgresqlService;

/**
 * @author tt
 */
@RestController
@RequestMapping("/postgresql")
public class PostgresqlController {

    @Autowired
    PostgresqlService service;

    @RequestMapping("/test1")
    public String test1() throws Exception {
        service.test1();
        return "success";
    }

    @RequestMapping("/test2")
    public String test2(@RequestParam(required = false, defaultValue = "false") boolean r) throws Exception {
        service.test2(r);
        return "success";
    }

    @RequestMapping("/test3")
    public String test3() throws Exception {
        service.test3();
        return "success";
    }


    @RequestMapping("/test4")
    public String test4(@RequestParam(required = false, defaultValue = "1") int n) throws Exception {
        service.test4(n);
        return "success";
    }

    @RequestMapping("/test5")
    public String test5(@RequestParam(required = false, defaultValue = "1") int n) throws Exception {
        service.test5(n);
        return "success";
    }

    @RequestMapping("/test6")
    public String test6(@RequestParam(required = false, defaultValue = "1") int n) {
        service.test6(n);
        return "success";
    }

    @RequestMapping("/test7")
    public String test7(@RequestParam(required = false, defaultValue = "1") int n) {
        service.test7(n);
        return "success";
    }

}
