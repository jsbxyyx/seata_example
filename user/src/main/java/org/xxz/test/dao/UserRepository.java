package org.xxz.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author jsbxyyx
 * @since
 */
@Repository
public class UserRepository {

    static final String insert_sql = "insert into t_user(id, `name`, create_time) values(?, ?, now())";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int save(int userId, String name) {
        return jdbcTemplate.update(insert_sql, new Object[]{userId, name});
    }
}
