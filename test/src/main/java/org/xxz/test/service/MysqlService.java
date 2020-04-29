package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

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
    @Qualifier("mysqljdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("mysqljdbcTemplateo")
    private JdbcTemplate jdbcTemplateo;

    @Autowired
    @Qualifier("mysqlnamedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;


    @Autowired
    @Qualifier("mysqlsqlSessionFactory")
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

    @GlobalTransactional
    public void test9() {
        String sql = "insert into test_uuid(id, name) values(uuid(), 'xx')";
        jdbcTemplate.update(sql);
    }

    @GlobalTransactional
    public void test10() {
        String sql = "insert into test1(id) values(?)";
        jdbcTemplate.update(sql, new Object[]{null});
    }

    /**
     * multi sql
     * @param n
     */
    @GlobalTransactional
    public void test11(int n) {
        switch (n) {
            case 1:
                String sql = "update test1 set id = ? where id = ?; update test1 set id = ? where id = ?;";
                jdbcTemplate.update(sql, new Object[]{1, 1, 2, 2});
                break;
            case 2:
                String sql2 = "update test1 set id = ? where id = ?; update test1 set name = ? where name = ?; update test1 set name = '11';";
                jdbcTemplate.update(sql2, new Object[]{1, 1, "aa", "aa"});
                break;
            case 3:
                String sql3 = "delete from test1 where id = ?; delete from test1 where name = ?; delete from test1;";
                jdbcTemplate.update(sql3, new Object[]{1, 1});
                break;
            case 4:
                String sql4 = "update test1 set id = 1 where id = 1; update test1 set name = '11' where name = '11';";
                jdbcTemplate.update(sql4);
                break;
            case 5:
                String sql5 = "delete from test1 where id = 1; delete from test1 where name = '11';";
                jdbcTemplate.update(sql5);
                break;
            case 6:
                String sql6 = "update test1 set name = 'xx1' where name = 'xx'; update test2 set name = 'xx1' where name = 'xx';";
                jdbcTemplate.update(sql6);
                break;
            case 7:
                String sql7 = "update test1 set name = 'xx2' where name = 'xx1';";
                jdbcTemplate.update(sql7);
        }
    }

    /**
     * test pkvalues support.
     */
    @GlobalTransactional
    public void test12(int n) throws Exception {
        jdbcTemplate.update("delete from test1");
        switch (n) {
            case 1: {
                String sql = "insert into test1(id, name) values(null, ?), (null, ?)";
                jdbcTemplate.update(sql, new Object[]{"xx", "xx"});
                break;
            }
            case 2: {
                String sql = "insert into test1(id, name) values(10000, ?), (10001, ?)";
                jdbcTemplate.update(sql, new Object[]{"xx", "xx"});
                break;
            }
            case 3: {
                String sql = "insert into test1(id, name) values(floor(now() + 1), ?), (floor(now() + 2), ?)";
                jdbcTemplate.update(sql, new Object[]{"xx", "xx"});
                break;
            }
            case 4: {
                String sql = "insert into test1(id, name) values(null, 'xx')";
                jdbcTemplate.update(sql);
                break;
            }
            case 5: {
                String sql = "insert into test1(id, name) values(10002, 'xx'), (10003, 'xx1')";
                jdbcTemplate.update(sql);
                break;
            }
            case 6: {
                // not support
                String sql = "insert into test1(id, name) values(now(), 'xx')";
                jdbcTemplate.update(sql);
                break;
            }
        }
    }

    @GlobalTransactional
    public void test13(int n) {
        switch (n) {
            case 1: {
                String sql = "delete a from test1 a where a.id = ?";
                jdbcTemplate.update(sql, new Object[]{1});
            }
            case 2: {
                String sql = "delete from test1 where id = ?";
                jdbcTemplate.update(sql, new Object[]{1});
            }
        }
    }
}
