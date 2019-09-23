package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.xxz.test.dao.Test1Mapper;
import org.xxz.test.dao.Test1;
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
    public void test7() throws Exception {
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

    @GlobalTransactional
    public void test8() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_escape(`sid`,`param`,`createTime`) values (?, ?, ?)",
                new Object[]{sid, "a", new Date()});
    }

    @GlobalTransactional
    public void test9() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_keyword values (?, ?)",
                new Object[]{sid, "a"});
    }

    @GlobalTransactional
    public void test10() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_escape(\"sid\",\"param\", \"createTime\") values (?, ?, ?)",
                new Object[]{sid, "a", new Date()});
    }

    @GlobalTransactional
    public void test11() {
        String sid = UUID.randomUUID().toString();
        String param = "a";
        Date createTime = new Date();
        test1Mapper.save1(sid, param, createTime);
    }

    @GlobalTransactional
    public void test12() {
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
    @Transactional(rollbackFor = Exception.class)
    public void test13() {
        jdbcTemplate.update("insert into test values(null, ?, ?)", new Object[]{"11", "111"});
        throw new RuntimeException("rollback");
    }

    @GlobalTransactional
    public void test14() {
        test12();
        restTemplate.getForObject("http://127.0.0.1:8003/test13", String.class);
    }

}

