package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.xxz.test.dao.ProcessTaskConfig;
import org.xxz.test.dao.Test1Mapper;
import org.xxz.test.dao.Test1;
import org.xxz.test.dao.TestUuid;
import org.xxz.test.dao.TestUuidMapper;
import org.xxz.test.dao.TkMapper;
import org.xxz.test.dao.TkTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author jsbxyyx
 * @since
 */
@Service
public class TestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

    Random r = new Random();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test1_oracle() {
        jdbcTemplate.update("insert into test values(test_seq.nextval, ?, ?)", new Object[]{"11", "111"});
        throw new RuntimeException("rollback");
    }

    @Autowired
    private Test1Mapper test1Mapper;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test2_oracle() {
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
    public void test3_mysql() {
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
    public void test8_mysql() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_escape(`sid`,`param`,`createTime`) values (?, ?, ?)",
                new Object[]{sid, "a", new Date()});
    }

    @GlobalTransactional(rollbackFor = {})
    public void test9(boolean r) {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_keyword values (?, ?)",
                new Object[]{sid, "a"});
        if (r) {
            throw new RuntimeException("rollback");
        }
    }

    @GlobalTransactional
    public void test10_oracle() {
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
    public void test13_mysql() {
        jdbcTemplate.update("insert into test values(null, ?, ?)", new Object[]{"11", "111"});
        throw new RuntimeException("rollback");
    }

    @GlobalTransactional
    public void test14() {
        test12();
        restTemplate.getForObject("http://127.0.0.1:8003/test13", String.class);
    }

    @GlobalTransactional
    public void test15_mysql() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_escape(sid,param,createTime) values (?, ?, ?)",
                new Object[]{sid, "a", new Date()});
    }


    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @GlobalTransactional
    public void test16_oracle() {

        List<ProcessTaskConfig> task = new ArrayList<>();
        ProcessTaskConfig pro1 = new ProcessTaskConfig();
        pro1.setId(UUID.randomUUID().toString().replace("-", ""));
        pro1.setProcessDefId("928f3e9c2bc94a9fa531a7bb66f47658");
        pro1.setTaskKey("归档");
        pro1.setTimeLimit(1.0);
        pro1.setRapidSubmitType(1);
        task.add(pro1);

        ProcessTaskConfig pro2 = new ProcessTaskConfig();
        pro2.setId(UUID.randomUUID().toString().replace("-", ""));
        pro2.setProcessDefId("928f3e9c2bc94a9fa531a7bb66f47663");
        pro2.setTaskKey("缮证");
        pro2.setTimeLimit(2.0);
        pro2.setRapidSubmitType(2);
        task.add(pro2);

        SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            int size = task.size();
            String sqlStatement = "org.xxz.test.dao.ProcessTaskConfigMapper.save";

            for(int i = 0; i < size; ++i) {
                batchSqlSession.insert(sqlStatement, task.get(i));
                if (i >= 1 && i % 2 == 0) {
                    batchSqlSession.flushStatements();
                }
            }

            batchSqlSession.flushStatements();
        } catch (Throwable var15) {
            throw var15;
        } finally {
            if (batchSqlSession != null) {
                try {
                    batchSqlSession.close();
                } catch (Throwable var14) {
                }
            }

        }

    }

    @GlobalTransactional
    public void test17_oracle() {
        List<Object[]> args = new ArrayList<>();
        args.add(new Object[]{"xx", "yy"});
        args.add(new Object[]{"xx1", "yy1"});
        jdbcTemplate.batchUpdate("insert into TEST (ID, name, name2) values (TEST_SEQ.nextval, ?, ?)", args);
    }

    @GlobalTransactional
    public void test18_oracle() {
        List<Object[]> args = new ArrayList<>();
        args.add(new Object[]{"xx", "yy"});
        args.add(new Object[]{"xx1", "yy1"});
        jdbcTemplate.batchUpdate("insert into TEST (name, name2) values (?, ?)", args);
    }

    @GlobalTransactional
    public void test19_oracle() {
        jdbcTemplate.update("insert into TEST (name, name2) values ('xx', 'xx2')");
    }


    @GlobalTransactional
    public void test20_oracle() {
        jdbcTemplate.update("insert into TEST (id, name, name2) values (null, 'xx', 'xx2')");
    }


    @GlobalTransactional
    public void test21_oracle() {
        String id = UUID.randomUUID().toString();
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        PreparedStatementCreator preparedStatementCreator = null;
//        preparedStatementCreator = (con) -> {
//            PreparedStatement ps = con.prepareStatement("insert into test_str(id, name) values (?, ?)");
//            int i = 1;
//            ps.setObject(i++, id);
//            ps.setObject(i++, "xx");
//            return ps;
//        };
//        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        jdbcTemplate.update("insert into TEST_STR(id, name) values (?, ?)", new Object[]{id, "xx"});
        System.out.println();
    }

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @GlobalTransactional
    public void test22_oracle(int cs) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = null;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        switch (cs) {
            case 1:
                sql = "insert into TEST1(id, name) values (test1_seq.nextval, :name)";
                sqlParameterSource.addValue("name", "xx");
                break;
            case 2:
                sql = "insert into TEST1 values (test1_seq.nextval, :name, :name2)";
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
                LOGGER.info("***********[{}]", val);
                Assert.isTrue(val != 0, "error");
            }
        }
    }


    @GlobalTransactional
    public void test23_mysql(int cs) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = null;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        switch (cs) {
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
                LOGGER.info("***********[{}]", val);
                Assert.isTrue(val != 0, "error");
            }
        }
    }


    @GlobalTransactional
    public void test24_oracle(Integer cs) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = null;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        switch (cs) {
            case 1:
                sql = "insert into \"test_low\" values (1, :name)";
                sqlParameterSource.addValue("name", "xx");
                break;
            case 2:
                sql = "update \"test_low\" set \"id\" = :nid, \"name\" = :name where \"id\" = :id";
                sqlParameterSource.addValue("nid", 1000);
                sqlParameterSource.addValue("name", "yy");
                sqlParameterSource.addValue("id", 1);
                break;
            case 3:
                sql = "delete from \"test_low\" where \"id\" = :id";
                sqlParameterSource.addValue("id", 1000);
                break;
            case 4:
                sql = "update \"test_low\" set \"name\" = :nname where \"name\" = :name";
                sqlParameterSource.addValue("nname", "xx");
                sqlParameterSource.addValue("name", "xx");
                break;
        }

        namedJdbcTemplate.update(sql, sqlParameterSource, keyHolder, new String[]{"id"});
        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        LOGGER.info("keyList=[{}]", keyList);
//        Assert.isTrue(keyList.size() > 0, "");
    }

    @GlobalTransactional
    public void test25_mysql(Integer cs) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = null;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        switch (cs) {
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
        LOGGER.info("keyList=[{}]", keyList);
//        Assert.isTrue(keyList.size() > 0, "");
    }

    @GlobalTransactional
    public void test26() {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", null);
        namedJdbcTemplate.update("update test set id = :id where id = :id", sqlParameterSource);
    }

    @Autowired
    TestUuidMapper testUuidMapper;

    @GlobalTransactional
    public void test27() {

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

        restTemplate.getForObject("http://127.0.0.1:8003/test13", String.class);
    }

}

