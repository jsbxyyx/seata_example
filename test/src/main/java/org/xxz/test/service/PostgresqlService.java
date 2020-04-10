package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xxz.test.dao.Test1Mapper;
import org.xxz.test.dao.TkMapper;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;

/**
 * @author tt
 */
@Service
public class PostgresqlService {

    Random r = new Random();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    private Test1Mapper test1Mapper;

    @Autowired
    private TkMapper mapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private RestTemplate restTemplate;

    @GlobalTransactional
    public void test1() {
        String sql = "insert into test_uuid values(?, ?)";
        jdbcTemplate.update(sql, UUID.randomUUID().toString(), "xx");
    }

    @GlobalTransactional
    public void test2(boolean r) {
        String sql = "insert into \"TEST\" values(?, ?)";
        jdbcTemplate.update(sql, UUID.randomUUID().toString(), "xx");
        restTemplate.getForObject("http://127.0.0.1:8003/postgresql/test1", String.class);
        if (r) {
            throw new RuntimeException("rollback");
        }
    }

    @GlobalTransactional
    public void test3() {
        String sql = "insert into test(id, name) values(nextval('test_seq'), ?)";
        jdbcTemplate.update(sql, "xxx");
    }

    @GlobalTransactional
    public void test4() {
        String sql = "insert into test(id) values(nextval('test_seq'))";
        jdbcTemplate.update(sql);
    }
}
