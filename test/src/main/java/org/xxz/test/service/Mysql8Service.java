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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.Random;

/**
 * @author jsbxyyx
 */
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
    @Transactional(rollbackFor = Exception.class)
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

}
