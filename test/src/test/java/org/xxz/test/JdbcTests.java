package org.xxz.test;

import io.seata.common.util.IOUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author jsbxyyx
 */
public class JdbcTests {

    public static void main(String[] args) throws Exception {

//        mysqlTest();

//        postgresqlTest();

        mysqlQuery();
    }

    private static void mysqlQuery() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://rm-2zetd9474ydd1g5955o.mysql.rds.aliyuncs.com:3306/test",
                    "workshop", "Workshop123");
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("select * from test1");
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getObject(1));
                System.out.println(rs.getObject(2));
                System.out.println(rs.getObject(3));
                System.out.println("=============");
            }
        } finally {
            IOUtil.close(ps, conn);
        }
    }

    public static void mysqlTest() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://rm-2zetd9474ydd1g5955o.mysql.rds.aliyuncs.com:3306/test",
                    "workshop", "Workshop123");
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("insert into test1(id, name) values(null, ?)");
            ps.setObject(1, "xx");
            ps.execute();
            System.out.println(1 / 0);
        } finally {
            IOUtil.close(ps, conn);
        }
    }

    public static void postgresqlTest() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://47.95.143.87:5432/postgres",
                    "test", "123456");
            ps = conn.prepareStatement("insert into test_array(id, arr) values(?, ?)");
            ps.setObject(1, 1);
            ps.setObject(2, conn.createArrayOf("integer", new Object[]{1, 2, 3}));
            ps.execute();
        } finally {
            IOUtil.close(ps, conn);
        }
    }

}
