package org.xxz.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Test1Dao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Object select() {
        return jdbcTemplate.queryForList("select * from test1 where name = ? for update", new Object[]{"xx"});
    }

    public void update() {
        jdbcTemplate.update("update test1 set name2 = ? where name = ?", new Object[]{"xxxx", "xx"});
    }
}
