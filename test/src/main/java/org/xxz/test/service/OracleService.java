package org.xxz.test.service;

import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
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
import org.xxz.test.dao.ProcessTaskConfig;
import org.xxz.test.dao.Test1;
import org.xxz.test.dao.Test1Mapper;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class OracleService {

    Random r = new Random();

    @Autowired
    @Qualifier("oraclejdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("oraclenamedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    @Qualifier("oraclesqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private RestTemplate restTemplate;

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test1() {
        jdbcTemplate.update("insert into test values(test_seq.nextval, ?, ?)", new Object[]{"11", "111"});
        throw new RuntimeException("rollback");
    }

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test2() {
        Test1 param = new Test1();
        param.setName("xx");
        param.setName2("xx2");
        Test1Mapper test1Mapper = sqlSessionFactory.openSession().getMapper(Test1Mapper.class);
        test1Mapper.saveOracle(param);
        System.out.println(String.format("id=%s", param.getId()));
    }

    @GlobalTransactional
    public void test3() {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_escape(\"sid\",\"param\", \"createTime\") values (?, ?, ?)",
                new Object[]{sid, "a", new Date()});
    }

    @GlobalTransactional
    public void test4() {

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
    public void test5() {
        List<Object[]> args = new ArrayList<>();
        args.add(new Object[]{"xx", "yy"});
        args.add(new Object[]{"xx1", "yy1"});
        jdbcTemplate.batchUpdate("insert into TEST (ID, name, name2) values (TEST_SEQ.nextval, ?, ?)", args);
    }

    @GlobalTransactional
    public void test6() {
        List<Object[]> args = new ArrayList<>();
        args.add(new Object[]{"xx", "yy"});
        args.add(new Object[]{"xx1", "yy1"});
        jdbcTemplate.batchUpdate("insert into TEST (name, name2) values (?, ?)", args);
    }

    @GlobalTransactional
    public void test7() {
        jdbcTemplate.update("insert into TEST (name, name2) values ('xx', 'xx2')");
    }


    @GlobalTransactional
    public void test8() {
        jdbcTemplate.update("insert into TEST (id, name, name2) values (null, 'xx', 'xx2')");
    }


    @GlobalTransactional
    public void test9() {
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

    @GlobalTransactional
    public void test10(int c) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = null;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        switch (c) {
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
                System.out.printf("***********[%s]\n", val);
                Assert.isTrue(val != 0, "error");
            }
        }
    }

    @GlobalTransactional
    public void test11(Integer cs) {
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
        System.out.printf("keyList=[%s]\n", keyList);
//        Assert.isTrue(keyList.size() > 0, "");
    }

    /**
     * test pkvalues support.
     * @param n
     */
    @GlobalTransactional
    public void test12(int n) {
        // select SEQUENCE_OWNER, SEQUENCE_NAME from dba_sequences where sequence_owner = '用户名';
        // sequence_owner必须为大写，不管你的用户名是否大写。只有大写才能识别。
        jdbcTemplate.update("delete from test1");
        switch (n) {
            case 1: {
                String sql = "insert into test1(id, name) values(test1_seq.nextval, ?), (test1_seq.nextval, ?)";
                jdbcTemplate.update(sql, new Object[]{"xx", "xx"});
                break;
            }
            case 2: {
                String sql = "insert into test1(id, name) values(10000, ?), (10001, ?)";
                jdbcTemplate.update(sql, new Object[]{"xx", "xx"});
                break;
            }
            case 3: {
                String sql = "insert into test1(id, name) values(floor(dbms_random.value(900,1000)), ?), (floor(dbms_random.value(900,1000)), ?)";
                jdbcTemplate.update(sql, new Object[]{"xx", "xx"});
                break;
            }
            case 4: {
                String sql = "insert into test1(id, name) values(test1_seq.nextval, 'xx')";
                jdbcTemplate.update(sql);
            }
            case 5: {
                String sql = "insert into test1(id, name) values(10002, 'xx')";
                jdbcTemplate.update(sql);
            }
            case 6: {
                String sql = "insert into test1(id, name) values(floor(dbms_random.value(900,1000)), 'xx')";
                jdbcTemplate.update(sql);
            }
        }
    }
}
