package org.xxz.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxz.test.service.Mysql8Service;

/**
 * @author jsbxyyx
 */
@ConditionalOnProperty(value = "spring.datasource.mysql8.enable", havingValue = "true")
@RestController
@RequestMapping("/mysql8")
public class Mysql8Controller {

    @Autowired
    Mysql8Service service;

    @RequestMapping("/test")
    public String test(@RequestParam(value = "n", required = false, defaultValue = "1") int n,
                       @RequestParam(value = "c", required = false, defaultValue = "1") int c) throws Exception {
        service.getClass().getDeclaredMethod("test" + c, new Class[]{int.class}).invoke(service, new Object[]{n});
        return "success";
    }

}
