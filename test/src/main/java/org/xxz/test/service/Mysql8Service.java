package org.xxz.test.service;

import io.seata.spring.annotation.GlobalLock;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.xxz.test.dao.TkMapper;
import org.xxz.test.dao.TkTest;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author jsbxyyx
 */
@ConditionalOnProperty(value = "spring.datasource.mysql8.enable", havingValue = "true")
@Service
public class Mysql8Service {

    Random r = new Random();

    @Autowired
    @Qualifier("mysql8jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("mysql8jdbcTemplateorigin")
    private JdbcTemplate jdbcTemplateorigin;

    @Autowired
    @Qualifier("mysql8namedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;


    @Autowired
    @Qualifier("mysql8sqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CommonService commonService;

    @GlobalTransactional(timeoutMills = 5 * 60000)
    @Transactional(transactionManager = "mysql8TM", rollbackFor = Exception.class)
    public void test1(int n) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into test1(id, name) values(null, ?), (null, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setObject(i++, "xx");
            ps.setObject(i++, "xx");
            return ps;
        }, keyHolder);
        Assert.isTrue(keyHolder.getKeyList().size() == 2, "size != 2");
    }


    @GlobalTransactional
//    @Transactional(transactionManager = "mysql8TM")
    public void test2(int n) {
        TkMapper mapper = sqlSessionFactory.openSession().getMapper(TkMapper.class);
        List<TkTest> list = new ArrayList<>();
        TkTest tkTest = null;
        for (int i = 0; i < 10000; i++) {
            tkTest = new TkTest();
            tkTest.setName("xx" + i);
            list.add(tkTest);
        }
        mapper.insertList(list);

        commonService.error();
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test3(int n) {
        String sql = "insert into test1(id) values(?)";
        jdbcTemplate.update(sql, new Object[]{null});

        test4(n);

        commonService.ok();
    }

    @GlobalLock
    public void test4(int n) {
        String sql = "select * from test1 where id = 9";
        jdbcTemplate.queryForList(sql);
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test5(int n) {
//        String sql = "update test1, test2" +
//                " set test1.name = '1', test2.name2 = '2xxxxx'" +
//                " where test2.name = '2xx'";

        String sql = "update test1, (select 1 as id) t set test1.name = '1' where test1.id = t.id";
        jdbcTemplate.update(sql);

        commonService.error();
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    @Transactional(rollbackFor = Exception.class)
    public void test6(int n) {
        // 40MB
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(new File(System.getProperty("user.dir") + "/test/tmp.txt").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final byte[] bs = bytes;
        boolean insert = (n == 0);
        if (insert) {
            jdbcTemplate.update("truncate test");
            String sql = "insert into test(id, name, file) values(?,?,?)";
            jdbcTemplate.update(con -> {
                System.out.println(con.getClass().getName());
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setObject(1, 1);
                ps.setObject(2, "name");
                ps.setObject(3, new ByteArrayInputStream(bs));
                return ps;
            });
        } else {
            String sql = "update test set name=?, file=? where id=1";
            jdbcTemplate.update(con -> {
                System.out.println(con.getClass().getName());
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setObject(1, "name2");
                ps.setObject(2, new ByteArrayInputStream(bs));
                return ps;
            });
        }
    }

    public void http(int c, int n) {
        restTemplate.getForObject("http://127.0.0.1:8003/mysql8/test?c=" + c + "&n=" + n, String.class);
    }

}
