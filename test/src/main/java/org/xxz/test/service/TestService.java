package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.xxz.test.dao.Test1;
import org.xxz.test.dao.Test1Mapper;
import org.xxz.test.dao.TestUuid;
import org.xxz.test.dao.TestUuidMapper;
import org.xxz.test.dao.TkMapper;
import org.xxz.test.dao.TkTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author jsbxyyx
 * @since
 */
@Service
public class TestService {

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
    public void test1() {
        List<TkTest> list = new ArrayList<>();
        list.add(new TkTest(null, "xx", "xx2"));
        list.add(new TkTest(null, "yy", "yy2"));
        mapper.insertList(list);
    }

    @GlobalTransactional
    public void test2() {
        List<TkTest> list = new ArrayList<>();
        list.add(new TkTest(3L, "xxxx", "xxxx2"));
        list.add(new TkTest(4L, "yyyy", "yyyy2"));
        mapper.batchUpdate(list);
    }

    @GlobalTransactional
    public void test3() {
        List<Object[]> args = new ArrayList<>();
        args.add(new Object[]{"xxxx", "xxxx2", 3L});
        args.add(new Object[]{"yyyy", "yyyy2", 4L});
        jdbcTemplate.batchUpdate("update tk_test set name = ?, name2 = ? where id = ?", args);
    }

    @GlobalTransactional
    public void test4() throws Exception {
        byte[] bin = "bin".getBytes();
        String txt = "txt";
//        jdbcTemplate.update("insert into TEST_BIN(id, name, bin, txt) values(test_bin_seq.nextval, ?, ?, ?)", new Object[]{"xx", bin, txt});

        final File image = new File(System.getProperty("user.dir") + "/test/src/main/java/org/xxz/test/service/TestService.java");
        final InputStream imageIs = new FileInputStream(image);

        jdbcTemplate.update("update test_bin set name = ?, bin = ?, txt = ? where id = ?",
                new Object[] {
                        "xx" + r.nextInt(100),
                        new SqlLobValue(imageIs, (int)image.length(), new DefaultLobHandler()),
                        txt,
                        2
                },
                new int[] {Types.VARCHAR, Types.BLOB, Types.CLOB, Types.INTEGER});
        restTemplate.getForObject("http://127.0.0.1:8004/test1", String.class);
    }

    @GlobalTransactional(rollbackFor = {})
    public void test5(boolean r) {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_keyword values (?, ?)",
                new Object[]{sid, "a"});
        if (r) {
            throw new RuntimeException("rollback");
        }
    }

    @GlobalTransactional
    public void test6() {
        String sid = UUID.randomUUID().toString();
        String param = "a";
        Date createTime = new Date();
        int rows = test1Mapper.save1(sid, param, createTime);
        System.out.println("rows:" + rows);
    }

    @GlobalTransactional
    public void test7() {
        String sql = "update test_case " +
                "set set_value = " +
                "case id " +
                "when ? then ? when ? then ? " +
                "when ? then ? when ? then ? " +
                "when ? then ? when ? then ? " +
                "when ? then ? when ? then ? " +
                "when ? then ? when ? then ? " +
                "when ? then ? when ? then ? " +
                "when ? then ? " +
                "end " +
                ",updater_id= " +
                "case id " +
                "when ? then ? when ? then ? " +
                "when ? then ? when ? then ? " +
                "when ? then ? when ? then ? " +
                "when ? then ? when ? then ? " +
                "when ? then ? when ? then ? " +
                "when ? then ? when ? then ? " +
                "when ? then ? " +
                "end " +
                ",update_time=now() " +
                "where id in ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        jdbcTemplate.update(sql, new Object[]{
                20827, "7", 20828, "false",
                20829, "0", 12939, "pdf",
                20830, "true", 20831, "true",
                20832, "true", 20833, "false",
                20834, "false", 20835, "s",
                20836, "s", 14425, "money",
                20837, "s",
                20827, 188, 20828, 188,
                20829, 188, 12939, 188,
                20830, 188, 20831, 188,
                20832, 188, 20833, 188,
                20834, 188, 20835, 188,
                20836, 188, 14425, 188,
                20837, 188,
                20827, 20828, 20829, 12939, 20830, 20831, 20832, 20833, 20834, 20835, 20836, 14425, 20837
        });
    }

    @GlobalTransactional
    public void test8(int c) {
        switch (c) {
            case 0:
                MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
                sqlParameterSource.addValue("nname", null);
                sqlParameterSource.addValue("oname", null);
                namedJdbcTemplate.update("update test_uuid set name = :nname where name = :oname", sqlParameterSource);
                break;
            case 1:
                jdbcTemplate.update("update test_uuid set name = null where name = null");
                break;
                default:
                    break;
        }

    }

    @GlobalTransactional
    public void test9() {

        // server db mode branch_table lock_key data too long, update lock_key type to text resolve it.

        List<TestUuid> list = new ArrayList<>();
        list.add(new TestUuid(UUID.randomUUID().toString(), "xx"));
        list.add(new TestUuid(UUID.randomUUID().toString(), "xx"));
        list.add(new TestUuid(UUID.randomUUID().toString(), "xx"));
        list.add(new TestUuid(UUID.randomUUID().toString(), "xx"));
        list.add(new TestUuid(UUID.randomUUID().toString(), "xx"));
        list.add(new TestUuid(UUID.randomUUID().toString(), "xx"));
        list.add(new TestUuid(UUID.randomUUID().toString(), "xx"));
        list.add(new TestUuid(UUID.randomUUID().toString(), "xx"));
        list.add(new TestUuid(UUID.randomUUID().toString(), "xx"));
        list.add(new TestUuid(UUID.randomUUID().toString(), "xx"));
        testUuidMapper.batchInsert(list);
        restTemplate.getForObject("http://127.0.0.1:8003/test1", String.class);
    }

    @GlobalTransactional
    public void test10() {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("created", null);
        int update = namedJdbcTemplate.update("insert into test_date(created) values(:created)", paramSource);
        Assert.isTrue(update == 1, "update is " + update);
        
        restTemplate.getForObject("http://127.0.0.1:8003/test28", String.class);
    }

    @GlobalTransactional
    @Transactional
    public void test11() {
        Test1 param = new Test1();
        int affected = test1Mapper.save(param);
        System.out.println("affected:" + affected);
    }

}

