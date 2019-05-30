package org.xxz.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author jsbxyyx
 * @since
 */
@Repository
public class OrderRepository {

    static final String insert_sql = "insert into t_order(user_id, amount, create_time) values(?, ?, now())";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int save(int userId, double amount) {
        return jdbcTemplate.update(insert_sql, new Object[]{userId, amount});
    }
}
