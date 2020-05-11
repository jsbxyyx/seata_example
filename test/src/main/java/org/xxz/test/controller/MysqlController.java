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

    @RequestMapping("/test")
    public String test(@RequestParam(value = "n", required = false, defaultValue = "1") int n,
                       @RequestParam(value = "c", required = false, defaultValue = "1") int c) throws Exception {
        service.getClass().getDeclaredMethod("test" + c, new Class[]{int.class}).invoke(service, new Object[]{n});
        return "success";
    }

}
