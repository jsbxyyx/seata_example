package org.xxz.test.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxz.test.service.CommonService;

import javax.annotation.Resource;

/**
 * @author jsbxyyx
 */
@ConditionalOnProperty(value = "spring.datasource.mysql8.enable", havingValue = "true")
@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    private CommonService commonService;

    @RequestMapping("/error")
    public void error(@RequestParam(value = "error", required = false, defaultValue = "1") int n) {
        commonService.doError(n);
    }

}
