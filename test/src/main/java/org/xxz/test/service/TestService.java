package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.xxz.test.dao.Test1Mapper;
import org.xxz.test.dao.Test1;
import org.xxz.test.dao.TkMapper;
import org.xxz.test.dao.TkTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author jsbxyyx
 * @since
 */
@Service
public class TestService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test1() {
        jdbcTemplate.update("insert into test values(test_seq.nextval, ?, ?)", new Object[]{"11", "111"});
        throw new RuntimeException("rollback");
    }

    @Autowired
    private Test1Mapper test1Mapper;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test2() {
        Test1 param = new Test1();
        param.setName("xx");
        param.setName2("xx2");
        test1Mapper.saveOracle(param);
        System.out.println(String.format("id=%s", param.getId()));
    }

    @Resource
    private RestTemplate restTemplate;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test3() {
        jdbcTemplate.update("insert into test values(?, ?, ?)", new Object[]{null, "xx", "xx2"});
        restTemplate.getForObject("http://127.0.0.1:8004/test3", String.class);
    }

    @Autowired
    private TkMapper mapper;

    @GlobalTransactional
    public void test4() {
        List<TkTest> list = new ArrayList<>();
        list.add(new TkTest(null, "xx", "xx2"));
        list.add(new TkTest(null, "yy", "yy2"));
        mapper.insertList(list);
    }

    @GlobalTransactional
    public void test5() {
        List<TkTest> list = new ArrayList<>();
        list.add(new TkTest(3L, "xxxx", "xxxx2"));
        list.add(new TkTest(4L, "yyyy", "yyyy2"));
        mapper.batchUpdate(list);
    }

    @GlobalTransactional
    public void test6() {
        List<Object[]> args = new ArrayList<>();
        args.add(new Object[]{"xxxx", "xxxx2", 3L});
        args.add(new Object[]{"yyyy", "yyyy2", 4L});
        jdbcTemplate.batchUpdate("update tk_test set name = ?, name2 = ? where id = ?", args);
    }

    @GlobalTransactional
    public void test7() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_escape(`sid`,`param`,`createTime`) values (?, ?, ?)",
                new Object[]{sid, "a", new Date()});
    }

    @GlobalTransactional
    public void test8() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_keyword values (?, ?)",
                new Object[]{sid, "a"});
    }

    @GlobalTransactional
    public void test9() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_escape(\"sid\",\"param\", \"createTime\") values (?, ?, ?)",
                new Object[]{sid, "a", new Date()});
    }

}

