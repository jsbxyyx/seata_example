package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author jsbxyyx
 */
@ConditionalOnProperty(value = "spring.datasource.mysql8.enable", havingValue = "true")
@Service
public class CommonService {

    @Resource
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("mysql8jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public void error() {
        restTemplate.getForObject("http://127.0.0.1:8003/common/error?error=0", String.class);
    }

    public void ok() {
        restTemplate.getForObject("http://127.0.0.1:8003/common/error?error=1", String.class);
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
//    @Transactional(transactionManager = "mysqlTM")
    public void doError(int n) {
        jdbcTemplate.update("update test1 set id = ?, name = ? where id = ?", new Object[]{9, "xx", 9});
        if (n % 2 == 0) {
            throw new RuntimeException("故意抛异常");
        }
    }
}
