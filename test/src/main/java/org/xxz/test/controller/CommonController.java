package org.xxz.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jsbxyyx
 */
@RestController
@RequestMapping("/common")
public class CommonController {


    @RequestMapping("/error")
    public void error(@RequestParam(value = "error", required = false, defaultValue = "1") int n) {
        if (n % 2 == 0) {
            throw new RuntimeException("故意抛异常");
        }
    }

}
