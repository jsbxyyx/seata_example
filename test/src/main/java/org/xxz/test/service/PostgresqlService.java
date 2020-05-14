package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author tt
 */
@Service
public class PostgresqlService {

    Random r = new Random();

    @Autowired
    @Qualifier("postgresqljdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("postgresqljdbcTemplateorigin")
    private JdbcTemplate jdbcTemplateorigin;

    @Autowired
    @Qualifier("postgresqlnamedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    @Qualifier("postgresqlsqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CommonService commonService;

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test1() {
        String sql = "insert into test_uuid values(?, ?)";
        jdbcTemplate.update(sql, UUID.randomUUID().toString(), "xx");
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test2(boolean r) {
        String sql = "insert into \"TEST\" values(?, ?)";
        jdbcTemplate.update(sql, UUID.randomUUID().toString(), "xx");
        restTemplate.getForObject("http://127.0.0.1:8003/postgresql/test1", String.class);
        if (r) {
            throw new RuntimeException("rollback");
        }
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test3() {
        String sql = "insert into test(id, name) values(nextval('test_seq'), ?)";
        jdbcTemplate.update(sql, "xxx");
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test4(int n) {
        switch (n) {
            case 1: {
                String sql = "insert into test(id, name) values(nextval('test_seq'), 'xx'), (nextval('test_seq'), 'xx2')";
                jdbcTemplate.update(sql);
                break;
            }
            case 2: {
                String sql = "insert into test(id) values(nextval('test_seq'), ?), (nextval('test_seq'), ?)";
                jdbcTemplate.update(sql, new Object[]{"xx", "xx2"});
                break;
            }
        }

    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test5(int n) {
        switch (n) {
            case 1: {
                String sql = "insert into test1(id, name) values(default, ?), (default, ?)";
                jdbcTemplate.update(sql, new Object[]{"xx", "xx2"});
                break;
            }
            case 2: {
                String sql = "insert into test1(id, name) values(default, 'xx'), (default, 'xx2')";
                jdbcTemplate.update(sql);
                break;
            }
        }

    }

    /**
     * test pkvalues support.
     * @param n
     */
    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test6(int n) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        // SELECT c.relname FROM pg_class c WHERE c.relkind = 'S';
        jdbcTemplate.update("delete from test1");
        switch (n) {
            case 1: {
                String sql = "insert into test1(id, name) values(nextval('test1_seq'), ?), (nextval('test1_seq'), ?)";
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql);
                    int i = 1;
                    ps.setObject(i++, "xx");
                    ps.setObject(i++, "xx");
                    return ps;
                }, keyHolder);
                Assert.isTrue(keyHolder.getKeyList().size() == 2, "size != 2");
                break;
            }
            case 2: {
                String sql = "insert into test1(id, name) values(10000, ?), (10001, ?)";
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql);
                    int i = 1;
                    ps.setObject(i++, "xx");
                    ps.setObject(i++, "xx");
                    return ps;
                }, keyHolder);
                Assert.isTrue(keyHolder.getKeyList().size() == 2, "size != 2");
                break;
            }
            case 3: {
                String sql = "insert into test1(id, name) values(floor(random()*1000), ?), (floor(random()*1000), ?)";
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql);
                    int i = 1;
                    ps.setObject(i++, "xx");
                    ps.setObject(i++, "xx");
                    return ps;
                }, keyHolder);
                Assert.isTrue(keyHolder.getKeyList().size() == 2, "size != 2");
                break;
            }
            case 4: {
                String sql = "insert into test1(id, name) values(default, ?), (default, ?)";
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql);
                    int i = 1;
                    ps.setObject(i++, "xx");
                    ps.setObject(i++, "xx");
                    return ps;
                }, keyHolder);
                Assert.isTrue(keyHolder.getKeyList().size() == 2, "size != 2");
                break;
            }
            case 5: {
                String sql = "insert into test1(id, name) values(nextval('test1_seq'), 'xx')";
                jdbcTemplate.update(sql);
                break;
            }
            case 6: {
                String sql = "insert into test1(id, name) values(10002, 'xx'), (10003, 'xx1')";
                jdbcTemplate.update(sql);
                break;
            }
            case 7: {
                // not support.
                String sql = "insert into test1(id, name) values(floor(random()*1000), 'xx')";
                jdbcTemplate.update(sql);
                break;
            }
            case 8: {
                String sql = "insert into test1(id, name) values(default, 'xx')";
                jdbcTemplate.update(sql);
                break;
            }
        }
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test7(int n) {
        switch (n) {
            case 1: {
                String sql = "delete from test1 t1 where t1.id = ?";
                jdbcTemplate.update(sql, new Object[]{10002});
            }
            case 2: {
                String sql = "delete from test1 where id = ?";
                jdbcTemplate.update(sql, new Object[]{10003});
            }
        }
    }

    public void test8(int n) {
        switch (n) {
            case 1: {
                String sql = "select name from test1 where name like concat('%', ?)";
                List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, new Object[]{"xx"});
                System.out.println(maps);
            }
            case 2: {
                String sql = "select name from test1 where name like concat('%', ?)";
                List<Map<String, Object>> maps = jdbcTemplateorigin.queryForList(sql, new Object[]{"xx"});
                System.out.println(maps);
            }
            case 3: {
                String sql = "select name from test1 where name like ?";
                List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, new Object[]{"%xx%"});
                System.out.println(maps);
            }
        }
    }
}
