package org.xxz.test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;

/**
 * @author jsbxyyx
 */
public class SQLTest {

    public static void main(String[] args) {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements("update t_project_info set\n" +
            "          cost=1,\n" +
            "          labor_fee = 1,\n" +
            "          material_fee = 1,\n" +
            "          other_fee = 1,\n" +
            "         update_user=1\n" +
            "        where id = null and available = 1", JdbcConstants.MYSQL);
        System.out.println(sqlStatements);
    }

}
