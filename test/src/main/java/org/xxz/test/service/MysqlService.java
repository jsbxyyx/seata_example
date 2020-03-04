package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.xxz.test.dao.Test1Mapper;
import org.xxz.test.dao.TestUuidMapper;
import org.xxz.test.dao.TkMapper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author tt
 */
@Service
public class MysqlService {

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
    TestUuidMapper testUuidMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private RestTemplate restTemplate;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test1() {
        jdbcTemplate.update("insert into test values(?, ?, ?)", new Object[]{null, "xx", "xx2"});
        restTemplate.getForObject("http://127.0.0.1:8004/mysql/test1", String.class);
    }

    @GlobalTransactional
    public void test2() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_escape(`sid`,`param`,`createTime`) values (?, ?, ?)",
                new Object[]{sid, "a", new Date()});
    }

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test3() {
        jdbcTemplate.update("insert into test values(null, ?, ?)", new Object[]{"11", "111"});
        throw new RuntimeException("rollback");
    }

    @GlobalTransactional
    public void test4() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_escape(sid,param,createTime) values (?, ?, ?)",
                new Object[]{sid, "a", new Date()});
    }

    @GlobalTransactional
    public void test5(int c) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = null;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        switch (c) {
            case 1:
                sql = "insert into test3 (id, name) values (null, :name)";
                sqlParameterSource.addValue("name", "xx");
                break;
            case 2:
                sql = "insert into test3 values (null, :name, :name2)";
                sqlParameterSource.addValue("name", "xx");
                sqlParameterSource.addValue("name2", "xx2");
                break;
            default:
                break;
        }

        namedJdbcTemplate.update(sql, sqlParameterSource, keyHolder, new String[]{"id"});
        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        Assert.isTrue(keyList.size() > 0, "");
        for (Map<String, Object> key : keyList) {
            Iterator<Object> iterator = key.values().iterator();
            while (iterator.hasNext()) {
                long val = ((Number) iterator.next()).longValue();
                System.out.printf("***********[%s]\n", val);
                Assert.isTrue(val != 0, "error");
            }
        }
    }

    @GlobalTransactional
    public void test6(Integer c) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = null;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        switch (c) {
            case 1:
                sql = "insert into `test_low` values (1, :name)";
                sqlParameterSource.addValue("name", "xx");
                break;
            case 2:
                sql = "update `test_low` set `id` = :nid, name = :name where `id` = :id";
                sqlParameterSource.addValue("nid", 1000);
                sqlParameterSource.addValue("name", "yy");
                sqlParameterSource.addValue("id", 1);
                break;
            case 3:
                sql = "delete from `test_low` where `id` = :id";
                sqlParameterSource.addValue("id", 1000);
                break;
            case 4:
                sql = "update `test_low` set `name` = :nname where `name` = :name";
                sqlParameterSource.addValue("nname", "xx");
                sqlParameterSource.addValue("name", "xx");
                break;
        }

        namedJdbcTemplate.update(sql, sqlParameterSource, keyHolder, new String[]{"id"});
        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        System.out.printf("keyList=[%s]\n", keyList);
        Assert.isTrue(keyList.size() > 0, "");
    }

    @GlobalTransactional
    public void test7() {
        String sql = "insert into test.`test1`(id, name) values(?, ?)";
        jdbcTemplate.update(sql, new Object[]{null, "xx"});
    }

    @GlobalTransactional
    public void test8() {
        String sql = "insert into test_uuid(id, name) values(uuid(), 'xx')";
        jdbcTemplate.update(sql);
    }
}
