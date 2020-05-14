package org.xxz.test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Test;

import java.util.List;

/**
 * @author jsbxyyx
 */
public class SQLTest {

    @Test
    public void mysql_update_sql() throws Exception {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements("update t_project_info set\n" +
                "          cost=1,\n" +
                "          labor_fee = 1,\n" +
                "          material_fee = 1,\n" +
                "          other_fee = 1,\n" +
                "         update_user=1\n" +
                "        where id = null and available = 1", JdbcConstants.MYSQL);
        System.out.println(sqlStatements);
    }

    @Test
    public void oracle_insert_all_sql() throws Exception {
        List<SQLStatement> sqlStatements1 = SQLUtils.parseStatements(" INSERT ALL " +
                "INTO T_CARD_BALANCE ( id,card_no,balance,available_amount,frozen_amount,crt_time,up_time ) VALUES ( ?,?,?,?,?,?,? ) " +
                "INTO T_CARD_BALANCE ( id,card_no,balance,available_amount,frozen_amount,crt_time,up_time ) VALUES ( ?,?,?,?,?,?,? ) " +
                "INTO T_CARD_BALANCE ( id,card_no,balance,available_amount,frozen_amount,crt_time,up_time ) VALUES ( ?,?,?,?,?,?,? ) " +
                "INTO T_CARD_BALANCE ( id,card_no,balance,available_amount,frozen_amount,crt_time,up_time ) VALUES ( ?,?,?,?,?,?,? ) " +
                "INTO T_CARD_BALANCE ( id,card_no,balance,available_amount,frozen_amount,crt_time,up_time ) VALUES ( ?,?,?,?,?,?,? ) " +
                "INTO T_CARD_BALANCE ( id,card_no,balance,available_amount,frozen_amount,crt_time,up_time ) VALUES ( ?,?,?,?,?,?,? ) " +
                "INTO T_CARD_BALANCE ( id,card_no,balance,available_amount,frozen_amount,crt_time,up_time ) VALUES ( ?,?,?,?,?,?,? ) " +
                "INTO T_CARD_BALANCE ( id,card_no,balance,available_amount,frozen_amount,crt_time,up_time ) VALUES ( ?,?,?,?,?,?,? ) " +
                "INTO T_CARD_BALANCE ( id,card_no,balance,available_amount,frozen_amount,crt_time,up_time ) VALUES ( ?,?,?,?,?,?,? ) " +
                "INTO T_CARD_BALANCE ( id,card_no,balance,available_amount,frozen_amount,crt_time,up_time ) VALUES ( ?,?,?,?,?,?,? ) " +
                "SELECT 1 FROM DUAL ", JdbcConstants.ORACLE);
        System.out.println(sqlStatements1);
    }

    @Test
    public void oracle_sequence_sql() throws Exception {
        String sql = "insert into test values(test_seq.nextval)";
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.ORACLE);
        System.out.println(sqlStatements);
    }

    @Test
    public void postgresql_insert_pk_default_sql() throws Exception {
        String sql = "insert into test_auto(id, name) values(DEFAULT, 'xx')";
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.POSTGRESQL);
        System.out.println(sqlStatements);
    }

    @Test
    public void postgresql_next_value_sequence_sql() throws Exception {
        String sql = "insert into test_auto(id, name) values(nextval('undo_log_id_seq'), 'xx')";
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.POSTGRESQL);
        System.out.println(sqlStatements);
    }

    @Test
    public void mysql_default_keyword() throws Exception {
        String sql = "insert into test(id, name) values(default, '1')";
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        System.out.println(sqlStatements);
    }

    @Test
    public void mysql_delete_alias() {
        String sql = "delete a from test1 as a where a.id = ?";
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        System.out.println(sqlStatements);

        sql = "delete t1 from test1 as t1 where t1.id = (select id from test2 t2 where t2.id = ?)";
        sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        System.out.println(sqlStatements);
    }

    @Test
    public void oracle_sysdate() {
        String sql = "insert into test1(create_time) values(sysdate)";
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.ORACLE);
        System.out.println(sqlStatements);
    }

    @Test
    public void postgresql_current_date() {
        String sql = "insert into test1(created) values(current_timestamp)";
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.POSTGRESQL);
        System.out.println(sqlStatements);
    }

    @Test
    public void placeholder() {
        String sql = "insert into test1(created) values(?)";
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.POSTGRESQL);
        System.out.println(sqlStatements);

        sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.ORACLE);
        System.out.println(sqlStatements);

        sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        System.out.println(sqlStatements);

        // SQLVariantRefExpr
    }
}
