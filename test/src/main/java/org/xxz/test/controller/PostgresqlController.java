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
}
