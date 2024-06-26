package org.xxz.test.service;

import org.apache.seata.common.util.IOUtil;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
@ConditionalOnProperty(value = "spring.datasource.oracle.enable", havingValue = "true")
@Service
public class OracleService {

    Random r = new Random();

    @Autowired
    @Qualifier("oraclejdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("oraclejdbcTemplateorigin")
    private JdbcTemplate jdbcTemplateorigin;

    @Autowired
    @Qualifier("oraclenamedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    @Qualifier("oraclesqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CommonService commonService;

    @GlobalTransactional(timeoutMills = 5 * 60000)
    @Transactional(rollbackFor = Exception.class)
    public void test1(int n) {
        jdbcTemplate.update("insert into test values(test_seq.nextval, ?, ?)", new Object[] { "11", "111" });
        throw new RuntimeException("rollback");
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    @Transactional(rollbackFor = Exception.class)
    public void test2(int n) {
        Test1 param = new Test1();
        param.setName("xx");
        param.setName2("xx2");
        Test1Mapper test1Mapper = sqlSessionFactory.openSession().getMapper(Test1Mapper.class);
        test1Mapper.saveOracle(param);
        System.out.println(String.format("id=%s", param.getId()));
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test3(int n) {
        String sid = UUID.randomUUID().toString();
        jdbcTemplate.update("insert into test_escape(\"sid\",\"param\", \"createTime\") values (?, ?, ?)",
                new Object[] { sid, "a", new Date() });
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test4(int n) {

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

            for (int i = 0; i < size; ++i) {
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

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test5(int n) {
        List<Object[]> args = new ArrayList<>();
        args.add(new Object[] { "xx", "yy" });
        args.add(new Object[] { "xx1", "yy1" });
        jdbcTemplate.batchUpdate("insert into TEST (ID, name, name2) values (TEST_SEQ.nextval, ?, ?)", args);
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test6(int n) {
        List<Object[]> args = new ArrayList<>();
        args.add(new Object[] { "xx", "yy" });
        args.add(new Object[] { "xx1", "yy1" });
        jdbcTemplate.batchUpdate("insert into TEST (name, name2) values (?, ?)", args);
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test7(int n) {
        jdbcTemplate.update("insert into TEST (name, name2) values ('xx', 'xx2')");
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test8(int c) {
        jdbcTemplate.update("insert into TEST (id, name, name2) values (null, 'xx', 'xx2')");
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test9(int c) {
        String id = UUID.randomUUID().toString();
        // KeyHolder keyHolder = new GeneratedKeyHolder();
        // PreparedStatementCreator preparedStatementCreator = null;
        // preparedStatementCreator = (con) -> {
        // PreparedStatement ps = con.prepareStatement("insert into test_str(id, name)
        // values (?, ?)");
        // int i = 1;
        // ps.setObject(i++, id);
        // ps.setObject(i++, "xx");
        // return ps;
        // };
        // jdbcTemplate.update(preparedStatementCreator, keyHolder);

        jdbcTemplate.update("insert into TEST_STR(id, name) values (?, ?)", new Object[] { id, "xx" });
        System.out.println();
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
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

        namedJdbcTemplate.update(sql, sqlParameterSource, keyHolder, new String[] { "id" });
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

    @GlobalTransactional(timeoutMills = 5 * 60000)
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

        namedJdbcTemplate.update(sql, sqlParameterSource, keyHolder, new String[] { "id" });
        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        System.out.printf("keyList=[%s]\n", keyList);
        // Assert.isTrue(keyList.size() > 0, "");
    }

    /**
     * test pkvalues support.
     * 
     * @param n
     */
    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test12(int n) {
        // select SEQUENCE_OWNER, SEQUENCE_NAME from dba_sequences where sequence_owner
        // = '用户名';
        // sequence_owner必须为大写，不管你的用户名是否大写。只有大写才能识别。
        jdbcTemplate.update("delete from test1");
        switch (n) {
            case 1: {
                String sql = "insert into test1(id, name) values(test1_seq.nextval, ?)";
                jdbcTemplate.update(sql, new Object[] { "xx" });
                break;
            }
            case 2: {
                String sql = "insert into test1(id, name) values(10000, ?)";
                jdbcTemplate.update(sql, new Object[] { "xx" });
                break;
            }
            case 3: {
                String sql = "insert into test1(id, name) values(floor(dbms_random.value(900,1000)), ?)";
                jdbcTemplate.update(sql, new Object[] { "xx" });
                break;
            }
            case 4: {
                String sql = "insert into test1(id, name) values(test1_seq.nextval, 'xx')";
                jdbcTemplate.update(sql);
                break;
            }
            case 5: {
                String sql = "insert into test1(id, name) values(10002, 'xx')";
                jdbcTemplate.update(sql);
                break;
            }
            case 6: {
                // not support.
                String sql = "insert into test1(id, name) values(floor(dbms_random.value(900,1000)), 'xx')";
                jdbcTemplate.update(sql);
                break;
            }
        }
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test13(int n) {
        switch (n) {
            case 1: {
                String sql = "delete from test1 t1 where t1.id = ?";
                jdbcTemplate.update(sql, new Object[] { 10002 });
            }
            case 2: {
                String sql = "delete from test1 where id = ?";
                jdbcTemplate.update(sql, new Object[] { 10003 });
            }
        }
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test14(int n) {
        jdbcTemplate.update("delete from test1");
        switch (n) {
            case 1: {
                List<Object[]> args = new ArrayList<>();
                args.add(new Object[] { "xx" });
                args.add(new Object[] { "xx1" });
                jdbcTemplate.batchUpdate("insert into test1(id, name) values(test1_seq.nextval, ?)", args);
                break;
            }
            case 2: {
                Connection con = null;
                PreparedStatement ps = null;
                try {
                    con = jdbcTemplate.getDataSource().getConnection();
                    ps = con.prepareStatement("insert into test1(id, name) values(test1_seq.nextval, ?)");
                    ps.setObject(1, "xx");
                    ps.addBatch();
                    ps.setObject(1, "xx2");
                    ps.addBatch();
                    ps.executeBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtil.close(ps, con);
                }
                break;
            }
            case 3: {
                Connection con = null;
                PreparedStatement ps = null;
                try {
                    con = jdbcTemplate.getDataSource().getConnection();
                    ps = con.prepareStatement("insert into test1(id, name) values(?, ?)");
                    ps.setObject(1, 1);
                    ps.setObject(2, "xx");
                    ps.addBatch();
                    ps.setObject(1, 2);
                    ps.setObject(2, "xx2");
                    ps.addBatch();
                    ps.executeBatch();
                    // int i = 1 / 0;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtil.close(ps, con);
                }
                break;
            }
            case 4: {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                try {
                    connection = jdbcTemplate.getDataSource().getConnection();
                    String sql = "insert into storage_tbl (id,commodity_code,count) values(?,?,?)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setLong(1, 11);
                    preparedStatement.setString(2, "10001");
                    preparedStatement.setInt(3, 10);
                    preparedStatement.addBatch();
                    preparedStatement.setLong(1, 12);
                    preparedStatement.setString(2, "20002");
                    preparedStatement.setInt(3, 20);
                    preparedStatement.addBatch();
                    preparedStatement.setLong(1, 13);
                    preparedStatement.setString(2, "30003");
                    preparedStatement.setInt(3, 30);
                    preparedStatement.addBatch();
                    preparedStatement.executeBatch();
                    // System.out.println(1 / 0);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtil.close(preparedStatement, connection);
                }
                break;
            }
        }
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test15(int n) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        switch (n) {
            case 1: {
                String sql = "insert into test1(id, name) values(test1_seq.nextval, ?)";
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setObject(1, "xx");
                    return ps;
                }, keyHolder);
                Assert.isTrue(keyHolder.getKeyList().size() == 1, "size != 1");
                break;
            }
        }
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test16(int n) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        switch (n) {
            case 1: {
                String sql = "insert into \"test_date\"(\"id\", \"gmt_date\") values(test1_seq.nextval, sysdate)";
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql);
                    return ps;
                }, keyHolder);
                Assert.isTrue(keyHolder.getKeyList().size() == 1, "size != 1");
                commonService.error();
                break;
            }
        }
    }

    /**
     * test nclob
     */
    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test17(int n) {
        jdbcTemplate.update("insert into test_nclob (id, details) values(?, ?)", new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // DruidPooledConnection con1 = (DruidPooledConnection) ps.getConnection();
                // ConnectionSpy con2 = (ConnectionSpy) con1.getConnection();
                // Connection con = con2.getRealConnection();
                FileReader reader = null;
                try {
                    reader = new FileReader("/tmp/1.log");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ps.setObject(1, 1);
                ps.setClob(2, reader);
            }
        });
        commonService.error();
    }

    /**
     * test varchar 2000
     * 
     * @param n
     */
    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test18(int n) {
        StringBuilder a = new StringBuilder();
        a.append("YM4BWY5IKL-eyJsaWNDIyLTA4LTEwIiwiZXh0ZW5kZWQiOmZhbHNlfSx7ImNvZGUiOiJETSIsInBhaWRVcFRvIjoiMjAyMi0wOC0xMCIsImV4dGVuZGVkIjpmYWxzZX0seyJjb2RlIjoiUlNGIiwicGFpZFVwVG8iOiIyMDIyLTA4LTEwIiwiZXh0ZW5kZWQiOnRydWV9LHsiY29kZSI6IlBDIiwicGFpZFVwVG8iOiIyMDIyLTA4LTEwIiwiZXh0ZW5kZWQiOmZhbHNlfSx7ImNvZGUiOiJSQyIsInBhaWRVcFRvIjoiMjAyMi0wOC0xMCIsImV4dGVuZGVkIjpmYWxzZX0seyJjb2RlIjoiQ0wiLCJwYWlkVXBUbyI6IjIwMjItMDgtMTAiLCJleHRlbmRlZCI6ZmFsc2V9LHsiY29kZSI6IldTIiwicGFpZFVwVG8iOiIyMDIyLTA4LTEwIiwiZXh0ZW5kZWQiOmZhbHNlfSx7ImNvZGUiOiJSRCIsInBhaWRVcFRvIjoiMjAyMi0wOC0xMCIsImV4dGVuZGVkIjpmYWxzZX0seyJjb2RlIjoiUlMwIiwicGFpZFVwVG8iOiIyMDIyLTA4LTEwIiwiZXh0ZW5kZWQiOmZhbHNlfSx7ImNvZGUiOiJSTSIsInBhaWRVcFRvIjoiMjAyMi0wOC0xMCIsImV4dGVuZGVkIjpmYWxzZX0seyJjb2RlIjoiQUMiLCJwYWlkVXBUbyI6IjIwMjItMDgtMTAiLCJleHRlbmRlZCI6ZmFsc2V9LHsiY29kZSI6IlJTViIsInBhaWRVcFRvIjoiMjAyMi0wOC0xMCIsImV4dGVuZGVkIjp0cnVlfSx7ImNvZGUiOiJEQyIsInBhaWRVcFRvIjoiMjAyMi0wOC0xMCIsImV4dGVuZGVkIjpmYWxzZX0seyJjb2RlIjoiUlNVIiwicGFpZFVwVG8iOiIyMDIyLTA4LTEwIiwiZXh0ZW5kZWQiOmZhbHNlfSx7ImNvZGUiOiJEUCIsInBhaWRVcFRvIjoiMjAyMi0wOC0xMCIsImV4dGVuZGVkIjp0cnVlfSx7ImNvZGUiOiJQREIiLCJwYWlkVXBUbyI6IjIwMjItMDgtMTAiLCJleHRlbmRlZCI6dHJ1ZX0seyJjb2RlIjoiUFdTIiwicGFpZFVwVG8iOiIyMDIyLTA4LTEwIiwiZXh0ZW5kZWQiOnRydWV9LHsiY29kZSI6IlBTSSIsInBhaWRVcFRvIjoiMjAyMi0wOC0xMCIsImV4dGVuZGVkIjp0cnVlfSx7ImNvZGUiOiJQQ1dNUCIsInBhaWRVcFRvIjoiMjAyMi0wOC0xMCIsImV4dGVuZGVkIjp0cnVlfSx7ImNvZGUiOiJQUFMiLCJwYWlkVXBUbyI6IjIwMjItMDgtMTAiLCJleHRlbmRlZCI6dHJ1ZX0seyJjb2RlIjoiUEdPIiwicGFpZFVwVG8iOiIyMDIyLTA4LTEwIiwiZXh0ZW5kZWQiOnRydWV9LHsiY29kZSI6IlBQQyIsInBhaWRVcFRvIjoiMjAyMi0wOC0xMCIsImV4dGVuZGVkIjp0cnVlfSx7ImNvZGUiOiJQUkIiLCJwYWlkVXBUbyI6IjIwMjItMDgtMTAiLCJleHRlbmRlZCI6dHJ1ZX0seyJjb2RlIjoiUFNXIiwicGFpZFVwVG8iOiIyMDIyLTA4LTEwIiwiZXh0ZW5kZWQiOnRydWV9LHsiY29kZSI6IlJTIiwicGFpZFVwVG8iOiIyMDIyLTA4LTEwIiwiZXh0ZW5kZWQiOnRydWV9XSwibWV0YWRhdGEiOiIwMTIwMjEwODExRVBKQTAwMDAwOSIsImhhc2giOiIyNTcwNTI2NS8wOjQ4MzAwMjEzOCIsImdyYWNlUGVyaW9kRGF5cyI6MCwiYXV0b1Byb2xvbmdhdGVkIjpmYWxzZSwiaXNBdXRvUHJvbG9uZ2F0ZWQiOmZhbHNlfQ==-cj2bMGKFLLyaQnq9kulRVEyeCs2K1NO1KLMPbnm48ZNmcIr9KjsMu/TcZdf/s217Jy1+aRf3twtURbOSNf4Y4rEUr7y6sckafzwT+oRf023rc2/obObpEP+GoE8NjskcKtV2UYWCTwcCJ4sLtdImiWyOB8rJV2aW16iu++5OJ2G7Z4y0YJ5+DNdKQyPsiO+5+uzqYmJUP8CiN9tebe7MUbZ3Va/S2xiRZwMHDWRqkxbM54E01Ma4jTEfHYxy2wIH7R4gcZYZO9fX7b5n3nPLK0Vhq6vWMl2me2Waqlkb2vC1z7WNU/By6wns5MoZoxX80FTOhn8n7Vq/YJxko0XZSA==-MIIETDCCAjSgAwIBAgIBDTANBgkqhkiG9w0BAQsFADAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBMB4XDTIwMTAxOTA5MDU1M1oXDTIyMTAyMTA5MDU1M1owHzEdMBsGA1UEAwwUcHJvZDJ5LWZyb20tMjAyMDEwMTkwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDCP4uk4SlVdA5nuA3DQC+NsEnZS9npFnO0zrmMWcz1++q2UWJNuGTh0rwi+3fUJIArfvVh7gNtIp93rxjtrQAuf4/Fa6sySp4c32MeFACfC0q+oUoWebhOIaYTYUxm4LAZ355vzt8YeDPmvWKxA81udqEk4gU9NNAOz1Um5/8LyR8SGsSc4EDBRSjcMWMwMkYSauGqGcEUK8WhfplsyF61lKSOFA6VmfUmeDK15rUWWLbOMKgn2cxFA98A+s74T9Oo96CU7rp/umDXvhnyhAXSukw/qCGOVhwKR8B6aeDtoBWQgjnvMtPgOUPRTPkPGbwPwwDkvAHYiuKJ7Bd2wH7rAgMBAAGjgZkwgZYwCQYDVR0TBAIwADAdBgNVHQ4EFgQUJNoRIpb1hUHAk0foMSNM9MCEAv8wSAYDVR0jBEEwP4AUo562SGdCEjZBvW3gubSgUouX8bOhHKQaMBgxFjAUBgNVBAMMDUpldFByb2ZpbGUgQ0GCCQDSbLGDsoN54TATBgNVHSUEDDAKBggrBgEFBQcDATALBgNVHQ8EBAMCBaAwDQYJKoZIhvcNAQELBQADggIBAB2J1ysRudbkqmkUFK8xqhiZaYPd30TlmCmSAaGJ0eBpvkVeqA2jGYhAQRqFiAlFC63JKvWvRZO1iRuWCEfUMkdqQ9VQPXziE/BlsOIgrL6RlJfuFcEZ8TK3syIfIGQZNCxYhLLUuet2HE6LJYPQ5c0jH4kDooRpcVZ4rBxNwddpctUO2te9UU5/FjhioZQsPvd92qOTsV+8Cyl2fvNhNKD1Uu9ff5AkVIQn4JU23ozdB/R5oUlebwaTE6WZNBs+TA/qPj+5/wi9NH71WRB0hqUoLI2AKKyiPw++FtN4Su1vsdDlrAzDj9ILjpjJKA1ImuVcG329/WTYIKysZ1CWK3zATg9BeCUPAV1pQy8ToXOq+RSYen6winZ2OO93eyHv2Iw5kbn1dqfBw1BuTE29V2FJKicJSu8iEOpfoafwJISXmz1wnnWL3V/0NxTulfWsXugOoLfv0ZIBP1xH9kmf22jjQ2JiHhQZP7ZDsreRrOeIQ/c4yR8IQvMLfC0WKQqrHu5ZzXTH4NO3CwGWSlTY74kE91zXB5mwWAx1jig+UXYc2w4RkVhy0//lOmVya/PEepuuTTI4+UJwC7qbVlh5zfhj8oTNUXgN0AOc+Q0/WFPl1aw5VV/VrO8FCoB15lFVlpKaQ1Yh+DVU8ke+rt9Th0BCHXe0uZOEmH0nOnH/0onD");
        jdbcTemplate.update("insert into test_varchar(ID_, TEST) values(?, ?)", new Object[] { "1430362753712893954", a.toString() });
        // commonService.error();
    }

    @GlobalTransactional(timeoutMills = 5 * 60000)
    public void test19(int n) {
        int i = new Random().nextInt(10);
//        jdbcTemplate.update("insert into tb_multi(ID_, ID2_, COL_) values(?, ?, ?)", new Object[] { "ID_" + i, "ID2_" + i, "COL_" });
        jdbcTemplate.update("update tb_multi set COL_ = ? where ID_ = ? and ID2_ = ?", new Object[] { "COL_", "ID_", "ID2_" });
        if (n == 0) {
            commonService.error();
        }
    }
}
