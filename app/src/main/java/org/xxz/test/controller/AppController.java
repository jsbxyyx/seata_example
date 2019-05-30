package org.xxz.test.controller;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxz.test.service.AppService;

/**
 * @author jsbxyyx
 * @since
 */
@RestController
public class AppController {

    @Autowired
    AppService appService;

    @GlobalTransactional
    @RequestMapping("/call")
    public String call() {
        int code = appService.call();
        return String.valueOf(code);
    }

}
