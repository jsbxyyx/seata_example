package org.xxz.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author jsbxyyx
 * @since
 */
@Repository
public class UserRepository {

    static final String insert_sql = "insert into t_user(id, `name`, create_time) values(?, ?, ?)";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int save(int userId, String name) {
        return jdbcTemplate.update(insert_sql, new Object[]{userId, name, new Date()});
    }
}
