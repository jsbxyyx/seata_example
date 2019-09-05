package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jsbxyyx
 * @since
 */
@Service
public class TestService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GlobalTransactional
    @Transactional
    public void test1() {

        jdbcTemplate.update("insert into test values(test_seq.nextval, ?, ?)", new Object[]{"11", "111"});

        throw new RuntimeException("rollback");
    }
}

